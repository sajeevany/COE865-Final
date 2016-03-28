/*
 * Gumi is love.
 * Gumi is life.
 */
package project865;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Krishna Deoram
 */
public class RoutingTableEntry {

	private String nextHop;
	private int nextHopSocket;
	private String uniqueID;
	private HashMap<String, ArrayList<String>> neighbourAndResource = new HashMap<String, ArrayList<String>>();

	public RoutingTableEntry(String nextHop, int nextHopSocket, String uniqueID, HashMap<String, String> neighbourResourceMap) {
		this.nextHop = nextHop;
		this.nextHopSocket = nextHopSocket;
		this.uniqueID = uniqueID;
	}

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	public int getNextHopSocket() {
		return nextHopSocket;
	}

	public void setNextHopSocket(int nextHopSocket) {
		this.nextHopSocket = nextHopSocket;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public HashMap<String, ArrayList<String>> getNeighbourAndResource() {
		return this.neighbourAndResource;
	}
        
        /*
         *   Returns the uniqueID of the client with the associated resource.
         */
	public String hasResource(String resource) { 
            String uniqueID = "NaN";

            for (Map.Entry<String, ArrayList<String>> nR : this.getNeighbourAndResource().entrySet())
            {
                if (nR.getValue().contains(resource))
                {
                    return nR.getKey();
                }
            }

            return uniqueID;
	}

	@Override
	public String toString() {
		return "RoutingTableEntry{" + "nextHop=" + nextHop + ", nextHopSocket=" + nextHopSocket + ", uniqueID="
				+ uniqueID + ", neighbourAndResource=" + neighbourAndResource.toString() + '}';
	}
}
