package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.packets.DataPacket;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Module{
	public static final String ip = "localhost";
	public static final int port = 7576;
	public boolean connect = false;
	Client client;

	public void Init(){
		if(!connect) return;
		try{
			client = new Client();
			client.start();
			client.connect(5000, ip, port);
		}catch(Exception e){
			e.printStackTrace();
			Novi.log("Connection failed!");
			Gdx.app.exit();
		}
	}

	class Listen extends Listener{
		@Override
		public void received(Connection connection, Object object){
			if(object instanceof DataPacket){
				DataPacket data = (DataPacket)object;
				GetModule(ClientData.class).player.resetID(data.playerid);;
			}else if(object instanceof Entity){
				Entity entity = (Entity)object;
				entity.onRecieve();
				entity.AddSelf();
			}
		}
	}

	public Network(Novi n){
		super(n);
	}

	@Override
	public void Update(){

	}

}
