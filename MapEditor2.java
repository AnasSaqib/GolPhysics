/*MapEditor2.java
 *Vinay Jayachandiran and Anas Saqib
 * *MapEditor2 is the main class that calls map. This is the 2nd MapEditor because the 1st one
 *failed and this one is called 2 so we can sperate them.
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

public class MapEditor2{
	public static void main(String [] args){
		Map map = new Map();//initalize
		while(map.notdone){//notdone is true when user is not done, when they are done, this program will stop
			map.key();//checks all keyboard input
			map.update();//draw everything
		}
	}
}
/*Map.java
 *Vinay Jayachandiran and Anas Saqib
 *This is the "Map" function that is used to create all levels for the game Golphysics.
 *Using the keyboard the user gets to pick a varirty of tools, such as normal blocks, lines, blocks with different bounciness,
 *the balls start position, end position etc. The most basic thing the user can put on are blocks, there are horizontal and vertical
 *blocks. When using this tool a grid appears in the back so the user can see where they are placing these blocks. On top of Horizontal
 *and Vertical, there are 3 colours of blocks, blue, green and orange. Blue blocks are normal bounce, green blocks are not
 *very bouncy and orange blocks are very bouncy. In addition to block, the other thing that can be used are lines, lines can be 
 *drawn from one end to the other and all 3 colours of lines are available. Another main feature is the gravity, the basis of this
 *game is the different directions of Gravity, so obviously there is an option pick that. On top of that other small feature have
 *been added. Here is a complete set of controls.
 *
 *Blocks
 *0-blue horizontal blocks
 *1-blue vertical blocks
 *2-green horizontal blocks
 *3-green vertical blocks
 *4-orange horizontal blocks
 *5-orange vertical blocks
 *After you pick the right block, simply click where you want to place it on the grid.
 *
 *Lines
 *L-blue lines
 *K-green lines
 *J-orange lines
 *Click to pick start point, simply drag around to pick endpoint and press enter to finalize and draw line.
 *
 *Start Point
 *Simply press "Z" and click on desired point. This is wher ethe ball will start in the level
 *
 *End Point
 *Home-Vertical rectangle
 *End-Horizontal rectangle
 *Pick desired one and drag around the screen to drop. In the game, if the ball hits this point, the game is over.
 *
 *Gravity Direction
 *W-Up Gravity (turns background white after finalized)
 *A-Left Gravity (turns background green after finalized)
 *S-Down Gravity (turns background black after finalized)
 *D-Right Gravity (turns background blue after finalized)
 *
 *Pick the direction, when you click the button to pick the tool, that will be the start point of the rectangle, and you can drag around the recatangle
 *to encompass whatever region you want. When you have the rectangle you want, press g to finalize.
 *
 *Portal
 *Press P and click to set portal 1, click again to set portal 2. 1stportal will be yellow and seconds will be green, in the game
 *if you go through the 1st portal you will come out the 2nd one.
 *
 *Delete
 *Press "Delete" key and click on any blocks to delete them. Only blocks can be deleted.
 *
 *This class was created for the creators to use, however other people can use it, but it will take some getting used to.
 *Also to store the locations of these barriers, we have the Barrier class and also when the use is done, they can press the "+" button
 *to write all the information to a text file.
 *
**/

class Map extends JFrame implements MouseListener , KeyListener{
	private Image dbImage;//double buffering
	private Graphics dbg;
	
	//keep track of user mouse presses. mouseDown2 is same as mouseDown but used by a different method.
	private boolean clicked=false,mouseDown=false,mouseDown2=false,unclicked=false;
	
	private boolean [] keys = new boolean[256]; // lets us check which keys are being pressed
	
	//mouse x, mouse y, old mouse x, old mouse y
	private int mx=0,my=0,omx=0,omy=0;
	
	//imx,and imy are used to move the screen, but they are not used in this anymore because we just used a bigger screen
	//is there are any imx and imy left in the code they are useless because they have a value of 0
	private int imx=0,imy=0;
	
	//all the block pictures
	private Image bv;//blue vertical
	private Image bh;//blue horizontal
	private Image ov;//orange
	private Image oh;
	private Image gv;//green
	private Image gh;
	
	private Image curImage=bh;//the curImage is the image being used by the current tool, default is bh
	
	private Image portal1,portal2;// 2 corresponding portal picture
	
	private String colour="blue";//default start colour
	
	//each block is stored in this HashMap, to acceess it easily, the key is the x coordinate +"-"+ ycoorrdinate
	private HashMap<String,Barrier> barr = new HashMap<String,Barrier>();
	
