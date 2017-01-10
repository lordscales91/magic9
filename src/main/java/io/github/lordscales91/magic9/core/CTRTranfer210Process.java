package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;

import java.io.File;
import java.io.IOException;

public class CTRTranfer210Process extends HackingProcess {

	protected CTRTranfer210Process(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// TODO: Implement a safety check to ensure there is a NAND backup created at this point.
		// Prepare the materials
		File ctrimagesdir = new File(hackingDir, MagicConstants.CTRIMAGES_FOLDER);
		if(!ctrimagesdir.exists()) {
			ctrimagesdir.mkdir();
		}
		HackingPath hp = HackingPath.getInstance();
		if(hp==null) {
			throw new IOException("Hacking path is not initialized");
		}
		File ctrimagezip = new File(hackingDir, MagicUtils.getCTR210ZipName(hp.getFwver()));
		MagicUtils.extractZipFile(ctrimagezip, ctrimagesdir);
		// Prepare the SD
		File ctrimagebin = new File(ctrimagesdir, MagicUtils.getCTR210ImageName(hp.getFwver()));
		File ctrimagesha = new File(ctrimagesdir, MagicUtils.getCTR210ImageShaFile(hp.getFwver()));
		File files9dir = new File(sdCardDir, MagicConstants.FILES9_FOLDER);
		// Copy the .bin and .bin.sha to the files9 folder
		MagicUtils.copyFile(ctrimagebin, new File(files9dir, ctrimagebin.getName()));
		MagicUtils.copyFile(ctrimagesha, new File(files9dir, ctrimagesha.getName()));
	}

	@Override
	public boolean isSafeToPause() {
		// At this point we started already to modify the system.
		// So, we should continue until the end
		return false;
	}

}
