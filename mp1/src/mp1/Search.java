package mp1;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.TreeNode;

import mp1.Frontier;
import mp1.Cell;
import mp1.SearchNode;

public class Search {
	private SearchNode current,root;
	private ArrayList<State> explored;
	private ArrayList<Cell> exploredCell;
	private final Frontier frontier;
	private int expandedNodes=0;
	public Search(SearchNode root,Frontier frontier){
		this.root=root;
		this.frontier=frontier;
		this.explored=new ArrayList<State>();
		this.exploredCell=new ArrayList<Cell>();
	}
	public TreeNode[] findSol1(){
		//System.out.println("start searching");
		frontier.add(root);
		while(!frontier.isEmpty()){
			current=frontier.getNext();
			if(current.getState().getCell().isGoal()){
//				Main.goals.remove(current.getCell());
//				if(Main.goals.size()==0){
					TreeNode [] solution=current.getPath();
					System.out.println("nodes:"+expandedNodes+" pathcost:"+current.getCost());		
					return solution;
//				}
			}
			explored.add(current.getState());
			current.expandNode();
			expandedNodes++;
			//System.out.println(current.getCell());
			@SuppressWarnings("unchecked")
			Enumeration<SearchNode> e = current.children();
			for(int i=0;i<current.getChildCount();i++){
				SearchNode temp=e.nextElement();
				if(!explored.contains(temp.getState())){
					//System.out.println("duplicated node:"+temp.getState().getCell().toString());
					SearchNode test= frontier.getDuplicate(temp);
					if(test!=null){
						if(test.getCost()>temp.getCost()){
							frontier.removeNode(test);
							frontier.add(temp);
							//System.out.println(temp.getCost());
						}
					}else{
					  frontier.add(temp);
					}
				}
			}
		}
		return null;
	}
	
	
	public TreeNode[] findSol2(){
		//System.out.println("start searching");
		frontier.add(root);
		int goalNum=-1;
		while(!frontier.isEmpty()){
			current=frontier.getNext();
			
			if(current.getState().getCell().isGoal()){
				//Main.goals.remove(current.getCell());
				//current.getCell().setGoal(goalNum--);
//				if(Main.goals.size()==0){
				//mark the goal in goalstate
				if(((current.getState().getState()>>(current.getState().getCell().goalIndex))&1)==1)	current.setGoalState(current.getState().goalState-(1<<current.getState().getCell().goalIndex));
				//add cell with this goalstate
				//frontier.reset();
				//frontier.add(current);
				//System.out.println(current.getCost());
				//System.out.println(Integer.toBinaryString(current.getState().getState()));
				//Main.updateSolution(solution);
				if(current.getState().getState() ==0){
					TreeNode [] solution=current.getPath(); 
					System.out.println(current.getCost());
					System.out.println(expandedNodes);
					return solution;
				}
			}
			
			explored.add(current.getState());
			current.expandNode();
			expandedNodes++;
			//System.out.println(current.getCell());
			@SuppressWarnings("unchecked")
			Enumeration<SearchNode> e = current.children();
			for(int i=0;i<current.getChildCount();i++){
				SearchNode temp=e.nextElement();
				if(!explored.contains(temp.getState())){
					//System.out.println("duplicated node:"+temp.getCell().toString());
					SearchNode test= frontier.getDuplicate(temp);
					if(test!=null){
						if(test.getCost()>temp.getCost()){
							frontier.removeNode(test);
							frontier.add(temp);
							//System.out.println(temp.getCost());
						}
					}else{
					  frontier.add(temp);
					}
				}
			}
		}
		return null;
	}
	public TreeNode[] findAppleToApple(Cell cell0, Cell cell1){
		//System.out.println("start searching");
		exploredCell.clear();
		frontier.reset();
		frontier.add(new SearchNode(cell0, 0));
		while(!frontier.isEmpty()){
			current=frontier.getNext();
		
			if(current.getState().getCell().equals(cell1)){
				TreeNode [] solution=current.getPath();
				//System.out.println("nodes:"+expandedNodes+" pathcost:"+current.getCost());	
				return solution;
			}
			
			exploredCell.add(current.getState().getCell());
			current.expandNode();
			expandedNodes++;
			//System.out.println(current.getCell());
			@SuppressWarnings("unchecked")
			Enumeration<SearchNode> e = current.children();
			for(int i=0;i<current.getChildCount();i++){
				SearchNode temp=e.nextElement();
				if(!exploredCell.contains(temp.getState().getCell())){
					//System.out.println("duplicated node:"+temp.getCell().toString());
					SearchNode test= frontier.getDuplicate(temp);
					if(test!=null){
						if(test.getCost()>temp.getCost()){
							frontier.removeNode(test);
							frontier.add(temp);
							//System.out.println(temp.getCost());
						}
					}else{
					  frontier.add(temp);
					}
				}
			}
		}
		return null;
	}
	public TreeNode[] findSolEC(){
		//System.out.println("start searching");
		frontier.add(root);
		int goalNum=-1;
		while(!frontier.isEmpty()){
			current=frontier.getNext();
			
			if(current.getState().getCell().isGoal()){
				Main.goals.remove(current.getState().getCell());
				current.getState().getCell().setGoal(goalNum--);
				
				//mark the goal in goalstate
//				if(((current.getState().getState()>>(current.getState().getCell().goalIndex))&1)==1)	current.setGoalState(current.getState().goalState-(1<<current.getState().getCell().goalIndex));
				//add cell with this goalstate
				frontier.reset();
				frontier.add(current);
				exploredCell.clear();
//				System.out.println(current.getCost());
				//System.out.println(Integer.toBinaryString(current.getState().getState()));
				
//				if(current.getState().getState() ==0){
				if(Main.goals.size()==0){
					TreeNode [] solution=current.getPath(); 
					System.out.println("nodes:"+expandedNodes+" pathcost:"+current.getCost());	
					return solution;
				}
			}
			
			exploredCell.add(current.getState().getCell());
			current.expandNode();
			expandedNodes++;
			//System.out.println(current.getCell());
			@SuppressWarnings("unchecked")
			Enumeration<SearchNode> e = current.children();
			for(int i=0;i<current.getChildCount();i++){
				SearchNode temp=e.nextElement();
				if(!exploredCell.contains(temp.getState().getCell())){
					//System.out.println("duplicated node:"+temp.getCell().toString());
					SearchNode test= frontier.getDuplicate(temp);
					if(test!=null){
						if(test.getCost()>temp.getCost()){
							frontier.removeNode(test);
							frontier.add(temp);
							//System.out.println(temp.getCost());
						}
					}else{
					  frontier.add(temp);
					}
				}
			}
		}
		return null;
	}
}
