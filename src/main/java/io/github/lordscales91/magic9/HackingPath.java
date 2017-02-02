package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.HackingProcess;
import io.github.lordscales91.magic9.core.HackingResource;
import io.github.lordscales91.magic9.core.MagicConstants;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class HackingPath {
	
	private static HackingPath INSTANCE;
	private static final FirmwareVersion[][] O3DS_RANGES = new FirmwareVersion[][]{
		// From, To
		new FirmwareVersion[]{
			// No Browser | Browser
			// Update
			new FirmwareVersion(1, 0, 0), new FirmwareVersion(1, 1, 0)
		},
		new FirmwareVersion[]{
			// Update | install arm9loaderhax
			new FirmwareVersion(2, 1, 0), new FirmwareVersion(2, 1, 0)
		},
		new FirmwareVersion[]{
			// Update
			new FirmwareVersion(2, 2, 0), new FirmwareVersion(3, 1, 0)
		},
		new FirmwareVersion[]{
			// Decrypt9(MSET) | Decrypt9(Browser)
			new FirmwareVersion(4, 0, 0), new FirmwareVersion(4, 5, 0)
		},
		new FirmwareVersion[]{
			// Update | Decrypt9(Browser)
			new FirmwareVersion(5, 0, 0), new FirmwareVersion(5, 1, 0)
		},
		new FirmwareVersion[]{
			// Decrypt9(MSET) | Decrypt9(Browser)
			new FirmwareVersion(6, 0, 0), new FirmwareVersion(6, 3, 0)
		},
		new FirmwareVersion[]{
			// Update | Decrypt9(Browser)
			new FirmwareVersion(7, 0, 0), new FirmwareVersion(8, 1, 0)
		},
		new FirmwareVersion[]{
			// Homebrew Launcher(Soundhax)
			new FirmwareVersion(9, 0, 0), new FirmwareVersion(11, 2, 0)
		}
	};
	private static final FirmwareVersion[][] N3DS_RANGES = new FirmwareVersion[][]{
		new FirmwareVersion[]{
			// NTR and Cubic Ninja (JPN Only). Otherwise an update is required.
			new FirmwareVersion(8, 1, 0), new FirmwareVersion(8, 1, 0)
		},
		new FirmwareVersion[]{
			// Homebrew Launcher(Soundhax)
			new FirmwareVersion(9, 0, 0), new FirmwareVersion(11, 2, 0)
		}
	};
	
	public static final Properties URLS = loadURLs();
	
	private static Properties loadURLs() {
		Properties props = new Properties();
		try {
			FileReader fr = new FileReader("data/urls.properties");
			props.load(fr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
	
	private List<HackingStep> steps = new ArrayList<HackingStep>();
	private int currentStep;
	private FirmwareVersion fwver;
	private boolean cartUpdated;
	
	public static HackingPath resolve(FirmwareVersion fwver, boolean cartUpdated) {
		if(fwver == null) {
			throw new IllegalArgumentException("Firmware was not specified");
		}
		if(INSTANCE == null) {
			INSTANCE = new HackingPath();
		}		
		INSTANCE.setFwver(fwver);
		INSTANCE.setCartUpdated(cartUpdated);
		INSTANCE.init();
		return INSTANCE;
	}
	
	public static HackingPath getInstance() {
		if(INSTANCE == null) {
			throw new IllegalStateException("HackingPath has not been initialized");
		}
		return INSTANCE;
	}

	private HackingPath(){}
	
	private void init() {
		if(MagicConstants.O3DS.equals(this.fwver.getModel())) {
			int range = -1;
			for(int i=0;i<O3DS_RANGES.length;i++) {
				FirmwareVersion from = O3DS_RANGES[i][0];
				FirmwareVersion to = O3DS_RANGES[i][1];
				if(this.fwver.gte(from) && this.fwver.lte(to)) {
					range = i;
					i = O3DS_RANGES.length;
				}
			}
			boolean usablebrowser = false;
			if(this.fwver.getBrowser() > 25) {
				usablebrowser = true;
			} else if(this.fwver.getBrowser() > 0) {
				if(this.wasCartUpdated()) {
					if(this.fwver.lt("9.9.0")) {
						usablebrowser = true;
					}
				} else {
					usablebrowser = true;
				}
			}
			
			switch(range) {
				case 0:
				case 2:
					steps.add(HackingStep.REQUIRES_UPDATE);
					break;
				case 1:
					if(usablebrowser) {
						// Proceed directly to install arm9loaderhax
						steps.add(HackingStep.INSTALL_ARM9LOADERHAX);
					} else {
						steps.add(HackingStep.REQUIRES_UPDATE);
					}
					break;
				case 3:
				case 5:
					if(usablebrowser) {
						// Proceed to launch Decrypt9 with this hax
						steps.add(HackingStep.DECRYPT9_BROWSER);
					} else {
						// Use mset hax
						steps.add(HackingStep.DECRYPT9_MSET);						
					}
					steps.add(HackingStep.CTRTRANSFER_210);
					steps.add(HackingStep.INSTALL_ARM9LOADERHAX);
					break;
				case 4:
				case 6:
					if(usablebrowser) {
						// Proceed to launch Decrypt9 with this hax
						steps.add(HackingStep.DECRYPT9_BROWSER);
						steps.add(HackingStep.CTRTRANSFER_210);
						steps.add(HackingStep.INSTALL_ARM9LOADERHAX);
					} else {
						// Require an update
						steps.add(HackingStep.REQUIRES_UPDATE);						
					}
					break;
				case 7:
					steps.add(HackingStep.HOMEBREW_SOUNDHAX);
					steps.add(HackingStep.DECRYPT9_HOMEBREW);
					steps.add(HackingStep.CTRTRANSFER_210);
					steps.add(HackingStep.INSTALL_ARM9LOADERHAX);
					break;
				default:
					// TODO: handle versions out of any range. Are they possible in first place?
					break;
			}
		} else if(MagicConstants.N3DS.equals(this.fwver.getModel())) {
			int range = -1;
			for(int i=0;i<N3DS_RANGES.length;i++) {
				FirmwareVersion from = N3DS_RANGES[i][0];
				FirmwareVersion to = N3DS_RANGES[i][1];
				if(this.fwver.gte(from) && this.fwver.lte(to)) {
					range = i;
					i = N3DS_RANGES.length;
				}
			}
			if(ConsoleRegion.JPN.equals(this.getFwver().getRegion()) && range == 0) {
				// TODO: Should we consider the Cubic Ninja option?
				steps.add(HackingStep.REQUIRES_UPDATE);
			} else if(range == 1) {
				steps.add(HackingStep.HOMEBREW_SOUNDHAX);
				steps.add(HackingStep.DECRYPT9_HOMEBREW);
				steps.add(HackingStep.CTRTRANSFER_210);
				steps.add(HackingStep.INSTALL_ARM9LOADERHAX);
			} else if(range < 0) {
				// TODO: handle versions out of any range. Are they possible in first place?
			} else {
				steps.add(HackingStep.REQUIRES_UPDATE);
			}
		}
	}
	
	public List<HackingResource> resolveResources(String hackingDir) {
		// Use set to avoid duplicates
		Set<HackingResource> resourceSet = new HashSet<HackingResource>();
		String sdCardDir = null; // We do not need the SD to retrieve the resources
		for(HackingStep step:steps) {
			HackingProcess proc = HackingProcess.getInstance(step, hackingDir, sdCardDir);
			if(proc == null) {
				continue;
			}
			List<HackingResource> res = proc.getRequiredResources();
			if(res != null) {
				resourceSet.addAll(res);
			}
		}	
		return new ArrayList<HackingResource>(resourceSet);
	}

	public boolean requiresUpdate() {
		return steps.size() == 1 && HackingStep.REQUIRES_UPDATE.equals(steps.get(0));
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

	public FirmwareVersion getFwver() {
		return fwver;
	}

	public void setFwver(FirmwareVersion fwver) {
		this.fwver = fwver;
	}

	public boolean wasCartUpdated() {
		return cartUpdated;
	}

	public void setCartUpdated(boolean cartUpdate) {
		this.cartUpdated = cartUpdate;
	}

}
