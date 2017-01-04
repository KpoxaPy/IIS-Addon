package iisaddon.mods.gregtech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        		ArrayList<String> aFarOres,
        		int aNear, int aMiddle, int aRadius) {
        	
			GT_Utility.ItemNBT.setBookTitle(aStack, "Raw Prospection Data");
			
            NBTTagCompound tNBT = GT_Utility.ItemNBT.getNBT(aStack);
            
            tNBT.setByte("prospection_tier", aTier);
            tNBT.setString("prospection_pos", "X: " + aX + " Y: " + aY + " Z: " + aZ + " Dim: " + aDim);

            // ores
            tNBT.setString("prospection_near", joinListToString(aNearOres));
            tNBT.setString("prospection_middle", joinListToString(aMiddleOres));
            tNBT.setString("prospection_far", joinListToString(aFarOres));
            
            // oils
            ArrayList<String> tOilsTransformed = new ArrayList<String>(aOils.size());
            for (String aStr : aOils) {
            	String[] aStats = aStr.split(",");
            	tOilsTransformed.add(aStats[3] + " " + aStats[2] + "L");
            }
            tNBT.setString("prospection_oils", joinListToString(tOilsTransformed));
            
            tNBT.setString("prospection_bounds", aNear + "|" + aMiddle + "|" + aRadius);
            
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
            	String[] tBounds = tNBT.getString("prospection_bounds").split("\\|");
            	
            	String tNearOresStr = tNBT.getString("prospection_near");
            	String tMiddleOresStr = tNBT.getString("prospection_middle");
            	String tFarOresStr = tNBT.getString("prospection_far");
            	String tOilsStr = tNBT.getString("prospection_oils");
            	
            	String[] tNearOres = tNearOresStr.isEmpty() ? null : tNearOresStr.split("\\|");
            	String[] tMiddleOres = tMiddleOresStr.isEmpty() ? null : tMiddleOresStr.split("\\|");
            	String[] tFarOres = tFarOresStr.isEmpty() ? null : tFarOresStr.split("\\|");
            	String[] tOils = tOilsStr.isEmpty() ? null : tOilsStr.split("\\|");
            	
                NBTTagList tNBTList = new NBTTagList();

                String tPageText = "Advanced prospection\n"
                	+ tPos + "\n"
                	+ "Results:\n"
                	+ "- Close Range Ores: " + (tNearOres != null ? tNearOres.length : 0) + "\n"
                	+ "- Mid Range Ores: " + (tMiddleOres != null ? tMiddleOres.length : 0) + "\n"
                	+ "- Far Range Ores: " + (tFarOres != null ? tFarOres.length : 0) + "\n"
                    + "- Oils: " + (tOils != null ? tOils.length : 0) + "\n\n"
                	+ "Lists was sorted by volume";
                tNBTList.appendTag(new NBTTagString(tPageText));
                
                if (tNearOres != null)
                	fillBookWithList(tNBTList, "Close Range Ores%s\n\n", ", ", Core.config.GT.Machines.AdvancedProspector.oresPerPage, tNearOres);
                if (tMiddleOres != null)
                	fillBookWithList(tNBTList, "Mid Range Ores%s\n\n", ", ", Core.config.GT.Machines.AdvancedProspector.oresPerPage, tMiddleOres);
                if (tFarOres != null)
                	fillBookWithList(tNBTList, "Far Range Ores%s\n\n", ", ", Core.config.GT.Machines.AdvancedProspector.oresPerPage, tFarOres);
                if (tOils != null)
                	fillBookWithList(tNBTList, "Oils%s\n\n", "\n", Core.config.GT.Machines.AdvancedProspector.oilsPerPage, tOils);
                
                tPageText = "Notes\n\n"
                	+ "Close range:\nR <= " + tBounds[0] + "\n"
                    + "Mid range:\n" + tBounds[0] + " < R <= " + tBounds[1] + "\n"
                    + "Far range:\n" + tBounds[1] + " < R <= " + tBounds[2];
                tNBTList.appendTag(new NBTTagString(tPageText));
                
                tNBT.setString("author", tPos);
                tNBT.setTag("pages", tNBTList);
                GT_Utility.ItemNBT.setNBT(aStack, tNBT);
            }
        }
        
        private static void fillBookWithList(NBTTagList aBook, String aPageHeader, String aListDelimiter, int aItemsPerPage, String[] list) {
        	String aPageFormatter = " %d/%d";
        	int tTotalPages = list.length / aItemsPerPage + (list.length % aItemsPerPage > 0 ? 1 : 0);
            int tPage = 0;
            String tPageText;
            do {
                tPageText = "";
                for (int i = tPage*aItemsPerPage; i < (tPage+1)*aItemsPerPage && i < list.length; i += 1)
                	tPageText += (tPageText.isEmpty() ? "" : aListDelimiter) + list[i];
            	
            	if (!tPageText.isEmpty()) {
            		String tPageCounter = tTotalPages > 1 ? String.format(aPageFormatter, tPage + 1, tTotalPages) : "";
            		NBTTagString tPageTag = new NBTTagString(String.format(aPageHeader, tPageCounter) + tPageText);
            		aBook.appendTag(tPageTag);
            	}
            	
                ++tPage;
            } while (!tPageText.isEmpty());
        }
        
        private static String joinListToString(List<String> list) {
        	String result = "";
        	for (String s : list)
        		result += (result.isEmpty() ? "" : "|") + s;
        	return result;
        }
    }
}
