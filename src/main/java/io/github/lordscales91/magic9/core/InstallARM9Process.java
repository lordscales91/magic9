package io.github.lordscales91.magic9.core;

import java.io.File;
import java.io.IOException;

public class InstallARM9Process extends HackingProcess {

	protected InstallARM9Process(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// Prepare the materials
		// Extract the homebrew starter kit. At this point it should already there, 
		// but it doesn't hurt to ensure.
		File starterkit = new File(hackingDir, MagicConstants.STARTER_KIT_FOLDER);
		MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.STARTER_KIT_ZIP), starterkit);
		File safe9HLFolder = new File(hackingDir, MagicConstants.SAFE9HLINSTALLER_FOLDER);
		MagicUtils.extract7ZipFile(new File(hackingDir, MagicConstants.SAFE9HLINSTALLER_7Z), safe9HLFolder);
		File dataInputFolder = new File(hackingDir, MagicConstants.DATA_INPUT_FOLDER);
		MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.DATA_INPUT_ZIP), dataInputFolder);
		File arm9hax = new File(hackingDir, MagicConstants.ARM9_HAX_FOLDER);
		MagicUtils.extract7ZipFile(new File(hackingDir, MagicConstants.ARM9_HAX_7Z), arm9hax);
		// Put all the cias in the same folder
		File ciasorig = new File(hackingDir, MagicConstants.CIAS_FOLDER);
		MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.HBLAUNCHER_ZIP), 
				MagicConstants.HBLAUNCHER_CIA, new File(ciasorig, MagicConstants.HBLAUNCHER_CIA));
		MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.LUMA_UPDATER_ZIP), 
				MagicConstants.LUMA_UPDATER_CIA, new File(ciasorig, MagicConstants.LUMA_UPDATER_CIA));
		// FBI.cia should be downloaded there directly
		File luma3dsdir = new File(hackingDir, MagicConstants.LUMA3DS_FOLDER);
		File arm9loaderbin = new File(luma3dsdir, MagicConstants.ARM9_LOADER_HAX_BIN);
		MagicUtils.extractFileFrom7Zip(new File(hackingDir, MagicConstants.LUMA3DS_7Z),
				MagicConstants.ARM9_LOADER_HAX_BIN, arm9loaderbin);
		// Prepare hourglass and Godmde
		File hourglasszip = new File(hackingDir, MagicConstants.HOURGLASS9_ZIP);
		File hourglassbin = new File(hackingDir, MagicConstants.HOURGLASS9_BIN);
		MagicUtils.extractFileFromZip(hourglasszip, MagicConstants.HOURGLASS9_BIN, hourglassbin);
		File godmodezip = new File(hackingDir, MagicConstants.GODMODE9_ZIP);
		File godmodebin = new File(hackingDir, MagicConstants.GODMODE9_BIN);
		MagicUtils.extractFileFromZip(godmodezip, MagicConstants.GODMODE9_BIN, godmodebin);
		// Prepare file variables
		File aeskeydb = new File(hackingDir, MagicConstants.AESKEYDB_BIN);
		File dspdump = new File(hackingDir, MagicConstants.DSPDUMP);
		// Prepare fbi injectable
		File fbinjectable = new File(hackingDir, MagicConstants.FBI_INJECTABLE_FOLDER);
		MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.FBI_INJECTABLE_ZIP), fbinjectable);
		// Prepare the SD
		// Create a cias directory at the root of the SD
		File ciasdir = new File(sdCardDir, MagicConstants.CIAS_FOLDER);
		if(!ciasdir.exists()) {
			ciasdir.mkdir();
		}
		// Delete the a9lh from root if it exists
		// Why 1 l and I look so damn similar?
		File foo9 = new File(sdCardDir, MagicConstants.ARM9LH_FOLDER);
		if(foo9.exists()) {
			MagicUtils.clearDirectory(foo9);
			foo9.delete();
		}
		// Delete the 3ds folder if it exists
		File $3ds = new File(sdCardDir, MagicConstants.THREE_D_S_FOLDER);
		if($3ds.exists()) {
			MagicUtils.clearDirectory($3ds);
			$3ds.delete();
		}		
		// Copy the Homebrew starter kit to the root of the SD card
		MagicUtils.copyDirectory(starterkit, new File(sdCardDir));
		// Copy the contents of Safe9HLInstaller to the SD root
		MagicUtils.copyDirectory(safe9HLFolder, new File(sdCardDir));
		// Copy the a9hl folder from the data_input to the SD root
		MagicUtils.copyDirectory(new File(dataInputFolder, MagicConstants.ARM9LH_FOLDER), foo9);
		// Copy the contents of arm9loaderhax release to a9lh folder
		MagicUtils.copyDirectory(arm9hax, foo9);
		// Copy all the cias to the cias folder on the SD card
		MagicUtils.copyDirectory(ciasorig, ciasdir);
		// Copy arm9loaderhax.bin to the root
		MagicUtils.copyFile(arm9loaderbin, new File(sdCardDir, MagicConstants.ARM9_LOADER_HAX_BIN));
		// Create a luma folder with a payloads folder inside
		File payloadsdir = new File(sdCardDir, MagicConstants.LUMA_PAYLOADS_DIR);
		if(!payloadsdir.exists()) {
			payloadsdir.mkdirs();
		}
		// Copy hourglass
		MagicUtils.copyFile(hourglassbin, new File(payloadsdir, MagicConstants.HOURGLASS9_PAYLOAD));
		// Copy GodMode
		MagicUtils.copyFile(godmodebin, new File(payloadsdir, MagicConstants.GODMODE9_PAYLOAD));
		// Copy aeskeydb.bin to files9	
		File files9 = new File(sdCardDir, MagicConstants.FILES9_FOLDER);
		MagicUtils.copyFile(aeskeydb, new File(files9, aeskeydb.getName()));
		// Copy DSPDump to 3ds
		MagicUtils.copyFile(dspdump, new File($3ds, dspdump.getName()));
		// Copy the contents of fbi_injectable to files8
		MagicUtils.copyDirectory(fbinjectable, files9);
	}

	@Override
	public boolean isSafeToPause() {
		/* 
		 * Once finished, then yes. Otherwise it is not safe.
		 * The problem here is that we can't track what the user is doing on the device.
		 * Perhaps a good approach would be to "emulate" the screens of the the device
		 * and ask the user to recreate his/her steps there.
		 */
		return false;
	}

}
