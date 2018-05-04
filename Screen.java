/*Screen.java
 *Vinay Jaychandiran and Anas Saqib
 *Screen class is the big graphics class that does all the graphics. Also since the level map's are bigger than the screen,
 *this class can be used to move the screen left and right. This is also the class that keeps track of what field of gravity
 *the ball is in. Since this is the class that passes all other classes the info, this opens the text file for the corresponding level
 *reads the txt file and loads all the information. Not only does it allow the user to pan the screen it automatically pans the screen
 *so the ball is always inside the screen.
 */


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Robot; 

public class Screen extends JFrame implements MouseListener {

	private Image dbImage;//double buffering
	private Graphics dbg;
	
	private BufferedImage image;
	private Image back;//background
	private int cx,cy;//current x and current y
	private int imx=0,imy=0; //used to move the screen


	private Scanner infile;//txt file with all level info
	private HashMap <String,Barrier> points = new HashMap<String,Barrier>();//stores all blocks and lines from txt file
	private ArrayList<int []> fields = new ArrayList<int []>();//store gravity fields
	private String [] gravityKey = {"up","down","left","right"};//for convenience when reading txt file
	//boolean values for varies statements
	private boolean breaks =false,clicked;
	private Rectangle end;//end point
	private boolean levelPlaying=true;//turns false when ball reaches end
	private Move move;//next class that moves the ball and checks collisions
	
	//mb keeps track of mousebutton, counter and sx,sy are start point of ball
	private int mb = 0,count=1,sx,sy;
	private int par,strokes=0;//par of course and normal number of strokes
	private String level,status="Game";//status used to go back to menu
	private String numLevel;//level number
	
	//a ball can go through an start portal and come out an end portal
	private ArrayList<Rectangle> sportals = new ArrayList<Rectangle>();//start portals 
	private ArrayList<Rectangle> eportals = new ArrayList<Rectangle>();//corresponding end portals
	
	private Rectangle reset = new Rectangle(524,619,100,20);//to reset ball button
	private Rectangle menu = new Rectangle(383,619,100,20);//go back to menu button
	//status pictures
	private Image resetp;
	private Image menup;
	private Image infobar;
	
    public Screen(String level) throws IOException{
    	super("Field.java");
    	
    	infile = new Scanner(new BufferedReader(new FileReader(level+".txt")));//open corresponding txt file
    	try{
    		//load images
    		infobar = ImageIO.read(new File("InfoBar.png"));
    		resetp = ImageIO.read(new File("ResetPress.png"));
    		menup = ImageIO.read(new File("MenuPressed.png"));
    		back = ImageIO.read(new File(level+".jpg"));
    		image = (BufferedImage)back;
    	}
    	catch(Exception e){
    		System.out.println("Opps.."+e);
    	}
    	load();//reads txt file and reads barriers
    	this.level=level;
    	
    	numLevel=level.substring(level.length()-1,level.length());//number is last in level
    	move=new Move("none",sx,sy);//start ball, none is direction of gravity will be changed later
    	if(level.equals("Level7")){//fixes little glitch
    		move.reset();
    	}
    	
    	setSize(800,667);
    	addMouseListener(this);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	setVisible(true);
    }

