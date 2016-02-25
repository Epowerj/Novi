package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.modules.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;

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
		float x = 0,y = 0;
		Renderer renderer = Entity.renderer;
		Vector3 vector = renderer.camera.project(new Vector3(renderer.player.x, renderer.player.y, 0));
		x = vector.x;
		y = vector.y;
		Vector2 v = new Vector2(Gdx.input.getX() - x, Gdx.graphics.getHeight() - Gdx.input.getY() - y);
		return v.angle();
	}
}
