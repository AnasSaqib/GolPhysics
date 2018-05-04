//Move.java
//Anas Saqib and Vinay Jayachandiran
////This class is where the ball's x and y coordinates are kept track of. This is also where collisions with barriers are checked as are entrances into portals. 
//Basically this class stores the ball's position and collions are checked and the ball class is called to find the corresponding velocities.

import javax.swing.*; //imports
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
class Move extends JFrame{
	private Image fball=new ImageIcon("ball.png").getImage(); //ball image
	private Image ballring=new ImageIcon("ballring.png").getImage(); //the ring around the ball
	private int mx,my;
	private int ballx,bally,launchy,launchx;
	private boolean ballstatus,mouseclicked; //keeps track of whether the ball is in motion 
	private Ball ball=new Ball(0,0); //creates a ball with 0 velocity
	private double ringangle; //angle for the ring 
	private String gravity; //gravity direction
	private Rectangle ballr; //rectangle that contains the ball
	private int strokes=0; //keeps track of the strokes
	
	public Move(String gravdirec,int sx,int sy){ //takes in the gravity direction
		gravity=gravdirec; //defines the gravity direction
		ballx=sx; //ball's x coor
		bally=sy; //ball's y coor
		ballr=new Rectangle(ballx,bally,20,20); //defines the rectangle that contains the ball
		ballstatus=true; //starts off as true so the ball drops down
		ringangle=0; //angle for the ring around the ball
		launchy=bally; //coor where the ball was launched from (for reseting)
		launchx=ballx;
	}
	
	public void paint(Graphics g, int imx, int imy){ //paint method
		g.setColor(Color.BLACK);
		g.setColor(new Color(220,15,55));		
		g.drawImage(fball,ballx+imx,bally+imy,this); //draws the ball
		if (ballstatus==false){ //if ball isnt in motion
			Graphics2D g2D=(Graphics2D)g; //rotates and draws the ring around the ball
			AffineTransform SaveXform=g2D.getTransform();
			AffineTransform at=new AffineTransform();
			at.rotate(Math.toRadians(ringangle),ballx+imx+10,bally+imy+10);
			g2D.transform(at);
			g2D.drawImage(ballring,ballx+imx-10,bally+imy-10,this);
			g2D.setTransform(SaveXform);
			ringangle+=5;	
		}

		if (mouseclicked&&ballstatus==false){ //if mouse has been clicked but the ball isnt moving
			if(Math.sqrt(Math.pow(ballx+imx-mx,2)+Math.pow(bally+imy-my,2))<250){ //limits how hard the ball can be launched
				g.drawLine(ballx+imx+10,bally+imy+10,mx,my); //draws the launching line
			}
			else{
				double ang = toAng((double)(my-bally-10-imy),(double)(mx-ballx-10-imx));
				g.drawLine(ballx+imx+10,bally+imy+10,ballx+10+imx+(int)(250*Math.cos(ang)),bally+10+imy+(int)(250*Math.sin(ang)));
			}
		}

	}
	public double toAng(double y, double x){ //takes in delta y and delta x. returns the angle that the line is at
		if(x<0){
			return Math.toRadians(180)+Math.atan(y/x);
		}
		else if(x==0&&y>0){
			return Math.toRadians(90);
		}
		else if(x==0&&y<0){
			return Math.toRadians(270);
		}
		return Math.atan(y/x);
	}
	public boolean playing(){return true;}
	
