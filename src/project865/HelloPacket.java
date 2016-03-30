package project865;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class HelloPacket implements Serializable {

	private String myUniqueID = null;
	private String myAddress;
	private int myFTSocket;
    private ArrayList<AttributeTrio> attributesList;

	public static final int sendInterval = 10;
	public static final int timeout = 3 * sendInterval;

   /* Create Hello Packet
    *
    * @uniqueID - UniqueID of the sender
    * @attributeTrio - encapsulates sender's resources lists. 
                       {uniqueID, resources[], directly connected machines}
    * @myAddress - IP address of the sender
    * @myFTSocket - The socket to which iQuery messages should be sent to 
                    potentially initialize file transfers
    */
	public HelloPacket(String uniqueID, ArrayList<AttributeTrio> attributesList, String myAddress, int myFTSocket)
	{	
		this.myUniqueID = uniqueID;
		//this.uniqueIDResourceMap = uniqueIDResourceMap;
                this.attributesList = attributesList;
		this.myAddress = myAddress;
		this.myFTSocket = myFTSocket;
	}
	
    @Override
	public String toString()
	{
		StringBuilder sB = new StringBuilder("myUniqueID: " + getMyUniqueID() + "\n");
		
		sB.append("InetAddr" + ": "  + getMyAddress() + "\n");
		sB.append("DatagramSockets" + ": "  + getMyFTSocket() + "\n");
				
		sB.append("\nResource List:\n");
		
		for(AttributeTrio aTrio: getAttributesList())
		{
            sB.append("From: " + aTrio.getUniqueID() + "\n");
            for (Map.Entry<String, ArrayList<String>> s : aTrio.getHostResourceMap().entrySet())
            {
                sB.append("host: " + s.getKey() + " Resources: ");
                for (String str : s.getValue())
                {
                    sB.append(s + ",");
                }
                 sB.deleteCharAt(sB.length() - 1); //delete last
                 sB.append("\n");
            }
            sB.deleteCharAt(sB.length() - 1); //delete last ,
            sB.append("Directly Connected Neighbours: ");
            for (String s : aTrio.getDirectlyConnectedNeighbourUniqueIDs())
            {
                sB.append(s + ",");
            }
            sB.deleteCharAt(sB.length() - 1); //delete last ,
            sB.append("\n");
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
     * @return the myAddress
     */
    public String getMyAddress() {
        return myAddress;
    }

    /**
     * @return the myFTSocket
     */
    public int getMyFTSocket() {
        return myFTSocket;
    }

    /**
     * @return the attributesList
     */
    public ArrayList<AttributeTrio> getAttributesList() {
        return attributesList;
    }
}

