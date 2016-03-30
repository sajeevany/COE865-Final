/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project865;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttributeTrio implements Serializable{
    
    private String uniqueID = null;
    //private ArrayList<String> resourceList = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> hostResourceMap = new HashMap<String,  ArrayList<String>>();
    private ArrayList<String> myDirectlyConnectedNeighbourUniqueIDs = new ArrayList<String>();

    /*
    * For use by the Hello Packet and Routing Table entry
    *
    *
    *   @param myUniqueID - the uniqueID of the host that is sending the data
    *   @param uniqueIDResourceMap - Map of known resource distribution. ie R1 has "myFiles, test.txt", R2 has "guessMe.txt, finalExams.pdf"
    *   @param myDirectlyConnectedNeighbourUniqueIDs - List of UniqueIDs of the 
               resources to which the packet sender is directly connected to. 
               Should include the destination host. This will allow Shortest Path
               Table to be more easily calculated
    *    
    */
    public AttributeTrio(String uniqueID, HashMap<String, ArrayList<String>> hostResourceMap, ArrayList<String> myDirectlyConnectedNeighbourUniqueIDs)           
    {
        this.uniqueID = uniqueID;
        this.hostResourceMap = hostResourceMap;
        this.myDirectlyConnectedNeighbourUniqueIDs = myDirectlyConnectedNeighbourUniqueIDs;
    }
    
    public String getUniqueID() {
        return uniqueID;
    }

    public HashMap<String, ArrayList<String>> getHostResourceMap() {
        return hostResourceMap;
    }

    public ArrayList<String> getDirectlyConnectedNeighbourUniqueIDs() {
        return myDirectlyConnectedNeighbourUniqueIDs;
    }
    
    public String toString()
    {
        StringBuilder sB = new StringBuilder();
        

        sB.append("\nUniqueID: ");
        sB.append(this.uniqueID);
        for (Map.Entry<String, ArrayList<String>> s : this.hostResourceMap.entrySet())
        {
            sB.append("\nhost: " + s.getKey() + " resource: ");
            for (String str : s.getValue())
            {
                sB.append(str + ",");
            }
             sB.deleteCharAt(sB.length() - 1); //delete last ','
        }
       
        sB.append("\nDirectly Connected Neighbours: \n");
        
        if (this.myDirectlyConnectedNeighbourUniqueIDs.isEmpty())
        {
        	sB.append("UniqueIDs of DC neighbours are unknown\n");
        }
        else
        {
        	for (String s : this.myDirectlyConnectedNeighbourUniqueIDs)
            {
                sB.append(s + ",");
            }
        	sB.deleteCharAt(sB.length() - 1); //delete last ','
            sB.append("\n");
        }
        
		
        return sB.toString();       
    }
    
}
