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

public class HelloManager{
	
    private String myUniqueID = null;
    private Map<String, Integer> myIPSocketMap = null;
    public static final int helloProtocolPort = 10090;
    private ArrayList<InetAddress> targetIPs;      
    private ArrayList<HelloReceiver> hReceiver = new ArrayList<HelloReceiver>();
    private HelloSender myHSenderManager = null;
    private  HelloReceiver myHRecvManager = null;
        
       
	//public HelloManager(String uniqueID, ArrayList<InetAddress> targetIPs, Map<InetAddress, DatagramSocket> myIPSocketMap, String[] myResources) throws SocketException
	//public HelloManager(String uniqueID, HashMap<String,String> srcTargetIPMap, Map<InetAddress, DatagramSocket> myIPSocketMap, String[] myResources) throws SocketException
    public HelloManager(String uniqueID, ArrayList<SourceTargetIPPair> stIPPairList, Map<String, Integer> myIPSocketMap, String[] myResources) throws SocketException
	{
        //initialize default values that should not change
        this.myUniqueID = uniqueID;
        //this.targetIPs = targetIPs;
        this.myIPSocketMap = myIPSocketMap;

        //initialize routing table
        HashMap<String, ArrayList<String>> hrMap = new HashMap<String, ArrayList<String>>();
        hrMap.put(this.myUniqueID, new ArrayList<String>(Arrays.asList(myResources)));
        
        //Add local information into routing table
        ArrayList<String> myDCNeighbours = new ArrayList<String>();
        for (SourceTargetIPPair ipPair : stIPPairList)
        {
        	myDCNeighbours.add(ipPair.getTargetIP());
        }
        final AttributeTrio myATrio = new AttributeTrio(this.myUniqueID, hrMap, myDCNeighbours);
        for (Map.Entry<String, Integer> sockMap : myIPSocketMap.entrySet())
        {
            RoutingTable.getRoutingTableInstance().addRoute(new RoutingTableEntry(sockMap.getKey(), sockMap.getValue(), uniqueID, new ArrayList<AttributeTrio>(){{add(myATrio);}} ));
        }
        
        //Attach a receiver to listen to the default hello port
        //myHSenderManager = new HelloSender(targetIPs, this.myUniqueID, this.myIPSocketMap);
        myHSenderManager = new HelloSender(stIPPairList, this.myUniqueID, this.myIPSocketMap);
        //require single hello receiver for listening
        myHRecvManager = new HelloReceiver(new DatagramSocket(this.helloProtocolPort));

		/*hReceiver.add(new HelloReceiver(helloProtocolPort));*/     
	}
        
     	
	//start hello receiver and sender threads
	public void runManager()
	{
            myHSenderManager.startHSenderThreads();
            myHRecvManager.startHRecvThread();
	}
		
