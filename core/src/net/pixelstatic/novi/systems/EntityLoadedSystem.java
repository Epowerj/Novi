package net.pixelstatic.novi.systems;

import net.pixelstatic.novi.entities.Entity;

public class EntityLoadedSystem extends EntitySystem{
	private Entity player;
	
	public EntityLoadedSystem(Entity player){
		this.player = player;
	}
	
	@Override
	public void update(Entity entity){
		
	}
	
	public boolean accept(Entity entity){
		return entity.loaded(player.x, player.y);
	}

}
