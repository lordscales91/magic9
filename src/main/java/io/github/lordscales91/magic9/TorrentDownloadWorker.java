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

import javax.swing.SwingWorker;

import org.apache.commons.io.IOUtils;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

public class TorrentDownloadWorker extends SwingWorker<Client, String> implements Observer {

	public static final String REAL_PROGRESS = "real_progress";
	
	private File torrent;
	private File output;
	private CallbackReceiver receiver;
	private String tag;
	private float progress;

	private Client client;

	private boolean wasStopped;

	public TorrentDownloadWorker(File torrent, File output, String tag, CallbackReceiver receiver) {
		this.torrent = torrent;
		this.output = output;
		this.tag=tag;
		this.receiver = receiver;
	}

	@Override
	protected Client doInBackground() throws Exception {
		SharedTorrent torr = SharedTorrent.fromFile(torrent, output);
		prepareTrackers(torr);
		client = new Client(InetAddress.getLocalHost(), torr);		
		client.addObserver(this);
		client.download();
		client.waitForCompletion();
		return client;
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
	public void stop() {
		wasStopped = true;
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
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}

	@Override
	protected void done() {
		try {
			if(!wasStopped) {
				receiver.receiveData(get(), tag);
			}			
		} catch (Exception e) {
			// Pass exception to notify an error happened
			receiver.receiveData(e, tag);
		}
	}
}
