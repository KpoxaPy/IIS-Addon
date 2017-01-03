package iisaddon.interfaces;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;

public interface IHandlerProxy {
	public void preInit(FMLPreInitializationEvent event);
	public void init(FMLInitializationEvent event);
	public void postInit(FMLPostInitializationEvent event);
	public void serverStarting(FMLServerStartingEvent event);
	public void serverStarted(FMLServerStartedEvent event);
	public void serverStopping(FMLServerStoppingEvent event);
	public void serverStopped(FMLServerStoppedEvent event);
}
