package project865;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	// uniqueID, resource
	private Map<String, String> uniqueIDResourceMap = new HashMap<String, String>();
	private String myAddress;
	private int mySocket;

	public static final int sendInterval = 10;
	public static final int timeout = 3 * sendInterval;

	public HelloPacket(String uniqueID,Map<String, String> uniqueIDResourceMap, InetAddress myAddress, DatagramSocket mySocket)
	{	
		this.myUniqueID = uniqueID;
		this.uniqueIDResourceMap = uniqueIDResourceMap;
		this.myAddress = myAddress.getHostAddress();
		this.mySocket = mySocket.getPort();
	}
	
	public String toString()
	{
		StringBuilder sB = new StringBuilder("myUniqueID: " + myUniqueID + "\n");
		
		sB.append("InetAddr" + ": "  + myAddress + "\n");
		sB.append("DatagramSockets" + ": "  + mySocket + "\n");
				
		sB.append("\nResource List\n");
		
		for(Map.Entry<String, String> entrySet : uniqueIDResourceMap.entrySet())
		{
			sB.append("key: " + entrySet.getKey() + " value: " + entrySet.getValue() + "\n");
		}
		
		return sB.toString();
	}
}

