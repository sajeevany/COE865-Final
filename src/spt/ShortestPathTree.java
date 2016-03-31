package spt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ShortestPathTree {

	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private String rootNode;
	

	/*
	 * @param sourceNeighMap: source neighbour map extracted from routing table
	 * @param rootNode: root node of graph. This should be the local node's uniqueID.
	 */
	public ShortestPathTree(HashMap<String, ArrayList<String>> sourceNeighMap, String rootNode)
	{
		edges = extractAlignedEdges(sourceNeighMap);
		edges = removeDuplicates(edges);
		
		for (Edge ed : edges)
		{
			System.out.println(ed);
		}
	}
	
	private ArrayList<Edge> extractAlignedEdges(HashMap<String, ArrayList<String>> sourceNeighMap) {
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		for (Map.Entry<String, ArrayList<String>> rMap : sourceNeighMap.entrySet())
		{
			for (String tail : rMap.getValue())
			{
				int strComparisson = rMap.getKey().compareTo(tail);
				
				//create edge with "lower" valued node at root. This will allow us to easily filter repeats
				if (strComparisson < 0)
					edgeList.add(new Edge(rMap.getKey(), tail,1));
				else if (strComparisson == 0)
				{
					//TODO remove debug message
					System.err.println("Useless edge dropped, root" +  rMap.getKey() + " tail "+  tail + " weight "+1);
				}
				else
					edgeList.add(new Edge(tail, rMap.getKey(),1));
			}
		}
		
		return edgeList;
	}
	
	private ArrayList<Edge> removeDuplicates(ArrayList<Edge> edgeList) {
		
		ArrayList<Edge> fEdgeList = new ArrayList<Edge>();
		for (Edge ed : edgeList)
		{
			if (!fEdgeList.contains(ed))
			{
				fEdgeList.add(ed);
			}
		}
		
		return fEdgeList;
	}
	
	public void buildGraph()
	{
		System.out.println("Graph is being build around node " +  this.rootNode);
		
		ArrayList<String> path;
	}
	
	private String getNodeIDWithMinDistance(String node, ArrayList<Edge> unsettledEdges)
	{
		
		
		return null;
	}
	
	private ArrayList<String> getDCNeighbours(String node, ArrayList<Edge> edges)
	{
		ArrayList<String> myNeighbours = new ArrayList<String>();
		
		for(Edge edge : edges)
		{
			if (edge.getRootID().equals(node))
				myNeighbours.add(edge.getTailID());
			else if (edge.getTailID().equals(node))
				myNeighbours.add(edge.getRootID());
		}
		
		return myNeighbours;
	}
	

	
	public static void main(String[] args)
	{
		
		HashMap<String, ArrayList<String>> sourceDCNeiMap = new HashMap<String, ArrayList<String>>();
		sourceDCNeiMap.put("R100", new ArrayList<String>(){{add("R2"); add("R3"); add("R4"); add("R5"); add("R10");}});
		sourceDCNeiMap.put("R3", new ArrayList<String>(){{add("R100"); add("R3");}});
		sourceDCNeiMap.put("R101", new ArrayList<String>(){{add("R9"); add("R8"); add("R7"); add("R6");}});
		sourceDCNeiMap.put("R102", new ArrayList<String>(){{add("R11"); add("R10");}});
		sourceDCNeiMap.put("R10", new ArrayList<String>(){{add("R100"); add("R102");}});
		
		ShortestPathTree spt = new ShortestPathTree(sourceDCNeiMap, "R3");
		
	}
}

class PathToNode
{
	public String from;
	public String to;
	public LinkedList<String> path;
	
	public PathToNode(String from, String to, LinkedList<String> path)
	{
		this.from = from;
		this.to = to;
		this.path = path;
	}
}
