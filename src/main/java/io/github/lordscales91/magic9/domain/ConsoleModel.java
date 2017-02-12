package io.github.lordscales91.magic9.domain;

public enum ConsoleModel {
	O3DS_GENERIC, N3DS_GENERIC, // Added for compatibility
	O2DS, O3DS, O3DSXL, N3DS, N3DSXL;
	
	public static ConsoleModel fromModelType(String model) {
		for(ConsoleModel cm:ConsoleModel.values()) {
			if(cm.modelType().equals(model)) {
				return cm;
			}
		}
		return null;
	}
	
	/**
	 * Returns <code>true</code> if this value represents a New 3DS model
	 * and <code>false</code> if it's an Old 3DS model
	 */
	public boolean isNew() {
		return this.name().startsWith("N");
	}
	
	public String modelType() {
		if(O3DS.equals(this)) {
			return "O3DS";
		} else if(O3DSXL.equals(this)) {
			return "O3DSXL";
		} else if(O2DS.equals(this)) {
			return "O2DS";
		} else if(N3DS.equals(this)) {
			return "N3DS";
		} else if(N3DSXL.equals(this)) {
			return "N3DSXL";
		} else if(O3DS_GENERIC.equals(this)) {
			return "OLD";
		} else if(N3DS_GENERIC.equals(this)) {
			return "NEW";
		}
		return "UKN";
	}
	
	public String displayName() {
		if(O3DS.equals(this)) {
			return "3DS (Old)";
		} else if(O3DSXL.equals(this)) {
			return "3DS XL";
		} else if(O2DS.equals(this)) {
			return "2DS";
		} else if(N3DS.equals(this)) {
			return "New 3DS";
		} else if(N3DSXL.equals(this)) {
			return "New 3DS XL";
		} else if(O3DS_GENERIC.equals(this)) {
			return "Old 3DS";
		} else if(N3DS_GENERIC.equals(this)) {
			return "New 3DS";
		}
		return "Unknown";
	}
}
