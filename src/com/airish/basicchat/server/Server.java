package com.airish.basicchat.server;

import java.net.DatagramSocket;
import java.net.SocketException;

// TODO convert to TCP, use server authentication
public class Server implements Runnable{
	
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
		}
		
		run = new Thread(this, "Server");
	}
	
	public void run(){
		running = true;
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
					
				}
			}
		};
		receive.start();
	}
}
