package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.constants.MagicTags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Boot9BrowserProcess extends HackingProcess {

    protected Boot9BrowserProcess(String hackingDir, String sdCardDir) {
        super(hackingDir, sdCardDir);
    }

    @Override
    public void process() throws IOException {
        // Extract the resources
        // Extract boot.firm from Luma3DS to its folder
        File luma3dsFolder = new File(hackingDir, MagicConstants.LUMA3DS_FOLDER);
        File lumaBootFirm = new File(luma3dsFolder, "boot.firm");
        MagicUtils.extractFileFrom7Zip(new File(hackingDir, MagicConstants.LUMA3DS_7Z), 
                    "boot.firm", lumaBootFirm);
        // Extract the homebrew starter kit
        File starterkit = new File(hackingDir, MagicConstants.STARTER_KIT_FOLDER);
		MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.STARTER_KIT_ZIP), starterkit);
		// Extract boot9strap
        File boot9strapDir = new File(hackingDir, MagicConstants.BOOT9STRAP_FOLDER);
        MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.BOOT9STRAP_ZIP), boot9strapDir);
        // Extract SafeB9SInstaller files
        File installerDir = new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_FOLDER);
        String installerDat = "SafeB9SInstaller.dat";
        File installerDatFile = new File(installerDir, installerDat);
        String launcherDat = "Launcher.dat";
        File launcherDatFile = new File(installerDir, launcherDat);
        MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_ZIP), 
                    launcherDat, launcherDatFile);
        MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_ZIP), 
            installerDat, installerDatFile);
		
		// Copy to SD Card
		// Copy boot.firm to SD Card root
		MagicUtils.copyFile(lumaBootFirm, new File(sdCardDir, "boot.firm"));
		// The homebrew starter kit to the root
		MagicUtils.copyDirectory(starterkit, new File(sdCardDir));
		// Create boot9strap folder
		File boot9strapSDDir = new File(sdCardDir, MagicConstants.BOOT9STRAP_FOLDER);
		if(!boot9strapSDDir.exists()) {
		    boot9strapSDDir.mkdirs();
		}
		// Copy boot9strap files
		MagicUtils.copyDirectory(boot9strapDir, boot9strapSDDir);
		// Copy SafeB9SInstaller files
		MagicUtils.copyFile(installerDatFile, new File(sdCardDir, installerDat));
		MagicUtils.copyFile(launcherDatFile, new File(sdCardDir, launcherDat));
    }

    @Override
    public boolean isSafeToPause() {
        return false;
    }

    @Override
    public List<HackingResource> getRequiredResources() {
        List<HackingResource> resources = new ArrayList<>();
        String url = HackingPath.URLS.getProperty(MagicPropKeys.SAFEB9S_INSTALLER);
        File out = new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_ZIP);
        resources.add(new HackingResourceGithub(url, out, MagicTags.SAFEB9S_TAG, ".zip"));
        url = HackingPath.URLS.getProperty(MagicPropKeys.BOOT9STRAP);
        out = new File(hackingDir, MagicConstants.BOOT9STRAP_ZIP);
        resources.add(new HackingResourceGithub(url, out, MagicTags.BOOT9STRAP_TAG, null, 
                        MagicConstants.BOOT9S_STANDARD_REGEX));
        url = HackingPath.URLS.getProperty(MagicPropKeys.LUMA3DS_KEY);
        out = new File(hackingDir, MagicConstants.LUMA3DS_7Z);
		resources.add(new HackingResourceGithub(url, out, MagicTags.LUMA3DS_TAG, ".7z"));
		url = HackingPath.URLS.getProperty(MagicPropKeys.STARTER_KIT_KEY);
		out = new File(hackingDir, MagicConstants.STARTER_KIT_ZIP);
		resources.add(new HackingResource(url, out, MagicTags.STARTER_KIT_TAG));
        return resources;
    }
    
}
