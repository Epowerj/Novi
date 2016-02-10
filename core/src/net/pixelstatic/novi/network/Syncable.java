package net.pixelstatic.novi.network;

public interface Syncable{
	public SyncBuffer writeSync();
	public void readSync(SyncBuffer buffer);
}
