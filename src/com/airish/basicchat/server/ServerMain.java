package com.airish.basicchat.server;

public class ServerMain {
	private int port;
	private Server server;
	
	public ServerMain(int port){
		this.port = port;
		server = new Server(port);
	}
	
	public static void main(String...args){
		// Server must be passed a port as command line argument
		if(args.length != 1){
			System.out.println("Usage: java -jar BasicChatServer.jar [port]");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		new ServerMain(port);
	}
	
}
