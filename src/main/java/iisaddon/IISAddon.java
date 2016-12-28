package iisaddon;

import iisaddon.utils.Core;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = Core.MODID, name = Core.NAME, version = Core.VERSION, dependencies = Core.DEPS)
public class IISAddon {
	@Mod.Instance(Core.MODID)
	public static IISAddon instance; 
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Core.proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Core.proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Core.proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		Core.proxy.serverStarting(event);
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		Core.proxy.serverStopping(event);
	}
}
