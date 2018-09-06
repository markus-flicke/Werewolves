package roles;

import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

public abstract class Player {
	
	private PlayerStatus status;
	private String name;
	
	public Player(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the status of this player.
	 * @return the status.
	 */
	public PlayerStatus getStatus() {
		return status; 
	}
	
	/**
	 * Sets the status of this player.
	 * @param status the status.
	 */
	public void setStatus(PlayerStatus status) {
		this.status = status; 
	}
	
	/**
	 * Describes the in-game status of a player.∂
	 */
	public enum PlayerStatus {
		DEAD,
		ASLEEP,
		AWAKE
	}
	
	/**
	 * Returns whether this player is alive, or not.
	 * @return true, if this player is alive, false otherwise.
	 */
	public boolean isAlive() {
		return this.status != PlayerStatus.DEAD;
	}
	
	/**
	 * Kills this player.
	 */
	public void kill() {
		setStatus(PlayerStatus.DEAD);
	}
	
	/**
	 * Returns the name of this player.
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Indicates, whether a given player meets their win condition or not.
	 * @param players the player.
	 * @return true, if the player meets their win condition, false otherwise.
	 */
	public abstract boolean hasWon(List<Player> players);

}
