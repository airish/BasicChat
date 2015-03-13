package com.airish.basicchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

// TODO convert to TCP, use server authentication
public class Server implements Runnable{
	
	
	private List<User> users = new ArrayList<User>();
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	
	private Thread run;
	private Thread manage;
    private Thread send;
    private Thread receive;
	
	public Server(int port){
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		run = new Thread(this, "Server");
		run.start();
	}
	
	public void run(){
		running = true;
		System.out.println("Server started on port "+port);
		manageClients();
		receive();
	}
	
	public void manageClients(){
		manage = new Thread("Manage"){
			public void run(){
				while(running){
					
				}	
			}
		};
		manage.start();
	}
	
	public void receive(){	
		receive = new Thread("Receive"){
			public void run(){
				while(running){
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					processPacket(packet);
				
					users.add(new User("Name", 
							packet.getAddress(),
							packet.getPort(), 
							50)
					);
					
					System.out.println(users.get(0).address().toString()
							+users.get(0).port());
				}	
			}
		};
		receive.start();
	}
	
	public void processPacket(DatagramPacket packet){
		String packetData = new String(packet.getData());
		if(packetData.startsWith("/c/")){ // Connection packet
			//UUID id = UUID.randomUUID();
			int id = UniqueIdentifier.getIdentifier();
			users.add(new User(packetData.substring(3,packetData.length()), 
						packet.getAddress(),
						packet.getPort(), 
						id)
			);
		} else {
			System.out.println(packetData);
		}
	}
}
