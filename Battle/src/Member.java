import javafx.scene.image.Image;
enum Forces{
	HUMAN,GENIE,
}
public enum Member {//banding the members with their own icon and force
	RED("source/1.png","source/1d.png",Forces.HUMAN), 
	ORANGE("source/2.png","source/2d.png",Forces.HUMAN),
	YELLOW("source/3.png","source/3d.png",Forces.HUMAN),
	GREEN("source/4.png","source/4d.png",Forces.HUMAN),
	CYAN("source/5.png","source/5d.png",Forces.HUMAN),
	BLUE("source/6.png","source/6d.png",Forces.HUMAN),
	PURPLE("source/7.png","source/7d.png",Forces.HUMAN),
	GRANDFATHER("source/grandpa.png","source/grandpad.png",Forces.HUMAN),
	SCORPION("source/sco.png","source/scod.png",Forces.GENIE),
	SNAKE("source/snake.png","source/snaked.png",Forces.GENIE),
	MONSTER("source/mon.png","source/mond.png",Forces.GENIE);
	
	Member(String route1,String route2,Forces force){
		this.force=force;
		this.image=new Image(route1,60,60,false,true);
		this.dimage=new Image(route2,60,60,false,true);
	}
	
	Image getImage() {
		return image;
	}
	
	Image getDImage() {
		return dimage;
	}
	
	Forces getForces() {
		return force;
	}
	
	private Forces force; 
	
	private Image image,dimage;
}
