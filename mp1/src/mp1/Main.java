package mp1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.TreeNode;

public class Main {
	static Cell [][] maze;
	static Cell [][] printMaze;
	static int maze_width=0;
	static int maze_height=1;
	public static SearchNode root;
	public static ArrayList<Cell> goals;
	public static TreeNode[] solution;
	public static Map<Integer, Integer> cellDistancesMap;
	public static void main(String[] args) throws IOException{
		
		readInput(args);
		int temp=0;
		cellDistancesMap = new HashMap<Integer, Integer>();
		for (int i=0; i<goals.size();i++){
			goals.get(i).goalIndex=i;
			temp+=(1<<i);
		}
		root.setGoalState(temp);
//        part1();
        
        part2();
        
//        partEC();
		
	}
	public static void partEC(){

		System.out.println("extra credit:");
		final Search bfs=new Search(root,new bfsFrontier());
		solution=bfs.findSolEC();
		updateSolution(solution);
		printMaze();
		
	}
	public static void part2(){

		/*the bfs test, only works on tinysearch*/
//		System.out.println("bfs:");
//		final Search bfs=new Search(root,new bfsFrontier());
//		solution=bfs.findSol2();
//		updateSolution(solution);
//		printMaze();		
		
		calAllAppleToApple();
	}
	
	public static void part1(){
		System.out.println("bfs:");
		final Search bfs=new Search(root,new bfsFrontier());
		solution=bfs.findSol1();
		printSolution(solution);
		
		System.out.println("dfs:");
		final Search dfs=new Search(root,new dfsFrontier());
		solution=dfs.findSol1();
		printSolution(solution);
		
		System.out.println("aStar:");
		final Search aStar=new Search(root,new aStarFrontier());
		solution=aStar.findSol1();
		printSolution(solution);
		
		System.out.println("greedy:");
		final Search greedy=new Search(root,new greedyFrontier());
		solution=greedy.findSol1();
		printSolution(solution);
	}
	
	public static void printSolution(TreeNode[] solution){
		printMaze = new Cell[maze_width][maze_height];
	    for (int r = 0; r < maze.length; r++) {
	        printMaze[r] = maze[r].clone();
	    }
	    boolean isFirst = true;
	    for(TreeNode t: solution){
	    	if(isFirst){
	    		isFirst = false;
	    		continue;
	    	}
	    	int x=((SearchNode)t).getState().getCell().getX();
	    	int y=((SearchNode)t).getState().getCell().getY();
	    	printMaze[x][y]=new Cell(x,y,1);
	      //System.out.println(((SearchNode)t).getCell().toString());
	    }
	    printMaze();
	}
	
	
	public static void updateSolution(TreeNode[] solution){
		printMaze = new Cell[maze_width][maze_height];
	    for (int r = 0; r < maze.length; r++) {
	        printMaze[r] = maze[r].clone();
	    }
	    boolean isFirst = true;
	    for(TreeNode t: solution){
	    	if(isFirst){
	    		isFirst = false;
	    		continue;
	    	}
	    	int x=((SearchNode)t).getState().getCell().getX();
	    	int y=((SearchNode)t).getState().getCell().getY();
	    	if(maze[x][y].content>0)	printMaze[x][y]=new Cell(x,y,1);
	      //System.out.println(((SearchNode)t).getCell().toString());
	    }
	    
	    //printMaze();
	}
	
//	public static void printSolution2(TreeNode[] solution){
//		printMaze = new Cell[maze_width][maze_height];
//	    for (int r = 0; r < maze.length; r++) {
//	        printMaze[r] = maze[r].clone();
//	    }
//	    boolean isFirst = true;
//	    for(TreeNode t: solution){
//	    	if(isFirst){
//	    		isFirst = false;
//	    		continue;
//	    	}
//	    	int x=((SearchNode)t).getCell().getX();
//	    	int y=((SearchNode)t).getCell().getY();
//	    	printMaze[x][y]=new Cell(x,y,1);
//	      //System.out.println(((SearchNode)t).getCell().toString());
//	    }
//	    
//	    printMaze();
//	}
//	
	public static void printMaze(){
		for(int y = 0;y<maze_height;y++){
			for(int x = 0; x < maze_width; x++){
				int curState=printMaze[x][y].content;
				//0=start,1=goal, 2=none or 3=wall
				
				switch(curState){
				  case 0:System.out.print('P');break;
				  case 1:System.out.print('.');break;
				  case 2:System.out.print(' ');break;
				  case 3:System.out.print('%');break;
				  default: if(curState>-10)System.out.print(Math.abs(curState));
				           else if(curState>-36)System.out.print( (char)((int)'a'+Math.abs(curState+10)));
				           else System.out.print( (char)((int)'A'+Math.abs(curState+36)));
				}
			}
			System.out.println();
		}
	}

	public static void readInput(String[] args) throws IOException{
//		args0:maze file 
		int num=0,content;
		String readline;
		
		BufferedReader read = new BufferedReader(new FileReader(args[0]));
		readline=read.readLine();
		maze_width= readline.length();
		while(read.readLine()!=null){maze_height+=1;}
		read.close();
		maze = new Cell[maze_width][maze_height];
		System.out.println("x:"+maze_width+",y:"+maze_height);
		goals=new ArrayList<Cell>();
		read = new BufferedReader(new FileReader(args[0]));
		while((readline=read.readLine()) != null){
            char[] ch = readline.toCharArray();
            for(int i = 0;i < maze_width;i++){
            	if(ch[i]=='P')	content=0;		//start
            	else if(ch[i]=='.')	content=1;	//goal
            	else if(ch[i]=='%') content=3;	//wall
            	else	content=2;				//empty
                maze[i][num] = new Cell(i,num,content);
                if(content==0)	root=new SearchNode(maze[i][num],0);
                if(content==1)	goals.add(maze[i][num]);
            }
            num++;
        }
		read.close();//finished reading maze
	}
	public static void calAllAppleToApple(){
		int count=0;
		for(int i = 0; i < goals.size()-1; i++){
			for(int j = i+1; j < goals.size(); j++){
				final Search bfs=new Search(root,new bfsFrontier());
				int key = getCellsKey(goals.get(i),goals.get(j));
				System.out.println("key:" + key);
				solution=bfs.findAppleToApple(goals.get(i), goals.get(j));
				cellDistancesMap.put(key, solution.length);
				System.out.println("length:"+ solution.length);
				count++;
			}
		}
		System.out.println(count+","+goals.size());
	}
	
	public static int getCellsKey(Cell cell0, Cell cell1){
		int cell0_val = cell0.y_pos * maze_width + cell0.x_pos; 
		int cell1_val = cell1.y_pos * maze_width + cell1.x_pos; 
		if(cell0_val>cell1_val){
		  cell0_val = cell0_val ^ cell1_val;
		  cell1_val = cell0_val ^ cell1_val;
		  cell0_val = cell0_val ^ cell1_val;
		}
		return cell0_val * 10000 + cell1_val;
	}

}
