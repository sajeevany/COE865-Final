package project865;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	// uniqueID, resource
	private Map<String, String> uniqueIDResourceMap = new HashMap<String, String>();
	private InetAddress myAddresses;
	private DatagramSocket mySockets;

	public static final int sendInterval = 10;
	public static final int timeout = 3 * sendInterval;

	public HelloPacket(String uniqueID,Map<String, String> uniqueIDResourceMap, InetAddress myAddresses, DatagramSocket mySockets)
	{
		//if (myAddresses.length != mySockets.length)
		//{
		//	throw new IllegalArgumentException("Number of addresses and sockets do not match");
		//}
		
		this.myUniqueID = uniqueID;
		this.uniqueIDResourceMap = uniqueIDResourceMap;
		this.myAddresses = myAddresses;
		this.mySockets = mySockets;
	}
	
	public String toString()
	{
		StringBuilder sB = new StringBuilder("myUniqueID: " + myUniqueID + "\n");
		
		//for (int i = 0; i < myAddresses.length; i++)
		//{
			sB.append("InetAddr" + ": "  + myAddresses.getHostAddress() + "\n");
			sB.append("DatagramSockets" + ": "  + mySockets.getLocalPort() + "\n");
		//}
				
		sB.append("\nResource List\n");
		
		for(Map.Entry<String, String> entrySet : uniqueIDResourceMap.entrySet())
		{
			sB.append("key: " + entrySet.getKey() + " value: " + entrySet.getValue() + "\n");
		}
		
		return sB.toString();
	}
}

