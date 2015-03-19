package com.airish.basicchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

// TODO convert to TCP, use server authentication
// TODO: add server history of last 50 things said, to be sent to user when
// logging in

// TODO: Have the server maintain a log, and output that log as a text file
// at regular intervals, or when shutting down the server
public class Server implements Runnable{
	
	
	private List<User> users = new ArrayList<User>();
	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();
	private List<Integer> userResponses = new ArrayList<Integer>(); // TODO: Change to dictionary?
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	
	private Thread run;
	private Thread manage;
    private Thread send;
    private Thread receive;
    
    private final int MAX_ATTEMPTS = 5;
	
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
		
		// Take in console input
		Scanner scanner = new Scanner(System.in);
		while(running){
			String text = scanner.nextLine();
			if(!text.startsWith("/")){ // Send a message to the users
				sendToAll("/m/Server: "+text);
			} else if(text.startsWith("/kick/")){ // Kick a user from the server
				int id = Integer.parseInt(text.substring(6));
				User u = userMap.get(id);
				disconnect(id, false);
				
				send("/k/Server has terminated your connection".getBytes(), 
						u.address(),
						u.port());
			} else if(text.startsWith("/users")){ // Print out all user information
				for(int i = 0; i < users.size(); i++){
					User u = users.get(i);
					System.out.println("User "+u.ID()+" Name: "+u.name()
							+", Address: "+u.address()+", Port: "+u.port());
				}
			}

		}
	
	}
	
	
	public void manageClients(){
		manage = new Thread("Manage"){
			public void run(){
				while(running){
					sendToAll("/i/server");
					// Wait two seconds for responses
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i = 0; i < users.size(); i++){
						User u = users.get(i);
						if(!userResponses.contains(users.get(i).ID())){
							System.out.println("No response");
							if(u.attempts() >= MAX_ATTEMPTS){
								disconnect(u.ID(), false);
							} else {
								u.incrementAttempts();
							}
						} else {
							userResponses.remove(new Integer(u.ID()));
							u.resetAttempts();
						}
					}
				}	
			}
		};
		manage.start();
	}
	
	public void receive(){	
		receive = new Thread("Receive"){
			public void run(){
				while(running){
					System.out.println("Number of clients: "+users.size());
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					processPacket(packet);
				}	
			}
		};
		receive.start();
	}
	
	private void sendToAll(String message){
		
		System.out.println(message.trim());
		for(int i = 0; i < users.size(); i++){
			send(message.getBytes(), users.get(i).address(), users.get(i).port());
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
		String packetData = new String(packet.getData()).trim();
		System.out.println("Packet: "+packetData);
		if(packetData.startsWith("/c/")){ // Connection packet
			int id = UniqueIdentifier.getIdentifier();
			String name = packetData.substring(3,packetData.length());
			User newUser =new User(name,
									packet.getAddress(),
									packet.getPort(), 
									id);
			users.add(newUser);
			userMap.put(id, newUser);
			String m = "/c/" + id;
			System.out.println(name+" has connected to the server as user # "+id);
			send(m.getBytes(), packet.getAddress(), packet.getPort());
			sendToAll("/m/"+name+" has connected to the server.");
		} else if(packetData.startsWith("/m/")){ // Message packet
			sendToAll(packetData);
		} else if(packetData.startsWith("/d/")){ // Disconnection packet
			System.out.println("User disconnecting");
			boolean status = packetData.substring(3,4).equals("t");
			String id = packetData.substring(5);
			disconnect(Integer.parseInt(id), status);
		} else if(packetData.startsWith("/i/")){ // Ping response packet
			userResponses.add(Integer.parseInt(packetData.substring(3))); 
		} else {
			System.out.println(packetData);
		}
	}

	
	private void disconnect(int id, boolean status){
		System.out.println("Disconnect "+id);
		String name;
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).ID() == id){
				name = users.get(i).name();
				users.remove(i);
				userMap.remove(id);
				if(status){
					sendToAll("/m/"+name+" has logged out.");
				} else {
					sendToAll("/m/"+name+" has been disconnected.");
				}
				return;
			}
		}
	}
}
