package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;

import server.ChatroomServer.PushMsgTask;
import server.ChatroomServer.Task;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerWindow extends JFrame {

	private JPanel contentPane;
	final ChatroomServer cserver = new ChatroomServer(); // 
	JLabel number = new JLabel("0");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow frame = new ServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public ServerWindow() throws Exception {
		InetAddress addr = InetAddress.getLocalHost();  
        //String ip=addr.getHostAddress().toString(); 
		String ip=GetHostIP.getHostIp();//get this PC's IP
        String hostName=addr.getHostName().toString(); //get this PC's hostname  
        
		setTitle("ChatroomLAN-Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ChatroomLAN-Serverside");
		lblNewLabel.setFont(new Font("华文行楷", Font.PLAIN, 32));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 0, 404, 60);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Host Server IP:");
		lblNewLabel_1.setBounds(38, 90, 142, 15);
		panel.add(lblNewLabel_1);
		
		JLabel ipshow = new JLabel("");
		ipshow.setBounds(243, 90, 99, 15);
		panel.add(ipshow);
		
		ipshow.setText(ip);
		
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setBounds(38, 130, 164, 15);
		panel.add(lblServerStatus);
		
		final JLabel statusshow = new JLabel("Off");
		statusshow.setBounds(243, 130, 30, 15);
		panel.add(statusshow);
		
		
		
		JButton start = new JButton("Start");
		//start room after clicking 
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Start()).start(); 
				new Thread(new Count()).start(); 
				statusshow.setText("On");
			}
		});
		start.setBounds(38, 167, 142, 23);
		panel.add(start);
		
		JButton stop = new JButton("Stop");
		//click to stop server
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				statusshow.setText("Off");
			}
		});
		stop.setBounds(249, 167, 142, 23);
		panel.add(stop);
		
		JLabel lblNewLabel_2 = new JLabel("Users Online");
		lblNewLabel_2.setBounds(259, 202, 112, 15);
		panel.add(lblNewLabel_2);
		
		
		number.setBounds(48, 202, 83, 15);
		panel.add(number);
		
		
	}
	
	//thread used for start server
	class Start implements Runnable {
		 
		@Override
		public void run() {
			try {
				cserver.load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
	}
	//thread used for update list
	class Count implements Runnable {
		 
		@Override
		public void run() {
			while(true){
				number.setText(Integer.toString(ChatroomServer.userList.size()));
			}
		}
 
	}
}
