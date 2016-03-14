package net.pixelstatic.novi.network;

import com.badlogic.gdx.math.Vector2;

public class PlayerSyncBuffer extends SyncBuffer{
	public Vector2 velocity;
	public float rotation, respawntime;

	public PlayerSyncBuffer(long id, float x, float y, float rotation, float respawntime, Vector2 velocity){
		super(id, x, y);
		this.velocity = velocity;
		this.rotation = rotation;
		this.respawntime = respawntime;
	}
	
	public PlayerSyncBuffer(){
		//empty constructor
	}
}
