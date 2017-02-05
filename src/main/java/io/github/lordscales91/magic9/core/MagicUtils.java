package io.github.lordscales91.magic9.core;

import io.github.lordscales91.magic9.HackingPath;
import io.github.lordscales91.magic9.domain.ConsoleRegion;
import io.github.lordscales91.magic9.domain.FirmwareVersion;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class MagicUtils {

	private MagicUtils() {
	}

	/**
	 * This calls {@link IOUtils#copy(InputStream, OutputStream)}
	 * 
	 * @param is
	 *            The input stream to read from
	 * @param ous
	 *            The output stream to write to
	 * @throws IOException
	 */
	public static void streamCopy(InputStream is, OutputStream ous)
			throws IOException {
		IOUtils.copy(is, ous);
	}

	public static String getOtherAppURL(FirmwareVersion ver) {
		return MagicConstants.OTHERAPP_BASE_URL + getOtherAppFilename(ver);
	}
	
	public static String getOtherAppFilename(FirmwareVersion ver) {
		return getPayloadFilename(true, ver);
	}
	
	private static String getPayloadFilename(boolean isOtherApp, FirmwareVersion ver) {
		// Actually, the ropbin filenames are the same, only changes the base URL
		StringBuilder sb = new StringBuilder();
		if (MagicConstants.N3DS.equals(ver.getModel())) {
			sb.append("N3DS");
		} else if (MagicConstants.O3DS.equals(ver.getModel())) {
			if (ver.getMajor() < 5) {
				sb.append("PRE5");
			} else {
				sb.append("POST5");
			}
		}
		sb.append('_');
		sb.append(ver.getRegion().toLetter());
		sb.append('_');
		appendMenuVersion(sb, ver);
		sb.append('_');
		if (ver.getMajor() == 9 && ver.getMinor() < 6) {
			sb.append("8203");
		} else {
			sb.append("9221");
		}
		sb.append(".bin");
		return sb.toString();
	}

	private static void appendMenuVersion(StringBuilder sb, FirmwareVersion ver) {
		if (ConsoleRegion.KOR.equals(ver.getRegion())) {
			if (ver.getMajor() == 9) {
				if (ver.getMinor() == 6) {
					sb.append("6166_kor");
				} else if (ver.getMinor() > 6) {
					sb.append("7175_kor");
				}
			} else if (ver.getMajor() == 10) {
				if (ver.getMinor() == 0) {
					sb.append("7175_kor");
				} else if (ver.getMinor() == 1) {
					sb.append("8192_kor");
				} else if (ver.getMinor() == 2) {
					sb.append("9216_kor");
				} else if (ver.getMinor() == 3) {
					sb.append("10240_kor");
				} else if (ver.getMinor() >= 6) {
					sb.append("12288_kor");
				} else if (ver.getMinor() >= 4) {
					sb.append("11266_kor");
				}
			} else if (ver.getMajor() == 11) {
				if (ver.getMinor() == 0) {
					sb.append("12288_kor");
				} else if (ver.getMinor() == 1 || ver.getMinor() == 2) {
					sb.append("13312_kor");
				}
			}
		} else {
			if (ver.getMajor() == 9) {
				if (ver.getMinor() == 0 || ver.getMinor() == 1) {
					sb.append("11272");
				} else if (ver.getMinor() == 2) {
					sb.append("12288");
				} else if (ver.getMinor() == 3) {
					sb.append("13330");
				} else if (ver.getMinor() == 4) {
					sb.append("14336");
				} else if (ver.getMinor() == 5) {
					sb.append("15360");
				} else if (ver.getMinor() == 6) {
					sb.append("16404");
				} else if (ver.getMinor() == 7) {
					sb.append("17415");
				} else if (ver.getMinor() == 9
						&& ConsoleRegion.USA.equals(ver.getRegion())) {
					sb.append("20480_usa");
				} else if (ver.getMinor() >= 8) {
					sb.append("19456");
				}
			} else if (ver.getMajor() == 10) {
				if (ver.getMinor() == 0) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("20480_usa");
					} else {
						sb.append("19456");
					}
				} else if (ver.getMinor() == 1) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("21504_usa");
					} else {
						sb.append("20480");
					}
				} else if (ver.getMinor() == 2) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("22528_usa");
					} else {
						sb.append("21504");
					}
				} else if (ver.getMinor() == 3) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("23552_usa");
					} else {
						sb.append("22528");
					}
				} else if (ver.getMinor() == 4 || ver.getMinor() == 5) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("24578_usa");
					} else {
						sb.append("23554");
					}
				} else if (ver.getMinor() >= 6) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("25600_usa");
					} else {
						sb.append("24576");
					}
				}
			} else if (ver.getMajor() == 11) {
				if (ver.getMinor() == 0) {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("25600_usa");
					} else {
						sb.append("24576");
					}
				} else {
					if (ConsoleRegion.USA.equals(ver.getRegion())) {
						sb.append("26624_usa");
					} else {
						sb.append("25600");
					}
				}
			}

		}
	}
	
	public static String getSoundhaxFilename(FirmwareVersion ver) {
		String model = (MagicConstants.N3DS.equals(ver.getModel()))?"n3ds":"o3ds";
		return String.format("soundhax-%s-%s.m4a", ver.getRegion().toString().toLowerCase(), model);
	}
	public static String getSoundhaxURL(FirmwareVersion ver) {
		return MagicConstants.SOUNDHAX_BASE_URL + getSoundhaxFilename(ver)+"?raw=true";
	}
	
	/**
	 * Returns the name of the CTR Image
	 */
	public static String getCTR210ImageName(FirmwareVersion ver) {
		return getCTR210Name(ver) + ".bin";
	}
	
	/**
	 * Returns the name of the CTR Image Sha file
	 */
	public static String getCTR210ImageShaFile(FirmwareVersion ver) {
		return getCTR210ImageName(ver) + ".sha";
	}
	
	/**
	 * Returns the name of the zip file that contains the CTR Image
	 */
	public static String getCTR210ZipName(FirmwareVersion ver) {
		return getCTR210Name(ver)+".zip";
	}
	
	private static String getCTR210Name(FirmwareVersion ver) {
		String model = "o3ds"; // New 3DS uses same image
		return getCTRName(MagicConstants.CTR_VERSION_210, ver.getRegion().toLetter(), model);
	}
	
	private static String getCTRName(String version, char region, String model) {
		return String.format("%s%c_ctrtransfer_%s", version, region, model);
	}
	
	/**
	 * Copies the directory contents to the target directory ensuring the files
	 * were copied successfully by comparing their checksums.
	 * It delegates the actual copy of files to {@link MagicUtils#copyFile(File, File)} 
	 * which will actually compare the checksums
	 */
	public static void copyDirectory(File srcdir, File dstdir) throws IOException {
		if(!dstdir.exists()) {
			dstdir.mkdirs();
		}
		for(File f: srcdir.listFiles()) {
			File out = new File(dstdir, f.getName());
			if(f.isDirectory()) {
				copyDirectory(f, out);
			} else {
				copyFile(f, out);
			}
		}
	}
	
	/**
	 * This is a wrapper for {@link FileUtils#copyFile(File, File)} which computes the 
	 * checksums to ensure the copy was successful.
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		FileUtils.copyFile(srcFile, destFile);
		if(FileUtils.checksumCRC32(srcFile) != FileUtils.checksumCRC32(destFile)) {
			throw new IOException("Copy possibly failed. Checksums do not match");
		}
	}
	
	/**
	 * This is actually just a wrapper for {@link FileUtils#cleanDirectory(File)}.
	 * The wrapper is here to allow an easy transition to another library if necessary.
	 */
	public static void clearDirectory(File directory) throws IOException {
		FileUtils.cleanDirectory(directory);;
	}
	
	/**
	 * Extracts a single file from the Zip file
	 * @param zipfile The zip file to extract
	 * @param filename The entry name to extract
	 * @param destination The location to extract the entry
	 * @throws IOException
	 */
	public static void extractFileFromZip(File zipfile, String filename, File destination) throws IOException {
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipfile);
			ZipArchiveEntry entry = zip.getEntry(filename);
			if(entry == null) {
				throw new IOException("Entry not found: " + filename);
			}
			InputStream is = zip.getInputStream(entry);
			FileUtils.copyInputStreamToFile(is, destination);
			//  Check correct extraction
			if(FileUtils.checksumCRC32(destination) != entry.getCrc()) {
				throw new IOException("Extraction possibly failed. Checksums do not match");
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}
	}
	
	/**
	 * Extracts the contents of a Zip file to the specified output directory.
	 * The output directory is created if it doesn't exists. 
	 * @param zipfile The zip file to extract
	 * @param outputdir The directory to extract the contents
	 * @throws IOException 
	 */
	public static void extractZipFile(File zipfile, File outputdir) throws IOException {
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipfile);
			Enumeration<ZipArchiveEntry> entries = zip.getEntries();
			Map<File, Long> origChecksums = new HashMap<File, Long>();
			while(entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();
				File out = new File(outputdir, entry.getName());
				if(entry.isDirectory()) {
					out.mkdirs();
				} else {
					File parent = out.getParentFile();
					if(!parent.exists()) {
						parent.mkdirs();
					}
					InputStream is = zip.getInputStream(entry);
					FileUtils.copyInputStreamToFile(is, out);
					origChecksums.put(out, entry.getCrc());
				}
			}
			for(Map.Entry<File, Long> entry:origChecksums.entrySet()) {
				if(FileUtils.checksumCRC32(entry.getKey()) != entry.getValue().longValue()) {
					throw new IOException("Extraction possibliy failed. Checksums do not match");
				}
			}
			
		} finally {
			IOUtils.closeQuietly(zip);
		}
	}
	
	/**
	 * Extracts the contents of a 7-Zip file to the specified output directory.
	 * The output directory is created if it doesn't exists. 
	 * @param zipfile The 7-Zip file to extract
	 * @param outputdir The directory to extract the contents
	 * @throws IOException 
	 */
	public static void extract7ZipFile(File zipfile, File outputdir) throws IOException {
		SevenZFile zip = null;
		try {
			zip = new SevenZFile(zipfile);
			Iterator<SevenZArchiveEntry> entries = zip.getEntries().iterator();	
			Map<File, Long> origChecksums = new HashMap<File, Long>();
			while(entries.hasNext()) {
				SevenZArchiveEntry entry = entries.next();
				zip.getNextEntry(); // This will switch the internal inputstream
				File out = new File(outputdir, entry.getName());
				if(entry.isDirectory()) {
					out.mkdirs();
				} else {
					File parent = out.getParentFile();
					if(!parent.exists()) {
						parent.mkdirs();
					}
					// Little workaround to get a working inputstream for this entry.
					final SevenZFile sevenz = zip;
					InputStream is = new InputStream() {						
						@Override
						public int read() throws IOException {
							return sevenz.read();
						}
					};
					FileUtils.copyInputStreamToFile(is, out);
					origChecksums.put(out, entry.getCrcValue());
				}
			}
			for(Map.Entry<File, Long> entry:origChecksums.entrySet()) {
				if(FileUtils.checksumCRC32(entry.getKey()) != entry.getValue().longValue()) {
					throw new IOException("Extraction possibliy failed. Checksums do not match");
				}
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}
	}
	
	/**
	 * Extracts a single file from the 7-Zip file
	 * @param zipfile The 7z file to extract
	 * @param filename The entry name to extract
	 * @param destination The location to extract the entry
	 * @throws IOException
	 */
	public static void extractFileFrom7Zip(File zipfile, String filename, File destination) throws IOException {
		SevenZFile zip = null;
		try {
			zip = new SevenZFile(zipfile);
			SevenZArchiveEntry entry = null;
			Iterator<SevenZArchiveEntry> entries = zip.getEntries().iterator();
			while(entries.hasNext()) {
				SevenZArchiveEntry currEntry = entries.next();
				zip.getNextEntry(); // This will switch the internal inputstream
				if(currEntry.getName().equalsIgnoreCase(filename)) {
					entry = currEntry;
					break; // Letting the loop to continue would cause to extract a different entry
				}
			}
			if(entry == null) {
				throw new IOException("Entry not found: " + filename);
			}
			// Little workaround to get a working inputstream for this entry.
			final SevenZFile sevenz = zip;
			InputStream is = new InputStream() {						
				@Override
				public int read() throws IOException {
					return sevenz.read();
				}
			};
			FileUtils.copyInputStreamToFile(is, destination);
			if(FileUtils.checksumCRC32(destination) != entry.getCrcValue()) {
				throw new IOException("Extraction possibliy failed. Checksums do not match");
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}
	}
	
	/**
	 * Computes the checksum of the given files and returns them in map
	 * using filenames as key.<br/>
	 * If the first argument is a directory the checksums of the files 
	 * inside it will be computed. Nested directories will be ignored.
	 * @param files to be processed
	 * @return the computed checksums
	 * @throws IOException 
	 */
	public static Map<String, Long> computeChecksums(File... files) throws IOException {
		Map<String, Long> checksums = new HashMap<String, Long>();
		if(files[0].isDirectory()) {
			File[] dirfiles = files[0].listFiles(new FileFilter() {				
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
			return computeChecksums(dirfiles);
		}
		for(File f:files) {
			if(f.isFile()) {
				checksums.put(f.getName(), FileUtils.checksumCRC32(f));
			}
		}
		return checksums;
	}

	public static void moveFile(File src, File dst) throws IOException {
		if(!src.getAbsolutePath().equals(dst.getAbsolutePath())) {
			Files.move(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}		
	}
	
	public static void saveStreamToFile(InputStream in, File out) throws IOException {
		FileUtils.copyInputStreamToFile(in, out);
	}

	public static String getSDCheckURL() {
		String osname = System.getProperty("os.name", "").toLowerCase();
		String path = "/h2testw-(windows)";		
		if(osname.contains("os x") || osname.contains("osx")) {
			path = "/f3x-(mac)";
		} else if(osname.contains("linux")) {
			path = "/f3-(linux)";
		}
		return HackingPath.URLS.getProperty(MagicPropKeys.$3DS_GUIDE) + path;
	}

	public static String getSDChecker() {
		String osname = System.getProperty("os.name", "").toLowerCase();
		String checker = "H2testw (Windows)";
		if(osname.contains("os x") || osname.contains("osx")) {
			checker = "F3X (Mac)";
		} else if(osname.contains("linux")) {
			checker = "F3 (Linux)";
		}
		return checker;
	}
}
