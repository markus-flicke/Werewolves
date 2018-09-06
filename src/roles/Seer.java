package roles;

import static roles.Seer.PlayerAlignment.OTHER;
import static roles.Seer.PlayerAlignment.UNKNOWN;
import static roles.Seer.PlayerAlignment.WEREWOLF;

public class Seer extends Townie {
	
	public Seer(String name) {
		super(name);
	}
	
	/**
	 * Returns the target player's alignment.
	 * @param player the target player.
	 * @return PlayerAlignment.WEREWOLF if the player is a werewolf,<br>PlayerAlignment.OTHER otherwise.
	 */
	public PlayerAlignment revealPlayerAlignment(Player player) {
	    if(!this.isAlive()){
	        return UNKNOWN;
        }
		if(player instanceof Werewolf){
		    return WEREWOLF;
        }
		return OTHER;
	}
		
	public enum PlayerAlignment {
		WEREWOLF,
		OTHER,
		UNKNOWN
	}

}
