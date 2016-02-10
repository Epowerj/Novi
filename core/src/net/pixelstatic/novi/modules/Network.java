package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.Registrator;
import net.pixelstatic.novi.network.packets.*;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.*;

public class Network extends Module{
	public static final String ip = "localhost";
	public static final int port = 7576;
	public boolean connect = true;
	Client client;

	public void Init(){
		if(!connect) return;
		try{
			client = new Client();
			Registrator.register(client.getKryo());
			client.addListener(new Listen());
			client.start();
			client.connect(50000, ip, port, port);
			ConnectPacket packet = new ConnectPacket();
			client.sendTCP(packet);
			Novi.log("Connecting to server..");
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
				Novi.log("Recieved data packet.");
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
