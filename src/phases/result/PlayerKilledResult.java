package phases.result;

import roles.Player;

public class PlayerKilledResult implements PhaseResult {
	
	private Player player;
	
	public PlayerKilledResult(Player player) {
		this.player = player;
	}

	/* (non-Javadoc)
	 * @see phases.result.PhaseResult#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Killed player " + player.getName() + ". They were a " + player.getClass().getSimpleName() + ".";
	}
	
	/**
	 * Returns the player that was killed.
	 * @return the player.
	 */
	public Player getKilledPlayer() {
		return player;
	}

}
