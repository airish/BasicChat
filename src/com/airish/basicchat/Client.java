package com.airish.basicchat;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, address;
	private int port;
	private JTextField txtMessage;
	private JTextArea txtrHistory;
	private DefaultCaret caret;
	
	private Socket socket; 
	private InetAddress ip;

	/**
	 * Create the frame.
	 */
	public Client(String name, String address, int port) {
		setTitle("Basic Chat Client");
		this.name = name;
		this.address = address;
		this.port = port;
		boolean connected = openConnection(address, port);
		createWindow();
		if(connected)
			sendMessage(name+" Connecting to "+address+":"+port);
		else {
			sendMessage(name+" : Connection Failed!");
			System.out.println("Connection failure");
		}
	}
	
	private boolean openConnection(String address, int port){
		try {
			socket = new Socket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return true;
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
				sendMessage();
			}
		});
		
		
		txtMessage = new JTextField();
		// Send message to console when enter key is pressed
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					sendMessage();
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
	 *  Sends a message to the textArea of the client window.
	 *  If no strings are passed to the method, then text will be sent
	 *  from the User's textField. This generalized function can be used by
	 *  either the User or the program itself.
	 * @param strings
	 */
	
	public void sendMessage(String...strings){
		if(strings.length == 0){
			String message = txtMessage.getText();
			if(message.equals("")) return;
				
			txtrHistory.append(name+": "+message+"\n");
			txtMessage.setText("");
		}
		else for(String s : strings)
			txtrHistory.append(s+"\n");
		
		txtrHistory.setCaretPosition(txtrHistory.getDocument().getLength());
		txtMessage.requestFocusInWindow();
	}
}
