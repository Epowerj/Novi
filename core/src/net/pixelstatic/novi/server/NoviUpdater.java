package net.pixelstatic.novi.server;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.network.Syncable;
import net.pixelstatic.novi.network.packets.WorldUpdatePacket;

public class NoviUpdater{
	NoviServer server;
	private boolean isRunning = true;
	int fps;
	long lastFpsTime;
	

	void Loop(float delta){
		for(Entity entity : Entity.entities.values()){
			entity.Update();
			entity.serverUpdate();
			if(entity instanceof Player){
				WorldUpdatePacket worldupdate = new WorldUpdatePacket();
				for(Entity other : Entity.entities.values()){
					if(other.equals(entity) || !(other instanceof Syncable)) continue;
					Syncable sync = (Syncable)other;
					worldupdate.updates.put(other.GetID(), sync.writeSync());
				}
				server.server.sendToTCP(((Player)entity).connectionid, worldupdate);
			}
		}
	}

	public void run(){
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 10;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

		// keep looping round til the game ends
		while(isRunning){
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double)OPTIMAL_TIME);

			// update the frame counter
			lastFpsTime += updateLength;
			fps ++;

			// update our FPS counter if a second has passed since
			// we last recorded
			if(lastFpsTime >= 1000000000){
				//System.out.println("(FPS: " + fps + ")");
				lastFpsTime = 0;
				fps = 0;
			}

			// update the game logic
			Loop((float)delta);

			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give 
			// us our final value to wait for
			// remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
			try{
				Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
			}catch(Exception e){

			}
		}
	}
	
	public NoviUpdater(NoviServer server){
		this.server = server;
	}
}
