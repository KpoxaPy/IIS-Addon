package iisaddon.utils;

import cpw.mods.fml.common.Loader;

public class ModChecker {
	public static boolean Gregtech = false;
	
	public static void check() {
		if (Loader.isModLoaded("gregtech") == true) {
			Core.log.info("Gregtech related stuff is enabled");
			Gregtech = Core.config.GT.enabled;
		}
	}
}
