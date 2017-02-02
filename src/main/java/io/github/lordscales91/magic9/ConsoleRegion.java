package io.github.lordscales91.magic9;

public enum ConsoleRegion {
	EUR, JPN, USA, KOR, OTHER;
	
	public static ConsoleRegion fromFirmware(char letter) {
		switch(letter) {
			case 'E':
				return EUR;
			case 'U':
				return USA;
			case 'J':
				return JPN;
			case 'K':
				return KOR;
			default:
				return OTHER;
		}
	}
	public static ConsoleRegion fromFirmware(String letter) {
		return fromFirmware(letter.toUpperCase().charAt(0));
	}
	
	public char toLetter() {
		if(OTHER.equals(this)) {
			return 'X';
		} else {
			return this.name().charAt(0);
		}
	}
	/**
	 * Returns a human-readable name for this region.
	 */
	public String displayName() {
		String name = "Unknown";
		if(EUR.equals(this)) {
			name = "Europe";
		} else if(JPN.equals(this)) {
			name = "Japan";
		} else if(USA.equals(this)) {
			name = "USA";
		} else if(KOR.equals(this)) {
			name = "Korea";
		}
		return name;
	}
}
