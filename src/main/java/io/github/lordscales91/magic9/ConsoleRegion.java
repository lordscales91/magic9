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
		if(this.name().equals(OTHER.name())) {
			return 'X';
		} else {
			return this.name().charAt(0);
		}
	}
}
