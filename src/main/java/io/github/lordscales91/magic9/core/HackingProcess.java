package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.constants.MagicConstants;
import io.github.lordscales91.magic9.domain.HackingStep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class HackingProcess {
	protected String hackingDir;
	protected String sdCardDir;
	
	protected HackingProcess(String hackingDir, String sdCardDir) {
		this.hackingDir = hackingDir;
		this.sdCardDir = sdCardDir;
	}
	
	protected boolean isSDCardOk() {
		return sdCardDir != null && new File(sdCardDir).isDirectory()
				&& new File(sdCardDir).getTotalSpace() >= MagicConstants.SD_MIN_SIZE;
	}
	
	public static HackingProcess getInstance(HackingStep step, String hackingDir, String sdCardDir) {
		if(HackingStep.DECRYPT9_BROWSER.equals(step)) {
			return new Decrypt9BrowserProcess(hackingDir, sdCardDir);
		} else if(HackingStep.HOMEBREW_SOUNDHAX.equals(step)) {
			return new HomebrewProcess(hackingDir, sdCardDir);
		} else if(HackingStep.DECRYPT9_HOMEBREW.equals(step)) {
			return new Decrypt9HomebrewProcess(hackingDir, sdCardDir);
		} else if(HackingStep.DECRYPT9_MSET.equals(step)) {
			return new Decrypt9MSETProcess(hackingDir, sdCardDir);
		} else if(HackingStep.CTRTRANSFER_210.equals(step)) {
			return new CTRTranfer210Process(hackingDir, sdCardDir);
		} else if(HackingStep.INSTALL_ARM9LOADERHAX.equals(step)) {
			return new InstallARM9Process(hackingDir, sdCardDir);
		} else if(HackingStep.BOOT9STRAP_BROWSER.equals(step)) {
		    return new Boot9BrowserProcess(hackingDir, sdCardDir);
		} else if(HackingStep.BOOT9STRAP_HOMEBREW.equals(step)) {
		    return new Boot9HomebrewProcess(hackingDir, sdCardDir);
		}
		return null;
	}
	
	public static HackingProcess getInstance(HackingStep step, File hackingDir, File sdCardDir) {
		return getInstance(step, hackingDir.getAbsolutePath(), sdCardDir.getAbsolutePath());
	}
	
	public static HackingProcess getInstance(HackingStep step, File hackingDir, String sdCardDir) {
		return getInstance(step, hackingDir.getAbsolutePath(), sdCardDir);
	}
	
	public static HackingProcess getInstance(HackingStep step, String hackingDir, File sdCardDir) {
		return getInstance(step, hackingDir, sdCardDir.getAbsolutePath());
	}
	
	public void preProcess() {
		if(!isSDCardOk()) {
			throw new IllegalStateException("SD Card is not initialized or is not valid");
		}
	}
	
	public abstract void process() throws IOException;
	
	/**
	 * Indicates whether is safe to stop the process here or not.
	 */
	public abstract boolean isSafeToPause();
	
	public abstract List<HackingResource> getRequiredResources();
}
