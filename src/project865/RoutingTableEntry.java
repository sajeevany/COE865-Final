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
    private ArrayList<AttributeTrio> attributesList = new ArrayList<AttributeTrio>();

    public RoutingTableEntry(String nextHop, int nextHopSocket, String uniqueID, ArrayList<AttributeTrio> nRMap) {
            this.nextHop = nextHop;
            this.nextHopSocket = nextHopSocket;
            this.uniqueID = uniqueID;
            this.attributesList = nRMap;
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

    public ArrayList<AttributeTrio> getAttributesList() {
        return attributesList;
    }

    /*
     *   Returns the uniqueID of the client with the associated resource.
     */
    public String getResourceOwner(String resource) { 
        String uniqueID = "NaN";

        for (AttributeTrio trioA : this.getAttributesList())
        {
            if (trioA.getResourceList().contains(resource))
            {
                return trioA.getUniqueID();
            }
        }

        return uniqueID;
    }

    @Override
    public String toString() {
        return "RoutingTableEntry{" + "nextHop=" + nextHop + ", nextHopSocket=" + nextHopSocket + ", uniqueID="
                        + uniqueID + ", neighbourAndResource=" + this.getAttributesList().toString() + '}';
    }
}
