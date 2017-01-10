package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;

import java.io.File;
import java.io.IOException;

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
		if(hp==null) {
			throw new IOException("Hacking path is not initialized");
		}
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

}
