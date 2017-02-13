package io.github.lordscales91.magic9.workers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;

public class ChecksumWorker extends MagicWorker {

	private static final String REAL_PROGRESS = "real_progress";
	private File fileToCheck;
	private String algorithm;
	private float progress;
	private long totalBytes;
	private int bytesRead;
	public ChecksumWorker(File fileToCheck, String algorithm, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.fileToCheck = fileToCheck;
		this.algorithm = algorithm;
		this.totalBytes = fileToCheck.length();
		this.bytesRead = 0;
	}
	
	public ChecksumWorker(File fileToCheck, String tag, CallbackReceiver receiver) {
		this(fileToCheck, "SHA-256", tag, receiver);
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
	public void stop() {}

	@Override
	protected String doInBackground() throws Exception {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		FileInputStream fis = new FileInputStream(fileToCheck);		
		BufferedInputStream bfi = new BufferedInputStream(fis, 8192);
		byte[] buff = new byte[8192];
		int read = bfi.read(buff);
		while(read > 0) {
			md.update(buff, 0, read);
			bytesRead += read;
			updateProgress();
			read = bfi.read(buff);
		}
		MagicUtils.closeQuietly(bfi);
		MagicUtils.closeQuietly(fis);
		byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<mdbytes.length;i++) {
      	  sb.append(Integer.toHexString(0xFF & mdbytes[i]));
      	}
		return sb.toString();
	}

	private void updateProgress() {
		float oldProgress = progress;
		progress = (bytesRead * 100.0f) / totalBytes;
		int iProgress = (int) progress;
		setProgress((iProgress > 100)?100:iProgress); // deal with precision loss
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}

}
