package iisaddon.mods.gregtech.tileentities;

import iisaddon.mods.gregtech.tileentities.machines.basic.IISA_GT_CompatTileEntity_Scanner;
import iisaddon.mods.gregtech.tileentities.machines.basic.IISA_GT_MetaTileEntity_SeismicProspector;
import iisaddon.utils.Core;

public class TileEntitiesRegisterHandler {
	public static void preInit() {
		Core.log.verbose("Registering new gregtech tile entities");
		IISA_GT_MetaTileEntity_SeismicProspector.register();
		IISA_GT_CompatTileEntity_Scanner.register();
	}

	public static void init() {}

	public static void postInit() {
		Core.log.verbose("Replacing gregtech tile entities");
		IISA_GT_CompatTileEntity_Scanner.replace();
	}
}
