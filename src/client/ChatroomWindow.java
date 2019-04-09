package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Panel;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.border.MatteBorder;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class ChatroomWindow extends JFrame {

	
	public static String SERVER_IP = "127.0.0.1"; // default server IP
	public static String uname = "definite local cheater"; // default user ID
    private static final int SERVER_PORT = 3000; // server port
    private static final String END_MARK = "quit"; // secret code to exit the chatroom
 
    private Socket client;
 
    private Writer writer;//used for interact with port 3000
    private Writer writer2;//used for port 3001
 
    //message sending stream
    private BufferedReader in;
	
	Socket ss;//update user list
	Socket sss;//message sending and showing
	String who;//identify whom to talk
	
	
	
	
	JTextArea userList = new JTextArea();
	final JTextArea chatWindow = new JTextArea();
	final JTextPane inputWindow = new JTextPane();
	JComboBox comboBox = new JComboBox();
	JCheckBox whisper = new JCheckBox("Whisper");
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatroomWindow frame = new ChatroomWindow(SERVER_IP,uname);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ChatroomWindow(String ip,String id) throws UnknownHostException, IOException {
		setTitle("ChatroomLAN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Panel panel = new Panel();
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		
		userList.setEditable(false);
		scrollPane.setViewportView(userList);
		userList.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		userList.setText("——User-List——");
		
		Panel panel_1 = new Panel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		
		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"all"}));
		panel_2.add(comboBox);
		
		
		panel_2.add(whisper);
		
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		
		
		inputWindow.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.add(inputWindow, BorderLayout.SOUTH);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1, BorderLayout.CENTER);
		
		
		chatWindow.setEditable(false);
		chatWindow.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setViewportView(chatWindow);
		JButton sendButton = new JButton("Send");
		
		who=comboBox.getItemAt(0).toString();
		//set event listener for combox and set 'who' when select one item
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			who=e.getItem().toString();
			}
			});
		
		SERVER_IP=ip;uname=id;
		
		sss=new Socket(SERVER_IP,SERVER_PORT+1);
		
		ss=new Socket(SERVER_IP,SERVER_PORT);
		client=ss;
	
    writer = new OutputStreamWriter(ss.getOutputStream(), "UTF-8");
    writer2 = new OutputStreamWriter(sss.getOutputStream(), "UTF-8");
    
    new Thread(new ReceiveMsgTask()).start(); // start message receiving
    new Thread(new ReceiveUserList()).start(); // start user list listening
    
    
    writer.write(uname);//send id to server side ( add user to the user list )
    writer.write("\n");
    writer.flush(); 
    	/*
    	 * set listener for send button
    	 * send message when click
    	 * meanwhile update user list 
    	 */
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//test
				//String str=inputWindow.getText();
				//chatWindow.append("\n"+str);
				try {
					
		 
		        
		            in = new BufferedReader(new StringReader(inputWindow.getText()));
		            inputWindow.setText("");
		            
		            String inputMsg=""; 
		            inputMsg += in.readLine();
		            
		            if(!inputMsg.equals(""+null)){
		            	if(whisper.isSelected()){
		            	writer.write("whisper");
			            writer.write("\n");
		            	writer.flush(); // remember to flush
		            	}else{
		            		writer.write(" ");
				            writer.write("\n");
			            	writer.flush(); // remember to flush
		            	}
			            if(!who.isEmpty()){
			            	writer.write(who);writer.write("\n");
			            	writer.flush(); // remember to flush
				           
			            } else{
			            	writer.write(" ");
			            	writer.write("\n");
		            	writer.flush();}
		            writer.write(inputMsg);
		            writer.write("\n");
		            writer.flush(); // remember to flush
		            
		            
		            }
		            writer2.write(inputMsg);
		            writer2.write("\n");
		            writer2.flush(); 
		            //3 lines above used to update user list
				
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		};
		panel_2.add(sendButton);
		sendButton.addActionListener(al);
//		chatWindow.getDocument().addDocumentListener(new DocumentListener...);
		//2019.4.8 23:33
		//another attempt, to detect textArea's change, but cannot find enough instructions on the internet
		//btw it will effect client side's performance
		SwingUtils.enterPressesWhenFocused( inputWindow, al); 
	}

	
	
	
	
	
	
	//thread to receive message from server
	class ReceiveMsgTask implements Runnable {
		 
        private BufferedReader buff;
 
        @Override
        public void run() {
            try {
                this.buff = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                while (true) {
                    String result = buff.readLine();
                    if (END_MARK.equals(result)) { // exit when encounter 'quit'
                       // System.out.println("Cliect[port:" + uname + "] you just quit the room");
                        chatWindow.append("\nCliect[" + uname + "] you just exit the room");
                        break;
                    } else { // show message from server
                        //System.out.println(result);
                        chatWindow.append("\n"+result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    buff.close();
                    writer.close();
                    client.close();
                    in.close();
                } catch (Exception e) {
 
                }
                System.exit(0);
            }
        }
    }
	//thread to update user list
	class ReceiveUserList implements Runnable {
		 
        private BufferedReader buff;
 
        @Override
        public void run() {
            
                try {
                	
					this.buff = new BufferedReader(new InputStreamReader(sss.getInputStream(), "UTF-8"));
				while (true) {
					String result = buff.readLine();
                    if (END_MARK.equals(result)) { // exit when encounter 'quit'
                        //System.out.println("Cliect[port:" + uname + "] you just quit the room");
                        userList.append("\nCliect[" + uname + "] you just exit the room");
                        break;
                    }else if("clean".equals(result)){ // 
                        
                        userList.setText("");
                        comboBox.setModel(new DefaultComboBoxModel(new String[] {"select whom to talk"}));
                        comboBox.addItem("all");
                    } 
                    else { // 
                       // System.out.println(result);
                        userList.append("\n"+result);
                        if(!result.substring(0, 1).equals("=")){
                        comboBox.addItem(result);
                    }
                    }
 
				}
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
	                try {
	                    buff.close();
	                    writer2.close();
	                    sss.close();
	                    in.close();
	                } catch (Exception e) {
	 
	                }
	                
	                System.exit(0);
                }

            }
        }
}
 