package iisaddon.mods.gregtech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gregtech.api.util.GT_Utility;
import iisaddon.utils.Core;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fluids.FluidStack;

public class IISA_GT_Utility {
    public static class ItemNBT {

        public static void setAdvancedProspectionData(
        		byte aTier,
        		ItemStack aStack,
        		int aX, short aY, int aZ, int aDim,
        		ArrayList<String> aOils,
        		ArrayList<String> aNearOres,
        		ArrayList<String> aMiddleOres,
        		ArrayList<String> aFarOres) {
        	
			GT_Utility.ItemNBT.setBookTitle(aStack, "Raw Prospection Data");
			
            NBTTagCompound tNBT = GT_Utility.ItemNBT.getNBT(aStack);

            //tNBT.setString("prospection", "");
            
            tNBT.setByte("prospection_tier", aTier);
            tNBT.setString("prospection_pos", "X: " + aX + " Y: " + aY + " Z: " + aZ + " Dim: " + aDim);

            // ores & oils
            tNBT.setString("prospection_near", Core.helper.ListToString(aNearOres));
            tNBT.setString("prospection_middle", Core.helper.ListToString(aMiddleOres));
            tNBT.setString("prospection_far", Core.helper.ListToString(aFarOres));
            tNBT.setString("prospection_oils", Core.helper.ListToString(aOils));
            
            Core.log.verbose("Advanced prospector result: " + tNBT.toString());

            GT_Utility.ItemNBT.setNBT(aStack, tNBT);
        }
        
        public static void convertProspectionData(ItemStack aStack) {
            NBTTagCompound tNBT = GT_Utility.ItemNBT.getNBT(aStack);
            byte tTier = tNBT.getByte("prospection_tier");
            
            if (tTier == 0) { // basic prospection data - use super function
            	GT_Utility.ItemNBT.convertProspectionData(aStack);
            } else { // advanced prospection data
            	String tPos = tNBT.getString("prospection_pos");
            	String tNearOres = tNBT.getString("prospection_near").replace(",", ", ");
            	String tMiddleOres = tNBT.getString("prospection_middle").replace(",", ", ");
            	String tFarOres = tNBT.getString("prospection_far").replace(",", ", ");
            	String tOils = tNBT.getString("prospection_oils");
            	
                NBTTagList tNBTList = new NBTTagList();                
                String tPageText = "";
                
                tPageText = "Oils prospection\n";
                String[] tOilsArray = tOils.split(",");
                for (int i = 0; i < tOilsArray.length - 3; i += 4)
                	tPageText += tOilsArray[i] + ", " + tOilsArray[i+1] + ": " + tOilsArray[i+2] + "L " + tOilsArray[i+3] + "\n";
                tNBTList.appendTag(new NBTTagString(tPageText));
                
                tNBTList.appendTag(new NBTTagString("Near ores prospection\n" + tNearOres));
                tNBTList.appendTag(new NBTTagString("Middle ores prospection\n" + tMiddleOres));
                tNBTList.appendTag(new NBTTagString("Far ores prospection\n" + tFarOres));

                tNBT.setString("author", tPos);
                tNBT.setTag("pages", tNBTList);
                GT_Utility.ItemNBT.setNBT(aStack, tNBT);
            }
        }
    }
}
