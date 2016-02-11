
package net.pixelstatic.novi.items;

public class ArrowheadShip extends Ship {

	public ArrowheadShip() {
		super("ArrowheadShip", 1);
		
		spin = false; //whee!
		speed = 0.2f;
		turnspeed = 10f;
		maxvelocity = 4f;
		shootspeed = 5;
		kiteDebuffMultiplier = 0.7f;
	}

}
