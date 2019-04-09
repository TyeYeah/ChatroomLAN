package server;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GetHostIP {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//			getHostIp();
//	}
	public static String getHostIp(){
	       try{
	           Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
	          while (allNetInterfaces.hasMoreElements()){
	                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
	               Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
	              while (addresses.hasMoreElements()){
	                   InetAddress ip = (InetAddress) addresses.nextElement();
	                  if (ip != null 
	                         && ip instanceof Inet4Address
	                         && !ip.isLoopbackAddress() //loopback means lo,IPv4's loopback range is 127.0.0.0 ~ 127.255.255.255
	                          && ip.getHostAddress().indexOf(":")==-1){
	                     //System.out.println("Host IP is : " + ip.getHostAddress());
	                      return ip.getHostAddress();
	                   } 
	              }
	           }
	       }catch(Exception e){
	          e.printStackTrace();
	        }
	       return null;
	    }

}
