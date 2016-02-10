package net.pixelstatic.novi.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Angles{
	static public float ForwardDistance(float angle1, float angle2){
		if(angle1 > angle2){
			return angle1 - angle2;
		}else{
			return angle2 - angle1;
		}
	}

	static public float BackwardDistance(float angle1, float angle2){
		return 360 - ForwardDistance(angle1, angle2);
	}

	static public float angleDist(float a, float b){
		return Math.min(ForwardDistance(a, b), BackwardDistance(a, b));
	}

	static public float MoveToward(float angle, float to, float turnspeed){
		if(Math.abs(angleDist(angle, to)) < turnspeed){
			return to;
		}
		float speed = turnspeed;

		if(angle > to){
			if(BackwardDistance(angle, to) > ForwardDistance(angle, to)){
				angle -= speed;
			}else{
				angle += speed;
			}
		}else{
			if(BackwardDistance(angle, to) < ForwardDistance(angle, to)){
				angle -= speed;
			}else{
				angle += speed;
			}
		}
		return angle;
	}

	static public float mouseAngle(){
		Vector2 v = new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - Gdx.graphics.getHeight() / 2);
		return v.angle();
	}
}
