package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
 

public class ChatroomServer extends ServerSocket {
 
	private static final int SERVER_PORT = 3000; // server port
	private static final String END_MARK = "quit"; // secret code to exit
	private static final String VIEW_USER = "viewuser"; // secret code to show user list
 
	static List<String> userList = new CopyOnWriteArrayList<String>();// user list
	private static List<Task> threadList = new ArrayList<Task>(); // list for every client (used for msg)
	private static List<Task2> threadList2 = new ArrayList<Task2>(); // list for every client(used to update user list)
	private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(20); // msg queue
	private static BlockingQueue<String> msgQueueq = new ArrayBlockingQueue<String>(20); // user list queue
	
	ServerSocket ssss;//receive msg from port 3301
	Socket sss;//send to pc on port 3301
	String to; //identify whom to send
	public ChatroomServer() throws Exception {
		
		super(SERVER_PORT);
		InetAddress addr = InetAddress.getLocalHost();  
        //String ip=addr.getHostAddress().toString(); 
		String ip=GetHostIP.getHostIp();;//get this PC's IP
        String hostName=addr.getHostName().toString(); //get this PC's hostname  
        System.out.println("IP is : "+ip+" and hostName is : "+hostName);
	}
 
	/*
	  use thread to send msg to every client
	 */
	public void load() throws Exception {
		new Thread(new PushMsgTask()).start(); // start msg sending 
		new Thread(new PushMsgTaskq()).start(); // start list updating 
		ssss=new ServerSocket(SERVER_PORT+1);
		
		while (true) {
			// server accepting
			Socket socket = this.accept();
			//sss=ssss.accept();
			// use a new thread to cope with every connection
			new Thread(new Task(socket)).start();
			new Thread(new Task2(ssss.accept())).start();
		}
	}
	
 
	/*
	  take msg from message queue and send it to everyone
	 */
	class PushMsgTask implements Runnable {
 
		@Override
		public void run() {
			while (true) {
				String msg = null;
				try {
					msg = msgQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (msg != null) {
					for (Task thread : threadList) {
						thread.sendMsg(msg);
					}
					//2019.4.8 23:00
					//first attempt, to let list update with msg sending, but failed
//					for (Task2 thread : threadList2) {
//						thread.sendMsg("clean");
//						thread.sendMsg(thread.onlineUsers());
//					}
					//thread using unsafely, and concerning knowledge has not been commanded
					//and i know it's an important knowledge point often presenting on the interviews
					//but it's a nt :)
				}
			}
		}
 
	}
	
	/*
	  take msg from message queue and send it to everyone
	  used for whisper
	 */
	class PushMsgTaskq implements Runnable {
		 
		@Override
		public void run() {
			while (true) {
				String msg = null;
				try {
					msg = msgQueueq.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (msg != null) {
					for (Task thread : threadList) {
						if(thread.userName.equals(msg.split("\n")[1])||thread.userName.equals(msg.split("\n")[2]))
						{
							thread.sendMsg(msg.split("\n")[0]);
							}
					}
				}
			}
		}
 
	}

 
	/*
	  thread coping with every conn for msg
	 */
	class Task implements Runnable {
 
		private Socket socket;
 
		private BufferedReader buff;
 
		private Writer writer;
 
