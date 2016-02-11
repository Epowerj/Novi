package net.pixelstatic.novi.server;

import java.util.HashMap;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.modules.Network;
import net.pixelstatic.novi.network.Registrator;
import net.pixelstatic.novi.network.packets.*;

import com.esotericsoftware.kryonet.*;

public class NoviServer{
	public static boolean active;
	public static final int port = 7576;
	public Server server;
	public HashMap<Integer, Long> players = new HashMap<Integer, Long>(); //used for getting entities from connections
	public NoviUpdater updater; //this runs and updates the game objects

	void createServer(){
		createUpdater();
		try{
			server = new Server();
			Registrator.register(server.getKryo());
			server.addListener(new Listen(this));
			server.start();
			server.bind(Network.port, Network.port);
			Novi.log("Server up.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void createUpdater(){
		updater = new NoviUpdater(this);
		new Thread(new Runnable(){
			public void run(){
				updater.run();
			}
		}).start();
	}

	class Listen extends Listener{
		NoviServer novi;

		public Listen(NoviServer n){
			novi = n;
		}

		@Override
		public void disconnected(Connection connection){
			removeEntity(getPlayer(connection.getID()));
			players.remove(connection.getID());
			Novi.log("Someone has disconnected.");
		}

		@Override
		public void received(Connection connection, Object object){
			try{
				if(object instanceof ConnectPacket){
					//ConnectPacket connect = (ConnectPacket)object;
					Player player = new Player();
					player.connectionid = connection.getID();
					DataPacket data = new DataPacket();
					data.playerid = player.GetID();
					data.entities = Entity.entities;
					connection.sendTCP(data);
					server.sendToAllExceptTCP(connection.getID(), player.AddSelf());
					players.put(connection.getID(), player.GetID());
					Novi.log(connection.getRemoteAddressTCP().getAddress().toString() + " has joined.");
					//server.sendToAllExceptTCP(connection.getID(), connect);
				}else if(object instanceof PositionPacket){
					PositionPacket position = (PositionPacket)object;
					getPlayer(connection.getID()).setPosition(position.x, position.y);
					getPlayer(connection.getID()).rotation = position.rotation;
					getPlayer(connection.getID()).velocity = position.velocity;
				}
			}catch(Exception e){
				e.printStackTrace();
				Novi.log("Packet recieve error!");
			}
		}
	}

	public Player getPlayer(int cid){
		return (Player)Entity.getEntity(players.get(cid));
	}

	public void removeEntity(Entity entity){
		EntityRemovePacket remove = new EntityRemovePacket();
		remove.id = entity.GetID();
		server.sendToAllTCP(remove);
		entity.RemoveSelf();
	}

	public void removeEntity(long entityid){
		EntityRemovePacket remove = new EntityRemovePacket();
		remove.id = entityid;
		server.sendToAllTCP(remove);
		Entity.entities.remove(entityid);
	}

	public static void main(String[] args){
		active = true;
		new NoviServer().createServer();
	}
}
