package spt;

import java.security.cert.CRLReason;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ShortestPathTree {

	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private HashSet<String> nodes;
	private String rootNode;
	

	/*
	 * @param sourceNeighMap: source neighbour map extracted from routing table
	 * @param rootNode: root node of graph. This should be the local node's uniqueID.
	 */
	public ShortestPathTree(HashMap<String, ArrayList<String>> sourceNeighMap, String rootNode)
	{
		edges = extractAlignedEdges(sourceNeighMap);
		edges = removeDuplicates(edges);
		nodes = getNodes(edges);
		this.rootNode = rootNode;
		
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
	
	private HashSet<String> getNodes(ArrayList<Edge> edges2) {
		
		HashSet<String> nodez = new HashSet<String>();
		
		for (Edge e : edges2)
		{
			nodez.add(e.getRootID());
			nodez.add(e.getTailID());
		}
		
		return nodez;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<PathToNode> buildGraph()
	{
		System.out.println("Graph is being build around node " +  this.rootNode);
		ArrayList<PathToNode> paths = new ArrayList<PathToNode>();
		
		String lastNode = this.rootNode;
		String rootNode = this.rootNode;
		String tailNode = null;
		String currentNode = this.rootNode;
		ArrayList<String> traversedNodes = new ArrayList<String>();
		ArrayList<String> traversableNodes = new ArrayList<String>(nodes);
		LinkedList<String> myCurrentPath = new LinkedList<String>();
		
		traversableNodes.remove(rootNode);
		myCurrentPath.add(rootNode);
		ArrayList<String> localTraversableNeighbours = getDCNeighbours(currentNode, edges);
		ArrayList<String> unTraversableNodes = new ArrayList<String>();
		
		//while all nodes have not been traversed
		while(!traversableNodes.isEmpty())
		{
			if (lastNode.equals(rootNode))
			{
				myCurrentPath.clear();
				myCurrentPath.add(rootNode);
			}
			
			//while there doesn't exist an untraversed neighbour			
			while ((currentNode = getNodeIDWithMinDistance(currentNode, localTraversableNeighbours, edges)) != null)
			{
				//add current to path
				myCurrentPath.add(currentNode);
				//get potential neighbours
				localTraversableNeighbours = getDCNeighbours(currentNode, edges);
				//remove the last node
				localTraversableNeighbours.remove(lastNode);
				lastNode = currentNode;
				
			}
			tailNode = lastNode;
			//path has been found
			paths.add(new PathToNode(rootNode, tailNode, (LinkedList<String>) myCurrentPath.clone()));
			traversableNodes.remove(tailNode);
			
			if (traversableNodes.size() != 0)
			{
				unTraversableNodes.add(myCurrentPath.pollLast());
				currentNode = myCurrentPath.peekLast();
				lastNode = currentNode;
			}
			
			localTraversableNeighbours = getDCNeighbours(currentNode, edges);
			localTraversableNeighbours.removeAll(myCurrentPath);
			localTraversableNeighbours.removeAll(unTraversableNodes);
		}
		
		
		return paths;
	}
	
	private String getNodeIDWithMinDistance(String node, ArrayList<String> candidateNodes, ArrayList<Edge> edges)
	{
		//reached end of path
		if (candidateNodes == null || candidateNodes.isEmpty())
			return null;
		//since all weights are 1, we select first candidate node
		
		return candidateNodes.get(0);
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
		sourceDCNeiMap.put("R1", new ArrayList<String>(){{add("R2"); add("R6");}});
		sourceDCNeiMap.put("R6", new ArrayList<String>(){{}});
		sourceDCNeiMap.put("R2", new ArrayList<String>(){{add("R1"); add("R3");}});
		sourceDCNeiMap.put("R3", new ArrayList<String>(){{add("R2"); add("R4"); add("R5");}});
		sourceDCNeiMap.put("R4", new ArrayList<String>(){{add("R3");}});
		sourceDCNeiMap.put("R5", new ArrayList<String>(){{add("R3");}});

		
		ShortestPathTree spt = new ShortestPathTree(sourceDCNeiMap, "R2");
		ArrayList<PathToNode> pTree = spt.buildGraph();
		System.out.println(pTree);
		
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
	
	@Override
	public String toString()
	{
		StringBuilder sB = new StringBuilder();
		
		sB.append("From: "  + from);
		sB.append(" To: "  + to);
		sB.append(" Path: [");
		for (String i : path)
		{
			sB.append(i + ", ");
		}
		sB.replace(sB.length() -2, sB.length(), "");
		sB.append("]\n");
		
		
		return sB.toString();
	}
}
