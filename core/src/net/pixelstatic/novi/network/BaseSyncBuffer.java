package net.pixelstatic.novi.network;

import java.util.ArrayList;

import net.pixelstatic.novi.world.BlockUpdate;

public class BaseSyncBuffer extends SyncBuffer{
	public ArrayList<BlockUpdate> updates;
	
	public BaseSyncBuffer(){
		
	}
	
	public BaseSyncBuffer(ArrayList<BlockUpdate> updates){
		this.updates = updates;
	}
}
