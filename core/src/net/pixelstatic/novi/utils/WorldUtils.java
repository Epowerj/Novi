package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.modules.World;

//class full of arcane magic BS
//goddammit
public class WorldUtils{
	private static int hsize = World.worldSize / 2;

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
		return (Math.abs(relative3(x1, x2)) < rad && Math.abs(relative3(y1, y2)) < rad);
	}

	public static float wrappedDist(float x1, float y1, float x2, float y2){
		x1 = wrap(x1);
		y1 = wrap(y1);
		x2 = wrap(x2);
		y2 = wrap(y2);
		float ydst = relative3(y1,y2);
		float xdst = relative3(x1,x2);
		return (float)Math.sqrt(ydst * ydst + xdst * xdst);
	}

	//this works for turrets, don't ask me what it does
	public static float relative(float a, float b){
		if(a < hsize && b < hsize){
			return a - b;
		}else if(a > hsize && b < hsize){
			return -(a + (World.worldSize - b));
		}else{ //a < hsize && b > hsize
			return a + (World.worldSize - b);
		}
	}

	//returns relative coords with wrapping, maybe.
	public static float relative2(float a, float b){
		if(a < hsize && b < hsize){
			return a - b;
		}else if(a > hsize && b < hsize){
			return a - World.worldSize - b;
		}else{ //a < hsize && b > hsize
			return -(a - World.worldSize - b);
		}
	}

	//yeeee
	public static float relative3(float a, float b){
		/*
		if((a < hsize && b < hsize) || (a > hsize && b > hsize)){
			return a-b;
		}else if(a > hsize && b < hsize){
			return -(b-hsize+hsize-a);
		}else if(a < hsize && b > hsize){ //
			return -(b-hsize+hsize-a);
		}
		throw new IllegalArgumentException("Critical error!");
		*/

		float ndst = a - b;
		float wdst = owrapdst(a, b);

		return Math.abs(ndst) < Math.abs(wdst) ? ndst : wdst;
		//return 0;
	}

	//returns the wrapped distance from a to b
	public static float owrapdst(float a, float b){
		if(a > b){
			return a - (World.worldSize + b);
		}else{
			return -(b - (World.worldSize + a));
		}
	}

	//returns the unsigned wrapped distance from a to b
	public static float uowrapdst(float a, float b){
		return a - (World.worldSize + b);
	}

	//minimum distance from a to be with wrapping
	public static float wdist(float a, float b){
		float ndst = Math.abs(a - b);
		float wdst = Math.abs(uowrapdst(a, b));
		return Math.min(ndst, wdst);
	}

	//wraps crap
	public static float wrap(float i){
		if(i > World.worldSize / 2) return World.worldSize - i;
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
