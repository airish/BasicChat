package com.airish.basicchat;

public class ServerMain {
	int port;
	
	public ServerMain(int port){
		this.port = port;
		System.out.println("Port: "+port);
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
