
public class Genie extends Creature{
	Forces force;
	boolean positive;
	Genie(Member name,int positionX,int positionY) throws Exception{
		super(name,positionX,positionY);
		this.power=3;
		this.positive=true;
	}
}
class Snape extends Genie{
	Snape(Member name,int positionX,int positionY) throws Exception{
		super(name,positionX,positionY);
		this.power=1;
		this.positive=false;
	}
}
class Scorpion extends Genie{
	Scorpion(Member name,int positionX,int positionY) throws Exception{
		super(name,positionX,positionY);
		this.power=8;
		this.positive=true;
	}
}