package iisaddon.mods;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import iisaddon.interfaces.IHandlerProxy;
import iisaddon.mods.gregtech.GregtechHandler;
import iisaddon.utils.Core;

public class ModsHandlerProxy implements IHandlerProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.postInit(event);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.serverStarting(event);
	}

	@Override
	public void serverStarted(FMLServerStartedEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.serverStarted(event);
	}

	@Override
	public void serverStopping(FMLServerStoppingEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.serverStopping(event);
	}

	@Override
	public void serverStopped(FMLServerStoppedEvent event) {
		if (Core.mods.Gregtech) GregtechHandler.serverStopped(event);
	}
}
