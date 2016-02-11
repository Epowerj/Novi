package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;

import com.badlogic.gdx.math.Vector2;

public class InterpolationData{
	long lastupdate = -1;
	float updateframes;
	Vector4 lastv = new Vector4();
	Vector4 currentv = new Vector4();

	public void push(float x, float y, float xvelocity, float yvelocity){
		lastv.set(currentv);
		currentv.set(x, y, xvelocity, yvelocity);
		if(lastupdate != -1) updateframes = ((System.currentTimeMillis() - lastupdate) / 1000f) * 60f;
		lastupdate = System.currentTimeMillis();
		Novi.log(lastv);
	}
	
	public void update(Entity entity){
		if(!ready()) return;
		Vector2 out = Interpolations.Curve(elapsed(), lastv.x, lastv.y, currentv.x, currentv.y, elapsedx(false), elapsedy(false), elapsedx(true), elapsedy(true));
		entity.x = out.x;
		entity.y = out.y;
	//	Novi.log(out);
	}
	
	public float elapsedx(boolean end){
		Vector4 v = !end ? lastv : currentv;
		return v.x + v.xvelocity * (!end ? 1 : -1) * updateframes; 
	}
	
	public float elapsedy(boolean end){
		Vector4 v = !end ? lastv : currentv;
		return v.y + v.yvelocity * (!end ? 1 : -1) * updateframes; 
	}
	
	public float elapsed(){
		//Novi.log(updateframes);
		//Novi.log((((System.currentTimeMillis() - lastupdate) / 1000f) * 60f) / updateframes);
		return Math.min((((System.currentTimeMillis() - lastupdate) / 1000f) * 60f) / updateframes, 1.0f);
	}
	
	public boolean ready(){
		return updateframes > 0.01f;
	}
}
