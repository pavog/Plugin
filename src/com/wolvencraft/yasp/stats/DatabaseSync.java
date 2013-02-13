package com.wolvencraft.yasp.stats;

import com.wolvencraft.yasp.db.data.detailed.DetailedDataHolder;
import com.wolvencraft.yasp.db.data.normal.DataHolder;

public class DatabaseSync implements Runnable {

	@Override
	public void run() {
		for(DetailedDataHolder holder : DataCollector.getDetailedData()) {
			if(holder.isOnHold() && !holder.refresh()) continue;
			holder.pushData();
		}
		
		for(DataHolder holder : DataCollector.getNormalData()) {
			holder.pushData();
		}
	}

}
