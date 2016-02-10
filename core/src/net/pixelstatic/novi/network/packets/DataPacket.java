package net.pixelstatic.novi.network.packets;

import java.util.HashMap;

import net.pixelstatic.novi.entities.Entity;

public class DataPacket{
	public long playerid;
	public HashMap<Long, Entity> entities;
}
