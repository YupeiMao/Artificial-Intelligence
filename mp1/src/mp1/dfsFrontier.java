package mp1;

import java.util.Iterator;
import java.util.Stack;

public class dfsFrontier implements Frontier {
	private Stack<SearchNode> frontier;
	public dfsFrontier(){
		this.frontier=new Stack<SearchNode>();
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return frontier.isEmpty();
	}

	@Override
	public boolean add(SearchNode node) {
		// TODO Auto-generated method stub
		frontier.push(node);
		return true;
	}

	@Override
	public SearchNode getNext() {
		// TODO Auto-generated method stub
		return frontier.pop();
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
	public void removeNode(SearchNode node) {
		// TODO Auto-generated method stub
		frontier.remove(node);
	}
	
	@Override
	public void reset(){
      frontier.clear();
	}



}
