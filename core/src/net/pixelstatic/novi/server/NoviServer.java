package net.pixelstatic.novi.server;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.modules.Network;
import net.pixelstatic.novi.network.Registrator;
import net.pixelstatic.novi.network.packets.*;

import com.esotericsoftware.kryonet.*;

public class NoviServer{
	public static boolean active;
	public static final int port = 7576;
	public Server server;

	void createServer(){
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

	class Listen extends Listener{
		NoviServer novi;

		public Listen(NoviServer n){
			novi = n;
		}

		@Override
		public void connected(Connection connection){
			Novi.log(connection.getRemoteAddressTCP().getAddress().toString() + " has connected.");
		}

		@Override
		public void received(Connection connection, Object object){
			if(object instanceof ConnectPacket){
				//ConnectPacket connect = (ConnectPacket)object;
				Player player = new Player();
				DataPacket data = new DataPacket();
				data.playerid = player.GetID();
				connection.sendTCP(data);
				server.sendToAllExceptTCP(connection.getID(), player.AddSelf());
				Novi.log(connection.getRemoteAddressTCP().getAddress().toString() + " has joined.");
				//server.sendToAllExceptTCP(connection.getID(), connect);
			}
		}
	}

	public static void main(String[] args){
		active = true;
		new NoviServer().createServer();
	}
}
