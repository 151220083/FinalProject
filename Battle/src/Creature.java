
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Coordinate{
	private int x;
	private int y;
	
	Coordinate(int positionX,int positionY){
		this.x=positionX;
		this.y=positionY;
	}
	
	Coordinate(Coordinate position){
		this.x=position.x;
		this.y=position.y;
	}
	
	int getX() {
		return x;
	}
	
	int getY() {
		return y;
	}
	
	void setX(int positionX) {
		x=positionX;
	}
	
	void setY(int positionY){
		y=positionY;
	}
	
}
public class Creature extends Main implements Runnable{
	static ExecutorService exec;
	protected int power=0;
	static Square square=new Square();
	static boolean prepared=false;
	static int humanNum,genieNum;
	boolean isdead=false;
	Member name;
	ImageView imageView;
	Coordinate position;
	Coordinate target;
	
	Creature(Member name,int positionX,int positionY) throws Exception{
		this.name=name;
		this.imageView=new ImageView(name.getImage());
		if(targetVacant(positionX, positionY)) {
			locate(positionX,positionY);
		}
		else {
			System.out.println(name+" Locate fail");
			throw new Exception("Add wrong:overloap");
		}
	}
	
	int getPositionX() {
		int m = position.getX();
		return m;
	}
	
	int getPositionY() {
		return position.getY();
	}
	
	Coordinate getCoordinate(){
		return position;
	}
	
	boolean targetVacant(Coordinate positionXY) {
		return square.vacant(positionXY.getX(), positionXY.getY());
	}
	
	boolean targetVacant(int positionX,int positionY) {
		return square.vacant(positionX, positionY);
	}
	
	void moveTo(int targetX,int targetY) {
		int x =this.getPositionX(),y=this.getPositionY();
		int index = targetY*M+targetX;
		Platform.runLater(()-> {
			flowPane.getChildren().remove(y*M+x);
			flowPane.getChildren().add(y*M+x, new ImageView(new Image("source/blank.png",60,60,false,true)));
			flowPane.getChildren().remove(index);
			flowPane.getChildren().add(index, imageView);
		});
		square.Moveto(this, targetX, targetY);
		position.setX(targetX);
		position.setY(targetY);	
	}
	
	void locate(int targetX,int targetY) {
		position = new Coordinate(targetX,targetY);
		int index = targetY*M+targetX;
		Platform.runLater(() ->{
				flowPane.getChildren().remove(index);
				flowPane.getChildren().add(index, imageView);
		});
		square.locate(this, targetX, targetY);
	}
	
	void leave() {
		int x=getPositionX(),y=getPositionY();
		Platform.runLater(()-> {
				flowPane.getChildren().remove(y*N+x);
				flowPane.getChildren().add(y*N+x, new ImageView(new Image("source/blank.png",60,60,false,true)));
		});
		square.leave(this);
	}

	void die() throws InterruptedException {
		int x=getPositionX(),y=getPositionY();
		this.isdead=true;
		Platform.runLater(()-> {
			flowPane.getChildren().remove(y*M+x);
			flowPane.getChildren().add(y*M+x, new ImageView(name.getDImage()));
			System.out.println(this.name+" die at "+x+","+y);
		});
		square.recordDie(this);
		Thread.interrupted();
		throw new InterruptedException("dead");

	}
	
	Creature attack(){
		int x = this.position.getX();
		int y = this.position.getY();
		Forces force=name.getForces();
		
		if((y-1>=0)&&square.getMember(x, y-1)!=null){
			if(square.getMember(x, y-1).getForces()!=force) {
				System.out.println(name+"fight "+x+","+(y-1)+":"+square.getMember(x, y-1));				
				Creature enemy=square.getCreature(x, y-1);
				if(enemy!=null&&enemy.alive()) {
				return enemy;}
			}
		}
		
		if((x-1>=0)&&square.getMember(x-1, y)!=null){
			if(square.getMember(x-1, y).getForces()!=force) {
				Creature enemy=square.getCreature(x-1, y);
				if(enemy!=null&&enemy.alive()) {
					return enemy;}
			}
		}
		
		if((y+1<N)&&square.getMember(x, y+1)!=null){
			if(square.getMember(x, y+1).getForces()!=force) {
				Creature enemy=square.getCreature(x, y+1);
				if(enemy!=null&&enemy.alive()) {
				return enemy;}
			}
		}
		
		if((x+1<M)&&square.getMember(x+1, y)!=null){
			if(square.getMember(x+1, y).getForces()!=force) {
				Creature enemy=square.getCreature(x+1, y);
				if((enemy!=null)&&enemy.alive()) {
					return enemy;
				}
			}
		}
		return null;
	}
	
	boolean alive() {
		System.out.println(this.name+" "+this.isdead);
		return !isdead;
	}
	
	static boolean fightResult(Creature player1,Creature player2) {
		double m = player1.power+player2.power;
		double n = Math.random();
		m=player1.power/m;

		if(n<=m) 
			return true;
		else
			return false;
		
	}
	
