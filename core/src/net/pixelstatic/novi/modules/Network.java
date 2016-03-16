package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.packets.*;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.*;

public class Network extends Module{
	public static final String ip = System.getProperty("user.name").equals("cobalt") ? "localhost" : "75.179.181.100";
	public static final int port = 7576;
	public static final int ping = 100;
	private boolean connected = true;
	private boolean initialconnect = false;
	Client client;

	public void Init(){
		try{
			int buffer = (int)Math.pow(2, 5);
			client = new Client(8192*buffer, 8192*buffer);
			Registrator.register(client.getKryo());
			client.addListener(new Listener.LagListener(ping,ping,new Listen()));
			client.start();
			client.connect(12000, ip, port, port);
			ConnectPacket packet = new ConnectPacket();
			packet.name = System.getProperty("user.name");
			client.sendTCP(packet);
			initialconnect = true;
			Novi.log("Connecting to server..");
		}catch(Exception e){
			Novi.log(e);
			Novi.log("Connection failed!");
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
				}else if(object instanceof EffectPacket){
					EffectPacket effect = (EffectPacket)object;
					effect.apply(novi);
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
				}else if(object instanceof DeathPacket){
					getModule(ClientData.class).player.deathEvent();
				}else if(object instanceof WorldUpdatePacket){
					WorldUpdatePacket packet = (WorldUpdatePacket)object;
					getModule(ClientData.class).player.health = packet.health;
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
	
	public boolean connected(){
		return connected;
	}
	
	public boolean initialconnect(){
		return initialconnect;
	}
	
	public float pingInFrames(){
		return ((ping*2f + client.getReturnTripTime()) / 1000f) * Entity.delta() * 60f+1f;
	}

	@Override
	public void Update(){
		if(initialconnect)sendUpdate();
		connected = client.isConnected();
		if(Gdx.graphics.getFrameId() % 120 == 0) client.updateReturnTripTime();
	}

}