		private String userName; 

 
		/*
		  constructor
		  used for adding user
		 */
		public Task(Socket socket) {
			this.socket = socket;
			//this.userName = String.valueOf(socket.getPort());
			try {
				this.buff = new BufferedReader(new InputStreamReader(
						socket.getInputStream(), "UTF-8"));
				
				this.writer = new OutputStreamWriter(socket.getOutputStream(),
						"UTF-8");
				userName=buff.readLine();
				//System.out.println("userName :"+userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			userList.add(this.userName);
			threadList.add(this);
			
			pushMsg("【" + this.userName + " entered 】");
			//System.out.println("Form Cliect[port:" + userName + "] "+ this.userName + " entered ");
		}
 
		@Override
		public void run() {

			try {
				while (true) {
					String q,to,msg;
					q= buff.readLine();
					to = buff.readLine();
					msg = buff.readLine();
					if(q.equals("whisper"))
					{
						
						if(!to.equals("all"))
						{
							//pushMsg("username:"+userName+"  to:"+to+"\n");
							if (END_MARK.equals(msg)) { 
								sendMsg(END_MARK);
								break;
							} else {
								pushMsgq(String.format("Whisper：%1$s speaks to %2$s：%3$s", userName,to, msg)+"\n"+to+"\n"+userName);
							}
							
						}else{
							if (END_MARK.equals(msg)) { 
								sendMsg(END_MARK);
								break;
							} else {
								pushMsg(String.format("%1$s speaks ：%2$s", userName, msg));
							}
						}
					}
					else{
					
					if(to.equals("all")){
						if (END_MARK.equals(msg)) { 
							sendMsg(END_MARK);
							break;
						} else {
							pushMsg(String.format("%1$s speaks ：%2$s", userName, msg));
						}
					}else{
						if (END_MARK.equals(msg)) { 
							sendMsg(END_MARK);
							break;
						} else {
							pushMsg(String.format("%1$s speaks to %2$s：%3$s", userName, to,msg));
						}
					}
					
					}
					
					
					
					
					
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally { 
				try {
					writer.close();
					buff.close();
					socket.close();
				} catch (Exception e) {
 
				}
				userList.remove(userName);
				threadList.remove(this);
				pushMsg("【" + userName + " exit the room 】");
				//System.out.println("Form Cliect[port:" + socket.getPort() + "] "+ userName + " exit the room ");
			}
		}
 
		/*
		 enqueue msg 
		 */
		private void pushMsg(String msg) {
			try {
				msgQueue.put(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		private void pushMsgq(String msg) {
			try {
				msgQueueq.put(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
 
	    /*
	     send msg
	     */

		private void sendMsg(String msg) {
			try {
				writer.write(msg);
				writer.write("\015\012");
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
	    /*
	     list users
	     */

		
 
	}
	
	

	
	
	/*
	  this thread used for updating list
	 */
	class Task2 implements Runnable {

		private Socket socket;

		private BufferedReader buff;

		private Writer writer;


		/*
		  constructor
		 */
		public Task2(Socket socket) {
			this.socket = socket;
			
			try {
				this.buff = new BufferedReader(new InputStreamReader(
						socket.getInputStream(), "UTF-8"));
				this.writer = new OutputStreamWriter(socket.getOutputStream(),
						"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			threadList2.add(this);
			sendMsg("clean");
			sendMsg(onlineUsers());
			
		}

		@Override
		public void run() {
			try {
				while (true) {
					//String msg = buff.readLine();
					String msg = null;
					Thread.sleep(1000);
					//2019.4.8 23:40
					//so finally i managed !
					//what a simple but effective way -- set a time delay for updating
					//it's an automatic way, comparing with formal semi-auto way to update 
					//a real fool i am! that's why i spent such a long time
					if (END_MARK.equals(msg)) { // 
						sendMsg(END_MARK);
						break;
					} else {
						sendMsg("clean");
						sendMsg(onlineUsers());  
						//2019.4.8 22:40
						//find it :[
						//core code for updating user list(after 3 nights searching)
						//and if u want to update, send some msg first 
						//so it appears that you should click 'send' to update user list
						//fix it tonight !
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally { // 
				try {
					writer.close();
					buff.close();
					socket.close();
				} catch (Exception e) {

				}
				
				threadList2.remove(this);
				
			}
		}

		

		private void sendMsg(String msg) {
			try {
				writer.write(msg);
				writer.write("\015\012");
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	    /*
	     list users
	     */

		private String onlineUsers() {
			String sbf ="";
			sbf+="=Online Users("+userList.size()+")= ";
			for (int i = 0; i < userList.size(); i++) {
				sbf+= "\n"+userList.get(i) ;
			}
			//sbf.append("===============================");
			return sbf;
		}

	}


	
	
	
	
	public static void main(String[] args) {
		try {
			ChatroomServer server = new ChatroomServer(); // 
			server.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 

}
