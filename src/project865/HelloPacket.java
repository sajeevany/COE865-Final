package project865;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	// uniqueID, resource
	private HashMap<String, ArrayList<String>> uniqueIDResourceMap = new HashMap<String,  ArrayList<String>>();
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
		StringBuilder sB = new StringBuilder("myUniqueID: " + getMyUniqueID() + "\n");
		
		sB.append("InetAddr" + ": "  + getMyAddress() + "\n");
		sB.append("DatagramSockets" + ": "  + getMySocket() + "\n");
				
		sB.append("\nResource List\n");
		
		for(Map.Entry<String, ArrayList<String>> entrySet : getUniqueIDResourceMap().entrySet())
		{
                    for(String resource : entrySet.getValue())
                    {
                        sB.append("key: " + entrySet.getKey() + " value: " + resource + "\n");
                    }
		}
		
		return sB.toString();
	}

    /**
     * @return the myUniqueID
     */
    public String getMyUniqueID() {
        return myUniqueID;
    }

    /**
     * @param myUniqueID the myUniqueID to set
     */
    public void setMyUniqueID(String myUniqueID) {
        this.myUniqueID = myUniqueID;
    }

    /**
     * @return the uniqueIDResourceMap
     */
    public HashMap<String, ArrayList<String>> getUniqueIDResourceMap() {
        return uniqueIDResourceMap;
    }

    /**
     * @return the myAddress
     */
    public String getMyAddress() {
        return myAddress;
    }

    /**
     * @return the mySocket
     */
    public int getMySocket() {
        return mySocket;
    }
}

