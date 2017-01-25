package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicConstants;

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

import org.apache.commons.io.IOUtils;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class TorrentDownloadWorker extends MagicWorker implements Observer {

	public static final String REAL_PROGRESS = "real_progress";
	
	private File torrent;
	private File output;
	private float progress;

	private Client client;

	public TorrentDownloadWorker(File torrent, File output, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.torrent = torrent;
		this.output = output;
	}

	@Override
	protected String doInBackground() throws Exception {
		SharedTorrent torr = SharedTorrent.fromFile(torrent, output);
		prepareTrackers(torr);
		client = new Client(InetAddress.getLocalHost(), torr);		
		client.addObserver(this);
		client.download();
		client.waitForCompletion();
		return "success";
	}
	
	private void prepareTrackers(SharedTorrent torr) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		List<URI> trackers = new ArrayList<URI>();
		try {
			fr = new FileReader(new File(MagicConstants.TRACKERS_FILE));	
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null) {
				if(!line.isEmpty()) {
					try {
						URI tr = new URI(line);
						if(!trackers.contains(tr)) {
							trackers.add(tr);
						}						
					} catch (URISyntaxException ignore) {}
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
		if(client != null) {
			// If the client can't be stopped, at least it will not cause
			client.deleteObservers(); // an unexpected behavior
			client.stop();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Client client = (Client)o;
		float oldProgress = progress;
		progress = client.getTorrent().getCompletion();
		setProgress((int) progress);
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}
}
