package com.wolvencraft.yasp.stats;

import com.wolvencraft.yasp.db.QueryUtils;
import com.wolvencraft.yasp.db.data.detailed.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.normal.DataHolder;

public class DatabaseSync implements Runnable {

	@Override
	public void run() {
		for(DetailedDataHolder holder : CollectedData.getDetailedData()) {
			if(holder.isOnHold() && !holder.refresh()) continue;
			QueryUtils.pushData(holder.getQuery());
		}
		
		for(DataHolder holder : CollectedData.getNormalData()) {
			holder.pushData();
		}
	}

}
