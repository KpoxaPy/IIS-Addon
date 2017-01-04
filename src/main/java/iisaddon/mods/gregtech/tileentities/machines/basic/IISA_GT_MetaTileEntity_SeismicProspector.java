package iisaddon.mods.gregtech.tileentities.machines.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import ic2.core.Ic2Items;
import iisaddon.mods.gregtech.GregtechHandler;
import iisaddon.mods.gregtech.enums.IISA_GT_ItemList;
import iisaddon.mods.gregtech.interfaces.IIISA_GT_ItemContainer;
import iisaddon.mods.gregtech.util.IISA_GT_Utility;
import iisaddon.utils.Config;
import iisaddon.utils.Core;
import iisaddon.utils.Config.GT;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import sun.text.resources.es.CollationData_es;

public class IISA_GT_MetaTileEntity_SeismicProspector extends GT_MetaTileEntity_BasicMachine {
	private static boolean doLoad() {
		if (!Core.config.GT.Machines.AdvancedProspector.enable)
			return false;
		if (!Core.config.GT.Machines.Scanner.enable)
			return false;
		
		return true;
	}
	
	public static void register() {
		if (!doLoad())
			return;
		
		Core.log.info("Loading Advanced Seismic Prospector");
		
		IISA_GT_ItemList.Advanced_Seismic_Prospector.set(new IISA_GT_MetaTileEntity_SeismicProspector(
				Core.config.GT.provideNextId(),
				"basicmachine.seismicprospector.3",
				"Advanced Seismic Prospector", 3,
				Core.config.GT.Machines.AdvancedProspector.radius,
				Core.config.GT.Machines.AdvancedProspector.step).getStackForm(1));
		
		GT_ModHandler.addCraftingRecipe(IISA_GT_ItemList.Advanced_Seismic_Prospector.get(1L, new Object[0]),
				GregtechHandler.bitsd,
				new Object[] { "WWW", "EME", "CCC",
						'M', ItemList.Hull_EV,
						'W', OrePrefixes.plateDouble.get(Materials.VanadiumSteel),
						'E', OrePrefixes.circuit.get(Materials.Data),
						'C', ItemList.Sensor_EV });
	}
	/****/
	
	
	boolean ready = false;
	int radius;
	int near;
	int middle;
	int step;
	
	static enum TierUtil {
		MATERIALS,
		TIME;
		
		public int get(int aTier) {
			if (aTier == 1)
				return 1;
			
			switch (this) {
			case MATERIALS:
				return 2*(aTier + 1);
				
			case TIME:
				return 2*(aTier - 1);
			}
			
			return aTier;
		}
	}

