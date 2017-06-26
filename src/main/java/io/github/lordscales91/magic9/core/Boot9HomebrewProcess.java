package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.constants.MagicPropKeys;
import io.github.lordscales91.magic9.constants.MagicTags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Boot9HomebrewProcess extends HackingProcess {

    protected Boot9HomebrewProcess(String hackingDir, String sdCardDir) {
        super(hackingDir, sdCardDir);
    }

    @Override
    public void process() throws IOException {
        // Prepare the materials
        // Extract boot.firm from Luma3DS to its folder
        File luma3dsFolder = new File(hackingDir, MagicConstants.LUMA3DS_FOLDER);
        File lumaBootFirm = new File(luma3dsFolder, "boot.firm");
        MagicUtils.extractFileFrom7Zip(new File(hackingDir, MagicConstants.LUMA3DS_7Z), 
                    "boot.firm", lumaBootFirm);
        // Extract boot9strap
        File boot9strapDir = new File(hackingDir, MagicConstants.BOOT9STRAP_FOLDER);
        MagicUtils.extractZipFile(new File(hackingDir, MagicConstants.BOOT9STRAP_ZIP), boot9strapDir);
        File installerDir = new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_FOLDER);
        String installerBin = "SafeB9SInstaller.bin";
        File installerBinFile = new File(installerDir, installerBin);
        MagicUtils.extractFileFromZip(new File(hackingDir, MagicConstants.SAFEB9S_INSTALLER_ZIP), 
            installerBin, installerBinFile);
        File haxesDir = new File(hackingDir, MagicConstants.HAXES_DIR);
        // Prepare the SD
        // Copy boot.firm to the SD Card root
        MagicUtils.copyFile(lumaBootFirm, new File(sdCardDir, "boot.firm"));
        // Create boot9strap folder
		File boot9strapSDDir = new File(sdCardDir, MagicConstants.BOOT9STRAP_FOLDER);
		if(!boot9strapSDDir.exists()) {
		    boot9strapSDDir.mkdirs();
		}
		// Copy boot9strap files
		MagicUtils.copyDirectory(boot9strapDir, boot9strapSDDir);
		// Copy haxes
		File $3dsdir = new File(sdCardDir, "3ds");
		MagicUtils.copyDirectory(haxesDir, $3dsdir);
		// Copy Safe9BSInstaller files
		MagicUtils.copyFile(installerBinFile, new File(sdCardDir, "safehaxpayload.bin"));
        
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
		
		File haxesDir = new File(hackingDir, MagicConstants.HAXES_DIR);
		out = new File(haxesDir, "safehax.3dsx");
		url = HackingPath.URLS.getProperty(MagicPropKeys.SAFEHAX_KEY);
		resources.add(new HackingResourceGithub(url, out, MagicTags.SAFEHAX_TAG, ".3dsx"));
		
		out = new File(haxesDir, "udsploit.3dsx");
		url = HackingPath.URLS.getProperty(MagicPropKeys.UDSPLOIT_KEY);
		resources.add(new HackingResourceGithub(url, out, MagicTags.UDSPLOIT_TAG, ".3dsx"));
        return resources;
    }
}
