package iisaddon.mods.gregtech.enums;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import iisaddon.mods.gregtech.interfaces.IIISA_GT_ItemContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.W;

public enum IISA_GT_ItemList implements IIISA_GT_ItemContainer {
	Advanced_Seismic_Prospector;

    private boolean mHasNotBeenSet = true;
    private ItemStack mStack;

	@Override
    public Item getItem() {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return null;        
        return mStack.getItem();
    }

	@Override
    public Block getBlock() {
		check_set();
        return GT_Utility.getBlockFromStack(getItem());
    }

	@Override
    public boolean isStackEqual(Object aStack) {
        return isStackEqual(aStack, false, false);
    }

	@Override
    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (GT_Utility.isStackInvalid(aStack))
        	return false;
        
        return GT_Utility.areUnificationsEqual((ItemStack) aStack, aWildcard ? getWildcard(1) : get(1), aIgnoreNBT);
    }

	@Override
    public ItemStack get(long aAmount, Object... aReplacements) {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return GT_Utility.copyAmount(aAmount, aReplacements);        
        return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(mStack));
    }

	@Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, W, GT_OreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(mStack));
    }

	@Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage() - 1, GT_OreDictUnificator.get(mStack));
    }

	@Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
		check_set();
        if (GT_Utility.isStackInvalid(mStack))
        	return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(mStack));
    }

	@Override
    public IIISA_GT_ItemContainer set(Item aItem) {
        mHasNotBeenSet = false;
        
        if (aItem == null)
        	return this;
        
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = GT_Utility.copyAmount(1, aStack);
        
        return this;
    }

    @Override
    public IIISA_GT_ItemContainer set(ItemStack aStack) {
        mHasNotBeenSet = false;
        mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

	@Override
    public IIISA_GT_ItemContainer registerOre(Object... aOreNames) {
		check_set();
		
        for (Object tOreName : aOreNames)
        	GT_OreDictUnificator.registerOre(tOreName, get(1));
        
        return this;
    }

	@Override
    public IIISA_GT_ItemContainer registerWildcardAsOre(Object... aOreNames) {
		check_set();
		
        for (Object tOreName : aOreNames)
        	GT_OreDictUnificator.registerOre(tOreName, getWildcard(1));
        
        return this;
    }

	@Override
    public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        
        if (GT_Utility.isStackInvalid(rStack))
        	return null;
        
        GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

	@Override
    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        
        if (GT_Utility.isStackInvalid(rStack))
        	return null;
        
        rStack.setStackDisplayName(aDisplayName);
        return GT_Utility.copyAmount(aAmount, rStack);
    }
	
	@Override
    public final boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }
	
	private final void check_set() {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
	}

}