	public IISA_GT_MetaTileEntity_SeismicProspector(int aID, String aName, String aNameRegional, int aTier, int aRadius, int aStep) {
		super(aID, aName, aNameRegional, aTier, 1, // amperage
				"Place, activate with explosives ("
				+ 1 * TierUtil.MATERIALS.get(aTier) + " Glyceryl, "
				+ 4 * TierUtil.MATERIALS.get(aTier) + " TNT or "
				+ 2 * TierUtil.MATERIALS.get(aTier) + " ITNT), use Data Stick",
				1, // input slot count
				1, // output slot count
				"Default.png", // GUI name
				"", // NEI name
				new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER) });
		radius = aRadius;
		near = radius / 3;
		near = near + near % 2; // making near value even;
		middle = near * 2;
		step = aStep;
	}

	protected IISA_GT_MetaTileEntity_SeismicProspector(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
			String aGUIName, String aNEIName, int aNear, int aMiddle, int aRadius, int aStep) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
		radius = aRadius;
		near = aNear;
		middle = aMiddle;
		step = aStep;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new IISA_GT_MetaTileEntity_SeismicProspector(this.mName, this.mTier, this.mDescription, this.mTextures,
				this.mGUIName, this.mNEIName, this.near, this.middle, this.radius, this.step);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isServerSide()) {
			ItemStack aStack = aPlayer.getCurrentEquippedItem();

			int tMatTierMult = TierUtil.MATERIALS.get(mTier);
			if (!ready && (Core.helper.consumeItems(aPlayer, aStack, Item.getItemFromBlock(Blocks.tnt), 4 * tMatTierMult)
					|| Core.helper.consumeItems(aPlayer, aStack, Ic2Items.industrialTnt.getItem(), 2 * tMatTierMult)
					|| Core.helper.consumeItems(aPlayer, aStack, Materials.Glyceryl, 1 * tMatTierMult))) {
				
				this.ready = true;
				this.mMaxProgresstime = (aPlayer.capabilities.isCreativeMode ? 20 : 200 * TierUtil.TIME.get(mTier));
				
			} else if (ready && mMaxProgresstime == 0
					&& aStack != null && aStack.stackSize == 1
					&& aStack.getItem() == ItemList.Tool_DataStick.getItem()) {
				this.ready = false;

				// prospecting ores
				HashMap<String, Integer> tNearOres = new HashMap<String, Integer>();
				HashMap<String, Integer> tMiddleOres = new HashMap<String, Integer>();
				HashMap<String, Integer> tFarOres = new HashMap<String, Integer>();
				prospectOres(tNearOres, tMiddleOres, tFarOres);
				
				// prospecting oils
				HashMap<String, Integer> tOils = new HashMap<String, Integer>(9);
				prospectOils(tOils);

				IISA_GT_Utility.ItemNBT.setAdvancedProspectionData(mTier,
					aStack,
					this.getBaseMetaTileEntity().getXCoord(),
					this.getBaseMetaTileEntity().getYCoord(),
					this.getBaseMetaTileEntity().getZCoord(),
					this.getBaseMetaTileEntity().getWorld().provider.dimensionId,
					Core.helper.sortByValueToList(tOils),
					Core.helper.sortByValueToList(tNearOres),
					Core.helper.sortByValueToList(tMiddleOres),
					Core.helper.sortByValueToList(tFarOres),
					near, middle, radius);
			}
		}

		return true;
	}
	
	private void prospectOils(HashMap<String, Integer> aOils) {
		
		int tLeftXBound = this.getBaseMetaTileEntity().getXCoord() - radius;
		int tRightXBound = tLeftXBound + 2*radius;
		
		int tLeftZBound = this.getBaseMetaTileEntity().getZCoord() - radius;
		int tRightZBound = tLeftZBound + 2*radius;

		ArrayList<String> filterList = new ArrayList<String>(9);
		String filter;
		
		for (int x = tLeftXBound; x <= tRightXBound; ++x)
			for (int z = tLeftZBound; z <= tRightZBound; ++z) {
				filter = x/96 + "," + z/96;
				
				if (!filterList.contains(filter)) {
					filterList.add(filter);
					
					putOil((x/96)*96, (z/96)*96, aOils);
				}
			}
	}
	
	private void putOil(int x, int z, HashMap<String, Integer> aOils) {
		FluidStack tFluid = GT_Utility.getUndergroundOil(getBaseMetaTileEntity().getWorld(), x, z);
		if (tFluid.amount / 5000 > 0)
			aOils.put(x + "," + z + "," + (tFluid.amount / 5000) + "," + tFluid.getLocalizedName(), tFluid.amount / 5000);
	}

	private void prospectOres(Map<String, Integer> aNearOres, Map<String, Integer> aMiddleOres, Map<String, Integer> aFarOres) {		
		int tLeftXBound = this.getBaseMetaTileEntity().getXCoord() - radius;
		int tRightXBound = tLeftXBound + 2*radius;
		
		int tLeftZBound = this.getBaseMetaTileEntity().getZCoord() - radius;
		int tRightZBound = tLeftZBound + 2*radius;
		
		for (int i = tLeftXBound; i <= tRightXBound; i += step)
			for (int k = tLeftZBound; k <= tRightZBound; k += step) {
				int di = Math.abs(i - this.getBaseMetaTileEntity().getXCoord());
				int dk = Math.abs(k - this.getBaseMetaTileEntity().getZCoord());
				
				if (di <= near && dk <= near)
					prospectHole(i, k, aNearOres);
				else if (di <= middle && dk <= middle)
					prospectHole(i, k, aMiddleOres);
				else
					prospectHole(i, k, aFarOres);
			} 
	}
	
	private void prospectHole(
			int i, int k, Map<String, Integer> aOres) {

		String tFoundOre = null;
		for (int j = this.getBaseMetaTileEntity().getYCoord(); j > 0; j--) {
			tFoundOre = checkForOre(i, j, k);							
			if (tFoundOre == null)
				continue;
			
			countOre(aOres, tFoundOre);
		}
	}

	private String checkForOre(int x, int y, int z) {
		Block tBlock = this.getBaseMetaTileEntity().getBlock(x, y, z);

		if (tBlock instanceof GT_Block_Ores_Abstract) {
			TileEntity tTileEntity = getBaseMetaTileEntity().getWorld().getTileEntity(x, y, z);

			if ((tTileEntity instanceof GT_TileEntity_Ores)
				&& (((GT_TileEntity_Ores) tTileEntity).mMetaData < 16000)) { // Filtering small ores
				Materials tMaterial
					= GregTech_API.sGeneratedMaterials[((GT_TileEntity_Ores) tTileEntity).mMetaData % 1000];
				
				if ((tMaterial != null) && (tMaterial != Materials._NULL))
					return tMaterial.mDefaultLocalName;
			}
		} else {
			int tMetaID = getBaseMetaTileEntity().getWorld().getBlockMetadata(x, y, z);
			ItemData tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
			
			if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore")))
				return tAssotiation.mMaterial.mMaterial.mDefaultLocalName;
		}
		
		return null;
	}
	
	private static void countOre(Map<String, Integer> map, String ore) {
		Integer oldCount = map.get(ore);
		oldCount = (oldCount == null) ? 0 : oldCount;
		
		map.put(ore, oldCount + 1);
	}
}
