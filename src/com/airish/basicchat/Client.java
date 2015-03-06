package com.airish.basicchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, address;
	private int port;


	/**
	 * Create the frame.
	 */
	public Client(String name, String address, int port) {
		setTitle("Basic Chat Client");
		this.name = name;
		this.address = address;
		this.port = port;
		createWindow();
	}
	
	private void createWindow(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{5,590, 5};
		gbl_contentPane.rowHeights = new int[]{40, 350, 10};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTextArea txtrHistory = new JTextArea();
		GridBagConstraints gbc_txtrHistory = new GridBagConstraints();
		gbc_txtrHistory.fill = GridBagConstraints.BOTH;
		// Correspond to column and row widths/heights
		gbc_txtrHistory.gridx = 1;
		gbc_txtrHistory.gridy = 1;
		contentPane.add(txtrHistory, gbc_txtrHistory);
		
		setVisible(true);
	}

}
