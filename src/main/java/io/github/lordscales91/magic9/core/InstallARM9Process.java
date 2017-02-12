package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.constants.MagicTags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstallARM9Process extends HackingProcess {

	protected InstallARM9Process(String hackingDir, String sdCardDir) {
		super(hackingDir, sdCardDir);
	}

	@Override
	public void process() throws IOException {
		// Prepare the materials
		// Extract the homebrew starter kit. At this point it should be already there, 
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
//		MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.LUMA_UPDATER_ZIP), 
//				MagicConstants.LUMA_UPDATER_CIA, new File(ciasorig, MagicConstants.LUMA_UPDATER_CIA));
		// FBI.cia and lumaupdater.cia should be downloaded there directly
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
		MagicUtils.copyDirectory(new File(starterkit, MagicConstants.STARTER_KIT_SUB_FOLDER), new File(sdCardDir));
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

	@Override
	public List<HackingResource> getRequiredResources() {
		List<HackingResource> resources = new ArrayList<HackingResource>();
		File out = new File(hackingDir, MagicConstants.STARTER_KIT_ZIP);
		resources.add(new HackingResource(HackingPath.URLS.getProperty(MagicPropKeys.STARTER_KIT_KEY), out,
				MagicTags.STARTER_KIT_TAG));
		out = new File(hackingDir, MagicConstants.SAFE9HLINSTALLER_7Z);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.SAFEA9HL_KEY), out, 
				MagicTags.SAFEA9HL_TAG, ".7z"));
		File torrentsDir = new File(hackingDir, MagicConstants.TORRENTS_DIR);
		out = new File(torrentsDir, MagicConstants.DATA_INPUT_ZIP+".torrent");
		File rename = new File(hackingDir, MagicConstants.DATA_INPUT_ZIP);
		File basedir = new File(hackingDir);
		resources.add(new HackingResourceTorrent(HackingPath.URLS.getProperty(MagicPropKeys.DATA_INPUT_KEY), out, 
				rename, MagicTags.DATA_INPUT_TAG, basedir));
		out = new File(hackingDir, MagicConstants.ARM9_HAX_7Z);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.ARM9HAX_KEY), out, 
				MagicTags.ARM9HAX_TAG, ".7z"));
		out = new File(hackingDir, MagicConstants.HBLAUNCHER_ZIP);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.HBLAUNCHER_KEY), out, 
				MagicTags.HBLAUNCHER_TAG, ".zip"));
		// Add the CIAs
		File ciasorig = new File(hackingDir, MagicConstants.CIAS_FOLDER);
		out = new File(ciasorig, MagicConstants.LUMA_UPDATER_CIA);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.LUMA_UPDATER_KEY), out,
				MagicTags.LUMA_UPDATER_TAG, ".cia"));
		out = new File(ciasorig, MagicConstants.FBI_CIA);		
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.FBI_KEY), out, 
				MagicTags.FBI_TAG, ".cia"));
		// Now the utilities
		out = new File(hackingDir, MagicConstants.LUMA3DS_7Z);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.LUMA3DS_KEY), out, 
				MagicTags.LUMA3DS_TAG, ".7z"));
		out = new File(hackingDir, MagicConstants.HOURGLASS9_ZIP);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.HOURGLASS_KEY), out,
				MagicTags.HOURGLASS9_TAG, ".zip"));
		out = new File(hackingDir, MagicConstants.GODMODE9_ZIP);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.GODMODE_KEY), out,
				MagicTags.GODMODE9_TAG, ".zip"));
		out = new File(torrentsDir, MagicConstants.AESKEYDB_BIN+".torrent");
		rename = new File(hackingDir, MagicConstants.AESKEYDB_BIN);
		resources.add(new HackingResourceTorrent(HackingPath.URLS.getProperty(MagicPropKeys.AESKEYDB_KEY), out,
				rename, MagicTags.AESKEY_TAG, basedir));
		out = new File(hackingDir, MagicConstants.DSPDUMP);
		resources.add(new HackingResourceGithub(HackingPath.URLS.getProperty(MagicPropKeys.DSPDUMP_KEY), out,
				MagicTags.DSPDUMP_TAG, ".3dsx"));
		out = new File(torrentsDir, MagicConstants.FBI_INJECTABLE_ZIP+".torrent");
		rename = new File(hackingDir, MagicConstants.FBI_INJECTABLE_ZIP);
		resources.add(new HackingResourceTorrent(HackingPath.URLS.getProperty(MagicPropKeys.FBI_INJECTABLE_KEY), out,
				rename, MagicTags.FBI_INJECTABLE_TAG, basedir));
		return resources;
	}

}
