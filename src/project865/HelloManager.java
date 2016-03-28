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
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloManager{
	
	private String myUniqueID = null;
	public static final int helloProtocolPort = 10090;
	ArrayList<InetAddress> targetIPs;      
	ArrayList<HelloReceiver> hReceiver = new ArrayList<HelloReceiver>();
	//ArrayList<HelloSender> hSender = new ArrayList<HelloSender>();
    HelloSender myHSenderManager = null;
    HelloReceiver myHRecvManager = null;
        
        
	
	public HelloManager(String uniqueID, ArrayList<InetAddress> targetIPs) throws SocketException
	{
		//initialize default values that should not change
		this.myUniqueID = uniqueID;
		this.targetIPs = targetIPs;
		
		//initialize routing table
		//TODO add routing table
		
		//Attach a receiver to listen to the default hello port
        myHSenderManager = new HelloSender(targetIPs);
		//require single hello receiver for listening
        myHRecvManager = new HelloReceiver(new DatagramSocket(this.helloProtocolPort));

		/*hReceiver.add(new HelloReceiver(helloProtocolPort));*/     
	}
        
        protected void helloSender()
        {
            
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
                byte[] buffer = new byte[65508];
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutput hPacket = null;
		
		try {
			hPacket = new ObjectOutputStream(byteOutputStream);
			hPacket.writeObject(helloPacket);
			byte[] hPBytes = byteOutputStream.toByteArray();
			
			//DatagramPacket dPacket = new DatagramPacket(hPBytes, hPBytes.length);
                        DatagramPacket dPacket = new DatagramPacket(hPBytes, hPBytes.length, InetAddress.getByName(targetIp), 10090);
                        DatagramSocket dSocket = new DatagramSocket();
                        System.out.println("" + hPBytes.toString() + " "+ hPBytes.length + " " + targetIp + " 10090");
			dSocket.send(dPacket);
			byteOutputStream.close();
			
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
    byte[] buffer = new byte[65508];
    private ScheduledExecutorService scheduler = null;
    ArrayList<Runnable> hSendList = new ArrayList<Runnable>();

    private Runnable hSendRunnable(final InetAddress targetIPAddr, final DatagramSocket localSocket)
    {
        Runnable hSend = new Runnable()
        {
            public void run()
            {
                System.out.println("sending to " + targetIPAddr.getHostAddress() + " on local port " + localSocket.getLocalPort());
                System.out.println("my ip is " + localSocket.getLocalAddress());
            };
        };

        return hSend;
    };

    public HelloSender(ArrayList<InetAddress> targetIPs) throws SocketException {

        scheduler = Executors.newScheduledThreadPool(targetIPs.size() + 1);
        for (InetAddress t : targetIPs)
        {
            hSendList.add(hSendRunnable(t, new DatagramSocket()));
        }
        System.out.print("I have " + targetIPs.size() + " targets");
    }

    public void startHSenderThreads()
    {
        System.out.print("going going gone");
        for (Runnable hSR : hSendList)
        {
            scheduler.scheduleAtFixedRate(hSR, 0, HelloPacket.sendInterval, TimeUnit.SECONDS);
        }

    }
	
}

class HelloReceiver 
{
    byte[] buffer = new byte[65508];
    Runnable hReceiver = null;
    
    private Runnable hRecvRunnable(final DatagramSocket localSocket)
    {
        Runnable hSend = new Runnable()
        {
            public void run()
            {
                while(true)
                {
                    System.out.println("listening on " + localSocket.getLocalAddress() + " on local port " + localSocket.getLocalPort());
                    System.out.println("listening to " + localSocket.getRemoteSocketAddress());
                    HelloManager.sendHelloPacket(targetSocket.getLocalPort(), targetIPAddr.getHostAddress(), new HelloPacket(HelloManager.myUniqueID, map, targetIPAddr, targetSocket));
                }
            };
        };

        return hSend;
    };

    public HelloReceiver(DatagramSocket localSocket) throws SocketException {
        hReceiver = hRecvRunnable(localSocket);
    }

    public void startHRecvThread()
    {
        System.out.print("listening listening listen");
        hReceiver.run();

    }
}
