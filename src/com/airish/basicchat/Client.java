package com.airish.basicchat;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client  {	
	private String name, address;
	private int port;
	private int ID = -1;
	
	
	private DatagramSocket socket; 
	private InetAddress ip;
	
	private Thread send;

	
	
	/**
	 * Create the frame.
	 */
	public Client(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	// Open a connection to the socket using given address and port
	public boolean openConnection(String address){
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Receives a packet of data from network, returns it as String message
	public String receive(){
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		
		return message;
	}
	
	// Send message to network as datagram packet from given array of bytes
	public void send(final byte[] data){
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		send.start();
	}
	
	public String name(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public int port(){
		return port;
	}

	public int ID(){
		return ID;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
}
