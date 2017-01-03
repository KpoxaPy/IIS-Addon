package iisaddon.utils;

import java.io.File;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import iisaddon.interfaces.IHandlerProxy;
import iisaddon.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Core {
	public static final String MODID = "iisaddon";
	public static final String NAME = "IIS-Addon";
	public static final String VERSION = "0.1.0";    
	public static final String DEPS
		= "required-after:Forge;"
		+ "after:gregtech;";

	public static final String nameCompact = "IISAddon";
	public static final String configDir = "";
	public static final String configFileMain = "IISAddon.cfg";
	
	public static File fileConfigDir;
	
	public static Config config;
	public static ModChecker mods;
	public static Logger log;
	
	public static Helper helper;
	
	public static IHandlerProxy proxy = CommonProxy.instance;
}
