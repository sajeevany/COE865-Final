package project865;
import java.io.Serializable;
import java.util.ArrayList;

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
		StringBuilder sB = new StringBuilder("myUniqueID: " + myUniqueID + "\n");
		
		sB.append("InetAddr" + ": "  + myAddress + "\n");
		sB.append("DatagramSockets" + ": "  + myFTSocket + "\n");
				
		sB.append("\nResource List:\n");
		
		for(AttributeTrio aTrio: attributesList)
		{
                    sB.append(aTrio.getUniqueID() + " ");
                    for (String s : aTrio.getResourceList())
                    {
                        sB.append(s + ",");
                    }
                    sB.deleteCharAt(sB.length() - 1); //delete last ,
                    sB.append("\nDirectly Connected Neighbours:\n");
                    for (String s : aTrio.getDirectlyConnectedNeighbourUniqueIDs())
                    {
                        sB.append(s + ",");
                    }
		}
		
		return sB.toString();
	}
}

