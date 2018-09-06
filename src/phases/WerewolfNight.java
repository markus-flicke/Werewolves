package phases;

import java.util.ArrayList;
import java.util.List;

import game.Poll;
import phases.result.PhaseResult;
import phases.result.PlayerKilledResult;
import phases.result.PlayerRevealedResult;
import phases.result.VoteFailedResult;
import roles.Player;
import roles.Seer;
import roles.Townie;
import roles.Werewolf;

import static roles.Player.PlayerStatus.ASLEEP;
import static roles.Player.PlayerStatus.AWAKE;
import static roles.Player.PlayerStatus.DEAD;
import static roles.Seer.PlayerAlignment.OTHER;
import static roles.Seer.PlayerAlignment.UNKNOWN;
import static roles.Seer.PlayerAlignment.WEREWOLF;

public class WerewolfNight extends Phase {

	public WerewolfNight(List<Player> players) {
		super(players);
	}

	/* (non-Javadoc)
	 * @see phases.Phase#preparePlayers(java.util.List)
	 */
	@Override
	protected void preparePlayers(List<Player> players) {
		for(Player p: players){
			if(p.getStatus().equals(DEAD)){
				continue;
			}
			if(p instanceof Werewolf){
				p.setStatus(AWAKE);
				continue;
			}
			p.setStatus(ASLEEP);
		}
	}

	/* (non-Javadoc)
	 * @see phases.Phase#concludePhase()
	 */
	@Override
	public PhaseResult concludePhase() {
		Player votedPlayer = poll.getVotedPlayer();
		if(poll.isConclusive()){
		    votedPlayer.setStatus(DEAD);
			return new PlayerKilledResult(votedPlayer);
		}
		return new VoteFailedResult();
	}

	/* (non-Javadoc)
	 * @see phases.Phase#preparePoll(java.util.List)
	 */
	@Override
	protected Poll preparePoll(List<Player> players) {
		List<Player> voters = new ArrayList<>();
		List<Player> nominees = new ArrayList<>();

		for(Player p: players){
			if(p.isAlive() && p instanceof Werewolf){
				voters.add(p);
			}else if(p.isAlive()){
				nominees.add(p);
			}
		}
		return new Poll(voters, nominees);
	}
	
	/* (non-Javadoc)
	 * @see phases.Phase#voteIsValid(roles.Player, roles.Player)
	 */
	@Override
	protected boolean voteIsValid(Player voter, Player target) {
		return voter.isAlive() && voter instanceof Werewolf && target.isAlive();
	}
}