	//gravity x and y, use to keep track of gravity area
	private int gx,gy; 
		
	//to rotate things
	AffineTransform identity = new AffineTransform();
	
	private String type="rect";//default start type
	
	//notdone to keep track of end of program and other booleans to check things
	public boolean notdone=true,next=false,firstg=true,pclicked=false;
	
	//in the end to keep track of all features
	private PrintWriter outfile;
	
	//angle of rectangle, to keep track of the blocks angles, always 90 or 0
	private double angr=0.0;
	
	//directions of gravity
	private String direc;
	//we store each direction of gravity field in a different array
	private ArrayList<int []> up = new ArrayList<int []>();//up gravity, int[x,y,width,height]
	private ArrayList<int []> down = new ArrayList<int []>();
	private ArrayList<int []> left = new ArrayList<int []>();
	private ArrayList<int []> right = new ArrayList<int []>();
	
	
	private ArrayList<int []> portals = new ArrayList<int []>();//the 1st in portals
	private ArrayList<int []> portals2 = new ArrayList<int []>();//corresponding 2nd out portals
	
	//count random things
	private int count = 0;
	
	// the location of the ending rectangle
	private int endx=0,endy=0,endW=0,endH=0;
	
	//the start point of ball in game
	private int sx=0,sy=0;
	
	//constructor
	public Map(){
		super("Field.java");
		
		//load all images
		try{
			bv = ImageIO.read(new File("BlueVert.png"));
			bh = ImageIO.read(new File("BlueHor.png"));
			ov = ImageIO.read(new File("OrangeVert.png"));
			oh = ImageIO.read(new File("OrangeHor.png"));
			gv = ImageIO.read(new File("GreenVert.png"));
			gh = ImageIO.read(new File("GreenHor.png"));
			portal1 = ImageIO.read(new File("portal1.png"));
			portal2 = ImageIO.read(new File("portal2.png"));
			outfile = new PrintWriter(new FileWriter("Level4.txt"));
			int x=5;
		}
		catch(IOException e ){
			System.out.println(e);
		}
		
		curImage=bh;//default image
		
		setSize(1024,768);
    	addMouseListener(this);
    	addKeyListener(this);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	setVisible(true);
	}
	
	//THis method is used to stretch Images so the can be drawn as a line.
	//a line is just a strectched block at an angle
	
	//We had to look up this function online at http://www.componenthouse.com/article-20
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
  
  	//Paint is what draws everything the user specifies
	public void paint(Graphics g)  {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,1024,768);
		g.setColor(Color.BLACK);
		
