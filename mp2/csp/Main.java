import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	public static int grid_dim_row,grid_dim_col;
	public static ArrayList<Character> colors;
	public static int assignment_ct=0;
	
	public static void main(String[] args) throws IOException {
		Color[][] grid;
		String readline;
		
		//-------check input-------
		if(args[1]!=null)
			grid_dim_row=Integer.parseInt(args[1]);
		else{
			System.out.println("need a grid dimension");
			return;}
		if(args[2]!=null)
			grid_dim_col=Integer.parseInt(args[2]);
		else{
			System.out.println("need a grid dimension");
			return;}
		
		grid=new Color[grid_dim_row][grid_dim_col];
		colors=new ArrayList<Character>();
		
		//-------read input---------
		BufferedReader read = new BufferedReader(new FileReader(args[0]));
		int row=0;
		while((readline=read.readLine()) != null){
        	char[] ch = readline.toCharArray();
        	for(int col = 0;col < grid_dim_col;col++){
            	if(ch[col]=='_'){//empty
            		grid[row][col] = new Color(ch[col]);
            	}else{//color
            		if(!colors.contains(ch[col])) colors.add(ch[col]);				
            		grid[row][col] = new Color(ch[col],true);
            	}
        	}
        	row++;
        }
		read.close();//finished reading grid

		//-----------the three search method with timer-------------
		long startTime = System.currentTimeMillis();
		
//		grid=btSearch(grid);
//		grid=smartSearch(grid);
		grid=smarterSearch(grid);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime+"ms");//print timer
		
		//------------check and output-------------
		if(grid==null) {
			System.out.println("failed to find solution");
			return;
		}
		printSol(grid);//print grid
	}
	
	
