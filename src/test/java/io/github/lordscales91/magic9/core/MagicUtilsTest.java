package io.github.lordscales91.magic9.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.github.lordscales91.magic9.FirmwareVersion;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class MagicUtilsTest {
	
	public static Properties config = new Properties();
	private static File sdcarddir;
	
	@BeforeClass
	public static void setUp()  {
		File propFile = new File("data/local_test.properties");
		if(propFile.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(propFile);
				config.load(fis);
			} catch(IOException e)  {
				System.err.println(e.getMessage());
			} finally {
				IOUtils.closeQuietly(fis);
			}
			sdcarddir = new File("target/test-results/sdcard");
			if(!sdcarddir.exists()) {
				sdcarddir.mkdirs();
			}
		}
		
	}

	@Test
	public void testGetOtherAppURL() {
		FirmwareVersion fw = new FirmwareVersion("10.5.0-17E");
		fw.setModel(MagicConstants.O3DS);
		String expected = MagicConstants.OTHERAPP_BASE_URL + "POST5_E_23554_9221.bin";
		String actual = MagicUtils.getOtherAppURL(fw);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetOtherAppURL2() {
		FirmwareVersion fw = new FirmwareVersion("11.0.0-30K");
		fw.setModel(MagicConstants.N3DS);
		String expected = MagicConstants.OTHERAPP_BASE_URL + "N3DS_K_12288_kor_9221.bin";
		String actual = MagicUtils.getOtherAppURL(fw);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetSoundhaxFilename() {
		FirmwareVersion fw = new FirmwareVersion("10.5.0-17E");
		fw.setModel(MagicConstants.O3DS);
		String expected = "soundhax-eur-o3ds.m4a";
		String actual = MagicUtils.getSoundhaxFilename(fw);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetCTRImageName() {
		FirmwareVersion fw = new FirmwareVersion("10.5.0-17E");
		fw.setModel(MagicConstants.O3DS);
		String expected = "2.1.0-4E_ctrtransfer_o3ds.bin";
		String actual = MagicUtils.getCTR210ImageName(fw);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExtract7Zip() {
		File hackingDir = new File(config.getProperty("hackingdir"));
		File a7zip = new File(hackingDir, MagicConstants.SAFE9HLINSTALLER_7Z);
		File output = new File(sdcarddir.getParentFile(), "extract-test");
		try {
			MagicUtils.extract7ZipFile(a7zip, output);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
