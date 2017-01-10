package io.github.lordscales91.magic9.core;

import java.io.File;
import java.io.IOException;

public class Decrypt9BrowserProcess extends HackingProcess {

	protected Decrypt9BrowserProcess(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// Prepare the materials
		File d9dir = new File(hackingDir, MagicConstants.DECRYPT9_FOLDER);
		File d9zip = new File(hackingDir, MagicConstants.DECRYPT9_ZIP);
		File launcherDat = new File(d9dir, MagicConstants.DECRYPT9_LAUNCHER_DAT);
		File decrypt9Dat = new File(d9dir, MagicConstants.DECRYPT9_DAT);
		// Overwrite existing files with the latest version
		// Launcher.dat
		MagicUtils.extractFileFromZip(d9zip, MagicConstants.DECRYPT9_LAUNCHER_DAT, launcherDat);
		// Decrypt9WIP.dat
		MagicUtils.extractFileFromZip(d9zip, MagicConstants.DECRYPT9_DAT, decrypt9Dat);
		// Prepare the SD
		// Create a files9 directory at the root of the SD card if it doesn't exists.
		File files9dir = new File(sdCardDir, MagicConstants.FILES9_FOLDER);
		if(!files9dir.exists()) {
			files9dir.mkdir();
		}
		// Copy Launcher.dat to the root of the SD card
		MagicUtils.copyFile(launcherDat, new File(sdCardDir, launcherDat.getName()));
		// Copy Decrypt9WIP.dat to the root of the SD card
		MagicUtils.copyFile(decrypt9Dat, new File(sdCardDir, decrypt9Dat.getName()));
	}

	@Override
	public boolean isSafeToPause() {
		return true;
	}

}
