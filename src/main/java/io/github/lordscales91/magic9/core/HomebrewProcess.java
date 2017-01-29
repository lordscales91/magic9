package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomebrewProcess extends HackingProcess {

	protected HomebrewProcess(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// Prepare the materials
		File starterkit = new File(hackingDir, MagicConstants.STARTER_KIT_FOLDER);
		MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.STARTER_KIT_ZIP), starterkit);
		HackingPath hp = HackingPath.getInstance();
		// Prepare the SD
		// Copy the contents of the starter kit to the SD root
		MagicUtils.copyDirectory(starterkit, new File(sdCardDir));
		// Copy Soundhax to the root
		File soundhax = new File(hackingDir, MagicUtils.getSoundhaxFilename(hp.getFwver()));
		MagicUtils.copyFile(soundhax, new File(sdCardDir, soundhax.getName()));
		// Copy otherapp payload to the root as otherapp.bin
		File otherapp = new File(hackingDir, MagicUtils.getOtherAppFilename(hp.getFwver()));
		MagicUtils.copyFile(otherapp, new File(sdCardDir, MagicConstants.OTHERAPP_BIN));
	}

	@Override
	public boolean isSafeToPause() {
		return true;
	}

	@Override
	public List<HackingResource> getRequiredResources() {
		List<HackingResource> resources = new ArrayList<HackingResource>();
		File out = new File(hackingDir, MagicConstants.STARTER_KIT_ZIP);
		resources.add(new HackingResource(HackingPath.URLS.getProperty(MagicPropKeys.STARTER_KIT_KEY), out));
		HackingPath hp = HackingPath.getInstance();
		out = new File(hackingDir, MagicUtils.getSoundhaxFilename(hp.getFwver()));
		resources.add(new HackingResource(MagicUtils.getSoundhaxURL(hp.getFwver()), out));
		out = new File(hackingDir, MagicUtils.getOtherAppFilename(hp.getFwver()));
		resources.add(new HackingResource(MagicUtils.getOtherAppURL(hp.getFwver()), out));
		return resources;
	}

}
