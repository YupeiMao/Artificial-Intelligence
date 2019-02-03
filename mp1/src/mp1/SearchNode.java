package mp1;

import javax.swing.tree.DefaultMutableTreeNode;

public class SearchNode extends DefaultMutableTreeNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private State state;
	private final int cost;

	public SearchNode(Cell pos,int cost){
		state=new State(pos);
		this.cost=cost;
	}
	public SearchNode(Cell pos,int cost,State state){
		
		this.state=new State(pos);
		this.state.setState(state.goalState);
		this.cost=cost;
	}
	public int getCost() {
		return cost;
	}
	
	
	public void expandNode(){
		int x,y;
		x=state.getCell().getX();
		y=state.getCell().getY();
		if(x>0){
			if(!Main.maze[x-1][y].isWall()) this.add(new SearchNode(Main.maze[x-1][y],cost+1,this.getState()));
		}
		if(x<(Main.maze_width-1)){
			if(!Main.maze[x+1][y].isWall()) this.add(new SearchNode(Main.maze[x+1][y],cost+1,this.getState()));
		}
		if(y>0){
			if(!Main.maze[x][y-1].isWall()) this.add(new SearchNode(Main.maze[x][y-1],cost+1,this.getState()));
		}
		if(y<(Main.maze_height-1)){
			if(!Main.maze[x][y+1].isWall()) this.add(new SearchNode(Main.maze[x][y+1],cost+1,this.getState()));
		}
	}
	
	public int getHeuristicCost(Cell goal, SearchNode current){
		return Math.abs(goal.getY() - current.getState().getCell().getY()) + 
				Math.abs(goal.getX() - current.getState().getCell().getX());
	}
	public State getState(){
		return state;
	}
	public void setGoalState(int goalState){
		state.setState(goalState);
	}
	
}
