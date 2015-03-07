package com.airish.basicchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, address;
	private int port;
	private JTextField txtMessage;
	private JTextArea txtrHistory;

	/**
	 * Create the frame.
	 */
	public Client(String name, String address, int port) {
		setTitle("Basic Chat Client");
		this.name = name;
		this.address = address;
		this.port = port;
		createWindow();
		sendMessage(name+" Connecting to "+address+":"+port);
		sendMessage(name+" Disconnecting");
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
		GridBagConstraints gbc_txtrHistory = new GridBagConstraints();
		gbc_txtrHistory.insets = new Insets(0, 0, 5, 5);
		gbc_txtrHistory.fill = GridBagConstraints.BOTH;
		// Correspond to column and row widths/heights
		gbc_txtrHistory.gridx = 1;
		gbc_txtrHistory.gridy = 1;
		gbc_txtrHistory.gridwidth = 2;
		contentPane.add(txtrHistory, gbc_txtrHistory);
		
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
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 2;
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
	
	public void sendMessage(String...strings){
		if(strings.length == 0){
			String message = txtMessage.getText();
			if(!message.equals(""))
				txtrHistory.append(name+": "+message+"\n");
			txtMessage.setText("");
		}
		else for(String s : strings)
			txtrHistory.append(s+"\n");
		
		txtMessage.requestFocusInWindow();
	}
}
