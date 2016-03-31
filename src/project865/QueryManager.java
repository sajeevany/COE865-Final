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
import java.util.concurrent.ScheduledExecutorService;


public class QueryManager{
	
	private String myUniqueID = null;
	private String[] myIPs = null;
	private int[] mySockets = null;
	private String[] myResourceList = null;
	private HelloSender myHSenderManager = null;
	private  ArrayList<QueryReceiver> myQRecvReceiver = new ArrayList<QueryReceiver>();;
	private ArrayList<QueryReceiver> qReceiver = new ArrayList<QueryReceiver>();
	private Runnable qSender;
	
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
                       myQRecvReceiver.add(new QueryReceiver(new DatagramSocket(mySockets[i])));
                } catch (SocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
	     } 
		 

	}
	
	//start hello receiver and sender threads
	public void runManager()
	{          
            System.out.println("SENDING QUERY");
	            	QueryPacket sendToKrish = new QueryPacket("R1", "R2", true, "r3File.txt", false);
               QueryManager.sendQueryPacket(10903, "10.1.1.11", sendToKrish);
	
               for (QueryReceiver qRec :myQRecvReceiver)
               {
                   qRec.startQRecvThread();
               }
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
			if (queryPacket.isRequest()==true){
				 System.out.println("-------------------SEnt Reponse -------------------");
	             System.out.println("Query: " + queryPacket.toString());
	             System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				
			}else
			 System.out.println("-------------------Forwarding Packet -------------------");
             System.out.println("Query: " + queryPacket.toString());
             System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			
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
                        
	                System.out.println("listening on local port " + localSocket.getLocalPort() + "For Queries");
                            
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
	                        	   // Unique ID= Destination and it is a Response with result true
	                        	   	if(queryReceived.isRequest()==false && queryReceived.isRequestResult()==true){
	                        		 System.out.println("-------------------Response Received -------------------");
	 	                            System.out.println("File: " + queryReceived.getRequestedFileName() + "exists at" + queryReceived.getMyUniqueID() );
	 	                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	                        	   	}else if(queryReceived.isRequest()==false && queryReceived.isRequestResult()==false){
	                        	   	// Unique ID= Destination and it is a Response with result false
	                        		 System.out.println("-------------------Response Received -------------------");
		 	                            System.out.println("File: " + queryReceived.getRequestedFileName() + "NOT exist at" + queryReceived.getMyUniqueID() );
		 	                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	                        	   	}else{
	                        	   	// Unique ID=Destination and it is a Request, check file name exitsts in resourceList and respond accordingly
	                        	   		if(Arrays.asList(myResourceList).contains(queryReceived.getRequestedFileName())){
	                        	   		
	                        	   			QueryPacket queryResponse= new QueryPacket(myUniqueID, queryReceived.getMyUniqueID(), false, queryReceived.getRequestedFileName(), true);
	                        	   			//enter shortest Pathsock and IP
	                        	   			QueryManager.sendQueryPacket(8006, "10.1.1.13", queryResponse);
	                        	   			
	                        	   		}else {
	                        	   		// Unique ID=Destination and it is a Request, but resource doesnt exist
	                        	   			QueryPacket queryResponse= new QueryPacket(myUniqueID, queryReceived.getMyUniqueID(), false, queryReceived.getRequestedFileName(), false);
	                        	   			//enter shortest Pathsock and IP
	                        	   			QueryManager.sendQueryPacket(8006, "10.1.1.13", queryResponse);
	                        	   			}
	                        	   	}
	                           	}else {
	                           		// Package needs to be forwarded
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
	        System.out.println("Starting Query Receiver");
	        qReceiver.run();

	    }

	    

	}
}