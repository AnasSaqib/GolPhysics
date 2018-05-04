/*Vinay Jayachandiran and Anas Saqib
 *MiniPutt.java
 *Main class
 **/
 //http://ejocurimasini.com/golphysics.html
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class MiniPutt {
    public static void main(String [] args) throws IOException{
    	int count=4;
    	String startgame="Nothing";
		boolean done=false;
		String level="";
		while(true){
			while(level.equals("Level19")==false){
				level="Level"+Integer.toString(count);
				if(startgame.equals("Nothing")==false){
					Screen main = new Screen(level);  //Screen class is the class that deals with all graphics and movement of screen
					while(main.gamePlaying()){
		      			main.mouseCheck();     //identifies what user is doing
	    				main.update();
	    				startgame=main.status();
					}
					count++;
					delay(30);
					
				}
				else{
					introScreen intro=new introScreen();
					while(startgame.equals("Nothing")){
						intro.update();	
						startgame=intro.checkstatus();
						done=false;	
					}
					if(startgame.equals("Practice")){
						count=intro.level();
						System.out.println(count);
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
			System.out.println("oops........bitch "+e);
		}
	} 
}

//		
//Class for the intro screen
class introScreen extends JFrame implements MouseListener,KeyListener {
	private BufferedImage dbImage;
	private Graphics dbg;
	private Image options,arrow;
	private int mb = 0;
	private int mx,my,counter,level=0;
	private int[] arrowsx=new int[6];
	private int[] arrowsy=new int[3];
	private Rectangle fullcourse,practice,resume,inst;
	private Image hover,hover2,hover3,hover4;

	private boolean [] keys = new boolean[256];
    public introScreen(){
    	options = new ImageIcon("options.png").getImage();
    	arrow = new ImageIcon("arrow.png").getImage();
    	hover = new ImageIcon("Hover1.png").getImage();
    	hover2 = new ImageIcon("Hover2.png").getImage();
    	hover3 = new ImageIcon("Hover3.png").getImage();
    	hover4 = new ImageIcon("Hover4.png").getImage();
    	arrowsx[0]=46;
    	arrowsx[1]=197;
    	arrowsx[2]=348;
    	arrowsx[3]=499;
    	arrowsx[4]=650;
    	arrowsx[5]=801;
    	arrowsy[0]=30;
    	arrowsy[1]=220;
    	arrowsy[2]=410;
    	counter=0;
    	fullcourse =new Rectangle(280,150,235,55);
    	practice = new Rectangle(280,229,235,55);
    	resume = new Rectangle(280,310,235,55);
    	inst = new Rectangle(280,389,235,55);
    	setSize(800,667);
    	addMouseListener(this);
    	addKeyListener(this);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	setVisible(true);
    }
	public void update(){
    	Graphics g = getGraphics();
    	if (dbImage==null){
    		dbImage=new BufferedImage(getWidth(),getHeight(),1);
    		dbg = dbImage.getGraphics();
    	}
    	paint(dbg);
    	
    	g.drawImage(dbImage,0,0,this);
    }
    public void paint(Graphics g){
		if(practice.contains(mx,my)&&mb==1){
			g.setColor(Color.BLACK);
			g.fillRect(0,0,800,667);
			Image l1;
			Image l2;
			Image l3;
			try{
				l1 = new ImageIcon("Thumb Nails\\Level1Thumb.png").getImage();
				l2 = new ImageIcon("Thumb Nails\\Level2Thumb.png").getImage();
				l3 = new ImageIcon("Thumb Nails\\Level3Thumb.png").getImage();
				Image l4 = new ImageIcon("Thumb Nails\\Level4Thumb.png").getImage();
				g.setColor(Color.BLUE);
				g.drawImage(l1,100,100,null);
				g.drawString("1",100,90);
				g.drawImage(l2,325,100,null);
				g.drawString("2",325,90);
				g.drawImage(l3,550,100,null);
				g.drawString("3",559,90);
				g.drawImage(l4,100,300,null);
				g.drawString("4",100,290);
				//g.drawImage
				
			}
			catch(Exception e){
				System.out.println(e);
			}
			
			
		}
		else{
			g.setColor(Color.BLACK);
			g.fillRect(0,0,800,667);
	    	for(int i=0;i<arrowsx.length;i++){
	    		if(i==1||i==3||i==5){
	    			g.drawImage(arrow,arrowsx[i],arrowsy[0],this);
	    			g.drawImage(arrow,arrowsx[i],arrowsy[2],this);
	    		}
	    		else if(i==0||i==2||i==4){
	    			g.drawImage(arrow,arrowsx[i],arrowsy[1],this);
	    		}
	    		if(counter%15==0){
	    			arrowsx[i]-=1;
	    		}
	    		if (arrowsx[i]+105<=0){
	    			arrowsx[i]=800;
	    		}
	    	}
	    
	    	counter+=1;
	    	g.drawImage(options,280,150,this);
	    	mx=MouseInfo.getPointerInfo().getLocation().x;
    		my=MouseInfo.getPointerInfo().getLocation().y;
	    	if(fullcourse.contains(mx,my)){
	    		g.drawImage(hover,(int)fullcourse.getX(),(int)fullcourse.getY(),null);
	    	}
	    	else if(practice.contains(mx,my)){
	    		g.drawImage(hover2,(int)practice.getX(),(int)practice.getY(),null);
	    	}
	    	else if(resume.contains(mx,my)){
	    		g.drawImage(hover3,(int)resume.getX(),(int)resume.getY(),null);
	    	}
	    	else if(inst.contains(mx,my)){
	    		g.drawImage(hover4,(int)inst.getX(),(int)inst.getY(),null);
	    	}
		}
    }
	public String checkstatus(){
		if (fullcourse.contains(mx,my)&&mb==1){
			return "FullCourse";
		}
		else if(practice.contains(mx,my)&&mb==1){
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
    	System.out.println(mx+", "+my);
   		mb=1;
    }
    public void mouseReleased(MouseEvent e) {
       	mb=0;
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public static void delay(int n){
		try{
			Thread.sleep(n);
		}
		catch(InterruptedException e){
			System.out.println("oops........ "+e);
		}
	}
	public void keyTyped(KeyEvent e){
    	
    }
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;//sets the spot of the key pressed true
    	level=e.getKeyCode()-48;
    	//System.out.println(e.getKeyCode());
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;//when the key is released we set that to false
    }
    
}