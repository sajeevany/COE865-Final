package project865;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class QueryPacket implements Serializable {

	private String myUniqueID = null;
	private String myQueryID = null;
	// if false then results being returned 
	private Boolean direction = false;
	private String results = null;


	public QueryPacket(String uniqueID, String queryID)
	{	
		this.myUniqueID = uniqueID;
		this.myQueryID = queryID;
		this.direction = false;
		this.setResults(null);
		
	}
	
    @Override
	public String toString()
	{
		StringBuilder sB = new StringBuilder("myUniqueID: " + getMyUniqueID() + "\n");
		sB.append("QueryID" + ": "  + getMyQueryID() + "\n");
				
		return sB.toString();
	}

	/**
     * @return the myQueryID
     */
    private String getMyQueryID() {
		
		return myQueryID;
	}

	/**
     * @return the myUniqueID
     */
    public String getMyUniqueID() {
        return myUniqueID;
    }

	private Boolean getDirection() {
		return direction;
	}

	private void setDirection(Boolean newValue ) {
		this.direction = newValue;
	}

	private String getResults() {
		return results;
	}

	private void setResults(String results) {
		this.results = results;
	}

}

