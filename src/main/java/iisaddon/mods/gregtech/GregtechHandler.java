package iisaddon.mods.gregtech;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import gregtech.api.util.GT_ModHandler;
import iisaddon.mods.gregtech.tileentities.TileEntitiesRegisterHandler;
import iisaddon.proxy.CommonProxy;

public class GregtechHandler {
	public static long bitsd =
			GT_ModHandler.RecipeBits.DISMANTLEABLE
			| GT_ModHandler.RecipeBits.NOT_REMOVABLE
			| GT_ModHandler.RecipeBits.REVERSIBLE
			| GT_ModHandler.RecipeBits.BUFFERED;
	
	public static void preInit(FMLPreInitializationEvent event) {
		TileEntitiesRegisterHandler.preInit();
	}

	public static void init(FMLInitializationEvent event) {
		TileEntitiesRegisterHandler.init();
	}

	public static void postInit(FMLPostInitializationEvent event) {
		TileEntitiesRegisterHandler.postInit();
	}

	public static void serverStarting(FMLServerStartingEvent event) {
	}

	public static void serverStarted(FMLServerStartedEvent event) {
	}

	public static void serverStopping(FMLServerStoppingEvent event) {
	}

	public static void serverStopped(FMLServerStoppedEvent event) {
	}

}
