package iisaddon.mods.gregtech.tileentities.machines.basic;

import java.util.ArrayList;
import java.util.HashMap;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Scanner;
import iisaddon.mods.gregtech.util.IISA_GT_Utility;
import iisaddon.utils.Core;
import iisaddon.utils.RecipeRewriter;
import net.minecraft.item.ItemStack;

public class IISA_GT_CompatTileEntity_Scanner extends GT_MetaTileEntity_Scanner {
	private static boolean doLoad() {
		if (!Core.config.GT.Machines.Scanner.enable)
			return false;
		
		return true;
	}
	
	private static ArrayList<RecipeRewriter> mRewriters = new ArrayList<RecipeRewriter>(8);
	
	private static void addRewriter(ItemList aItemList, String aName, String aNameRegional, int aTier) {
		RecipeRewriter tRewriter = new RecipeRewriter();
		
		tRewriter.track(aItemList);
		tRewriter.setReplacement(new IISA_GT_CompatTileEntity_Scanner(tRewriter.getGTID(), aName, aNameRegional, aTier).getStackForm(1));
		mRewriters.add(tRewriter);
	}
	
	public static void register() {
		if (!doLoad())
			return;
		
		Core.log.info("Applying scanner compatibility for advanced prospector");
		
		addRewriter(ItemList.Machine_LV_Scanner,  "basicmachine.scanner.tier.01", "Basic Scanner", 1);
		addRewriter(ItemList.Machine_MV_Scanner,  "basicmachine.scanner.tier.02", "Advanced Scanner", 2);
		addRewriter(ItemList.Machine_HV_Scanner,  "basicmachine.scanner.tier.03", "Advanced Scanner II", 3);
		addRewriter(ItemList.Machine_EV_Scanner,  "basicmachine.scanner.tier.04", "Advanced Scanner III", 4);
		addRewriter(ItemList.Machine_IV_Scanner,  "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5);
		addRewriter(ItemList.Machine_LuV_Scanner, "basicmachine.scanner.tier.06", "Advanced Scanner V", 6);
		addRewriter(ItemList.Machine_ZPM_Scanner, "basicmachine.scanner.tier.07", "Advanced Scanner VI", 7);
		addRewriter(ItemList.Machine_UV_Scanner,  "basicmachine.scanner.tier.08", "Advanced Scanner VII", 8);

		Core.config.GT.Machines.Scanner.loaded = true;
	}
	
	public static void replace() {
		for (RecipeRewriter tRewriter : mRewriters)
			tRewriter.replace();
		mRewriters.clear();
		mRewriters = null;
		
		Core.config.GT.Machines.AdvancedProspector.loaded = true;
	}
	/****/
	
	
    public IISA_GT_CompatTileEntity_Scanner(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public IISA_GT_CompatTileEntity_Scanner(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, aDescription, aTextures, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new IISA_GT_CompatTileEntity_Scanner(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
    }

    @Override
    public int checkRecipe() {
    	Core.log.verbose("Scanner overriding works!");
        ItemStack aStack = getInputAt(0);
        
        if (getOutputAt(0) != null) {
            this.mOutputBlocked += 1;
            return 0;
        }
        
        if ((GT_Utility.isStackValid(aStack)) && (aStack.stackSize > 0)
        	&& getSpecialSlot() == null && ItemList.Tool_DataStick.isStackEqual(aStack, false, true)
        	&& GT_Utility.ItemNBT.getBookTitle(aStack).equals("Raw Prospection Data")) {
        	Core.log.verbose("Scanner overriding for seismic prospector works!");
        	
            GT_Utility.ItemNBT.setBookTitle(aStack, "Analyzed Prospection Data");
            IISA_GT_Utility.ItemNBT.convertProspectionData(aStack);
            aStack.stackSize -= 1;

            this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{aStack});
            this.mMaxProgresstime = (1000 / (1 << this.mTier - 1));
            this.mEUt = (32 * (1 << this.mTier - 1) * (1 << this.mTier - 1));
        	
        	return 2;
        }
        
        return super.checkRecipe();
    }
}
