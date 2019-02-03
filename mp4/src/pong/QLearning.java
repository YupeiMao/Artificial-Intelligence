package pong;

import java.util.Arrays;
import java.util.Random;

public class QLearning {
	private final double learning_constant = 70.0;
	private final double gamma = 0.9;
	private final int TRAIN_EPOCH = 100000;
	private final int TEST_EPOCH = 1000;
	private final static int DOWN=-1;
	private final static int STAY=0;
	private final static int UP=1;
	//array parameters
	private final static int ball_x_states = 12;
	private final static int ball_y_states = 12;
	private final int vel_x_states = 2;
	private final int vel_y_states = 3;
	private final static int paddle_y_states = 12;
	private final int action_numbers = 3;
	
	private final int statesCount=(ball_x_states*ball_y_states*vel_x_states*vel_y_states*paddle_y_states)+1;
    
	private int gameCount;
    private int[] prev_state;
    private int prev_action;
    private int prev_reward=0;
    
    static int[] ACTION= new int[] {DOWN,STAY,UP};
    
    Random random;
    
    
    double[][][][][][] Q;	//Q learning
    private int[][][][][][] N;		//N(s,a)
    
    
    public static void main(String args[]) {
    		QLearning ql = new QLearning();

        ql.init();
    }
    public void init() {
    		//initialize N table and Q table.
    		Q=new double[ball_x_states][ball_y_states][vel_x_states][vel_y_states][paddle_y_states][action_numbers];
    		N=new int[ball_x_states][ball_y_states][vel_x_states][vel_y_states][paddle_y_states][action_numbers];
    		for(int a = 0; a < ball_x_states; a++) {
    			for(int b = 0; b < ball_y_states; b++) {
    				for(int c = 0; c < vel_x_states; c++) {
    					for(int d = 0; d < vel_y_states; d++) {
    						for(int e = 0; e < paddle_y_states; e++) {
    							for(int f = 0; f<action_numbers; f++){
    			    				Q[a][b][c][d][e][f]=0;
    								N[a][b][c][d][e][f]=0;
    							}
    			    		}
    		    		}
    	    		}
        		}
    		}
    		
    		random = new Random();
    		train();
    		test();
    }
    
    
    /*input:none
     * output:none
     * This function loops 1000 times and counts number of bounces each time
     * It adds up total bounces to calculate average bounces per game.
     * 
     * */
    private void test() {
    	int total=0;
    	//start testing
    	for(int gameCount=0; gameCount<TEST_EPOCH; gameCount++){
    		boolean terminalState=false;
    		System.out.println("testing the "+gameCount+" time");
    		Paddle paddle=new Paddle(1);
    		Ball ball=new Ball(1);
    		int bounced=0;
    		
    		//iteration of one game
    		while(!terminalState){
    			int[] state= discrete(ball.getX(),ball.getY(),ball.getVx(),ball.getVy(),paddle.getY());
    			paddle.move(testAction(state));
    			ball.move();
    			
    			//check for boundaries
    			if(ball.getX()>=1){
    				if(ball.getY()>=paddle.getY() && ball.getY()<=(paddle.getY()+0.2)){
    					ball.collison();
    					//add reward
    					bounced+=1;
    				}else{
    					terminalState=true;//terminate the game
    				}
    			}
    			
    		}
    		total+=bounced;
			System.out.println("bounced: "+bounced);
    	}
    	System.out.println("average: "+((double)total)/1000);
	}
    public int testAction(int[] curr_state){
    	//return best action in test mode
    	return ACTION[maxAction(Q[curr_state[0]][curr_state[1]][curr_state[2]][curr_state[3]][curr_state[4]])];
    }
    
    
    
    /*=====================for training==========================*/
    //return action given a state and update reward
	public int getAction(int[] curr_state,int last_reward){
    	if(prev_state!=null){
    		//updating q table
    		double learning_rate = learning_constant / (learning_constant + N[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]);
    		N[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]+= 1;
    		Q[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]+=
    				learning_rate *(prev_reward+gamma*max(Q[curr_state[0]][curr_state[1]][curr_state[2]][curr_state[3]][curr_state[4]])
    				-Q[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]);
    	}
    	//set curr to previous
    	prev_state=Arrays.copyOf(curr_state,5);
    	
    	//exploration
    	if (gameCount>70000)
    		prev_action = maxAction(Q[curr_state[0]][curr_state[1]][curr_state[2]][curr_state[3]][curr_state[4]]);
    	else
    		prev_action = random.nextInt(action_numbers);
    	prev_reward = last_reward;
    	return ACTION[prev_action];
    }
    //discretify continuous state
	public static int[] discrete(double bx, double by, double bvx, double bvy, double py){
		int getPy=(int)Math.floor(py*paddle_y_states/(1-0.2));
    	int[] ret = new int[] {(int)(bx*ball_x_states),(int)(by*ball_y_states),(int)(bvx>0?1:0),(int)(bvy>=0.015?2:(bvy<=-0.015?0:1)),(getPy==paddle_y_states)?11:getPy};
    	return ret;
    }
	
	//main training thread
    public void train(){
    	for(gameCount=0; gameCount<TRAIN_EPOCH; gameCount++){
    		boolean terminalState=false;
    		System.out.println("training the "+gameCount+" time");
    		Paddle paddle=new Paddle(1);
    		Ball ball=new Ball(1);
    		int last_reward=0;
    		//for each game
    		while(!terminalState){
    			int[] state= discrete(ball.getX(),ball.getY(),ball.getVx(),ball.getVy(),paddle.getY());
    			paddle.move(getAction(state,last_reward));
    			ball.move();
    			last_reward=0;
    			//check boundaries and update reward
    			if(ball.getX()>=1){
    				if(ball.getY()>=paddle.getY() && ball.getY()<=(paddle.getY()+0.2)){
    					ball.collison();
    					//add reward
    					last_reward=1;
    					System.out.println("reward: "+last_reward);
    				}else{
    					last_reward=-1;
    					terminalState=true;
    				}
    			}
    			
    		}
    		//finish up reward after termination of game
    		N[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]+= 1;
    		double learning_rate = learning_constant / (learning_constant + N[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]);
    		Q[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]+=
    				learning_rate *(prev_reward+gamma*last_reward
    				-Q[prev_state[0]][prev_state[1]][prev_state[2]][prev_state[3]][prev_state[4]][prev_action]);
    	}
    	
    }
    
    //get max of a array
    private double max(double[] ds) {
    	double max=Double.NEGATIVE_INFINITY;
		for(int i=0;i<3;i++){
			if(ds[i]>max)
				max=ds[i];
		}
		return max;
	}
    
    //get max index of a array
    static int maxAction(double[] ds) {
    	double max=Double.NEGATIVE_INFINITY;
    	int index=-1;
		for(int i=0;i<3;i++){
			if(ds[i]>max){
				max=ds[i];
				index=i;}
		}
		return index;
	}
}
