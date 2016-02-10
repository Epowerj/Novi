package net.pixelstatic.novi.network.packets;

import java.util.HashMap;

import net.pixelstatic.novi.network.SyncBuffer;

public class WorldUpdatePacket{
	public HashMap<Long, SyncBuffer> updates = new HashMap<Long, SyncBuffer>();
}
