package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.TorrentDownloadWorker;

import java.io.File;

public class HackingResourceTorrent extends HackingResource {

	private File outdir;

	/**
	 * Constructs a resource to download a torrent file to the specified destination.
	 * @param url The URL to fetch the torrent file
	 * @param out Where to save the torrent file
	 * @param outdir Where to download the torrent's content (associated files)
	 */
	public HackingResourceTorrent(String url, File out, File outdir) {
		super(url, out);
		if(!outdir.exists()) {
			outdir.mkdirs();
		}
		this.outdir = outdir;
	}
	
	/**
	 * This will return a {@link TorrentDownloadWorker} with the specified torrent
	 * using the previously specified output directory.
	 * @param torrent The torrent file to download
	 * @param tag The tag to be passed to the <code>TorrentDownloadWorker</code>
	 * @param receiver The receiver to be passed to the <code>TorrentDownloadWorker</code>
	 * @return a configured <code>TorrentDownloadWorker</code>
	 */
	public TorrentDownloadWorker getTorrentWorker(File torrent, String tag, CallbackReceiver receiver) {
		return new TorrentDownloadWorker(torrent, outdir, tag, receiver);
	}

}
