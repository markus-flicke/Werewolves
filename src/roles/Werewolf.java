package roles;

import java.util.List;

public class Werewolf extends Player {
	
		
	
	public Werewolf(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see roles.Player#hasWon(java.util.List)
	 */
	@Override
	public boolean hasWon(List<Player> players) {
		int playerCount = 0;
		int wolfCount = 0;
		for(Player p: players){
			if(p.isAlive()){
				if(p instanceof Townie){
					playerCount++;
				}else{
					wolfCount++;
				}
			}
		}
		return playerCount <= wolfCount;
	}

}
