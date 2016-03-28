package project865;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	// uniqueID, resource
	private Map<String, ArrayList<String>> uniqueIDResourceMap = new HashMap<String,  ArrayList<String>>();
	private String myAddress;
	private int mySocket;

	public static final int sendInterval = 10;
	public static final int timeout = 3 * sendInterval;

	public HelloPacket(String uniqueID,HashMap<String, ArrayList<String>> uniqueIDResourceMap, String myAddress, int mySocket)
	{	
		this.myUniqueID = uniqueID;
		this.uniqueIDResourceMap = uniqueIDResourceMap;
		this.myAddress = myAddress;
		this.mySocket = mySocket;
	}
	
	public String toString()
	{
		StringBuilder sB = new StringBuilder("myUniqueID: " + myUniqueID + "\n");
		
		sB.append("InetAddr" + ": "  + myAddress + "\n");
		sB.append("DatagramSockets" + ": "  + mySocket + "\n");
				
		sB.append("\nResource List\n");
		
		for(Map.Entry<String, ArrayList<String>> entrySet : uniqueIDResourceMap.entrySet())
		{
                    for(String resource : entrySet.getValue())
                    {
                        sB.append("key: " + entrySet.getKey() + " value: " + resource + "\n");
                    }
		}
		
		return sB.toString();
	}
}

