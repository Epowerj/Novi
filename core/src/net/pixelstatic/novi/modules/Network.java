package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.packets.*;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.*;

public class Network extends Module{
	public static final String ip = "localhost";
	public static final int port = 7576;
	public boolean connect = true;
	Client client;

	public void Init(){
		if( !connect) return;
		try{
			int buffer = (int)Math.pow(2, 5);
			client = new Client(8192*buffer, 8192*buffer);
			Registrator.register(client.getKryo());
			client.addListener(new Listen());
			client.start();
			client.connect(100000, ip, port, port);
			ConnectPacket packet = new ConnectPacket();
			packet.name = System.getProperty("user.name");
			client.sendTCP(packet);
			Novi.log("Connecting to server..");
		}catch(Exception e){
			e.printStackTrace();
			Novi.log("Connection failed!");
			Gdx.app.exit();
		}
	}

	public void sendUpdate(){
		PositionPacket pos = new PositionPacket();
		pos.x = getModule(ClientData.class).player.x;
		pos.y = getModule(ClientData.class).player.y;
		pos.rotation = getModule(ClientData.class).player.getSpriteRotation();
		pos.velocity = getModule(ClientData.class).player.velocity;
		client.sendTCP(pos);
	}

	class Listen extends Listener{
		@Override
		public void received(Connection connection, Object object){
			try{
				if(object instanceof DataPacket){
					DataPacket data = (DataPacket)object;
					getModule(ClientData.class).player.resetID(data.playerid);
					Entity.entities = data.entities;
					getModule(ClientData.class).player.AddSelf();
					Novi.log("Recieved data packet.");
				}else if(object instanceof Entity){
					Entity entity = (Entity)object;
					entity.onRecieve();
					entity.AddSelf();
					//Novi.log("recieved entity of type " + entity.getClass().getSimpleName());
				}else if(object instanceof EntityRemovePacket){
					EntityRemovePacket remove = (EntityRemovePacket)object;
					if(Entity.entities.containsKey(remove.id))
					Entity.entities.get(remove.id).removeEvent();
					Entity.entities.remove(remove.id);
				}else if(object instanceof WorldUpdatePacket){
					WorldUpdatePacket packet = (WorldUpdatePacket)object;
					for(long key : packet.updates.keySet()){
						
						((Syncable)Entity.getEntity(key)).readSync(packet.updates.get(key));;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				Novi.log("Packet recieve error!");
			}
		}
	}

	public Network(Novi n){
		super(n);
	}

	@Override
	public void Update(){
		sendUpdate();
	}

}