	public void sendHelloPacket(DatagramSocket dSocket, HelloPacket helloPacket)
	{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutput hPacket = null;
		
		try {
			hPacket = new ObjectOutputStream(byteOutputStream);
			hPacket.writeObject(helloPacket);
			byte[] hPBytes = byteOutputStream.toByteArray();
			
			DatagramPacket dPacket = new DatagramPacket(hPBytes, hPBytes.length);
			dSocket.send(dPacket);
			byteOutputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendHelloPacket(int port, String targetIp, HelloPacket helloPacket)
	{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutput hPacket = null;
		
		try {
			hPacket = new ObjectOutputStream(byteOutputStream);
			hPacket.writeObject(helloPacket);
			byte[] hPBytes = byteOutputStream.toByteArray();
			
            DatagramPacket dPacket = new DatagramPacket(hPBytes, hPBytes.length, InetAddress.getByName(targetIp), 10090);
            DatagramSocket dSocket = new DatagramSocket();
     
			dSocket.send(dPacket);
			byteOutputStream.close();
			dSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public HelloPacket receiveHelloPacket(DatagramSocket dSocket, String expectedUniqueID)
	{
		byte[] buffer = new byte[2000];
		DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);
		
		try {
			dSocket.setSoTimeout(HelloPacket.timeout);
		} catch (SocketException e1) {
			System.err.println("Error setting socket timeout.");
			e1.printStackTrace();
		}
		
		try {
			
			dSocket.receive(dPacket);
		}catch (SocketException se)
		{
			System.err.println("Receive timed out. Initialize table prune.");
			//pruneRoutingTableEntries(expectedUniqueID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(dPacket.getData());
		ObjectInput helloPacketIn = null;
		HelloPacket hPacket  = null;
		
		try {
			
			helloPacketIn = new ObjectInputStream(byteInputStream);
			hPacket = (HelloPacket) helloPacketIn.readObject();
			byteInputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return hPacket;
		
	}
	
}

class HelloSender
{
    final String myUID = null;
    byte[] buffer = new byte[65508];
    private ScheduledExecutorService scheduler = null;
    ArrayList<Runnable> hSendList = new ArrayList<Runnable>();

    private Runnable hSendRunnable(final InetAddress targetIPAddr, final DatagramSocket targetSocket, final String myUniqueID, final String queryIP, final int myQueryPort)
    {
        Runnable hSend = new Runnable()
        {
            public void run()
            {
                
                              
                //in  actuality we are going to send our entire routing table's resource map
                //for testing 
                //HashMap<String, String> neighbourAndResource = new HashMap<String, String>();
                //neighbourAndResource.put("R5", "my exam");
                ArrayList<RoutingTableEntry> routeList = RoutingTable.getRoutingTableInstance().getRoutes();
                ArrayList<AttributeTrio> netResourceList = new ArrayList<AttributeTrio>();
                
                for (RoutingTableEntry route: routeList)
                {
                    netResourceList.addAll(route.getAttributesList());
                }
                netResourceList = RoutingTableEntry.removeDuplicates(netResourceList);
                HelloPacket helloP  = new HelloPacket(myUniqueID, netResourceList, queryIP, myQueryPort);
                
                HelloManager.sendHelloPacket(targetSocket.getLocalPort(), targetIPAddr.getHostAddress(), helloP);
                System.out.println("-------------------Sending Hello -------------------");
                System.out.println("sending to " + targetIPAddr.getHostAddress() + " on local port " + targetSocket.getLocalPort() + " my ip is " + targetSocket.getLocalAddress().getHostAddress());
                System.out.println("Hello: " + helloP.toString());
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            };
        };

        return hSend;
    };
    
    public HelloSender(ArrayList<SourceTargetIPPair> stIPPairList, String myUniqueID, Map<String, Integer> myIPSocketMap) throws SocketException {
    
        int iQueryPort = 0;
        String queryIP = null;
        scheduler = Executors.newScheduledThreadPool(stIPPairList.size() + 1);
               
        for (SourceTargetIPPair t :stIPPairList)
        {
            for (Map.Entry<String, Integer> sockMap : myIPSocketMap.entrySet())
            {
                //If currect socket map's ip is part of the same network as the target ip, set iQuert port
                //if (App.getNetwork(sockMap.getKey().getHostAddress()).equals(App.getNetwork(t.getHostAddress())))
            	if (sockMap.getKey().equals(t.getSourceIP()))
                {
                    iQueryPort = sockMap.getValue();
                    queryIP = sockMap.getKey();
                    break;
                }
            }
            
            if (queryIP == null)
            {
                throw new IllegalArgumentException("Unable to match target IP " + t + "with iQuery port");
            }
            else
            {
                try {
					hSendList.add(hSendRunnable(InetAddress.getByName(t.getTargetIP()), new DatagramSocket(), myUniqueID, queryIP, iQueryPort));
				} catch (UnknownHostException e) {
					System.out.println("Unknown host " + t.getTargetIP());
					e.printStackTrace();
				}
                queryIP = null;
            }
            
            
        }
        //TODO remove debug message at later date
        System.out.println("I have " + stIPPairList.size() + " targets");
    }

    public void startHSenderThreads()
    {
        System.out.println("Starting sender threads. I'm spinning up " + hSendList.size() + " threads");
        for (Runnable hSR : hSendList)
        {
            scheduler.scheduleAtFixedRate(hSR, 0, HelloPacket.sendInterval, TimeUnit.SECONDS);
        }

    }
	
}

class HelloReceiver {

    byte[] buffer = new byte[65508];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    Runnable hReceiver = null;

    private Runnable hRecvRunnable(final DatagramSocket localSocket) {
        Runnable hSend = new Runnable() {
            @Override
            public void run() {
                System.out.println("listening on " + localSocket.getLocalAddress() + " on local port " + localSocket.getLocalPort());
                System.out.println("listening to " + localSocket.getRemoteSocketAddress());
                while (true) {
                    packet.setLength(65508);
                    try {
                        localSocket.receive(packet);
                        ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
                        ObjectInputStream oos = new ObjectInputStream(baos);
                        HelloPacket helloReceived;
                        try {
                            helloReceived = (HelloPacket) oos.readObject();
                            System.out.println(helloReceived.toString());
                            
                            System.out.println("-------------------Recv Hello -------------------");
                            System.out.println("Hello: " + helloReceived.toString());
                            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            
                            RoutingTableEntry receivedTableEntry = new RoutingTableEntry(helloReceived.getMyAddress(), helloReceived.getMyFTSocket() , helloReceived.getMyUniqueID(), helloReceived.getAttributesList());
                            RoutingTable.getRoutingTableInstance().addRoute(receivedTableEntry);
                            System.out.println("\n\n\n\n\n\n" + RoutingTable.getRoutingTableInstance().getRoutes().toString() + "\n\n\n\n\n\n");
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

        return hSend;
    }

    public HelloReceiver(DatagramSocket localSocket) throws SocketException {
        hReceiver = hRecvRunnable(localSocket);
    }

    public void startHRecvThread()
    {
        System.out.println("Starting listener/receiver threads. I'm spinning up " + 1 + " thread");
        hReceiver.run();

    }
}
