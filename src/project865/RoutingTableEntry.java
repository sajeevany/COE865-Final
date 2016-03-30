/*
 * Gumi is love.
 * Gumi is life.
 */
package project865;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RoutingTableEntry {

    private String nextHopIP;
    private int nextHopSocket;
    private String uniqueID;
    private ArrayList<AttributeTrio> attributesList = new ArrayList<AttributeTrio>();

    public RoutingTableEntry(String nextHop, int nextHopSocket, String uniqueID, ArrayList<AttributeTrio> nRMap) {
            this.nextHopIP = nextHop;
            this.nextHopSocket = nextHopSocket;
            this.uniqueID = uniqueID;
            
            // Remove any duplicates            
            /*Set<AttributeTrio> attributeSet = new LinkedHashSet<AttributeTrio>(nRMap);
            this.attributesList.addAll(attributeSet);*/
            this.attributesList = removeDuplicates(nRMap);
            System.out.println("Created Routing Table entry nextHop: " + this.nextHopIP + " nH socket: " + this.nextHopSocket + " UID :"+this.uniqueID );
    }

    //@author Riccardo Cossu
    public static ArrayList<AttributeTrio> removeDuplicates(ArrayList<AttributeTrio> tr) {
        // ... the list is already populated
        Set<AttributeTrio> s = new TreeSet<AttributeTrio>(new Comparator<AttributeTrio>() {

            @Override
            public int compare(AttributeTrio o1, AttributeTrio o2) {
                
            	if (o1.getUniqueID().equals(o2.getUniqueID()))
            		return 0;
            	else
            		return 1;
            }
        });
        s.addAll(tr);
        ArrayList<AttributeTrio> atr = new ArrayList<AttributeTrio>();
        atr.addAll(s);
        //List<AttributeTrio> res = Arrays.asList(s.toArray());
        return atr;
    }
    
    public String getNextHop() {
            return nextHopIP;
    }

    public void setNextHop(String nextHop) {
            this.nextHopIP = nextHop;
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
            for (Map.Entry<String, ArrayList<String>> s : trioA.getHostResourceMap().entrySet())
            {
               if (s.getValue().contains(resource))
               {
                   return s.getKey();
               }
            }
        }

        return uniqueID;
    }

    @Override
    public String toString() {
        return "RoutingTableEntry{" + "nextHop=" + nextHopIP + ", nextHopSocket=" + nextHopSocket + ", uniqueID="
                        + uniqueID + ", neighbourAndResource=" + this.getAttributesList().toString() + '}';
    }
}
