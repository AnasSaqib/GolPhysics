/*Barrier.java
 *Vinay and Jayachandiran
 *Barrier class is used to store information about the plain barriers. They are either rectangular blocks(0 and 90 degrees) or lines.
 *This class stores the barrier's postition and boundaries. Also the lines have 2 sides so we have to save to lines to check both.
 *Other methods like collide are also present to check for collisions. 
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.geom.AffineTransform;
import java.awt.Image.*;
import java.awt.image.BufferedImage.*;

import java.awt.geom.*;

import javax.swing.JPanel;
import java.awt.image.*;     
import java.util.Arrays;
import javax.imageio.ImageIO;

class Barrier{
	int bounce;//stores how much bounce the barrier has
	double size,ang;//size and ang are use to keep track of how long a line is and at what angle it is, for rectangles they are 0
	Rectangle area;//rectangle barriers' boundaries
	
	Line2D line = new Line2D.Double();//line 1
	int lx1,ly1,lx2,ly2;//the 2 points composing line1
	Line2D line2 = new Line2D.Double();//line2
	int l2x1,l2y1,l2x2,l2y2;//the 2 points composing line2
	
	Image image;//the image of the barrier
	String type, colour;//type is rectangle or line and colour used to determine bounce
	
	//this contructor is for rectangle. 
	public Barrier(Image pic,String type,String colour,int x,int y,int w,int l,double size,double ang){
		if(type=="rect"){
			area = new Rectangle(x,y,w,l);
		}
		else if(type=="line"){//just incase
			line.setLine(x,y,w,l);
		}
		//save all info
		this.ang=ang;
		this.type=type;
		this.size=size;
		this.colour=colour;
		image=pic;
		
		if(colour=="green"){//green is most bouncy
			bounce=10;
		}
		else if(colour=="blue"){//blue is normal
			bounce=7;
		}
		else if(colour=="orange"){//orange isn;t that bouncyy
			bounce=5;
		}
	}
	
	//Constructor for rectangles, except this one doesn't need an image. Screen class already has the whole background so it doesn't
	//need to store image
	public Barrier(String type,String colour,int x,int y,int w,int l,double size,double ang){
		if(type=="rect"){
			area = new Rectangle(x,y,w,l);
		}
		else if(type=="line"){//just incase
			line.setLine(x,y,w,l);
			
		}
		
		//save all info
		this.ang=ang;
		this.type=type;
		this.size=size;
		this.colour=colour;
		

		if(colour=="green"){//green is most bouncy
			bounce=10;
		}
		else if(colour=="blue"){//blue is normal
			bounce=7;
		}
		else if(colour=="orange"){//orange isn;t that bouncyy
			bounce=5;
		}
	}
	
	//storing the line, we first have the points of the first line, then the size and angel in between and the points of the 2nd line
	public Barrier(Image pic,String type,String colour,int x,int y,int w,int l,double size,double ang,int x2,int y2,int w2,int l2){

		if(type=="line"){
			line.setLine(x,y,w,l);//setting line
			line2.setLine(x2,y2,w2,l2);
		}
		
		//storing info
		this.ang=ang;
		this.type=type;
		this.size=size;
		this.colour=colour;
		lx1=x;
		ly1=y;
		lx2=w;
		ly2=l;
		l2x1=x2;
		l2y1=y2;
		l2x2=w2;
		l2y2=l2;
		if(pic!=null){
			image=pic;
		}
		
		
		if(colour=="green"){//green is most bouncy
			bounce=10;
		}
		else if(colour=="blue"){//blue is normal
			bounce=7;
		}
		else if(colour=="orange"){//orange isn;t that bouncyy
			bounce=5;
		}
	}
	//if we need to store this barrier in a text file we just call this method
	public void write(PrintWriter outfile){
		if(type=="rect"){//if a rectangle
		//we store the colour, start point and dimensions which is always 18, also the angle to tell of ot os vertical or horizontal
			outfile.println("rect "+colour + " " + area.getX() + " " + area.getY() + " " +18 +" "+18+" "+ang);
		}
		else if(type=="line"){//if it is a line
		//we store colour, 2 points of 1st line, size, angle, and 2 points of 2nd line
			outfile.println("line "+colour + " " + line.getX1() + " " + line.getY1() + " " +line.getX2() + " " +line.getY2()+" " +size+" "+ang+" "+line2.getX1()+" "+line2.getY1()+" "+line2.getX2()+" "+line2.getY2());

		}
	}

	//this method is to test if a ball its a line
	public Line2D collide(int bx,int by,int imx,int imy,Graphics g){
		
		// first we need to reconstruct the 2 lines after accomadating for the imx and imy
		// so now we have the 2 lines imLine and imLine2
		Line2D imLine =  new Line2D.Double();
		imLine.setLine(lx1+imx,ly1+imy,lx2+imx,ly2+imy);//and imx and imy
	
		
		Line2D imLine2 =  new Line2D.Double();
		imLine2.setLine(l2x1+imx,l2y1+imy,l2x2+imx,l2y2+imy);//and imx and imy
		
		//First we see which line is closer to the ball, that is the only line we need to check if it hits the ball
		if(imLine.ptLineDist(bx,by)<imLine2.ptLineDist(bx,by)){//if imLine is close
			if(imLine.ptLineDist(bx,by)<=10){//we check if it is within the radius of the ball, the radius is 10
				//if it is within the radius, we have to check if it is in the domain of the line
				//Line2D gives use an entire line, we only check a line segment
				if(imLine.getX1()>imLine.getX2()){//if X1 is greater
					if(imLine.getX2()<=bx && bx<=imLine.getX1()){//ballx as to be less than X1 but greater than X2
						//if it satisfies all these conditions, this line hits the ball
						return imLine;
					}
				}
				else{
					if(imLine.getX1()<=bx && bx<=imLine.getX2()){//if X2 is greater x1<=bx<=x2
						return imLine;
					}									
				}
			}
		}
		else if(imLine2.ptLineDist(bx,by)<=10){//if imLine2 is close we check this one
			//once again we have to check the domain of this line segment
			if(imLine2.getX1()>imLine2.getX2()){//x1>x2
				if(imLine2.getX2()<=bx && bx<=imLine2.getX1()){//then x2<=bx<=x1
					return imLine2;
				}
			}
			else{//x2>x1
				if(imLine2.getX1()<=bx && bx<=imLine2.getX2()){//x1<=bx<=x2
					return imLine2;
				}									
			}
		}	
		return null;//if not intersection we return null	
	}

	public Image getImage(){//return the image
		return image;
	}
	
}