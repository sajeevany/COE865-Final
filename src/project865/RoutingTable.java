package project865;

import java.util.ArrayList;

/**
 *
 * @author s4yogesw
 */

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
        if (!routingEntries.contains(rTEntry))
        {
        	System.out.println(rTEntry.toString());
            //drop old one
            
            //add new one
            this.routingEntries.add(rTEntry);           
        }
        else
        {
            //TODO remove this block when completed
            //Debug message
            System.err.println("repeated entry");
        }
    }
    
    public boolean doesEntryExist(RoutingTableEntry rTEntry)
    {
        return routingEntries.contains(rTEntry);
    }
    
    public ArrayList<RoutingTableEntry> getRoutes()
    {
        return routingEntries;
    }
    
    public void removeAssociatedRoutes(String uniqueID)
    {
        for (RoutingTableEntry rte : routingEntries)
        {
            if (rte.getUniqueID().equals(uniqueID))
            {
                routingEntries.remove(rte);
            }
                    
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
    
}
