package iisaddon.utils;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean enableVerbose = false;

	public static class GT {
		public static boolean enabled = true;
		public static int idStart = 25001;
		
		public static class Machines {
			public static class AdvancedProspector {
				public static boolean loaded = false;
				public static boolean enable = true;
				public static int radius = 95;
				public static int step = 4;
				public static int oresPerPage = 20;
				public static int oilsPerPage = 9;
			}
			public static AdvancedProspector AdvancedProspector;
			
			public static class Scanner {
				public static boolean loaded = false;
				public static boolean enable = true;
			}
			public static Scanner Scanner;
		}
		public static Machines Machines;

		private static int idNext; // shall be set during config loading to idStart
		public static int provideNextId() { return idNext++; }
	}
	public static GT GT;
	
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
		loadHiddenConfigs();
		loadPublicConfigs();
	}
	
	private static void loadHiddenConfigs() {
		Core.log.verbose("Secret config phase");
		
		// Main config
		File fileMain = new File(Core.fileConfigDir, Core.configFileMain);
		Core.log.verbose("Loading main config [" + fileMain + "]");
		
		Configuration cfg = new Configuration(fileMain);
		cfg.load();

		// Gregtech
		GT.idNext = GT.idStart = cfg.getInt("id_start", "", GT.idStart, 12001, 32000, null);
		
		// Here is no config saving to not expose hidden options in default config
	}
	
	private static void loadPublicConfigs() {
		Core.log.verbose("Public config phase");
		
		// Main config
		File fileMain = new File(Core.fileConfigDir, Core.configFileMain);
		Core.log.verbose("Loading main config [" + fileMain + "]");
		
		Configuration cfg = new Configuration(fileMain);
		cfg.load();
		
		// Gregtech
		GT.enabled = cfg.getBoolean("enabled", "gregtech", GT.enabled, "Gregtech stuff");
		
		GT.Machines.AdvancedProspector.enable = cfg.getBoolean("enabled", "gregtech.machines.advanced_prospector",
				GT.Machines.AdvancedProspector.enable, "Advanced seismic prospector switch");
		GT.Machines.AdvancedProspector.radius = cfg.getInt("radius", "gregtech.machines.advanced_prospector",
				GT.Machines.AdvancedProspector.radius, 1, 511, "Prospection radius");
		GT.Machines.AdvancedProspector.step = cfg.getInt("step", "gregtech.machines.advanced_prospector",
				GT.Machines.AdvancedProspector.step, 1, 16, "Prospection step");
		GT.Machines.AdvancedProspector.oresPerPage = cfg.getInt("ores_per_page", "gregtech.machines.advanced_prospector",
				GT.Machines.AdvancedProspector.oresPerPage, 1, 25, "How many ores should be on one page");
		GT.Machines.AdvancedProspector.oilsPerPage = cfg.getInt("oils_per_page", "gregtech.machines.advanced_prospector",
				GT.Machines.AdvancedProspector.oilsPerPage, 1, 13, "How many oils should be on one pagep");
		GT.Machines.Scanner.enable = GT.Machines.AdvancedProspector.enable;

		cfg.save();
	}
}
