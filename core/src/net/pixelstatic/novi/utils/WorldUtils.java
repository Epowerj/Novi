package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.modules.World;

import com.badlogic.gdx.math.Vector2;

//class full of arcane magic BS
//goddammit
public class WorldUtils{
	private static int hsize = World.worldSize/2;
	
	/*
	//wrapped square dist, will not work properly so stop trying
	public static boolean loopDist(float x1, float x2, float y1, float y2, float rad){
		x1 = wrap(x1);
		y1 = wrap(y1);
		x2 = wrap(x2);
		y2 = wrap(y2);
		return (Math.abs(x1 - x2) < rad && Math.abs(y1 - y2) < rad);
	}
	*/
	
	public static boolean loopDist(float x1, float x2, float y1, float y2, float rad){
		return ((wdist(x1,x2)) < rad && (wdist(y1,y2)) < rad);
	}
	
	
	public static float wrappedDist(float x1, float y1, float x2, float y2){
		x1 = wrap(x1);
		y1 = wrap(y1);
		x2 = wrap(x2);
		y2 = wrap(y2);
		return Vector2.dst(x1, y1, x2, y2);
	}
	
	//this works for turrets, don't ask me what it does
	public static float relative(float a, float b){
		if(a < hsize && b < hsize){
			return a-b;
		}else if(a > hsize && b < hsize){
			return -(a+(World.worldSize-b));
		}else{ //a < hsize && b > hsize
			return a+(World.worldSize-b);
		}
	}
	
	//returns relative coords with wrapping, maybe.
	public static float relative2(float a, float b){
		if(a < hsize && b < hsize){
			return a-b;
		}else if(a > hsize && b < hsize){
			return a-World.worldSize-b;
		}else{ //a < hsize && b > hsize
			return -(a-World.worldSize-b);
		}
	}
	//90|10
	
	//abracadabra
	public static float wdist(float a, float b){
		return Math.min(wrap(a)+wrap(b), Math.abs(a-b));
	}
	
	public static float dwrap(float i) {
	    return wrap(i);
	}
	
	//wraps crap
	public static float wrap(float i){
		if(i > World.worldSize / 2) return World.worldSize-i;
		return i;
	}

	public static float bound(float i){
		if(i < 0){
			return World.worldSize + i;
		}else{
			return i % World.worldSize;
		}
	}
}
