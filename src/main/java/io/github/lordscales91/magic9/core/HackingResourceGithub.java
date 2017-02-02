package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.GithubDownloadWorker;
import io.github.lordscales91.magic9.MagicWorker;

import java.io.File;

public class HackingResourceGithub extends HackingResource {

	private String assetExtension;

	public HackingResourceGithub(String url, File out, String tag, String assetExtension) {
		super(url, out, tag);
		this.assetExtension = assetExtension;
	}

	@Override
	public String getSource() {
		return "Github";
	}
	
	@Override
	public MagicWorker getWorker(CallbackReceiver receiver) {
		return new GithubDownloadWorker(getUrl(), assetExtension, getOut(), getTag(), receiver);
	}
}
