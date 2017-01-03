package iisaddon.proxy;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import iisaddon.interfaces.IHandlerProxy;
import iisaddon.mods.ModsHandlerProxy;
import iisaddon.utils.Core;
import iisaddon.utils.ModOrderSorter;

public class CommonProxy implements IHandlerProxy {	
	@SidedProxy(clientSide="iisaddon.proxy.ClientProxy", serverSide="iisaddon.proxy.ServerProxy")
	public static CommonProxy instance;

	@SidedProxy(clientSide="iisaddon.mods.ModsHandlerClientProxy", serverSide="iisaddon.mods.ModsHandlerServerProxy")
	protected static IHandlerProxy modsProxy;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		Core.config.initConfig(event);
		
		Core.log.verbose("Verbose debug is ENABLED");
		Core.log.info("Loading " + Core.NAME + " " + Core.VERSION);
		
		Core.config.handleConfig();
		Core.mods.check();
		
		Core.log.verbose("PREINIT phase");
		modsProxy.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Core.log.verbose("INIT phase");
		modsProxy.init(event);
		
		// gregtech is sorting itself to the end of FML modlist, so sort our mod back just after gregtech
		ModOrderSorter.get()
			.after("gregtech")
			.flush();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		Core.log.verbose("POSTINIT phase");
		modsProxy.postInit(event);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		modsProxy.serverStarting(event);
	}

	@Override
	public void serverStarted(FMLServerStartedEvent event) {
		modsProxy.serverStarted(event);
	}

	@Override
	public void serverStopping(FMLServerStoppingEvent event) {
		modsProxy.serverStopping(event);
	}

	@Override
	public void serverStopped(FMLServerStoppedEvent event) {
		modsProxy.serverStopped(event);
	}
}
