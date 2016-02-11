package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.packets.*;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.*;

public class Network extends Module{
	public static final String ip = "2605:a000:110d:4020:e9b5:e12e:4cd0:7f9e";
	public static final int port = 7576;
	public boolean connect = true;
	Client client;

	public void Init(){
		if( !connect) return;
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

	public void sendUpdate(){
		PositionPacket pos = new PositionPacket();
		pos.x = GetModule(ClientData.class).player.x;
		pos.y = GetModule(ClientData.class).player.y;
		pos.rotation = GetModule(ClientData.class).player.getSpriteRotation();
		pos.velocity = GetModule(ClientData.class).player.velocity;
		client.sendTCP(pos);
	}

	class Listen extends Listener{
		@Override
		public void received(Connection connection, Object object){
			try{
				if(object instanceof DataPacket){
					DataPacket data = (DataPacket)object;
					GetModule(ClientData.class).player.resetID(data.playerid);
					Entity.entities = data.entities;
					GetModule(ClientData.class).player.AddSelf();
					Novi.log("Recieved data packet.");
				}else if(object instanceof Entity){
					Entity entity = (Entity)object;
					entity.onRecieve();
					entity.AddSelf();
				}else if(object instanceof EntityRemovePacket){
					EntityRemovePacket remove = (EntityRemovePacket)object;
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
