package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
	double x,paddle_y,yVel=0.04,paddle_height=0.2;
	boolean upAccel,downAccel;
	int player;
	public Paddle(int player) {
		upAccel=false;
		downAccel=false;
		paddle_y=0.4;
		if(player==1) {x=0;}
		else if(player==2) {x=1;}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)(x*500), (int)(paddle_y*500), (int)(0.01*500), (int)(paddle_height*500));
	}
	public void move() {
		if(upAccel) {
			paddle_y-=yVel;
		}
		else if(downAccel) {
			paddle_y+=yVel;
		}
		if(paddle_y<0){
			paddle_y=0;
		}
		if(paddle_y>(1-paddle_height)){
			paddle_y=1-paddle_height;
		}
	}
	public void move(int dir) {
		if(dir==-1) {
			paddle_y+=yVel;
		}
		else if(dir==1) {
			paddle_y-=yVel;
		}
		if(paddle_y<0){
			paddle_y=0;
		}
		if(paddle_y>(1-paddle_height)){
			paddle_y=1-paddle_height;
		}
	}
	public void setUpAccel(boolean input) {
		upAccel=input;
	}
	public void setDownAccel(boolean input) {
		downAccel=input;
	}
	public double getY() {
		return paddle_y;
	}
}
