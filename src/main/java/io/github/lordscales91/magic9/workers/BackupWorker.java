package io.github.lordscales91.magic9.workers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;

public class BackupWorker extends MagicWorker {

	private static final int BUFF_SIZE = 8192;
	private static final String REAL_PROGRESS = "real_progress";
	private File backupDir;
	private File[] files;
	private float progress;
	private long totalBytes;
	private long bytesRead;

	public BackupWorker(String tag, CallbackReceiver receiver, File backupDir, File... files) {
		super(tag, receiver);
		this.backupDir = backupDir;
		this.files = files;
	}

	@Override
	public void stop() {
	}
	
	@Override
	public boolean hasRealProgressSupport() {
		return true;
	}
	
	@Override
	public float getRealProgress() {
		return progress;
	}

	@Override
	protected String doInBackground() throws Exception {
		String basedir = null;
		if(files != null) {
			if(files.length == 1 && files[0].isDirectory()) {
				basedir = files[0].getAbsolutePath();
			} else if(files.length > 0) {
				basedir = files[0].getParent();
			}
		}
		if(basedir != null) {
			totalBytes = calculateSize();
			List<String> entries = getEntries(basedir, files);
			for(String e : entries) {
				File in = new File(basedir, e);
				File out = new File(backupDir, e);
				copyFile(in, out);
			}
			
		} else {
			// Handle exceptions
		}
		return "success";
	}
	
	private void copyFile(File in, File out) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bfi = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			if(!out.getParentFile().exists()) {
				out.getParentFile().mkdirs();
			}
			fis = new FileInputStream(in);
			bfi = new BufferedInputStream(fis, BUFF_SIZE);
			fos = new FileOutputStream(out);
			bos = new BufferedOutputStream(fos, BUFF_SIZE);
			byte buff[] = new byte[BUFF_SIZE];
			int read = bfi.read(buff);
			while(read > 0) {
				bos.write(buff, 0, read);
				bytesRead += read;
				updateProgress();
				read = bfi.read(buff);
			}
		} finally {
			MagicUtils.closeQuietly(bos);
			MagicUtils.closeQuietly(fos);
			MagicUtils.closeQuietly(bfi);
			MagicUtils.closeQuietly(fis);
		}
	}

	private void updateProgress() {
		float oldProgress = progress;
		progress = (bytesRead * 100.0f) / totalBytes;
		int iProgress = (int) progress;
		setProgress((iProgress > 100)?100:iProgress); // deal with precision loss
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}

	private List<String> getEntries(String basedir, File... entries) {
		List<String> strFiles = new ArrayList<String>();
		for(File f : entries) {
			if(f.isFile()) {
				String path = f.getAbsolutePath().replace(basedir, "");
				if(path.startsWith(File.separator)) {
					path = path.substring(1);
				}
				strFiles.add(path);
			} else if(f.isDirectory()) {
				strFiles.addAll(getEntries(basedir, f.listFiles()));
			}
		}
		return strFiles;
	}
	
	private long calculateSize() {
		return calculateSize(files);
	}
	
	private long calculateSize(File[] files) {
		long size = -1;
		for(File f : files) {
			if(f.isFile()) {
				size += f.length();
			} else if(f.isDirectory()) {
				size += calculateSize(f.listFiles());
			}
		}
		return size;
	}

}
