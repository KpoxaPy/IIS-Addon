package iisaddon.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Helper {
	/*
	 * Check if stack has enough items of given type and subtract from stack, if there's no creative or 111 stack.
	 */
	public static boolean consumeItems(EntityPlayer player, ItemStack stack, Item item, int count) {
		if (stack != null && stack.getItem() == item && stack.stackSize >= count) {
			if ((!player.capabilities.isCreativeMode) && (stack.stackSize != 111))
				stack.stackSize -= count;
			return true;
		}
		return false;
	}

	/*
	 * Check if stack has enough items of given gregtech material (will be oredicted)
	 * and subtract from stack, if there's no creative or 111 stack.
	 */
	public static boolean consumeItems(EntityPlayer player, ItemStack stack, gregtech.api.enums.Materials mat, int count) {
		if (stack != null
			&& GT_OreDictUnificator.getItemData(stack).mMaterial.mMaterial == mat
			&& stack.stackSize >= count) {
			if ((!player.capabilities.isCreativeMode) && (stack.stackSize != 111))
				stack.stackSize -= count;
			return true;
		}
		return false;
	}
	
	/*
	 * Reflection helper
	 * Originating from GregTech gregtech.api.util.GT_Utility.getField(...)
	 */
    public static Field getField(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
        try {
    		Class tClass = (aObject instanceof Class) ? (Class) aObject : (aObject instanceof String) ? Class.forName((String) aObject) : aObject.getClass();
        	try {
                Field tField = tClass.getDeclaredField(aField);
                if (aPrivate) tField.setAccessible(true);
                return tField;
            } catch (NoSuchFieldException e) {
            	if (aObject.getClass() != Object.class)
            		return getField(tClass.getSuperclass(), aField, aPrivate, aLogErrors);
            	else
            		throw e;
            }
        } catch (Throwable e) {
            if (aLogErrors) Core.log.error(e.toString());
        }
        return null;
    }

	
	/*
	 * Reflection helper
	 * Originating from GregTech gregtech.api.util.GT_Utility.getFieldContent(...)
	 */
    public static Object getFieldContent(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
        try {
            return getField(aObject, aField, aPrivate, aLogErrors).get(aObject instanceof Class || aObject instanceof String ? null : aObject);
        } catch (Throwable e) {
        	if (aLogErrors) Core.log.error(e.toString());
        }
        return null;
    }
    
    public static ArrayList<String> sortByValueToList( Map<String, Integer> map )
    {
        List<Map.Entry<String, Integer>> list =
            new LinkedList<Map.Entry<String, Integer>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return o2.getValue() - o1.getValue();
            }
        } );

        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<String, Integer> e : list)
            result.add(e.getKey());
        return result;
    }
}
