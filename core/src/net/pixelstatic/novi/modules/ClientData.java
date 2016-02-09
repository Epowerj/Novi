package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;

public class ClientData extends Module{
	public Player player;

	public ClientData(Novi n){
		super(n);
		player = new Player();
		player.AddSelf();
	}

	@Override
	public void Update(){

	}

}
