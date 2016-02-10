package net.pixelstatic.novi.server;

import net.pixelstatic.novi.modules.Network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NoviServer{
	public static final int port = 7576;
	Server server;

	void createServer(){
		try{
			Server server = new Server();
			server.addListener(new Listen());
			server.start();
			server.bind(Network.port, Network.port);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	class Listen extends Listener{
		@Override
		public void received(Connection connection, Object object){
			
		}
	}

	public static void main(String[] args){
		new NoviServer().createServer();
	}
}
