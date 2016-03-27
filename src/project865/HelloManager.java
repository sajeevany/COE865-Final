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
import java.util.Map;
import java.util.TimerTask;

public class HelloManager{
	
	private String myUniqueID = null;
	public static final int helloProtocolPort = 10090;
	ArrayList<InetAddress> targetIPs;
	ArrayList<HelloReceiver> hReceiver = new ArrayList<HelloReceiver>();
	ArrayList<HelloSender> hSender = new ArrayList<HelloSender>();
	
	public HelloManager(String uniqueID, ArrayList<InetAddress> targetIPs) throws SocketException
	{
		//initialize default values that should not change
		this.myUniqueID = uniqueID;
		this.targetIPs = targetIPs;
		
		//initialize routing table
		//TODO add routing table
		
		//Attach a receiver to listen to the default hello port
		for(InetAddress t: targetIPs)
		{
			hSender.add(new HelloSender(t));
		}
		//require single hello receiver for listening
		hReceiver.add(new HelloReceiver(helloProtocolPort));
	}
	
	//start hello receiver and sender threads
	public void runManager()
	{
		for (HelloReceiver hRecv: hReceiver)
		{
			hRecv.run();
		}
		
		for (HelloSender hSend: hSender)
		{
			hSend.run();
		}
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

class HelloReceiver implements Runnable
{
	DatagramSocket mySocket = null;
	
	public HelloReceiver(int myListeningPort) throws SocketException {
		this.mySocket = new DatagramSocket(myListeningPort);
	}
	
	//TODO Complete
	@Override
	public void run() {
		
		//Receiver code here
		System.out.println("Hello Receiver started on port " +  mySocket.getLocalPort());
		
	}
}

class HelloSender implements Runnable
{
	InetAddress targetIPAddr;
	DatagramSocket targetSocket = null;
	byte[] buffer = new byte[65508];
	
	public HelloSender(InetAddress myIPAddr) throws SocketException {
		this.targetIPAddr = myIPAddr;
		this.targetSocket = new DatagramSocket();
		
	}
	
	//TODO Complete
	@Override
	public void run() {
		
		TimerTask sendHello = new TimerTask(){

			@Override
			public void run() {
				//sendHelloPacket();
				//targetpacket = new DatagramSocket(buffer, buffer.length, targetIPAddr, HelloManager.helloProtocolPort);
			}
			
		};
		
		//Sender code here
		System.out.println("Hello Sender started on port " +  targetSocket.getLocalPort());
		
	}
}
