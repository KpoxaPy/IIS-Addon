package iisaddon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModOrderSorter {
	protected String mModID;
	protected LoadController mController;
	protected List<ModContainer> mModList;
	
	protected boolean mBufferedMode = true;	
	protected boolean mSortToEnd = false;
	protected Map<String, Boolean> mAfterMap = new TreeMap<String, Boolean>();
	
	protected ModOrderSorter(String aModID) {
		this.mModID = aModID.toLowerCase(Locale.ENGLISH);
		
		this.mController = (LoadController) Core.helper.getFieldContent(Loader.instance(), "modController", true, true);
		this.mModList = mController.getActiveModList();
	}
	
	public static ModOrderSorter get(String aModID) {
		return new ModOrderSorter(aModID);
	}
	
	public static ModOrderSorter get() {
		return get(Core.MODID);
	}
	
	public ModOrderSorter flush() {
		if (!mSortToEnd && mAfterMap.isEmpty())
			return this;
		
		ModContainer tContainer = null;		
        List<ModContainer> tNewModsList = new ArrayList(mModList.size());
        
        for (short i = 0; i < mModList.size(); i += 1) {
            ModContainer tMod = (ModContainer) mModList.get(i);
            if (tMod.getModId().equalsIgnoreCase(mModID)) {
            	tContainer = tMod;
            } else {
                tNewModsList.add(tMod);
            }
        }
        
        if (tContainer == null) {
        	Core.log.error_lines(mModID + " not found in FML mod list");
        	return this;
        }

		// sort by _after_ map only when we should not sort mod to the end
		if (!mSortToEnd) {
			boolean tPushed = false;
			
	        for (short i = 0; i < tNewModsList.size(); i += 1) {
	            String tModID = ((ModContainer) tNewModsList.get(i)).getModId().toLowerCase(Locale.ENGLISH);
	            
	            if (mAfterMap.containsKey(tModID))
	                mAfterMap.put(tModID, true);
	            
	            // if all after-requests found, push mod to list
	            if (checkAfterList()) {
	            	if (i + 1 < tNewModsList.size())
	            		tNewModsList.add(i+1, tContainer);
	            	else
	    				tNewModsList.add(tContainer);
	            	
	            	tPushed = true;
	            	break;
	            }
	        }
	        
	        // if not all after-requests found, pushing mod to the end (at least)
	        if (!tPushed)
				tNewModsList.add(tContainer);
		} else {
			tNewModsList.add(tContainer);
		}

        try {
        	Core.helper.getField(mController, "activeModList", true, true).set(mController, tNewModsList);
        } catch (Throwable e) {
        	Core.log.verbose(e.toString());
        }
    	mModList = mController.getActiveModList();
        
        this.mAfterMap.clear();
        this.mSortToEnd = false;
        
		return this;
	}
	
	public ModOrderSorter buffer(boolean mSwitch) {
		this.mBufferedMode = mSwitch;
		
		if (!mBufferedMode)
			flush();
		
		return this;
	}
	
	public ModOrderSorter after(String aModID) {
		mAfterMap.put(aModID.toLowerCase(Locale.ENGLISH), false);

		if (!mBufferedMode)
			flush();
		
		return this;
	}
	
	public ModOrderSorter toEnd() {
		this.mSortToEnd = true;

		if (!mBufferedMode)
			flush();
		
		return this;
	}
	
	public ModOrderSorter log() {
		Core.log.verbose("FML ModList: " + mModList.toString());
		
		return this;
	}
	
	private boolean checkAfterList() {
		boolean tResult = true;
		
		for (Boolean b : mAfterMap.values())
			tResult = tResult && b;
		
		return tResult;
	}
}