	private boolean change(Coordinate targetPosition) throws InterruptedException {
		synchronized(Creature.class) {
    		if(genieNum==0||humanNum==0) {
        		if(genieNum==0){
        			System.out.println("Gourds win");
        			Thread.interrupted();
        			throw new InterruptedException();
        		}
        		if(humanNum==0){
        			System.out.println("Gourds lose");
        			Thread.interrupted();
        			throw new InterruptedException();
        		}
    		}
			if(!prepared)
				return false;
			Creature aim=attack();
			if(aim!=null) {
				if (fightResult(this,aim)) {
					if(!aim.isdead) {
					System.out.println(this.name+" beats "+aim.name);
					aim.die();}
				}
				else {
					if(!this.isdead) {
					System.out.println(aim.name+" beats "+this.name);
					this.die();}
				}
				return true;
			}
			else {				if(targetPosition==null) 
					return false;
				else if(targetVacant(targetPosition)) {
					TimeUnit.MILLISECONDS.sleep(100);
					moveTo(targetPosition.getX(),targetPosition.getY());
					return true;	
				}
			}
			
			return false;
		}
	}
	
	private Coordinate findTarget() {//find way
		synchronized(Square.class) {
		int x=this.getPositionX(),x0=x-1,x1=x+1;
		int y=this.getPositionY(),y0=y-1,y1=y+1;
		double value[]=new double[5];
		value[1]=-99;
		value[2]=-99;
		value[3]=-99;
		value[4]=-99;
		if(x0>=0){
			if(square.vacant(x0, y))
				value[1]=0;
		}
		if(x1<M) {
			if(square.vacant(x1, y))
				value[2]=0;
		}
		if(y0>=0){
			if(square.vacant(x, y0))
				value[3]=0;
		}
		if(y1<N) {
			if(square.vacant(x, y1))
				value[4]=0;
		}
		for(int j = 0;j<N;j++) {		
			for(int i = 0;i<M;i++) {
				Creature pos=square.getCreature(i, j);
				if(pos!=null) {
					if(square.getMember(i, j).getForces()!=this.name.getForces()) {
						if(value[1]>=0.0) 
							value[1]+=100.0/((i-x0)*(i-x0)+(j-y)*(j-y));
 
						if(value[2]>=0.0)
							value[2]+=100.0/((i-x1)*(i-x1)+(j-y)*(j-y));

						if(value[3]>=0.0)
							value[3]+=100.0/((i-x)*(i-x)+(j-y0)*(j-y0));

						if(value[4]>=0.0){
							value[4]+=100.0/((i-x)*(i-x)+(j-y1)*(j-y1));
						}
					}
				}
			}
		}
		
		int best=1;
		double max;
		if(name.getForces()==Forces.HUMAN) {
			max=value[2];
			best=2;
			}
		else {
			max=value[1];
			best=1;
			}
		
		for(int i = 1;i<5;i++) {
			if(value[i]>max) {
				max=value[i];
//				secbest=best;
				best=i;
			}
		}
		if(best==0) {
			return null;//TODO:—∞’“µ–»À∑ΩœÚ£ø
		}
		else if (best==1){
			return new Coordinate(x0,y);
		}
		else if(best==2) {
			return new Coordinate(x1,y);
		}
		else if(best==3) {
			return new Coordinate(x,y0);
		}
		else {
			return new Coordinate(x,y1);
		}
//		return new Coordinate(x+1,y+1);
		}
}

	public void run(){
        try {
    		TimeUnit.MILLISECONDS.sleep(50);
			while (!Thread.interrupted()&&!isdead) {
            	target=findTarget();
            	while(change(target)==false);
        		TimeUnit.MILLISECONDS.sleep(400);
        		if(genieNum==0){
        			System.out.println("Gourds win");
        			Thread.interrupted();
        		}
        		if(humanNum==0){
        			System.out.println("Gourds lose");
        			Thread.interrupted();
        		}
            }
        } catch (InterruptedException e) {
        	System.out.println(name+"'s battle finished");
        }
    }
	
	static void startBattle() {
		prepared=true;
	}
	static void prepare() throws Exception {///prepare for the battle
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Gourd(Member.RED,2,1));
		exec.execute(new Gourd(Member.ORANGE,3,2));
		exec.execute(new Gourd(Member.YELLOW,2,3));
		exec.execute(new Gourd(Member.GREEN,3,4));
		exec.execute(new Gourd(Member.CYAN,2,5));
		exec.execute(new Gourd(Member.BLUE,3,6));
		exec.execute(new Gourd(Member.PURPLE,2,7));
		exec.execute(new Gourd(Member.GRANDFATHER,3,8));
		humanNum=8;
		genieNum=7;
		//exec.execute(new Genie(Member.MONSTER,6,5));
		exec.execute(new Genie(Member.MONSTER,5,8));
		exec.execute(new Genie(Member.MONSTER,5,2));
		exec.execute(new Genie(Member.MONSTER,6,7));
		exec.execute(new Genie(Member.MONSTER,6,3));
		exec.execute(new Genie(Member.MONSTER,7,4));
		exec.execute(new Genie(Member.SCORPION,7,6));
		exec.execute(new Genie(Member.SNAKE,8,5));
	}
}
