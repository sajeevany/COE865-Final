package project865;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QueryManager{
	
	private String myUniqueID = null;
	private String[] myIPs = null;
	private int[] mySockets = null;
	private String[] myResourceList = null;
	private  QueryReceiver myQRecvManager = null;
	private ArrayList<QueryReceiver> qReceiver = new ArrayList<QueryReceiver>();
	
	public QueryManager(String uniqueID, String[] ips, int[] sockets, String[] myResourceList)
	{
		this.myUniqueID = uniqueID;
		this.myIPs = ips;
		this.mySockets = sockets;
		this.myResourceList= myResourceList;
		
        //query receiver for listening for queries
		 for (int i = 0; i < myIPs.length; i++)
	     {
	      
	        	 try {
					myQRecvManager = new QueryReceiver(new DatagramSocket(mySockets[i]));
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     } 
		 

	}
	
	//start hello receiver and sender threads
	public void runManager()
	{
        myQRecvManager.startQRecvThread();
	}
	
	public static void sendQueryPacket(int port, String targetIp, QueryPacket queryPacket)
	{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutput hPacket = null;
		
		try {
			hPacket = new ObjectOutputStream(byteOutputStream);
			hPacket.writeObject(queryPacket);
			byte[] hPBytes = byteOutputStream.toByteArray();
			
            DatagramPacket dPacket = new DatagramPacket(hPBytes, hPBytes.length, InetAddress.getByName(targetIp), port);
            DatagramSocket dSocket = new DatagramSocket();
     
			dSocket.send(dPacket);
			byteOutputStream.close();
			dSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	class QueryReceiver {

	    byte[] buffer = new byte[65508];
	    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	    Runnable qReceiver = null;


	    private Runnable qRecvRunnable(final DatagramSocket localSocket) {
	        Runnable qRecv = new Runnable() {
	        	
	        	 ArrayList<RoutingTableEntry> routeList = RoutingTable.getRoutingTableInstance().getRoutes();
	             ArrayList<AttributeTrio> netResourceList = new ArrayList<AttributeTrio>();
	             
	               
	            @Override
	            public void run() {
	            	for (RoutingTableEntry route: routeList)
	                {
	                    netResourceList.addAll(route.getAttributesList());
	                }
	            	
	                System.out.println("listening on " + localSocket.getLocalAddress() + " on local port " + localSocket.getLocalPort());
	                System.out.println("listening to " + localSocket.getRemoteSocketAddress());
	                while (true) {
	                    packet.setLength(65508);
	                    try {
	                        localSocket.receive(packet);
	                        ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
	                        ObjectInputStream oos = new ObjectInputStream(baos);
	                        QueryPacket queryReceived;
	                        try {
	                        	queryReceived = (QueryPacket) oos.readObject();
	                            System.out.println(queryReceived.toString());
	                            
	                            System.out.println("-------------------Recv Query -------------------");
	                            System.out.println("Query: " + queryReceived.toString());
	                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	                            
	                           if(queryReceived.getMyDestinationID().equals(myUniqueID)){
	                        	 if(  Arrays.asList(myResourceList).contains(queryReceived.getRequestedFileName())){
	                        		 QueryPacket queryResponse= new QueryPacket(myUniqueID, queryReceived.getMyUniqueID(), false, queryReceived.getRequestedFileName(), true);
	                        		 //enter shortest Pathsock and IP
	                        		 QueryManager.sendQueryPacket(8006, "10.1.1.13", queryResponse);
	                        		 
	                        	 }else {
	                        		 QueryPacket queryResponse= new QueryPacket(myUniqueID, queryReceived.getMyUniqueID(), false, queryReceived.getRequestedFileName(), false);
	                        		//enter shortest Pathsock and IP
	                        		 QueryManager.sendQueryPacket(8006, "10.1.1.13", queryResponse);
	                        	 }
	                           }else {
	                        	   QueryManager.sendQueryPacket(8006, "10.1.1.13", queryReceived);
	                           }
	                            
	                            
	                            
	                            
	                        } catch (ClassNotFoundException ex) {
	                             ex.printStackTrace();
	                            // Logger.getLogger(HelloReceiver.class.getName()).log(Level.SEVERE, null, ex);
	                        }
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                      //  Logger.getLogger(HelloReceiver.class.getName()).log(Level.SEVERE, null, ex);
	                    }

	                }
	            }
	        ;
	        };

	        return qRecv;
	    }

	    public QueryReceiver(DatagramSocket localSocket) throws SocketException {
	        qReceiver = qRecvRunnable(localSocket);
	    }

	    public void startQRecvThread()
	    {
	        System.out.println("Starting listener/receiver threads. I'm spinning up " + 1 + " thread");
	        qReceiver.run();

	    }
	    

	}
}