package net.pixelstatic.novi.network.packets;

import java.util.concurrent.ConcurrentHashMap;

import net.pixelstatic.novi.entities.Entity;

public class DataPacket{
	public long playerid;
	public ConcurrentHashMap<Long, Entity> entities;
}
