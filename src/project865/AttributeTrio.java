/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project865;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class AttributeTrio implements Serializable{
    
    private String uniqueID = null;
    private ArrayList<String> uniqueIDResourceMap = new ArrayList<String>();
    private ArrayList<String> myDirectlyConnectedNeighbourUniqueIDs = null;

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
    public AttributeTrio(String uniqueID, ArrayList<String> resourceList, ArrayList<String> myDirectlyConnectedNeighbourUniqueIDs)           
    {
        this.uniqueID = uniqueID;
        this.uniqueIDResourceMap = resourceList;
        this.myDirectlyConnectedNeighbourUniqueIDs = myDirectlyConnectedNeighbourUniqueIDs;
    }
    
    public String getUniqueID() {
        return uniqueID;
    }

    public ArrayList<String> getResourceList() {
        return uniqueIDResourceMap;
    }

    public ArrayList<String> getDirectlyConnectedNeighbourUniqueIDs() {
        return myDirectlyConnectedNeighbourUniqueIDs;
    }
    
    

    
}
