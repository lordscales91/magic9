package io.github.lordscales91.magic9.core;

import static org.junit.Assert.*;
import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.domain.FirmwareVersion;
import io.github.lordscales91.magic9.domain.HackingStep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class HackingProcessTest {
	
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
	
	@After
	public void dummyTearDown(){}
	
	// Ideally each test should start with a clean state.
	// But with these tests it might be useful to check the files manually. 
//	@After
//	public void tearDown() {
//		try {
//			if(sdcarddir.isDirectory()) {
//				MagicUtils.clearDirectory(sdcarddir);
//				sdcarddir.delete();
//			}			
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
//	}
	
	
	@Test
	public void testDecrypt9BrowserProcess() {
		String hackingDir = config.getProperty("hackingdir");
		HackingProcess d9proc = HackingProcess.getInstance(HackingStep.DECRYPT9_BROWSER, 
				hackingDir, sdcarddir);
		assertNotNull("Hacking process couldn't be instantiated", d9proc);
		assertTrue("Wrong class", d9proc instanceof Decrypt9BrowserProcess);
		try {
			d9proc.process();
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testHomebrewProcess() {
		// First we need to initialize the hacking path
		FirmwareVersion ver = new FirmwareVersion("11.1.0-26E");
		HackingPath.resolve(ver, false);
		String hackingDir = config.getProperty("hackingdir");
		HackingProcess proc = HackingProcess.getInstance(HackingStep.HOMEBREW_SOUNDHAX, hackingDir, sdcarddir);
		assertNotNull("Hacking process couldn't be instantiated", proc);
		assertTrue("Wrong class", proc instanceof HomebrewProcess);
		try {
			proc.process();
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInstallARM9() {
		// First we need to initialize the hacking path
		FirmwareVersion ver = new FirmwareVersion("11.1.0-26E");
		HackingPath.resolve(ver, false);
		String hackingDir = config.getProperty("hackingdir");
		HackingProcess proc = HackingProcess.getInstance(HackingStep.INSTALL_ARM9LOADERHAX, hackingDir, sdcarddir);
		assertNotNull("Hacking process couldn't be instantiated", proc);
		assertTrue("Wrong class", proc instanceof InstallARM9Process);
		try {
			proc.process();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRequiredResourcesDecrypt9() {
		// First we need to initialize the hacking path
		FirmwareVersion ver = new FirmwareVersion("11.1.0-26E");
		HackingPath.resolve(ver, false);
		String hackingDir = config.getProperty("hackingdir");	
		HackingProcess proc = HackingProcess.getInstance(HackingStep.DECRYPT9_BROWSER, hackingDir, sdcarddir);
		assertEquals(1, proc.getRequiredResources().size());
		HackingResource res = proc.getRequiredResources().get(0);
		assertTrue(res instanceof HackingResourceGithub);
	}

}
