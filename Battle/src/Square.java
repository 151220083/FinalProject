import java.util.HashSet;
import java.util.Set;

public class Square implements SquareMode{
	
	static private Member[][]members;
	
	static private Set<Creature>creatures;
		
	Square(){
		creatures=new HashSet<>();
		this.members=new Member[N][M];
		for(int i = 0;i<N;i++) {
			members[i]=new Member[M];
		}
		System.out.println("square create ok");			//create the square
	}
	
	Member getMember(int positionX,int positionY) {
		return members[positionY][positionX];
	}
	
	boolean vacant(int positionX,int positionY) {
		return members[positionY][positionX]==null;
	}
	
	private void markCreature(Creature creature) {
		synchronized(Square.class) {
		creatures.add(creature);
		}
	}
	
	void Moveto(Creature creature,int targetX,int targetY) {
		leave(creature);
		members[targetY][targetX]=creature.name;
	}
	
	void locate(Creature creature,int targetX,int targetY) {	
		members[targetY][targetX]=creature.name;
		markCreature(creature);
	}
	
	void leave(Creature creature) {
		int x=creature.getPositionX(),y=creature.getPositionY();
		members[y][x]=null;
	}
	
	static synchronized void recordDie(Creature creature) {
		System.out.println(creature.name+" die at "+creature.getPositionX()+" "+creature.getPositionY());
		if(creature.name.getForces()==Forces.HUMAN)
			Creature.humanNum--;
		else
			Creature.genieNum--;
		synchronized(Square.class) {
		creatures.remove(creature);
		}
	}
	
	boolean outSquare(int positionX,int positionY) {
		return (positionX>=M)||(positionY>=N);
	}
	
	Creature getCreature(int positionX,int positionY) {
		synchronized(Square.class) {
			for(Creature m:creatures) {
				if(m!=null) {
					if(m.getPositionX()==positionX&&m.getPositionY()==positionY) {
							return m;
					}
				}
			}
		}
		return null;
	}
}
