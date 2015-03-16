package com.airish.basicchat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblIpAddress;
	private JTextField txtPort;
	private JLabel lblPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(250,300);
		setLocationRelativeTo(null);
		//setBounds(100, 100, 250, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(45, 39, 160, 28);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(49, 11, 51, 28);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(45, 95, 160, 28);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(49, 79, 96, 16);
		contentPane.add(lblIpAddress);
		
		txtPort = new JTextField();
		txtPort.setBounds(45, 155, 160, 28);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(49, 135, 61, 22);
		contentPane.add(lblPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String name = txtName.getText();
					String address = txtAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}
		});
		btnLogin.setBounds(66, 213, 117, 29);
		contentPane.add(btnLogin);
	}

	private void login(String name, String address, int port) {
		dispose(); // Close login window
		new ClientWindow(name, address, port);
	}
}
