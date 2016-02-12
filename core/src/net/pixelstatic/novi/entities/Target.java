package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.utils.InterpolationData;

public class Target extends SolidEntity implements Syncable{
	transient InterpolationData data = new InterpolationData();
	
	public Target(){
		material.getRectangle().setSize(20);
	}

	@Override
	public void Update(){
		data.update(this);
	}
	
	@Override
	public void serverUpdate(){
		x += Math.sin(y/10f+2) * delta();
		y += Math.sin(x/10f+2) * delta();
	}

	@Override
	public void Draw(){
		renderer.layer("tile", x, y);
	}

	@Override
	public SyncBuffer writeSync(){
		// TODO Auto-generated method stub
		return new SyncBuffer(GetID(), x,y);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		data.push(this, buffer.x, buffer.y, 0);
	//	x = buffer.x;
		//y = buffer.y;
	}

}
