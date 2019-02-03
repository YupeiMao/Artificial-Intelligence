package mp1;

public class State {
	Cell theCell;
	int goalState;
	public State(Cell cell,int state){
		theCell=cell;
		goalState=state;
	}
	public State(Cell cell){
		theCell=cell;
	}
	public Cell getCell(){
		return theCell;
	}
	public void setCell(Cell cell){
		theCell=cell;
	}
	public int getState(){
		return goalState;
	}
	public void setState(int state){
		goalState=state;
	}
	@Override
	public boolean equals(Object obj){
		return theCell.equals(((State)obj).theCell) && this.goalState==((State)obj).goalState;
	}
}
