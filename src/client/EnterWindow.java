package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EnterWindow extends JFrame {

	private JPanel contentPane;
	private JTextField ip;
	private JTextField id;

	/**
	 * Launch the application.
	 */
	static EnterWindow frame = new EnterWindow();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
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
	public EnterWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server IP");
		lblNewLabel.setBounds(69, 80, 108, 15);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nickname");
		lblNewLabel_1.setBounds(69, 160, 108, 15);
		panel.add(lblNewLabel_1);
		
		ip = new JTextField();
		ip.setText("127.0.0.1");
		ip.setBounds(187, 77, 108, 21);
		panel.add(ip);
		ip.setColumns(10);
		
		id = new JTextField();
		id.setBounds(187, 157, 108, 21);
		id.setText("anonymous");
		panel.add(id);
		id.setColumns(10);
		
		JButton btnEnter = new JButton("Enter Room");
		//use IP and ID to open chatroom 
		//if everything's alright ,u entered
		//or u get error message window
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				frame.dispose();
				ChatroomWindow cw;
				try {
					cw = new ChatroomWindow(ip.getText(),id.getText());
					cw.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "cannot connect the server");
					JOptionPane.showMessageDialog(null, "error when creating window");
				}
				
				
				
			}
		});
		btnEnter.setBounds(80, 200, 215, 23);
		panel.add(btnEnter);
		
		JLabel lblChatroomlan = new JLabel("ChatroomLAN");
		lblChatroomlan.setHorizontalAlignment(SwingConstants.CENTER);
		lblChatroomlan.setFont(new Font("华文新魏", Font.PLAIN, 24));
		lblChatroomlan.setBounds(80, 10, 215, 50);
		panel.add(lblChatroomlan);
	}

	
	
}
