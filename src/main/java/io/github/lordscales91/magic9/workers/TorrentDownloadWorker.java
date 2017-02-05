package io.github.lordscales91.magic9.workers;

import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.io.IOUtils;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class TorrentDownloadWorker extends MagicWorker implements Observer {

	public static final String REAL_PROGRESS = "real_progress";

	private File torrent;
	private File basedir;
	private float progress;
	private Client client;
	private File out;
	private String url;
	private Call getTorrent;

	@Deprecated
	public TorrentDownloadWorker(File torrent, File output, String tag,
			CallbackReceiver receiver) {
		this(null, torrent, output, null, tag, receiver);
	}
	/**
	 * Standard constructor
	 * @param url The url to retrieve the torrent file
	 * @param torrent where to save the downloaded torrent
	 * @param basedir base directory to download the torrent resource 
	 * @param out final name of the resource
	 */
	public TorrentDownloadWorker(String url, File torrent, File basedir,
			File out, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.torrent = torrent;
		this.basedir = basedir;
		this.out = out;
		this.url = url;
	}
	
	/**
	 * Constructor copy
	 * @param other The other worker to
	 */
	public TorrentDownloadWorker(TorrentDownloadWorker other) {
		this(other.url, other.torrent, other.basedir, other.out, other.tag, other.receiver);		
	}

	@Override
	protected String doInBackground() throws Exception {
		if (url != null) {
			// Retrieve the torrent from the URL
			retrieveTorrent();
		}
		SharedTorrent torr = SharedTorrent.fromFile(torrent, basedir);
		String name = null;
		if (!torr.isMultifile()) {
			name = torr.getName();
		}
		prepareTrackers(torr);
		client = new Client(InetAddress.getLocalHost(), torr);
		client.addObserver(this);
		client.download();
		client.waitForCompletion();
		if(name != null) {
			File src = new File(basedir, name);
			MagicUtils.moveFile(src, out);
		}		
		return "success";
	}

	private void retrieveTorrent() throws IOException {
		OkHttpClient okClient = new OkHttpClient();
		Request req = new Request.Builder().url(url).build();
		getTorrent = okClient.newCall(req);
		Response resp = getTorrent.execute();
		if (!resp.isSuccessful()) {
			throw new IOException("Couldn't retrieve the torrent file at: "
					+ url);
		}
		// FileUtils.copyInputStreamToFile(resp.body().byteStream(), torrent);
		MagicUtils.saveStreamToFile(resp.body().byteStream(), torrent);
	}

	private void prepareTrackers(SharedTorrent torr) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		List<URI> trackers = new ArrayList<URI>();
		try {
			fr = new FileReader(new File(MagicConstants.TRACKERS_FILE));
			br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if (!line.isEmpty()) {
					try {
						URI tr = new URI(line);
						if (!trackers.contains(tr)) {
							trackers.add(tr);
						}
					} catch (URISyntaxException ignore) {
					}
				}
				line = br.readLine();
			}
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(fr);
		}
		torr.getAnnounceList().add(trackers);
	}

	/**
	 * Stops the torrent client
	 */
	@Override
	public void stop() {
		setStopFlag();
		if (getTorrent != null) {
			getTorrent.cancel();
		}
		if (client != null) {
			// If the client can't be stopped, at least it will not cause
			client.deleteObservers(); // an unexpected behavior
			client.stop();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		Client client = (Client) o;
		float oldProgress = progress;
		progress = client.getTorrent().getCompletion();
		int iProgress = (int) progress;
		setProgress((iProgress > 100) ? 100 : iProgress); // deal with precision
															// loss
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}

	@Override
	public float getRealProgress() {
		return progress;
	}

	@Override
	public boolean hasRealProgressSupport() {
		return true;
	}
}
