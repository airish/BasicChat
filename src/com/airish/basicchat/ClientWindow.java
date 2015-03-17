package com.airish.basicchat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame implements Runnable{	
	
	private static final long serialVersionUID = 1L;

	
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea txtrHistory;
	
	private Thread listen, run;
	private Client client;
	
	private boolean running;
		

	public ClientWindow(String name, String address, int port) {
		setTitle("Basic Chat Client");
		client = new Client(name, address, port);
		
		// Connect to network
		boolean connected = client.openConnection(address);
		createWindow();
		if(connected){
			displayMessage(name+" Connecting to "+address+":"+port);
			String connection = "/c/"+name;
			client.send(connection.getBytes());
		}else {
			displayMessage(name+" : Connection Failed!");
			System.out.println("Connection failure");
		}
		
		run = new Thread(this, "Running");
		running = true;
		run.start();
	}
	
	private void createWindow(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{5,560, 30, 5};
		gbl_contentPane.rowHeights = new int[]{50, 340, 10};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		txtrHistory = new JTextArea();
		txtrHistory.setEditable(false);
		
		
		// Create scroll pane so textArea is scrollable.
		JScrollPane scroll = new JScrollPane(txtrHistory);
		
		//caret = (DefaultCaret)txtrHistory.getCaret();

		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		// Correspond to column and row widths/heights
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		contentPane.add(scroll, scrollConstraints);
		
		// Create the send button.
		JButton btnSend = new JButton("Send");
		
		// Send text message to console when send button is hit
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayMessage();
			}
		});
		
		
		txtMessage = new JTextField();
		// Send message to console when enter key is pressed
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					displayMessage();
				}
			}
		});
		
		
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		setVisible(true);
		txtMessage.requestFocusInWindow();

	}

	/**
	 *  Displays a message on the textArea of the client window.
	 *  If no strings are passed to the method, then text will be sent
	 *  from the User's textField. This generalized function can be used by
	 *  either the User or the program itself.
	 * @param strings
	 */
	public void displayMessage(String...strings){
		if(strings.length == 0){
			String message = txtMessage.getText();
			if(message.equals("")) return;
			
			message = client.name()+": "+message;
			txtrHistory.append(message+"\n");
			client.send(("/m/"+message).getBytes());
			txtMessage.setText("");
			txtMessage.requestFocusInWindow();
		}
		else {
			for(String s : strings){
				txtrHistory.append(s+"\n");
			}
		}
		txtrHistory.setCaretPosition(txtrHistory.getDocument().getLength());
	}
	
	public void listen(){
		
		listen = new Thread(){
			public void run(){
				while(running){
					String message = client.receive();
					
					// If a connection is establish, give the client an ID
					if(message.startsWith("/c/")){
						client.setID(Integer.parseInt(message.substring(3)));
					} else if(message.startsWith("/m/")){
						displayMessage(message.substring(3));
					}

				}
			}
		};
		
		listen.start();
	}
	
	public void run(){
		listen();
	}
}