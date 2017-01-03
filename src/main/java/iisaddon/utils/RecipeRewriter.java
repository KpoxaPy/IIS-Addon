package iisaddon.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeRewriter {
	private ArrayList<IRecipe> mRecipesBuffer;
	
	private ItemList mGTItemList;
	private ItemStack mGTItemStack;
	private ItemStack mNewGTItemStack;
	
	private int mID;
	
	public RecipeRewriter() {
		this.mRecipesBuffer = new ArrayList<IRecipe>();
	}
	
	public int getGTID() {
		return this.mID;
	}
	
	public void track(ItemList aGTItemList) {
		this.mGTItemList = aGTItemList;
		this.mGTItemStack = mGTItemList.get(1L, new Object[0]);
        this.mID = mGTItemStack.getItemDamage();
		
		GregTech_API.METATILEENTITIES[mID] = null;
	}
	
	public void setReplacement(ItemStack aItemStack) {
		this.mNewGTItemStack = aItemStack;
	}
	
	public void replace() {		
		if (!GregTech_API.sPostloadFinished) {
			Core.log.error("Gregtech wasn't post-loaded, addon's compat for gregtech broked it!");
			throw new RuntimeException("");
		}
		
		mGTItemList.set(this.mNewGTItemStack);

		fillRecipeBuffer(mGTItemStack);
		flushRecipeBuffer();
	}
	
	private void fillRecipeBuffer(ItemStack aItemStack) {
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
        for (int i = 0; i < tList.size(); i++) {
            IRecipe tRecipe = tList.get(i);
            if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get(tRecipe.getRecipeOutput()), aItemStack, true))
            	mRecipesBuffer.add(tList.get(i));
        }
	}
	
	private void flushRecipeBuffer() {
		for (IRecipe tRecipe : mRecipesBuffer) {
			Field tOutput = Core.helper.getField(tRecipe, "output", true, true);
			
	        try {
				tOutput.set(tRecipe, mNewGTItemStack);
	        } catch (Throwable e) {
	        	Core.log.verbose(e.toString());
	        };
	        
			Core.log.verbose(tRecipe.toString());
		}
		mRecipesBuffer.clear();
	}
}
