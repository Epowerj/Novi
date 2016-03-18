package net.pixelstatic.novi.network;

import java.lang.annotation.*;

public interface Syncable{
	public SyncBuffer writeSync();
	public void readSync(SyncBuffer buffer);
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface GlobalSyncable{}
}
