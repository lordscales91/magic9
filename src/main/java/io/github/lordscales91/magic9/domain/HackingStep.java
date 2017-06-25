package io.github.lordscales91.magic9.domain;

public enum HackingStep {
	DECRYPT9_BROWSER, DECRYPT9_MSET, DECRYPT9_HOMEBREW, HOMEBREW_SOUNDHAX,
	NFIRM_DOWNGRADE, NAND_BACKUP, CTRTRANSFER_210, REQUIRES_UPDATE, INSTALL_ARM9LOADERHAX, 
	NO_OPTION,
	BOOT9STRAP_BROWSER, BOOT9STRAP_MSET, BOOT9STRAP_2XRSA, BOOT9STRAP_HOMEBREW, 
	BOOT9STRAP_FINAL_SETUP;

	public String displayName()  {
		String display = "Unknown";
		if(this.equals(HOMEBREW_SOUNDHAX)) {
			display = "Homebrew Launcher (Soundhax)";
		} else if(this.equals(DECRYPT9_HOMEBREW)) {
			display = "Decrypt9 (Homebrew Launcher)";
		} else if(this.equals(DECRYPT9_BROWSER)) {
			display = "Decrypt9 (Browser)";
		} else if(this.equals(DECRYPT9_MSET)) {
			display = "Decrypt9 (MSET)";
		} else if(this.equals(CTRTRANSFER_210)) {
			display = "2.1.0 ctrtransfer";
		} else if(this.equals(INSTALL_ARM9LOADERHAX)) {
			display = "Installing arm9loaderhax";
		} else if(this.equals(NAND_BACKUP)) {
			display = "NAND Backup with Decrypt9";
		} else if(this.equals(BOOT9STRAP_2XRSA)) {
			display = "Boot9strap (2xrsa)";
		} else if(this.equals(BOOT9STRAP_BROWSER)) {
			display = "Boot9strap (Browser)";
		} else if(this.equals(BOOT9STRAP_MSET)) {
			display = "Boot9strap (MSET)";
		} else if(this.equals(BOOT9STRAP_HOMEBREW)) {
			display = "Boot9strap (Homebrew)";
		} else if(this.equals(BOOT9STRAP_FINAL_SETUP)) {
			display = "Finalizing Setup";
		}
		return display;
	}
	
	/**
	 * Returns to corresponding URL part for the <a href="http://3ds.guide">3ds.guide</a>
	 * step.
	 */
	public String getURLPart() {
		String result = "/unknown";
		if(this.equals(DECRYPT9_BROWSER)) {
			result = "/decrypt9-(browser)";
		} else if(this.equals(DECRYPT9_MSET)) {
			result = "/decrypt9-(mset)";
		} else if(this.equals(DECRYPT9_HOMEBREW)) {
			result = "/decrypt9-(homebrew-launcher)";
		} else if(this.equals(HOMEBREW_SOUNDHAX)) {
			result = "/homebrew-launcher-(soundhax)";
		} else if(this.equals(CTRTRANSFER_210)) {
			result = "/2.1.0-ctrtransfer";
		} else if(this.equals(INSTALL_ARM9LOADERHAX)) {
			result = "/installing-arm9loaderhax";
		} else if(this.equals(BOOT9STRAP_2XRSA)) {
			result = "/installing-boot9strap-(2xrsa)";
		} else if(this.equals(BOOT9STRAP_MSET)) {
			result = "/installing-boot9strap-(mset)";
		} else if(this.equals(BOOT9STRAP_BROWSER)) {
			result = "/installing-boot9strap-(browser)";
		} else if(this.equals(BOOT9STRAP_HOMEBREW)) {
			result = "/installing-boot9strap-(homebrew-launcher)";
		} else if(this.equals(BOOT9STRAP_FINAL_SETUP)) {
			result = "/finalizing-setup";
		}
		return result;
	};
}
