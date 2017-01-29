package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.MagicWorker;
import io.github.lordscales91.magic9.TorrentDownloadWorker;

import java.io.File;

public class HackingResourceTorrent extends HackingResource {

	private File outdir;

	/**
	 * Constructs a resource to download a torrent file to the specified destination.
	 * @param url The URL to fetch the torrent file
	 * @param out Where to save the torrent file
	 * @param tag The tag to associate to the worker
	 * @param outdir Where to download the torrent's content (associated files)
	 */
	public HackingResourceTorrent(String url, File out, String tag, File outdir) {
		super(url, out, tag);
		if(!outdir.exists()) {
			outdir.mkdirs();
		}
		this.outdir = outdir;
	}
	
	/**
	 * This will return a {@link TorrentDownloadWorker} using the <code>out</code> file
	 * as the input torrent, which will be previously downloaded, and the previously 
	 * specified output directory to store the torrent content(associated files).
	 * @param receiver The receiver to be passed to the <code>TorrentDownloadWorker</code>
	 * @return a configured <code>TorrentDownloadWorker</code>
	 */
	@Override
	public MagicWorker getWorker(CallbackReceiver receiver) {
		return new TorrentDownloadWorker(getUrl(), getOut(), outdir, getTag(), receiver);
	}

}
