/*Anas Saqib and Vinay Jayachandiran
 *MiniPutt.java
 *"Welcome to Golf...in the Future! Walls with variable bounciness, changing gravities, and much more superior technology render this golf game enjoyable and fun to play!
 *The objective of this game is to get through all seven levels in as less strokes as possible. You can practice your strokes in the practice mode or challenge yourself in the full game."
 *So Basically this is just another type of golf, except it is a side view. Also gravity can influence you from any side, up, left, right or down. This is just
 *the main class that class all the other ones. There are 7 levels in this game.
 **/

import java.io.*; //imports
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class Golphysics {
    public static void main(String [] args) throws IOException{
    	int count=1; //keeps track of which level to play
    	PrintWriter outfile = new PrintWriter(new FileWriter("Resume.txt"));
    	String startgame="Nothing";
		boolean done=false;
		String level=""; //keeps track of the levels' name
		while(true){
			while(level.equals("Level2")==false){ //games keeps going untill it becomes the eight level
				level="Level"+Integer.toString(count); //defines the level's name
				if(startgame.equals("Nothing")==false&&level.equals("Level2")==false){
					Screen main = new Screen(level);  //Screen class is the class that deals with all graphics and movement of screen
					while(main.gamePlaying()){
		      			main.mouseCheck();     //identifies what user is doing
	    				main.update();
	    				startgame=main.status();
					}
					outfile.println(count);
					count++;
					delay(200);
					
				}
				else{
					IntroScreen intro=new IntroScreen();
					while(startgame.equals("Nothing")){ //if an options hasnt been chosen
						intro.update();	
						startgame=intro.checkstatus(); //checks if an option has been chosen
						done=false;	
					}
					if(startgame.equals("Practice")){ //if practice gets chosen
						count=intro.level();
					}		
				}

			}
		}
		

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

/*IntroScreen.java
 *Vinay Jayachandiran and Anas Saqib
 *This class just does all the intro. You can look at the instructions, pick a level to start or play the whole course. 
*/		

class IntroScreen extends JFrame implements MouseListener,KeyListener {
	private BufferedImage dbImage; //imports
	private Graphics dbg; //variables
	private Image options,arrow;
	private int mb = 0;
	private int mx,my,counter,level=0; 
	private int[] arrowsx=new int[6]; //array for the x values of the arrow in the background of the intro screen
	private int[] arrowsy=new int[3]; //array for the x values of the arrow in the background of the intro screen
	private Rectangle fullcourse,practice,inst; //ractangles for the option buttons
	private Image hover,hover2,hover3,hover4; //images to highlight the button if the mouse is over it
	private Image instructions;
	private boolean [] keys = new boolean[256];
    public IntroScreen(){
    	options = new ImageIcon("options.png").getImage();
    	arrow = new ImageIcon("arrow.png").getImage(); //arrow in the background
    	hover = new ImageIcon("Hover1.png").getImage(); 
    	hover2 = new ImageIcon("Hover2.png").getImage();
    	hover4 = new ImageIcon("Hover4.png").getImage();
    	instructions = new ImageIcon("instructions.png").getImage();
		
    	arrowsx[0]=46; //the x coor values for where the arrows need to be drawn
    	arrowsx[1]=197;
    	arrowsx[2]=348;
    	arrowsx[3]=499;
    	arrowsx[4]=650;
    	arrowsx[5]=801;
    	arrowsy[0]=30; //the y coor values where the arrows need to be drawn
    	arrowsy[1]=220;
    	arrowsy[2]=410;
    	counter=0; //keeps track of when to move the arrows
    	fullcourse =new Rectangle(280,150,235,55); //buttons for the four options
    	practice = new Rectangle(280,229,235,55);
    	inst = new Rectangle(280,310,235,55);

    	setSize(800,667);
    	addMouseListener(this);
    	addKeyListener(this);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	setVisible(true);
    }
	public void update(){ //update method
    	Graphics g = getGraphics();
    	if (dbImage==null){
    		dbImage=new BufferedImage(getWidth(),getHeight(),1);
    		dbg = dbImage.getGraphics();
    	}
    	paint(dbg); //calls the paint
    	g.drawImage(dbImage,0,0,this);
    }
    public void paint(Graphics g){
		if(practice.contains(mx,my)&&mb==1){ //if practice is presed
			g.setColor(Color.BLACK);
			g.fillRect(0,0,800,667);
			Image l1;
			Image l2;
			Image l3;
			Image l4;
			Image l5;
			Image l6;
			Image l7;
			try{
				l1 = new ImageIcon("Thumb Nails\\Level1Thumb.png").getImage(); //thumbnails of the levels, so the user can pick
				l2 = new ImageIcon("Thumb Nails\\Level2Thumb.png").getImage();
				l3 = new ImageIcon("Thumb Nails\\Level3Thumb.png").getImage();
				l4 = new ImageIcon("Thumb Nails\\Level4Thumb.png").getImage();
				l5 = new ImageIcon("Thumb Nails\\Level5Thumb.png").getImage();
				l6 = new ImageIcon("Thumb Nails\\Level6Thumb.png").getImage();
				l7 = new ImageIcon("Thumb Nails\\Level7Thumb.png").getImage();
				g.setColor(Color.BLUE); //draws the thumbnails
				g.drawImage(l1,100,100,null); 
				g.drawString("1",100,90);
				g.drawImage(l2,325,100,null);
				g.drawString("2",325,90);
				g.drawImage(l3,550,100,null);
				g.drawString("3",559,90);
				g.drawImage(l4,100,300,null);
				g.drawString("4",100,290);
				g.drawImage(l5,325,300,null);
				g.drawString("5",325,290);
				g.drawImage(l6,550,300,null);
				g.drawString("6",550,290);
				g.drawImage(l7,100,500,null);
				g.drawString("7",100,490);
				g.drawString("Press Level Number You Want To Try.",400,550);
				
				//g.drawImage
				
			}
			catch(Exception e){
				System.out.println(e);
			}
			
			
		}
		else{
			g.setColor(Color.BLACK);
			g.fillRect(0,0,800,667); //draws the black background
	    	for(int i=0;i<arrowsx.length;i++){
	    		if(i==1||i==3||i==5){
	    			g.drawImage(arrow,arrowsx[i],arrowsy[0],this); //draws 6 of the arrowsw
	    			g.drawImage(arrow,arrowsx[i],arrowsy[2],this);
	    		}
	    		else if(i==0||i==2||i==4){ //draws the other three arrows
	    			g.drawImage(arrow,arrowsx[i],arrowsy[1],this);
	    		}
	    		if(counter%15==0){ //makes the arrows move to the left
	    			arrowsx[i]-=1;
	    		}
	    		if (arrowsx[i]+105<=0){ //if the arrows leave the screen they appear on the right side
	    			arrowsx[i]=800;
	    		}
	    	}
	    	counter+=1;
	    	g.drawImage(options,280,150,this); //draws the options buttons
	    	mx=MouseInfo.getPointerInfo().getLocation().x;
    		my=MouseInfo.getPointerInfo().getLocation().y;
	    	if(fullcourse.contains(mx,my)){ //if mouse is over fullcourse option
	    		g.drawImage(hover,(int)fullcourse.getX(),(int)fullcourse.getY(),null);
	    	}
	    	else if(practice.contains(mx,my)){ //if mouse is over practice option
	    		g.drawImage(hover2,(int)practice.getX(),(int)practice.getY(),null);
	    		
	    	}

	    	else if(inst.contains(mx,my)){ //if mouse is over instructions option
	    		g.drawImage(hover4,(int)inst.getX(),(int)inst.getY(),null);
	    		g.drawImage(instructions,193,30,null);
	    	}
		}
    }
	public String checkstatus(){
		if (fullcourse.contains(mx,my)&&mb==1){ //if fullcourse is chosen
			return "FullCourse";
		}
		else if(practice.contains(mx,my)&&mb==1){ //if practice is chosen
			update();
			while(level==0){
			}
			return "Practice";
		}

		return "Nothing";

	}
	public int level(){
		return level;
	}
    public void mousePressed(MouseEvent e) {
    	mx=MouseInfo.getPointerInfo().getLocation().x;
    	my=MouseInfo.getPointerInfo().getLocation().y;
   		mb=1;
    }
    public void mouseReleased(MouseEvent e) {
       	mb=0;
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public static void delay(int n){ //dealy method
		try{
			Thread.sleep(n);
		}
		catch(InterruptedException e){
			System.out.println("oops........ "+e);
		}
	}
	public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;//sets the spot of the key pressed true
    	level=e.getKeyCode()-48;
    	//System.out.println(e.getKeyCode());
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;//when the key is released we set that to false
    }
    
}