//-------print out the solution----------------
	public static void printSol(Color[][] grid) {
		System.out.println("Assignment number: "+assignment_ct);
		for(int row = 0;row<grid_dim_row;row++){
			for(int x = 0; x < grid_dim_col; x++)
				System.out.print(grid[row][x]);
			System.out.println();
		}
	}
	
	
	//-------------smart search with variable and value ordering, and forward checking-----------
	private static Color[][] smarterSearch(Color[][] grid) {
		//variable heuristic:choose variables neighboring to colored cell
		//value:prioritize source neighbor, then neighbored colors, then other color
		//constraint: use forward checking betterConstraints
		
		//1.------copy the input--------
		Color[][] temp=new Color[grid_dim_row][];
		for(int row = 0; row < grid_dim_row; row++)
		{
		  Color[] aMatrix = grid[row];
		  temp[row] = new Color[grid_dim_col];
		  System.arraycopy(aMatrix, 0, temp[row], 0, grid_dim_col);
		}
		grid=temp;
		
		//2.check for completeness
		if(complete(grid))	return grid;
		
		//3.get next variable
		int m=0,n=0;
		OUTER_LOOP:
		for(int row=0;row<grid_dim_row;row++)
			for(int col=0;col<grid_dim_col;col++)
				if(grid[row][col].isColor('_')&&neighborToColored(grid,row,col)){
					m=row;
					n=col;
					break OUTER_LOOP;
				}
		//4. get the domain of the variable
		Color[][] result;
		ArrayList<Character> domain=domain(grid,m,n);
		
		//5. check for consistency of each value and recursively search with possible assignments
		for(int i=0;i<domain.size();i++){
			grid[m][n]=new Color(domain.get(i));
			result=null;
			if(betterConstraints(grid,m,n)){//check constraints
				assignment_ct++;
				result=smarterSearch(grid);//recursion
				if(result!=null)	return result;
			}
		}
		return null;
	}
	
	
	//-----------constraint with forward checking-------------
	private static boolean betterConstraints(Color[][] grid,int m,int n) {
		ArrayList<Color> neighbor=new ArrayList<Color>();
		boolean flag=true;
		for(int row=m-1;row<=m+1;row++)
			for(int col=n-1;(col<=n+1);col++) {
				if(row<0||col<0||row>=grid_dim_row||col>=grid_dim_col)	continue;
				if((Math.abs(m-row)==1)&&(Math.abs(n-col)==1))	continue;
				neighbor.clear();
				if(grid[row][col].isColor('_'))	continue;
				if(row!=0)	neighbor.add(grid[row-1][col]);
				if(row!=(grid_dim_row-1))	neighbor.add(grid[row+1][col]);
				if(col!=0)	neighbor.add(grid[row][col-1]);
				if(col!=(grid_dim_col-1))	neighbor.add(grid[row][col+1]);
				int colored=0,same=0;
				for(int k=0;k<neighbor.size();k++) {
					if(colors.contains(neighbor.get(k).getColor()))	colored++;
					if(neighbor.get(k).equals(grid[row][col]))	same++;
				}
				if(colored==neighbor.size()) {
					if(grid[row][col].isSource())	flag=(same==1);
					else flag=(same==2);
				}else {
					if(grid[row][col].isSource())	flag=(same<=1);
					else flag=(same<=2);
				}
				
				if(!flag)	return flag;
			}
		return flag;
	}
	
	
	//---------search with variable and value ordering------------
	private static Color[][] smartSearch(Color[][] grid) {
		//variable heuristic:choose variables neighboring to colored cell
		//value:prioritize source neighbor, then neighbored colors, then other color
		//constraint: check all cells for consistency
		Color[][] temp=new Color[grid_dim_row][];
		for(int i = 0; i < grid_dim_row; i++)
		{
		  Color[] aMatrix = grid[i];
		  temp[i] = new Color[grid_dim_col];
		  System.arraycopy(aMatrix, 0, temp[i], 0, grid_dim_col);
		}
		grid=temp;
		if(complete(grid))	return grid;
		int m=0,n=0;
		OUTER_LOOP:
		for(int row=0;row<grid_dim_row;row++)
			for(int col=0;col<grid_dim_col;col++)
				if(grid[row][col].isColor('_')&&neighborToColored(grid,row,col)){
					m=row;
					n=col;
					break OUTER_LOOP;
				}

		Color[][] result;
		ArrayList<Character> domain=domain(grid,m,n);
		for(int i=0;i<domain.size();i++){
			grid[m][n]=new Color(domain.get(i));
			result=null;
			if(constraints(grid)){
				assignment_ct++;
				result=smartSearch(grid);
				if(result!=null)	return result;
			}
		}
		return null;
	}
	
	
	//-------return the available color for a cell-------
	private static ArrayList<Character> domain(Color[][] grid, int row, int col) {
		ArrayList<Color> neighbor=new ArrayList<Color>();
		ArrayList<Character> result=new ArrayList<Character>();
		if(row!=0)	neighbor.add(grid[row-1][col]);
		if(row!=(grid_dim_row-1))	neighbor.add(grid[row+1][col]);
		if(col!=0)	neighbor.add(grid[row][col-1]);
		if(col!=(grid_dim_col-1))	neighbor.add(grid[row][col+1]);
		for(int n=0;n<neighbor.size();n++) 
			if(neighbor.get(n).isSource()){	
				result.add(neighbor.get(n).getColor());
				neighbor.remove(n);}
		for(int n=0;n<neighbor.size();n++) {
			if(!neighbor.get(n).isColor('_'))	result.add(neighbor.get(n).getColor());}
		for(int n=0;n<colors.size();n++){
			if(!result.contains(colors.get(n)))	result.add(colors.get(n));
		}
		return result;
	}
	
	
	//-------------check if a cell has a colored neighbor------------
	private static boolean neighborToColored(Color[][] grid, int row, int col) {
		if((row!=0)&&!grid[row-1][col].isColor('_'))	return true;
		if((row!=(grid_dim_row-1))&&!grid[row+1][col].isColor('_'))	return true;
		if((col!=0)&&!grid[row][col-1].isColor('_'))	return true;
		if((col!=(grid_dim_col-1))&&!grid[row][col+1].isColor('_'))	return true;
		return false;
	}
	
	
	//------the random backtracking search---------------
	public static Color[][] btSearch(Color[][] grid){
		Color[][] temp=new Color[grid_dim_row][];
		for(int i = 0; i < grid_dim_row; i++)
		{
		  Color[] aMatrix = grid[i];
		  temp[i] = new Color[grid_dim_col];
		  System.arraycopy(aMatrix, 0, temp[i], 0, grid_dim_col);
		}
		grid=temp;
		if(complete(grid))	return grid;
		int row=0,n=0;
		//randomize
		do{
			row = ThreadLocalRandom.current().nextInt(0, grid_dim_row);
			n = ThreadLocalRandom.current().nextInt(0, grid_dim_col);
		}while(!grid[row][n].isColor('_'));
		
		Color[][] result;
		for(int i=0;i<colors.size();i++){
			grid[row][n]=new Color(colors.get(i));
			result=null;
			if(constraints(grid)){
				assignment_ct++;
				result=btSearch(grid);
				if(result!=null)	return result;
			}
		}
		
		return null;
	}
	
	
	
	//------------checks if a grid is complete-----------
	public static boolean complete(Color[][] grid){
		for(int row=0;row<grid_dim_row;row++)
			for(int col=0;col<grid_dim_col;col++){
				if(grid[row][col].isColor('_')) return false;
			}
		return true;
	}
	
	
	//-------------checks constraint for all cells----------
	//if cells around it all have color, two must be same as this one or one if its source
	//check the 4 cells around it to see if more than 2 colors of the same color
	public static boolean constraints(Color[][] grid){
		ArrayList<Color> neighbor=new ArrayList<Color>();
		boolean flag=true;
		for(int row=0;row<grid_dim_row;row++)
			for(int col=0;col<grid_dim_col;col++) {
				neighbor.clear();
				if(grid[row][col].isColor('_'))	continue;
				//add all neighbors
				if(row!=0)	neighbor.add(grid[row-1][col]);
				if(row!=(grid_dim_row-1))	neighbor.add(grid[row+1][col]);
				if(col!=0)	neighbor.add(grid[row][col-1]);
				if(col!=(grid_dim_col-1))	neighbor.add(grid[row][col+1]);
				int colored=0,same=0;
				//count colored neighbors, and neighbors with the same colors as current one
				for(int n=0;n<neighbor.size();n++) {
					if(colors.contains(neighbor.get(n).getColor()))	colored++;
					if(neighbor.get(n).equals(grid[row][col]))	same++;
				}
				if(colored==neighbor.size()) {
					if(grid[row][col].isSource())	flag=(same==1);
					else flag=(same==2);
				}else {
					if(grid[row][col].isSource())	flag=(same<=1);
					else flag=(same<=2);
				}
				if(!flag)	return flag;//cut off if found inconsistent cell
			}
		return flag;
		
	}
}


