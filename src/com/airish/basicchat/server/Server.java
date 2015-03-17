package com.airish.basicchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
					
					System.out.println(users.get(0).address().toString()
							+users.get(0).port());
				}	
			}
		};
		receive.start();
	}
	
	private void sendToAll(String message){
		for(User u : users){
			send(message.getBytes(), u.address(), u.port());
		}
	}
	
	private void send(final byte[] data,
					  final InetAddress  address,
					  final int port){
		send = new Thread("Send"){
			public void run(){
				DatagramPacket packet = new
						DatagramPacket(data, data.length,
								address, port);				
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public void processPacket(DatagramPacket packet){
		String packetData = new String(packet.getData());
		if(packetData.startsWith("/c/")){ // Connection packet
			int id = UniqueIdentifier.getIdentifier();
			String name = packetData.substring(3,packetData.length());
			users.add(new User(name,
						packet.getAddress(),
						packet.getPort(), 
						id)
			);
			String m = "/c/" + id;
			System.out.println(m);
			send(m.getBytes(), packet.getAddress(), packet.getPort());
			sendToAll(name+" has connected to the server.");
		} else if(packetData.startsWith("/m/")){ // Message packet
			sendToAll(packetData);
			System.out.println(packetData);
		} else {
			System.out.println(packetData);
		}
	}
}
