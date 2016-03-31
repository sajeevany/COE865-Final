package spt;
//test
public class Edge {

	private String rootID = null;
	private String tailID = null;
	private int weight = 1;
	
	public Edge(String rootID, String tailID, int weight) {
		
		if (rootID.equals(tailID))
		{}
		
		this.rootID = rootID;
		this.tailID = tailID;
		this.weight = 1;
	}

	public String getRootID() {
		return rootID;
	}

	public boolean hasNode(String node)
	{
		if (node.equals(rootID) || node.equals(tailID))
			return true;
				
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (rootID == null) {
			if (other.rootID != null)
				return false;
		} else if (!rootID.equals(other.rootID))
			return false;
		if (tailID == null) {
			if (other.tailID != null)
				return false;
		} else if (!tailID.equals(other.tailID))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	public String getTailID() {
		return tailID;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	@Override
	public String toString()
	{
		return "Edge root: " + rootID + " tail: " + tailID;
	}
	

	
	
	
}
