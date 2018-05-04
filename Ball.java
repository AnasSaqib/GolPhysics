//This class is what makes gravity affect the ball. It also changes the direction of the ball and slows it down, when it collides with a barrier.
//Also, this class is where the x and y velocities are kept track of, decreased/increased and stored. 
//Anas Saqib and Vinay Jayachandiran
class Ball{
	double vx,vy; //x and y velocities
	double xdirec,ydirec; //variables to keep track of the the x and y direc of the ball
	boolean stopped; //checks if the ball has stopped
	public Ball(double velx,double vely){ //constructor. takes in the starting velocities
		vx=velx; //sets the velocities
		vy=vely;
		xdirec=vx/(Math.abs(vx)); //sets the x and y directions; positive x direc - right, negative x direc - left
		ydirec=vy/(Math.abs(vy)); //positive y direc - down, negative y direc - up
		stopped=false;
	}
	public void changeDirecx(String gravity,String colour){ //method to change the x direc of the ball. (when contact is made with a vertical barrier)
		if(gravity.equals("left")==true||gravity.equals("right")==true){ //the ball will only stop if the gravity is right or left
			if(gravity.equals("left")==true&&vx<0){ //if gravity is to the left, then the ball has to be travelling towards the left in order for the ball to stop
				if(Math.round(vx)>=0&&Math.round(vx)<=1){ //stops the ball if the velocity approaches zero
					stopped=true; //stops the ball
				}	
			}
			else if(gravity.equals("right")==true&&vx>0){ //if gravity is to the right, then the ball has to be travelling towards the right in order for the ball to stop
				if(Math.round(vx)>=-1&&Math.round(vx)<=1){ //stops the ball if the velocity approaches zero
					stopped=true; //stops the ball
				}	
			}	
		}
		
		if(colour.equals("blue")==true){ //if the colour of the barrier is blue (normal bounce)
			vy=(int)vy*.7; //the ball keeps 70% of its velocity
			vx=(int)vx*-0.7;	
		}
		else if(colour.equals("orange")==true){ //if the colour of the barrier is orange (extra bounce)
			vy=(int)vy*.95; //the ball keeps 95% of its velocity
			vx=(int)vx*-.95;	
		}
		else if(colour.equals("green")==true){ //if the colour of the barrier is green (less bounce)
			vy=(int)vy*.5; //the ball keeps 50% of its velocity
			vx=(int)vx*-0.5;	
		}
		xdirec*=-1; //reverses the x direction
	}
	public void changeDirecy(String gravity,String colour){ //method to change the y direc of the ball. (when contact is made with a horizontal barrier)
		if(gravity.equals("down")==true||gravity.equals("up")==true){ //the ball will only stop if the gravity is up or down
			if(gravity.equals("down")==true&&vy>0){ //if gravity is down, then the ball has to be travelling down in order for the ball to stop
				if(Math.round(vy)<=1&&Math.round(vy)>=-1){	//stops the ball if the velocity approaches zero
					stopped=true; //stops the ball
				}	
			}
			else if(gravity.equals("up")==true&&vy<0){ //if gravity is up, then the ball has to be travelling up in order for the ball to stop
				if(Math.round(vy)<=1&&Math.round(vy)>=-1){ //stops the ball if the velocity approaches zero	
					stopped=true; //stops the ball
				}	
			}	
		}
		if(colour.equals("blue")==true){ //if the colour of the barrier is blue (normal bounce)
			vy=(int)vy*-.7; //the ball keeps 70% of its y velocity	
		}
		else if(colour.equals("orange")==true){ //if the colour of the barrier is orange (extra bounce)
			vy=(int)vy*-.9; //the ball keeps 95% of its y velocity	
		}
		else if(colour.equals("green")==true){ //if the colour of the barrier is green (less bounce)
			vy=(int)vy*-.5; //the ball keeps 50% of its y velocity	
		}
		ydirec*=-1; //reverse the ydirection
	}

	public double getx(){ //returns the x velocity
		return vx;		
	}
	public double gety(){ //returns teh y velocity
		return vy;
	}
	public void setx(double velx){ //sets the x velocity 
		vx=velx;	
	}
	public void sety(double vely){ //sets the y velocity
		vy=vely;
	}
	
	public void slowdown(String gravity,int bx,int by){ //slows/speeds the ball up
		if (gravity.equals("down")){ 
			vy+=1; //down	
		}
		else if (gravity.equals("up")){
			vy-=1; //up	
		}
		else if (gravity.equals("right")){
			vx+=1; //right	
		}
		else if(gravity.equals("left")){
			vx-=1; //left	
		}
		else if(gravity.equals("round")){ //if the gravity is round (for a specific level)
			double ang = toAng(by-358,bx-422);//center of field is 422,358
			vx-= 1*Math.cos(ang);
			vy-= 1*Math.sin(ang);
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
	
	public boolean checkStatus(boolean status){ //checks whether the ball is still in motion
		if (stopped==true){
			return false;
		}
		else{
			return status;
		}
	}
	
}