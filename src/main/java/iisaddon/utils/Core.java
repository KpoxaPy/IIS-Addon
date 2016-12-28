package iisaddon.utils;

import java.io.File;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import iisaddon.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Core {
	public static final String MODID = "iisaddon";
	public static final String NAME = "IIS-Addon";
    public static final String VERSION = "0.0.1";    
	public static final String DEPS
		= "required-after:Forge;"
		+ "required-after:gregtech;";
	
	public static final String nameCompact = "IISAddon";	
	public static final String configDir = "";
	public static final String configFileMain = "IISAddon.cfg";
	
	public static File fileConfigDir;
	
	public static Config config;
	public static Logger log;
	
	@SidedProxy(clientSide="iisaddon.proxy.ClientProxy", serverSide="iisaddon.proxy.ServerProxy")
	public static CommonProxy proxy;
}
