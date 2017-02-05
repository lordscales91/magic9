package io.github.lordscales91.magic9.core;

import org.apache.commons.io.FileUtils;

import io.github.lordscales91.magic9.domain.ConsoleModel;

public class MagicConstants {
	public static final String DECRYPT9_LAUNCHER_DAT = "Launcher.dat";
	public static final String DECRYPT9_DAT = "Decrypt9WIP.dat";
	public static final String DECRYPT9_ZIP = "Decrypt9WIP.zip";
	public static final String DECRYPT9_BIN = "Decrypt9WIP.bin";
	public static final String DECRYPT9_FOLDER = "Decrypt9";
	public static final String FILES9_FOLDER = "files9";
	public static final String O3DS = ConsoleModel.O3DS_GENERIC.modelType();
	public static final String N3DS = ConsoleModel.N3DS_GENERIC.modelType();
	public static final String OTHERAPP_BASE_URL = "http://smealum.github.io/ninjhax2/JL1Xf2KFVm/otherapp/";
	public static final String SOUNDHAX_BASE_URL = "https://github.com/nedwill/soundhax/blob/master/";
	public static final String OTHERAPP_BIN = "otherapp.bin";
	public static final String STARTER_KIT_FOLDER = "starterkit";
	public static final String STARTER_KIT_ZIP = "starter.zip";
	public static final String ARM9_BIN = "arm9.bin";
	public static final String ARM11_BIN = "arm11.bin";
	public static final String SAFEHAX_FASTHAX_FOLDER = "safehax_fasthax";
	public static final String CTRIMAGES_FOLDER = "ctrimages";
	public static final String CIAS_FOLDER = "cias";
	public static final String ARM9LH_FOLDER = "a9lh";
	public static final String THREE_D_S_FOLDER = "3ds";
	public static final String SAFE9HLINSTALLER_FOLDER = "SAFE9HL";
	public static final String SAFE9HLINSTALLER_7Z = "SafeA9LHInstaller.7z";
	public static final String DATA_INPUT_FOLDER = "data_input";
	public static final String DATA_INPUT_ZIP = "data_input.zip";
	public static final String CTR_VERSION_210 = "2.1.0-4";
	public static final String ARM9_HAX_FOLDER = "arm9loaderhax";
	public static final String ARM9_HAX_7Z = "arm9loaderhax.7z";
	public static final String HBLAUNCHER_ZIP = "hblauncher_loader.zip";
	public static final String HBLAUNCHER_CIA = "hblauncher_loader.cia";
	public static final String LUMA3DS_FOLDER = "Luma3DS";
	public static final String LUMA_UPDATER_ZIP = "lumaupdater.zip";
	public static final String LUMA_UPDATER_CIA = "lumaupdater.cia";
	public static final String LUMA3DS_7Z = "Luma3DS.7z";
	public static final String ARM9_LOADER_HAX_BIN = "arm9loaderhax.bin";
	public static final String LUMA_PAYLOADS_DIR = "luma/payloads";
	public static final String HOURGLASS9_ZIP = "Hourglass9.zip";
	public static final String HOURGLASS9_BIN = "Hourglass9.bin";
	public static final String HOURGLASS9_PAYLOAD = "start_Hourglass9.bin";
	public static final String GODMODE9_ZIP = "GodMode9.zip";
	public static final String GODMODE9_BIN = "GodMode9.bin";
	public static final String GODMODE9_PAYLOAD = "up_GodMode9.bin";
	public static final String AESKEYDB_BIN = "aeskeydb.bin";
	public static final String DSPDUMP = "DspDump.3dsx";
	public static final String FBI_INJECTABLE_ZIP = "fbi_injectable.zip";
	public static final String FBI_INJECTABLE_FOLDER = "fbi_injectable";
	public static final String SAFEHAX_PAYLOAD = "safehaxpayload.bin";
	public static final String TRACKERS_FILE = "data/trackers.txt";
	public static final String TORRENTS_DIR = "torrents";
	public static final String FBI_CIA = "FBI.cia";
	public static final String DECRYPT9_NDS = "Decrypt9.nds";
	// Storage devices always have a little less capacity than the specified
	public static final long SD_MIN_SIZE = FileUtils.ONE_GB * 4 - FileUtils.ONE_MB * 200;

	private MagicConstants() {}
}
