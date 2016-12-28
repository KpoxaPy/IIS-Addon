package iisaddon.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import iisaddon.utils.Core;

public class CommonProxy {
	public CommonProxy() {

	}
	
	public void preInit(FMLPreInitializationEvent event) {
		Core.config.initConfig(event);
		
		Core.log.info("Loading " + Core.NAME + " " + Core.VERSION);
		Core.log.verbose("Verbose debug is ENABLED");
		Core.config.handleConfig();
		
		Core.log.verbose("PREINIT phase");
	}

	public void init(FMLInitializationEvent event) {
		Core.log.verbose("INIT phase");
	}

	public void postInit(FMLPostInitializationEvent event) {
		Core.log.verbose("POSTINIT phase");
	}

	public void serverStarting(FMLServerStartingEvent event) {
		// don't do anything here, real handling happens at iisaddon.proxy.ServerProxy.serverStarting(e)
	}

	public void serverStopping(FMLServerStoppingEvent event) {
		// don't do anything here, real handling happens at iisaddon.proxy.ServerProxy.serverStopping(e)
	}
}
