package mp1;

import java.util.*;

public class aStarFrontier implements Frontier{
	private List<SearchNode> frontier;
	
	public aStarFrontier(){
		this.frontier=new ArrayList<SearchNode>();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean add(SearchNode node) {
		return frontier.add(node);
	}

	@Override
	public SearchNode getNext() {
		int minIndex = -1, minCost = Integer.MAX_VALUE;
		for(int i = 0; i < frontier.size(); i++){
			SearchNode current = frontier.get(i);
			int currentCost = current.getCost() + current.getHeuristicCost(Main.goals.get(0), current);
			if(minCost > currentCost){
				minCost = currentCost;
				minIndex = i;
			}
		}
		if(minIndex != -1){
		  return frontier.remove(minIndex);
		}
		return null;
	}

	@Override
	public void removeNode(SearchNode node) {
		frontier.remove(node);
	}
	
	@Override
	public SearchNode getDuplicate(SearchNode node) {
		// TODO Auto-generated method stub
		Iterator<SearchNode> it=frontier.iterator();
		for (int i=0;i<frontier.size();i++){
			SearchNode temp= it.next();
			if(temp.getState().equals(node.getState())){
				return temp;
			}
		}
		return null;
	}
	
	@Override
	public void reset(){
      frontier.clear();
	}


}
