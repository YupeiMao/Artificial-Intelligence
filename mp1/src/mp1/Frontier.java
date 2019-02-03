package mp1;


public interface Frontier {

/**
 * Represents the frontier set in the GraphSearch algorithm.
 * Implementing classes are basically adapters for some backing collection.
 */

	/**
	 * @return true if the frontier is empty, false otherwise
	 */
	public boolean isEmpty();
	
	/**
	 * Add the given node to the frontier, if it isn't in it already
	 * @param node The node to add
	 * @return true If the frontier didn't already contain the given node
	 */
	public boolean add(SearchNode node);
	
	/**
	 * Get the next node from the frontier and remove it
	 * @return The next node to examine
	 */
	public SearchNode getNext();
	
	public SearchNode getDuplicate(SearchNode node);
	public void removeNode(SearchNode node);
	public void reset();

}
