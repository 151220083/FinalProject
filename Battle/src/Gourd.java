public class Gourd extends Creature{
	boolean positive;
	Gourd(Member name,int positionX,int positionY) throws Exception{
		super(name,positionX,positionY);
		this.power=5;
		this.positive=true;
	}
}
class GrandPa extends Gourd{
	 GrandPa(Member name,int positionX,int positionY) throws Exception{
		super(name,positionX,positionY);
		this.power=0;
		this.positive=false;
	}
}