package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {
	double velocity_x,velocity_y,ball_x,ball_y;
	int numOfPlayer;
	public Ball(int num){
		velocity_x=0.03;
		velocity_y=0.01;
		ball_x=0.5;
		ball_y=0.5;
		this.numOfPlayer=num;
	}
	public void move(){
		ball_x+=velocity_x;
		ball_y+=velocity_y;
		
		//bounce
		if(ball_y<0){
			ball_y=-ball_y;
			velocity_y=-velocity_y;
		}else if(ball_y>=1){
			ball_y=2-ball_y;
			velocity_y=-velocity_y;
		}
		if(numOfPlayer==1) {
		if(ball_x<0){
			ball_x=-ball_x;
			velocity_x=-velocity_x;
		}
//		else if(ball_x>1){
//			ball_x=2-ball_x;
//			velocity_x=-velocity_x;
//		}
		}
		else if(numOfPlayer==2) {//do nothing
			}
		
	}
	public void collison(){
		if(numOfPlayer==1){
			ball_x=2-ball_x;
		}else{
			if(ball_x<=0)	ball_x=-ball_x;
			else ball_x=2-ball_x;
		}
		Random r = new Random();
		double U = -0.015 + 0.03 * r.nextDouble();
		double V = -0.03 + 0.06 * r.nextDouble();
		velocity_x=-velocity_x+U;
		velocity_y=-velocity_y+V;
		
		if(velocity_x>-0.03 && velocity_x<0) velocity_x=-0.03;
		if(velocity_x<0.03 && velocity_x>0) velocity_x=0.03;
		if(velocity_x>1) velocity_x=0.99;
		if(velocity_x<-1) velocity_x=-0.99;
		
		if(velocity_y>1) velocity_y=0.99;
		if(velocity_y<-1) velocity_y=-0.99;
	}
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.fillOval((int)(ball_x*500-5), (int)(ball_y*500-5), 10, 10);
	}
	public double getX(){
		return ball_x;
	}
	public double getY(){
		return ball_y;
	}
	public double getVx() {
		return velocity_x;
	}
	public double getVy() {
		return velocity_y;
	}
}