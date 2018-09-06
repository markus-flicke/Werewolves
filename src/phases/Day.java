package phases;

import java.util.List;

import game.Poll;
import phases.result.PhaseResult;
import phases.result.PlayerKilledResult;
import phases.result.PlayerRevealedResult;
import phases.result.VoteFailedResult;
import roles.Player;
import roles.Player.PlayerStatus;
import roles.Seer;
import roles.Townie;
import roles.Werewolf;

import static roles.Seer.PlayerAlignment.OTHER;
import static roles.Seer.PlayerAlignment.UNKNOWN;
import static roles.Seer.PlayerAlignment.WEREWOLF;

public class Day extends Phase {

	public Day(List<Player> players) {
		super(players);
	}

	/* (non-Javadoc)
	 * @see phases.Phase#preparePlayers(java.util.List)
	 */
	@Override
	protected void preparePlayers(List<Player> players) {
		for (Player player : players) {
			if (player.isAlive()) {
				player.setStatus(PlayerStatus.AWAKE);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see phases.Phase#preparePoll(java.util.List)
	 */
	@Override
	protected Poll preparePoll(List<Player> players) {
		return new Poll(players, players);
	}

	/* (non-Javadoc)
	 * @see phases.Phase#concludePhase()
	 */
	@Override
	public PhaseResult concludePhase() {
		Player votedPlayer = poll.getVotedPlayer();
		if(poll.isConclusive()){
			votedPlayer.setStatus(PlayerStatus.DEAD);
			return new PlayerKilledResult(votedPlayer);
		}
		return new VoteFailedResult();
	}

	/* (non-Javadoc)
	 * @see phases.Phase#voteIsValid(roles.Player, roles.Player)
	 */
	@Override
	protected boolean voteIsValid(Player voter, Player target) {
		return true;
	}
}