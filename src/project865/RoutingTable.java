package project865;

import java.util.ArrayList;
import java.util.HashMap;

public class RoutingTable {

    private static RoutingTable routingTableInstance = null;
    private ArrayList<RoutingTableEntry> routingEntries = new ArrayList<RoutingTableEntry>();
    
    private RoutingTable(){};
    
    public static RoutingTable getRoutingTableInstance()
    {
        if (routingTableInstance == null)
        {
            RoutingTable.routingTableInstance = new RoutingTable();
        }
        return RoutingTable.routingTableInstance;        
        
    }
    
    public void addRoute(RoutingTableEntry rTEntry)
    {
    	//remove old routes from rTEntry source if exists
    	removeAssociatedRoutes(rTEntry.getUniqueID());
    	
        //add new one
        this.routingEntries.add(rTEntry); 
    }
    
    public ArrayList<RoutingTableEntry> getRoutes()
    {
        return routingEntries;
    }
    
    public void removeAssociatedRoutes(String uniqueID)
    {
    	ArrayList<RoutingTableEntry> entriesToRemove = new ArrayList<RoutingTableEntry>();
    	
        for (RoutingTableEntry rte : routingEntries)
        {
            if (rte.getUniqueID().equals(uniqueID))
            {
            	entriesToRemove.add(rte);
            }
                    
        }
        
        if (!entriesToRemove.isEmpty())
    	{
    		routingEntries.removeAll(entriesToRemove);
    	}
    }
    
    public RoutingTableEntry getRoute(String uniqueID)
    {
        for (RoutingTableEntry rte : routingEntries)
        {
            if (rte.getUniqueID().equals(uniqueID))
            {
                return rte;
            }

        }
        
        return null;
    }
        
    /*
     * Returns list of all possible resources as per the routing tbale
     */
    public ArrayList<String> getNetResourceList()
    {
    	
    	return null;
    }
}