	public void moveStuff(int mb,int imx,int imy,HashMap<String,Barrier> points,Graphics g,ArrayList<Rectangle> sportals,ArrayList<Rectangle> eportals){ //movestuff method. takes in a hashmap with all the barriers, and arraylists for the portals
		mx= MouseInfo.getPointerInfo().getLocation().x; //gets the cursor's position
    	my= MouseInfo.getPointerInfo().getLocation().y;
		if (ballx+imx<=mx&&mx<=ballx+imx+20&bally+imy<=my&&my<bally+imy+20&mb==1){ //if mouse goes over the ball and gets clicked
			mouseclicked=true;
		}
		if (mouseclicked==true&&mb==0&&ballstatus==false){ //if mouse has been clicked and released. ball has to be still (prevents relaunch when the ball is already in motion)	
			if(Math.sqrt(Math.pow(ballx+imx-mx,2)+Math.pow(bally+imy-my,2))<250){
				ball=new Ball((int)(ballx+imx-mx)/10,(int)(bally+imy-my)/10); //creates a ball with the velocity depending on how far the mouse was pulled away from the ball
			}
			else{
				double ang = toAng((double)(my-bally-10-imy),(double)(mx-ballx-10-imx));
				ball=new Ball((int)(ballx+imx-(ballx+10+imx+(int)(250*Math.cos(ang))))/10,(int)(bally+imy-(bally+10+imy+(int)(250*Math.sin(ang))))/10);
				
				ballr=new Rectangle(ballx+imx,bally+imy,20,20);
			}
			strokes+=1; //adds one to the counter for strokes
			launchy=bally; //resets the launch coors
			launchx=ballx;
			ballstatus=true; //means the ball is now in motion
			mouseclicked=false;
		}
		ballstatus=ball.checkStatus(ballstatus); //checks the ball status	
		if (ballstatus==true){ //if ball is in motion
			ballx+=Math.round(ball.getx()); //moves the ball by changing the coors
			bally+=Math.round(ball.gety());
			ballr=new Rectangle(ballx,bally,20,20); //reset the rectangle containing the ball
			for(Rectangle sportal: sportals){ //goes through the list of entrance portals
				if(sportal.intersects(ballr)){ //if the ball enters a portal
					ballx=(int)eportals.get(sportals.indexOf(sportal)).getX(); //teleports the ball to the corresponding exit portal
					bally=(int)eportals.get(sportals.indexOf(sportal)).getY();					
				}
			}
				
			for(String key: points.keySet()){ //goes through the barriers
				if(points.get(key).type=="rect"){ //if it is a vertical or horizontal barrier
					Rectangle b = points.get(key).area; //gets the rectangle of the barrier
					double ang = points.get(key).ang; //gets the angle (horizontal or vertical)
					if(b.intersects(ballr)){
						if(ang==0 || ang==180 || ang==360){ //if barrier is horizontal
							if(ball.gety()>0){ //if ball is moving towards the right (vy is postive. NOTE: gety() gets the y velocity)
								bally=(int)b.getY()-20;	//makes sure the ball doesnt go through or into the barrier
							}
							else{
								bally=(int)b.getY()+20;	//makes sure the ball doesnt go through or into the barrier	
							}				
							ball.changeDirecy(gravity,points.get(key).colour); //changes the y direction of the ball
							ballr=new Rectangle(ballx,bally,20,20);	//resets the rectangle containing the ball
							
							
							//this is a precaution because the ball sometimes doesn't stop
							if(gravity.equals("down")&&ball.gety()<0){ //if the ball is going down and gravity is down,(if gravity was up it shouldn't stop on the bottom
								Vector Vball = new Vector(ball.getx(),ball.gety());
								Vector P1 = new Vector(b.getX(),b.getY());
								Vector P2 = new Vector(b.getX()+b.getWidth(),b.getY());
								
								Vector normal = P2.subtract(P1).unit().perpendicular();
								//the dot product gives us what part of the velocity is going towards the ground
								if(-2<normal.dotProd(Vball)&&normal.dotProd(Vball)<2){//if the part going to the ground is between 2 and -2 we stop the ball
									ball.setx(Vball.x);
									ball.sety(Vball.y);	
									ballstatus=false;
								}			
							}
							//same precaution if ball is going up and gravity is up.(if gravity is down it shouldn't stop near the top)
							if(gravity.equals("up")&&ball.gety()>0){
								Vector Vball = new Vector(ball.getx(),ball.gety());
								Vector P1 = new Vector(b.getX(),b.getY());
								Vector P2 = new Vector(b.getX()+b.getWidth(),b.getY());
								//the dot product gives us what part of the velocity is going towards the ground
								Vector normal = P2.subtract(P1).unit().perpendicular();
								if(-2<normal.dotProd(Vball)&&normal.dotProd(Vball)<2){
									ball.setx(Vball.x);
									ball.sety(Vball.y);	
									ballstatus=false;
								}			
							}
						}
						else if(ang==90||ang==270){ //if barrier is verticle
							if(ball.getx()<0){ //if ball is moving down (vx is negative. NOTE: getx() gets the x velocity)
								ballx=(int)b.getX()+20;	//makes sure the ball doesnt go through or into the barrier
							}
							else{
								ballx=(int)b.getX()-20; //makes sure the ball doesnt go through or into the barrier
							}				
							ball.changeDirecx(gravity,points.get(key).colour); //changes the x direction of the ball
							ballr=new Rectangle(ballx,bally,20,20); //resets the rectangle containing the ball	 
							
							//same precaution, sometimes ball doesn't stop, so we get the part that is going towards thr ground
							// and if that is relatively small, we stop the ball
							if(gravity.equals("right")&&ball.getx()<0){
								Vector Vball = new Vector(ball.getx(),ball.gety());
								Vector P1 = new Vector(b.getX(),b.getY());
								Vector P2 = new Vector(b.getX(),b.getY()+b.getHeight());
								
								Vector normal = P2.subtract(P1).unit().perpendicular();
								if(-2<normal.dotProd(Vball)&&normal.dotProd(Vball)<2){
									ball.setx(Vball.x);
									ball.sety(Vball.y);	
									ballstatus=false;
								}
							}
							//same precaution, sometimes ball doesn't stop, so we get the part that is going towards thr ground
							// and if that is relatively small, we stop the ball
							if(gravity.equals("left")&&ball.getx()>0){
								Vector Vball = new Vector(ball.getx(),ball.gety());
								Vector P1 = new Vector(b.getX(),b.getY());
								Vector P2 = new Vector(b.getX(),b.getY()+b.getHeight());
								
								Vector normal = P2.subtract(P1).unit().perpendicular();
								if(-2<normal.dotProd(Vball)&&normal.dotProd(Vball)<2){
									ball.setx(Vball.x);
									ball.sety(Vball.y);	
									ballstatus=false;
								}
							}

						}
		
					}
				}
				//To do all this vector and dot product calculations, Kevin Rupasinghe had to help us
				else{ //if the barrier is at an angle	
					Line2D line = points.get(key).collide(ballx+10+imx,bally+10+imy,imx,imy,g);//collide return the line that the ball is hitting
					if(line!=null){//collide returns null if the ball isn't hitting anything
						ballx-=Math.round(ball.getx());
						bally-=Math.round(ball.gety());
						Vector Vball = new Vector(ball.getx(),ball.gety());//ball vector
						//we get the 2 points of the line as a vector
						Vector P1 = new Vector(line.getX1(),line.getY1());
						Vector P2 = new Vector(line.getX2(),line.getY2());
						
						Vector normal = P2.subtract(P1).unit().perpendicular();//and using the points we find the normal
						
						//if the part heading toward the line is realtively small we stop it
						if(-2<=normal.dotProd(Vball)&&normal.dotProd(Vball)<2){
							ball.setx(Vball.x);
							ball.sety(Vball.y);	
							ballstatus=false;							
						}
						else{//otherwise we solve for the x and y using dot products
							Vball = Vball.subtract(normal.multiply(2*normal.dotProd(Vball)));
							ball.setx(Vball.x);
							ball.sety(Vball.y);						 
						}	
					}
				}
    		}	
				
			ball.slowdown(gravity,ballx+10,bally+10); //slows or speeds the ball up. makes gravity affect the ball	
		}
	}
	public void setG(String direc){ //sets the gravity
		gravity=direc;
	}
	public int getX(){ //returns the x coor of the ball
		return ballx;	
	}
	public int getY(){ //returns the y coor of the ball
		return bally;
	}
	public Rectangle ballr(){ //returns the rectanlge containing the ball
		return ballr;
	}
	public void setX(int x){ //sets the x coor of the ball
		ballx=x;
		ballr=new Rectangle(ballx,bally,20,20);
	}
	public void setY(int y){ //sets the y coor of the ball
		bally=y;
		ballr=new Rectangle(ballx,bally,20,20);
	}
	public double getVX(){ //returns the x velocity of the ball
		return ball.getx();
	}
	public double getVY(){ //returns the y velocity of the ball
		return ball.gety();
	}
	public void setVX(double vx){ //sets the x velocity of the ball
		ball.setx(vx);
	}
	public void setVY(double vy){ //sets the y velocity of the ball
		ball.sety(vy);
	}
	public int numStrokes(){ //return the number of strokes that have been played
		return strokes;
	}
	public void reset(){ //resets the variables (start of a new level)
		ballx=launchx;
		bally=launchy;
		ball.vx=0;
		ball.vy=0;
		ballstatus=false;
	}
	public boolean checkStatus(){ //returns the status of the ball
		return ballstatus;
	}
	
}