package pong;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Pong extends Applet implements Runnable,KeyListener{
	final int WIDTH=500,HEIGHT=500;
	Thread thread;
	Paddle p1;
	Paddle ai;
	Ball ball;
	private double[][][][][][] util;
	
	//numOfPlayer
	int player=1,ai_player=2;
	public void init() {
		System.out.println("initializing");
		QLearning ql = new QLearning();
		ql.init();
		p1=new Paddle(player);
		ai=new Paddle(ai_player);
		util=ql.Q;
		this.resize(WIDTH+20,HEIGHT);
		this.addKeyListener(this);
		ball=new Ball(2);
		thread=new Thread(this);
		thread.start();
		
		
	}
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH+20, HEIGHT);
		p1.draw(g);
		ai.draw(g);
		ball.draw(g);
		g.setColor(Color.white);
		
		
//		//dimensions
//		int getPy=(int)Math.floor(p1.getY()*12/(1-p1.paddle_height));
//		g.drawString("ball_x   "+(int)(((ball.getX())*12)), 510, 20);
//		g.drawString("ball_y   "+(int)(((ball.getY())*12)), 510, 40);
//		g.drawString("vel_x    "+(ball.getVx()>0?1:-1), 510, 60);
//		g.drawString("vel_y    "+(ball.getVy()>=0.015?1:(ball.getVy()<=-0.015?-1:0)), 510, 80);
//		g.drawString("paddle_y "+((getPy==12)?11:getPy), 510, 100);
		
	}
	public void update(Graphics g) {
		paint(g);
	}

	public void run() {
		
		boolean terminalState=false;
		while(!terminalState) {
			int bounced=0;
			int[] state= QLearning.discrete(ball.getX(),ball.getY(),ball.getVx(),ball.getVy(),ai.getY());
			ai.move(QLearning.ACTION[QLearning.maxAction(util[state[0]][state[1]][state[2]][state[3]][state[4]])]);
			p1.move();
			ball.move();
			if(ball.getX()>=1){
				if(ball.getY()>=ai.getY() && ball.getY()<=(ai.getY()+0.2)){
					ball.collison();
				}else{
					terminalState=true;
					System.out.println("human win with "+bounced+" bounces");
				}
			}else if(ball.getX()<=0){
				if(ball.getY()>=p1.getY() && ball.getY()<=(p1.getY()+0.2)){
					ball.collison();
					bounced+=1;
				}else{
					terminalState=true;
					System.out.println("human lose with "+bounced+" bounces");
				}
			}
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_UP) {
			p1.setUpAccel(true);
		}else if(arg0.getKeyCode()==KeyEvent.VK_DOWN) {
			p1.setDownAccel(true);
		}
		
	}
	
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_UP) {
			p1.setUpAccel(false);
		}else if(arg0.getKeyCode()==KeyEvent.VK_DOWN) {
			p1.setDownAccel(false);
		}
		
	}
	
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
