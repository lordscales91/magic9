package io.github.lordscales91.magic9.domain;

public enum HackingStep {
	DECRYPT9_BROWSER, DECRYPT9_MSET, DECRYPT9_HOMEBREW, HOMEBREW_SOUNDHAX,
	NFIRM_DOWNGRADE, NAND_BACKUP, CTRTRANSFER_210, REQUIRES_UPDATE, INSTALL_ARM9LOADERHAX;

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
		}
		return result;
	};
}
