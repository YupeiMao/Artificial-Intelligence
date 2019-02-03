package mp1;

public class Heuristic{
	
	public int getHeuristicCost(Cell goal, Cell current){
		return Math.abs(goal.getY() - current.getY()) + 
				Math.abs(goal.getX() - current.getX());
	}
}
