package net.pixelstatic.novi.systems;

import net.pixelstatic.novi.entities.Entity;

public abstract class EntitySystem{
	abstract public void update(Entity entity);
	public boolean accept(Entity entity){
		return true;
	}
	
}
