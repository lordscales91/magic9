package io.github.lordscales91.magic9.core;

import static org.junit.Assert.*;
import io.github.lordscales91.magic9.domain.FirmwareVersion;

import org.junit.Before;
import org.junit.Test;

public class FirmwareVersionTest {

	private FirmwareVersion ver610;

	@Before
	public void setUp() throws Exception {
		ver610 = new FirmwareVersion(6, 1, 0);
	}

	@Test
	public void testLtFirmwareVersion() {
		assertTrue(ver610.lt(new FirmwareVersion(7, 0, 0)));
	}

	@Test
	public void testLtString() {
		assertTrue(ver610.lt("7.0.0"));
	}

	@Test
	public void testSystemVersionEqualFirmwareVersion() {
		assertTrue(ver610.systemVersionEqual(new FirmwareVersion("6.1.0")));
	}
	
	@Test
	public void testSystemVersionEqualFirmwareVersionFalse() {
		assertFalse(ver610.systemVersionEqual(new FirmwareVersion("7.1.0")));
	}

	@Test
	public void testLteFirmwareVersion() {
		assertTrue(ver610.lte(new FirmwareVersion("7.1.0")));
	}

	@Test
	public void testGt() {
		assertTrue(ver610.gt(new FirmwareVersion("5.1.0")));
	}
}
