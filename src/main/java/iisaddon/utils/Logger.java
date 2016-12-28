package iisaddon.utils;

import cpw.mods.fml.common.FMLLog;

public class Logger {
	public static final String defaultFormat = Core.nameCompact + ": %s";

	public static void verbose(String msg) {
		if (Core.config.enableVerbose)
			FMLLog.info(defaultFormat, msg);
	}

	public static void verbose_lines(String msg) {
		if (Core.config.enableVerbose)
			FMLLog.info(msg);
	}

	public static void info(String msg) {
		FMLLog.info(defaultFormat, msg);
	}

	public static void info_lines(String msg) {
		FMLLog.info(msg);
	}

	public static void warn(String msg) {
		FMLLog.warning(defaultFormat, msg);
	}

	public static void warn_lines(String msg) {
		FMLLog.warning(msg);
	}

	public static void error(String msg) {
		FMLLog.severe(defaultFormat, msg);
	}

	public static void error_lines(String msg) {
		FMLLog.severe(msg);
	}
}
