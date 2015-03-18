package com.airish.basicchat.server;

import java.net.InetAddress;

/*
 * The User class corresponds to a client that has
 * logged in to the server. The server maintains a list of users.
 */
public class User {

	private String name;
	private InetAddress address;
	private int port;
	private final int ID;
	
	private int attempts;;
	
	public User(String name, InetAddress address, int port,  int ID){
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
		this.attempts = 0;
	}
	
	public String name(){
		return name;
	}
	
	public InetAddress address(){
		return address;
	}
	
	public int port(){
		return port;
	}
	
	public int ID(){
		return ID;
	}
	
	public int attempts(){
		return attempts;
	}
	
	public void incrementAttempts(){
		attempts++;
	}
	
	public void resetAttempts(){
		attempts = 0;
	}
}
