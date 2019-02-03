package mp1;

public class Cell {
	int x_pos;
	int y_pos;
	int content; //0=start,1=goal, 2=space or 3=wall
	//int goalState;
	int goalIndex=-1;
	public Cell(int x,int y,int ct) {
		x_pos=x;
		y_pos=y;
		content=ct;
		// TODO Auto-generated constructor stub
	}

	public boolean isWall(){return content==3;}
	public boolean isGoal(){return content==1;}
	public void deleteGoal(){content=2;}
	public int getX(){return x_pos;}
	public int getY(){return y_pos;}
	public String toString(){
		return "("+x_pos+","+y_pos+")";
	}
	
	public void setGoal(int goal){
		this.content = goal;
	}
	
	@Override
	public boolean equals(Object obj) {
	  return (((Cell) obj).x_pos == this.x_pos &&  ((Cell) obj).y_pos == this.y_pos );
	}
	
}