		if(next){//next becomes true when the user is done and wants to save the screen
			try{
				BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(0,0,1024,768));

     			File file = new File("level4.jpg");//we manually changed the name according to what level we were creating
     			ImageIO.write(screencapture, "jpg", file);
			}
			catch(Exception e){
				System.out.println(e);
			}
			notdone=false;//we are done
			outfile.close();
		}
		
		for(int [] field: up){//go through each up gravity field and draw it
			g.setColor(Color.WHITE);//up is white
			g.fillRect(field[0],field[1],field[2],field[3]);//the int [] is in the form x,y,width, height
		}
		for(int [] field: down){
			g.setColor(Color.BLACK);//down is black
			g.fillRect(field[0],field[1],field[2],field[3]);
		}
		for(int [] field: left){
			g.setColor(Color.GREEN);//left is green
			g.fillRect(field[0],field[1],field[2],field[3]);
		}
		for(int [] field: right){
			g.setColor(Color.BLUE);//right is blue
			g.fillRect(field[0],field[1],field[2],field[3]);
		}
		
		g.setColor(Color.BLACK);
		
		//type tells us what tool the user is using
		if(type=="line"){//if they want lines
			
			double ang = Math.toDegrees(Math.atan((double)(my-omy)/(mx-omx)));//find the angle of line from start point to end point
			
			if(mx-omx<0){//to adjust for the domain restrictions on atan (atan returns answere between pi/2 and -pi/2)
				ang+=180;
			}
			
			int dist = (int)Math.sqrt(Math.pow(mx-omx,2)+Math.pow(my-omy,2));//distance of line
			
			if(dist>5){
				Image resized = resize((BufferedImage)curImage,dist,18);//call ur resize funtion to stretch out our picture
				
				//To figure out the AffineTransform stuff, we looked at our teacher Mr. McKenzie's code and looked up stuff online
				Graphics2D g2 = (Graphics2D) g;
				AffineTransform tfm = new AffineTransform();//initalize
				int xshift = (int)(18*Math.sin(Math.toRadians(-ang)));
				int yshift = (int)(18*Math.cos(Math.toRadians(-ang)));
					
				//rotate angle, and x,y is pivot point
				tfm.rotate(Math.toRadians((ang)), omx+imx, omy+imy);
				g2.setTransform(tfm);
				g2.drawImage(resized,omx+imx, omy+imy, this);//draw the image after screen rotates
				
				if(keys[10]){//if the press enter, we store this
					String key=Integer.toString(omx+imx)+"-"+Integer.toString(omx+imx);
					//Barriers as the contructor, we need to send 2 lines, bcause there are 2 sides to this big block
					barr.put(key,new Barrier(resized,type,colour,omx+imx,omy+imy,mx+imx,my+imy,(double)dist,ang,omx-xshift+imx,omy+yshift+imy,mx-xshift+imx,my+yshift+imy));
					type="rect";
				}
				
				//reset the screen for rest of the normal drawins
				g2 = (Graphics2D) g;
				tfm = new AffineTransform();
	     		tfm.rotate(0,0,0);
				g2.setTransform(tfm);
			}
		}
		
		
		else if(keys[107]){//"+" sign means user is done and wish to save features
			for(String key: barr.keySet()){//go through each block and the barrier class as it's own write function
				barr.get(key).write(outfile);
			}
			for(int[] p : portals){//all the start portals boundaries
				outfile.println("portal1 "+p[0]+" "+p[1]+" "+p[2]+" "+p[3]);
			}
			for(int[] p : portals2){//all the start portals boundaries
				outfile.println("portal2 "+p[0]+" "+p[1]+" "+p[2]+" "+p[3]);
			}
			
			g.setColor(Color.BLACK);
			g.fillRect(0,0,1024,768);
			
			//now we need to redraw everything to save it with a black background
			for(int [] field: up){
				g.setColor(Color.WHITE);
				g.fillRect(field[0],field[1],field[2],field[3]);
			}
			for(int [] field: down){
				g.setColor(Color.BLACK);
				g.fillRect(field[0],field[1],field[2],field[3]);
			}
			for(int [] field: left){
				g.setColor(Color.GREEN);
				g.fillRect(field[0],field[1],field[2],field[3]);
			}
			for(int [] field: right){
				g.setColor(Color.BLUE);
				g.fillRect(field[0],field[1],field[2],field[3]);
			}
			
			outfile.println("end "+endx+" "+endy+" "+endW+" "+endH);//store end point
			outfile.println("start "+sx+" " +sy);//store start point
			
			//redraw all the blocks and lines			
			g.setColor(Color.BLACK);
			for(String key: barr.keySet()){
				Barrier b = barr.get(key);
				if (b.type=="rect"){
					
					g.drawImage(b.image,(int)b.area.getX()+imx,(int)b.area.getY()+imy,null);
				}
				else if(b.type=="line"){
				
		
					Graphics2D g2 = (Graphics2D) g;
					AffineTransform tfm = new AffineTransform();
					tfm.rotate(Math.toRadians(-(b.ang)), b.line.getX1()+imx, b.line.getY1() +imy);
					g2.setTransform(tfm);
					
					g2.drawImage(b.image,(int)b.line.getX1()+imx, (int)b.line.getY1()+imy, this);		
		
					g2 = (Graphics2D) g;
					tfm = new AffineTransform();
		     		tfm.rotate(0,0,0);
					g2.setTransform(tfm);
				}
			}
			
			//redraw portals on top
			for(int [] p:portals){
				g.setColor(Color.YELLOW);
				g.drawImage(portal1,p[0],p[1],this);//put the image at the right spot
			}
			for(int [] p:portals2){
				g.setColor(Color.GREEN);
				g.drawImage(portal2,p[0],p[1],this);
			}
			next=true;//this allows th program to go to screen capture and finish
			
		}
		else if(type=="gravity"){//if the tool is gravity
			if(firstg){//if they just clicked the too;
				gx=MouseInfo.getPointerInfo().getLocation().x;//this becomes their start point
    			gy=MouseInfo.getPointerInfo().getLocation().y;
    			firstg=false;
			}
			mx=MouseInfo.getPointerInfo().getLocation().x;//their current position is the end point
    		my=MouseInfo.getPointerInfo().getLocation().y;
    		
    		if(keys[71]){//71 is "g"
    			outfile.println(direc+" "+gx+" "+gy+" "+(mx-gx)+" "+(my-gy));//write it to outfile
    			
    			//now keep track of this rectangle in an ArrayList to redraw it
    			if(direc=="up"){
    				up.add(new int[4]);//new int with 4 slots
    				up.get(up.size()-1)[0]=gx;
    				up.get(up.size()-1)[1]=gy;
    				up.get(up.size()-1)[2]=mx-gx;
    				up.get(up.size()-1)[3]=my-gy;
    			}
    			else if(direc=="down"){
    				down.add(new int[4]);
    				down.get(down.size()-1)[0]=gx;
    				down.get(down.size()-1)[1]=gy;
    				down.get(down.size()-1)[2]=mx-gx;
    				down.get(down.size()-1)[3]=my-gy;
    			}
    			else if(direc=="left"){
    				left.add(new int[4]);
    				left.get(left.size()-1)[0]=gx;
    				left.get(left.size()-1)[1]=gy;
    				left.get(left.size()-1)[2]=mx-gx;
    				left.get(left.size()-1)[3]=my-gy;
    			}
    			else if(direc=="right"){
    				right.add(new int[4]);
    				right.get(right.size()-1)[0]=gx;
    				right.get(right.size()-1)[1]=gy;
    				right.get(right.size()-1)[2]=mx-gx;
    				right.get(right.size()-1)[3]=my-gy;
    			}
    			type="rect";//reset back to the default tools to avoid errors
    			colour="green";
    			curImage=bh;
    			angr=0.0;
    		}
			g.drawRect(gx,gy,mx-gx,my-gy);//draw a rect to their current position
		}
		else if(type.equals("end")){//set end position
			if(mouseDown&&direc.equals("hor")){//if they picked horizontal end
				endx=mx;
				endy=my;
				endW=50;//longer width
				endH=10;
			}
			else if(mouseDown && direc.equals("vert")){//if the pick vertical
				endx=mx;
				endy=my;
				endW=10;
				endH=50;//longer height
			}
		}
		else if(type.equals("portal")){//portal tool
			//everytime we add a portal the counter goes up by 1, so if counter is even it is the 1st portal
			//if it is odd it is the end portal
			
			if(pclicked && count%2==0){//even
				portals.add(new int[4]);//list of start portals
				portals.get(portals.size()-1)[0]=mx;
				portals.get(portals.size()-1)[1]=my;
				portals.get(portals.size()-1)[2]=25;
				portals.get(portals.size()-1)[3]=25;
				pclicked=false;
				count++;
			}
			else if(pclicked){//odd
				portals2.add(new int[4]);//list of end portals
				portals2.get(portals.size()-1)[0]=mx;
				portals2.get(portals.size()-1)[1]=my;
				portals2.get(portals.size()-1)[2]=25;
				portals2.get(portals.size()-1)[3]=25;
				pclicked=false;
				count++;				
			}
		}
		else if(type.equals("start")){//set the start point of the ball
			sx=mx-10;
			sy=my-10;
		}
		
		else{//if the user is drawing blocks or deleting them, then it comes to this else statement
			for(int x=imx; x<1024; x+=18){
				if(x>0){
					g.drawLine(x,0,x,768);//vertical grid lines
				}
				for(int y=imy; y<768; y+=18){
					if(y>0){
						g.drawLine(0,y,1024,y);//horizontal grid lines
					}
					if(mouseDown2){//if the user has the mouse down
						if(x<mx&&mx<x+18&&y<my&&my<y+18){// and the are clicking in the current grid
							String key = Integer.toString(x-imx)+"-"+Integer.toString(y-imy);//key is x+"-"+y
							if(type=="delete"){//if deleting
								if(barr.containsKey(key)){
									barr.remove(key);//we remove that block
								}
							}
							//if they are not deleting, they must be adding
							else if(barr.get(key)==null){//if there isn't already an object there
								barr.put(key,new Barrier(curImage,type,colour,x-imx,y-imy,26,26,18,angr));//we add the users choice	
							}
							clicked=false;//clicked is done
						}
					}
				}
			}
		}
		//now we draw all the blocks and lines
		for(String key: barr.keySet()){
			Barrier b = barr.get(key);
			if (b.type=="rect"){
				
				g.drawImage(b.image,(int)b.area.getX()+imx,(int)b.area.getY()+imy,null);//draw right image at right spot
			}
			else if(b.type=="line"){
				//rotate
				Graphics2D g2 = (Graphics2D) g;
				AffineTransform tfm = new AffineTransform();
				tfm.rotate(Math.toRadians(-(b.ang)), b.line.getX1()+imx, b.line.getY1() +imy);
				g2.setTransform(tfm);
				
				//draw line
				g2.drawImage(b.image,(int)b.line.getX1()+imx, (int)b.line.getY1()+imy, this);		
				
				//unrotate
				g2 = (Graphics2D) g;
				tfm = new AffineTransform();
	     		tfm.rotate(0,0,0);
				g2.setTransform(tfm);
			}
		}
		
		//Draw all portals
		for(int [] p:portals){//go through each portal and draw it
			g.setColor(Color.YELLOW);
			g.drawImage(portal1,p[0],p[1],this);
		}
		for(int [] p:portals2){
			g.setColor(Color.GREEN);
			g.drawImage(portal2,p[0],p[1],this);
		}
		
		
		g.setColor(Color.RED);//red coloured end rectangle
		g.fillRect(endx,endy,endW,endH);//end point

		mouse(g);//update mouse points
    }
    
   	public void update(){//Double Buffering
    	Graphics g = getGraphics();
    	if (dbImage==null){
    		dbImage = createImage(getWidth(),getHeight());
    		dbg = dbImage.getGraphics();
    	}
    	
    	paint(dbg);
    	g.drawImage(dbImage,0,0,this);
    }
    
    public void mouse(Graphics g){//update mouse points    	
    	mx=MouseInfo.getPointerInfo().getLocation().x;
    	my=MouseInfo.getPointerInfo().getLocation().y;
    }
    
    //looks at keyboard input and gets right tool
    public void key(){
    	if(keys[48]){//0
    		curImage=bh;
    		colour="blue";//colour used to determine bounce
    		type="rect";//type
    		angr=0.0;//angle of rect horizontals are 0 and verticals are 90
    	}
    	else if(keys[49]){//1
    		curImage=bv;
    		colour="blue";
    		type="rect";
    		angr=90.0;
    	}
    	else if(keys[2+48]){//2
    		curImage=gh;
    		colour="green";
    		type="rect";
    		angr=0.0;
    		
    	}
    	else if(keys[3+48]){//3
    		curImage=gv;
    		colour="green";
    		type="rect";
    		angr=90.0;
    	}
    	else if(keys[4+48]){//4
    		curImage=oh;
    		colour="orange";
    		type="rect";
    		angr=0.0;
    	}
    	else if(keys[5+48]){//4
    		curImage=ov;
    		colour="orange";
    		type="rect";
    		angr=90.0;
    	}
    	else if(keys[127]){//delete
    		type="delete";
    	}
    	else if(keys[76]){//l
    		type="line";
    		colour="blue";
    		curImage=bh;
    	}
    	else if(keys[75]){//k
    		type="line";
    		colour="green";
    		curImage=gh;
    	}
    	else if(keys[74]){//j
    		type="line";
    		colour="orange";
    		curImage=oh;
    	}
    	else if(keys[87]){//w
    		type="gravity";
    		direc="up";
    		firstg=true;
    	}
    	else if(keys[65]){//a
    		type="gravity";
    		direc="left";
    		firstg=true;
    	}
    	else if(keys[83]){//s
    		type="gravity";
    		direc="down";
    		firstg=true;
    	}
    	else if(keys[68]){//d
    		type="gravity";
    		direc="right";
    		firstg=true;
    	}
    	else if(keys[35]){//end
    		type="end";
    		direc="hor";	
    	}
    	else if(keys[36]){//home
    		type="end";
    		direc="vert";
    	}
    	else if(keys[80]){//p
    		type="portal";
    	}
    	else if(keys[90]){//z
    		type="start";
    	}
    }
    
    //All mouse Interfaces
    public void mousePressed(MouseEvent e) { 
    	//true when mouse down and false when mouse up
    	//we have 3 different ones because different methods change them to false before mouse up
		clicked=true;
		mouseDown=true;
		mouseDown2=true;

    }
    public void mouseReleased(MouseEvent e) {
    	//mouse up
		mouseDown=false;
		unclicked=true;//not clicked
		mouseDown2=false;
    }

    public void mouseEntered(MouseEvent e) {
       	
    }

    public void mouseExited(MouseEvent e) {
      	
    }

    public void mouseClicked(MouseEvent e) {
    	pclicked=true;//clicked is pressed and release used to draw portals
		omx=mx;
    	omy=my;
    }
    
    //All Keyboard interfaces
    public void keyTyped(KeyEvent e){
    	
    }
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;//sets the spot of the key pressed true

    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;//when the key is released we set that to false
    }
}


