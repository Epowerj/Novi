package net.pixelstatic.novi.entities;



public abstract class Effect extends Entity{
	float life;
	float lifetime = 100;
	
	@Override
	public final void Update(){
		life += delta();
		if(life > lifetime) RemoveSelf();
	}
	
	public final Entity AddSelf(){
		if(server != null) throw new RuntimeException("Effects should not be added serverside!");
		entities.put(this.GetID(), this);
		return this;
	}
}
