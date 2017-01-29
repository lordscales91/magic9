package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Decrypt9HomebrewProcess extends HackingProcess {

	protected Decrypt9HomebrewProcess(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// Prepare the materials
		File d9zip = new File(hackingDir, MagicConstants.DECRYPT9_ZIP);
		File d9folder = new File(hackingDir, MagicConstants.DECRYPT9_FOLDER);
		File decrypt9Bin = new File(d9folder, MagicConstants.DECRYPT9_BIN);
		MagicUtils.extractFileFromZip(d9zip, MagicConstants.DECRYPT9_BIN, decrypt9Bin);
		File safehaxDir = new File(hackingDir, MagicConstants.SAFEHAX_FASTHAX_FOLDER);
		// .3ds files should be downloaded there directly		
		// Prepare the SD
		// Create a files9 directory at the root of the SD card if it doesn't exists.
		File files9dir = new File(sdCardDir, MagicConstants.FILES9_FOLDER);
		if(!files9dir.exists()) {
			files9dir.mkdir();
		}
		// Copy the safehax and fasthax to the /3ds/ folder on the SD
		MagicUtils.copyDirectory(safehaxDir, new File(sdCardDir, MagicConstants.THREE_D_S_FOLDER));
		// Copy Decrypt9WIP.bin to the root as arm9.bin
		MagicUtils.copyFile(decrypt9Bin, new File(sdCardDir, MagicConstants.SAFEHAX_PAYLOAD));
	}

	@Override
	public boolean isSafeToPause() {
		return true;
	}

	@Override
	public List<HackingResource> getRequiredResources() {
		List<HackingResource> resources = new ArrayList<HackingResource>();
		File out = new File(hackingDir, MagicConstants.DECRYPT9_ZIP);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.DECRYPT9_KEY), out, ".zip"));
		File safehaxDir = new File(hackingDir, MagicConstants.SAFEHAX_FASTHAX_FOLDER);
		out = new File(safehaxDir, "safehax.3dsx");
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.SAFEHAX_KEY), out, ".3dsx"));
		out = new File(safehaxDir, "fasthax.3dsx");
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.FASTHAX_KEY), out, ".3dsx"));
		// Iconpacks (SMDH) are not necessary, but they are very small in size and would help to find the app in the homebrew list
		out = new File(safehaxDir, "safehax.smdh");
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.SAFEHAX_KEY), out, ".smdh"));
		return resources;
	}

}
