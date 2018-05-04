/*Vector.java
 *Vinay Jayachandiran and Anas Saqib
 *This class is used to simple vector stuff like subtract, multiply and dotproduct.
 */


public class Vector {
	double x,y;
	
    public Vector(double x, double y) {//constructor
    	this.x=x;
    	this.y=y;
    }
    public Vector subtract(Vector v){
    	return new Vector(x-v.x,y-v.y);//subtract components
    }
    public Vector perpendicular(){
    	return new Vector(-y,x);//slope of perpendicular line is -1/m
    }
    public double ang(){
    	return Math.atan(y/x);
    }
    public double mag(){
    	return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));//square root of x square plus y square
    }
    public Vector unit(){//returns just the unit vector of this vector
    	double ang = ang();
    	return new Vector(Math.cos(ang),Math.sin(ang));
    }
    public double dotProd(Vector v){//dot product
    	return x*v.x+y*v.y;
    }
    public Vector multiply(double k){//multiply by a constant
    	return new Vector(k*x,k*y);
    }
    public Vector add(Vector v){
    	return new Vector(x+v.x,y+v.y);//add components
    }
    
    
    
}