    public void load(){
    	while(infile.hasNextLine()){
    		String [] info = infile.nextLine().split(" ");
    		//the first thing in info is always the description
    		
    		if(info[0].equals("up")){//up is gravity direction
    			fields.add(new int [5]);
    			fields.get(fields.size()-1)[0]=0;//the 0 keeps track that this is up field
    			for(int i=1;i<5;i++){//add this to the arraylist
    				fields.get(fields.size()-1)[i]=Integer.parseInt(info[i]);
    			}
    		}
    		else if(info[0].equals("down")){
    			fields.add(new int [5]);
    			fields.get(fields.size()-1)[0]=1;//1 keeps track that this is down field
    			for(int i=1;i<5;i++){
    				fields.get(fields.size()-1)[i]=Integer.parseInt(info[i]);
    			}
    		}
    		else if(info[0].equals("left")){
    			fields.add(new int [5]);
    			fields.get(fields.size()-1)[0]=2;//2 keeps track that this is left field
    			for(int i=1;i<5;i++){
    				fields.get(fields.size()-1)[i]=Integer.parseInt(info[i]);
    			}
    		}
    		else if(info[0].equals("right")){
    			fields.add(new int [5]);
    			fields.get(fields.size()-1)[0]=3;//3 keeps track that this is right field
    			for(int i=1;i<5;i++){
    				fields.get(fields.size()-1)[i]=Integer.parseInt(info[i]);
    			}
    		}
    		else if(info[0].equals("end")){
    			//store end rectangle
    			end=new Rectangle(Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4]));
    		}
    		else if(info[0].equals("portal1")){
    			//start portals
    			sportals.add(new Rectangle(Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4])));
    		}
    		else if(info[0].equals("portal2")){
    			//corresponding end portals
    			eportals.add(new Rectangle(Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3]),Integer.parseInt(info[4])));
    		}
    		
    		else if(info[0].equals("start")){
    			//start point of ball
    			sx=Integer.parseInt(info[1]);
    			sy=Integer.parseInt(info[2]);
    		}
    		else if(info[0].equals("par")){
    			//par for this level
    			par=Integer.parseInt(info[1]);
    		}
    		else{
    			//this would be the block and line barriers
    			String key=getKey((int)Double.parseDouble(info[2])+imx, (int)Double.parseDouble(info[3])+imx);//key is x+"-"+y
				if(info[0].equals("rect")){
					//rect constructor
					points.put(key,new Barrier("rect",info[1],(int)Double.parseDouble(info[2]),(int)Double.parseDouble(info[3]),18,18,0.0,Double.parseDouble(info[6])));
				}
				else if(info[0].equals("line")){
					//line constructor
					points.put(key,new Barrier(null,"line",info[1],(int)Double.parseDouble(info[2]),(int)Double.parseDouble(info[3]),(int)Double.parseDouble(info[4]),(int)Double.parseDouble(info[5]),Double.parseDouble(info[6]),Double.parseDouble(info[7]),(int)Double.parseDouble(info[8]),(int)Double.parseDouble(info[9]),(int)Double.parseDouble(info[10]),(int)Double.parseDouble(info[11])));
				}
    		}
			
    	}
    }
    //paint paints the background, anyother features and gives graphics to other classes to draw
    public void paint(Graphics g){
    	
		g.drawImage(back,imx,imy,null);
		
    	move.moveStuff(mb,imx,imy,points,g,sportals,eportals);//move stuff moves the ball and checks for collisions
    	move.paint(g,imx,imy);//paints the ball
    	
    	g.drawImage(infobar,0,595,null);//bar under that keeps track of level, par, strokes
    	
    	if(menu.contains(cx,cy)&&clicked){//if user clicks menu button
    		g.drawImage(menup,383,619,null);//button pressed
    		clicked=false;
    		levelPlaying=false;//level stops
    		status="Nothing";//and since startgame becomes nothing the program goes to menu
    		
    	}
    	if(reset.contains(cx,cy)&&mb==1){//if reset
    		g.drawImage(resetp,524,619,null);//presed button photo
    		move.reset();//ball goes back to last launch positions
    	}
    	
    	Font font = new Font("Serif", Font.PLAIN, 50);
    	g.setFont(font);
    	strokes=move.numStrokes();
    	
    	if(strokes>par){//if strokes are above par
    		g.setColor(Color.RED);//red coloured number to warn user
    		g.drawString(Integer.toString(strokes),305,645);
    	}
    	else{
    		g.setColor(Color.BLUE);//otherwise normal blue numbers
    		g.drawString(Integer.toString(strokes),305,645);	
    	}
    	font = new Font("Serif", Font.PLAIN, 25);
    	g.setFont(font);
    	g.setColor(Color.BLUE);
    	g.drawString(Integer.toString(par),106,648);//par number display
    	g.drawString(numLevel,105,625);//Hole number display
    	
    	delay(20);

    }
   	public void update(){//double buffering
    	Graphics g = getGraphics();
    	if (dbImage==null){
    		dbImage = createImage(getWidth(),getHeight());
    		dbg = dbImage.getGraphics();
    	}

    	paint(dbg);
    	g.drawImage(dbImage,0,0,this);
    }
	
    
    public void mouseCheck(){
    	cx=MouseInfo.getPointerInfo().getLocation().x;//update mouse location
    	cy=MouseInfo.getPointerInfo().getLocation().y;

		
		boolean foundG=false;//keeps track of wether there is gravity here
		
		if(level.equals("Level7")){//level 2 has special graivty a round field
    		move.setG("round");
    		foundG=true;
    	}
    	else{
    		for(int [] field:fields){//otherwise we check if the ball is in any normal field
	    		Rectangle box = new Rectangle(field[1]+imx,field[2]+imy,field[3],field[4]);//new box to compensate for imx and imy
	    		if(box.contains(move.getX()+10+imx,move.getY()+10+imy)){//+10 is to check center of ball
	    			move.setG(gravityKey[field[0]]); 
	    			foundG=true;//ball is in a gravity field
	    			break;
	    		}
	    	}
    	}
    	
    	if(foundG==false){//if there is no gravity
    		move.setG("none");//gravity is none
    	}
    	
    
    	Rectangle newEnd = new Rectangle((int)end.getX()+imx,(int)end.getY()+imy,(int)end.getWidth(),(int)end.getHeight());//newEnd is the end rectangle with imx and imy compensation
    	
    	//ball is the ball rectangle plus imx and imy
  		Rectangle ball = new Rectangle((int)move.ballr().getX()+imx,(int)move.ballr().getY()+imy,(int)move.ballr().getWidth(),(int)move.ballr().getHeight());

    	if(newEnd.intersects(ball)){//if ball hits end
    		levelPlaying=false;//level is done
    	}
    		
						//800-1024=-224
    	if(cx>750&&imx>-224){//if mouse is near right end
    		imx-=5;//move everything to the left, so we can see the right
    	}
    	if(cx<50&&imx<0){//if mouse is near left
    		imx+=5;//move everything right to see the left
    	}
    							//600-768=-168
    	if(600>cy&&cy>550&&imy>-168){//set upperbound 600 so that user can access menu bar under 600
    		imy-=5;//move everything up, to see the bottom
    	}
    	if(cy<50&&imy<0){
    		imy+=5;//move everything down to see top
    	}
    	
    	//when we hit the ball the ball might go of screen so we have to compensate for that
    	if(move.checkStatus()){
    		if(move.getX()+imx+10>750&& move.getVX()>0  && imx>-224){//if ball is near right and moving right
    			//imx+ballx=750
    			//so:
    			imx=750-(move.getX()+10);
    		}
    		else if(move.getX()+imx+10<50&&move.getVX()<0&&imx<0){//if ball is near left and moving left
    			//imx+ballx=50
    			//so:
    			imx=50-(move.getX()+10);
    		}
    		if(move.getY()+imy+10>550 && move.getVY()>0  &&imy>-168){//if ball is near bottom and it is movind down
    			//imy+bally=550
    			//so:
    			imy=550-(move.getY()+10);
    		}
    		else if(move.getY()+imy+10<50 && move.getVY()<0 && imy<0){//if ball is near top and is moving up
    			//imy+bally=50
    			//so:
    			imy=50-(move.getY()+10);
    		}
    	}
    	

  
    	
    }
    public String status(){//status, wether we are in game or done
    	return status;
    }
    public String level(){//what level we are in
    	return level;
    }
    
    public String getKey(int x, int y){
    	return Integer.toString(x)+"-"+Integer.toString(y);//returns key (x+"-"+y) of HashMap
    }
    
    public boolean gamePlaying(){//wether the level is done or not
    	return levelPlaying;
    }

	//mouse interfaces
    public void mousePressed(MouseEvent e) {
    	cx=MouseInfo.getPointerInfo().getLocation().x;
    	cy=MouseInfo.getPointerInfo().getLocation().y;
   		mb=1;//mouse pressed
    }
    
    public void mouseReleased(MouseEvent e) {
       	mb=0;//released
    }

    public void mouseEntered(MouseEvent e) {
       	
    }

    public void mouseExited(MouseEvent e) {
      	
    }

    public void mouseClicked(MouseEvent e) {
       clicked=true;
    }
    public static void delay(int n){
		try{
			Thread.sleep(n);
		}
		catch(InterruptedException e){
			System.out.println("oops........ "+e);
		}
	}
    
}