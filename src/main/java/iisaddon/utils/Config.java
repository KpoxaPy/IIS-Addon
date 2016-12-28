package iisaddon.utils;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean enableVerbose = false;

	public static class Machines {
		public static boolean enableAdvancedProspector = true;
	}
	
	public static void initConfig(FMLPreInitializationEvent event) {
		// Saving config dir File for possible later use
		Core.fileConfigDir = new File(event.getModConfigurationDirectory(), Core.configDir);
		
		// Cherry-picking debug options from config
		File fileMain = new File(Core.fileConfigDir, Core.configFileMain);
		Configuration cfg = new Configuration(fileMain);
		cfg.load();

		enableVerbose = cfg.getBoolean("verbose_debug", "general", enableVerbose, "Enable verbose debug output");
		
		// Here is no config saving to not expose debug options in default config
	}

	public static void handleConfig() {
		// Main config
		File fileMain = new File(Core.fileConfigDir, Core.configFileMain);
		Core.log.verbose("Loading main config [" + fileMain + "]");
		
		Configuration cfg = new Configuration(fileMain);
		cfg.load();

		// Gregtech machines
		Machines.enableAdvancedProspector = cfg.getBoolean("advanced_prospector", "gregtech.machines",
				Machines.enableAdvancedProspector, "Advanced seismic prospector switch");

		cfg.save();
	}
}
