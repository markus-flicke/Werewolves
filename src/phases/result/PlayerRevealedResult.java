package phases.result;

import roles.Player;
import roles.Seer.PlayerAlignment;

public class PlayerRevealedResult implements PhaseResult {
	
	private Player player;
	private PlayerAlignment playerAlignment;
	
	public PlayerRevealedResult(Player player, PlayerAlignment playerAlignment) {
		this.player = player;
		this.playerAlignment = playerAlignment;
	}

	/* (non-Javadoc)
	 * @see phases.result.PhaseResult#getMessage()
	 */
	@Override
	public String getMessage() {
		System.out.println(player.getName());
		System.out.println(playerAlignment);
		System.out.println(PlayerAlignment.WEREWOLF);
		return "Revealed " + player.getName() + "'s alignment. They are " + (playerAlignment == PlayerAlignment.WEREWOLF ? "" : "NOT") + " a werewolf.";
	}

}
