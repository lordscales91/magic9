package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.workers.GithubDownloadWorker;
import io.github.lordscales91.magic9.workers.MagicWorker;

import java.io.File;

public class HackingResourceGithub extends HackingResource {

	private String assetExtension;
    private String nameRegex;

	public HackingResourceGithub(String url, File out, String tag, String assetExtension) {
		super(url, out, tag);
		this.assetExtension = assetExtension;
	}
	
	public HackingResourceGithub(String url, File out, String tag, String assetExtension, String nameRegex) {
		this(url, out, tag, assetExtension);
		this.nameRegex = nameRegex;
	}

	@Override
	public String getSource() {
		return "Github";
	}
	
	@Override
	public MagicWorker getWorker(CallbackReceiver receiver) {
		return new GithubDownloadWorker(getUrl(), assetExtension, nameRegex, getOut(), getTag(), receiver);
	}
}
