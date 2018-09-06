package phases;

import java.util.List;
import java.util.stream.Collectors;

import game.Poll;
import phases.result.PhaseResult;
import roles.Player;

public abstract class Phase {

	protected List<Player> players;
	protected Poll poll;

	public Phase(List<Player> players) {
		this.players = players;
	}

	/**
	 * Prepares this phase for execution.
	 */
	public void preparePhase() {
		this.poll = preparePoll(players);
		preparePlayers(players);
	}

	
	/**
	 * Registers a vote of player.
	 * @param voter the voting player.
	 * @param target the target player of the vote.
	 */
	public void registerVote(Player voter, Player target) {
		if (voter.isAlive() && target.isAlive() && voteIsValid(voter, target)) {
			poll.registerVote(voter, target);
		}
	}

	/**
	 * Prepares the player for the execution of this phase.
	 * @param players the players partaking in this phase.
	 */
	protected abstract void preparePlayers(List<Player> players);

	/**
	 * Prepares a poll for the execution of this phase.
	 * @param players the players partaking in this phase.
	 * @return the poll to handle the votes of this phase.
	 */
	protected abstract Poll preparePoll(List<Player> players);
	
	/**
	 * Determines whether a vote is valid or not.
	 * @param voter the voting player.
	 * @param nominee the target player of the vote.
	 * @return true, if the vote is valid, false otherwise.
	 */
	protected abstract boolean voteIsValid(Player voter, Player nominee);

	/**
	 * Concludes this phase and returns an according PhaseResult.
	 * @return the result of this phase.
	 */
	public abstract PhaseResult concludePhase();

	/**
	 * Returns a list of players that fulfill their win conditions.
	 * @return the list of players that have won.
	 */
	public List<Player> getWinningPlayers() {
		return players.stream().filter(player -> player.hasWon(players)).collect(Collectors.toList());
	}
}
