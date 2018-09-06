package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import game.io.WerewolfIOHandler;
import game.notification.PhaseConcludedNotification;
import game.notification.PhasePreparedNotification;
import game.notification.VoteCompletedNotification;
import phases.Phase;
import phases.result.PhaseResult;
import roles.Player;

/**
 * @author ruby
 *
 */
public class Game {

	private List<Player> players;
	private Phase[] phases;
	private Phase currentPhase;

	private WerewolfIOHandler ioHandler;

	protected Game(List<Player> players, Phase[] phases, WerewolfIOHandler ioHandler) {
		this.players = players;
		this.phases = phases;
		this.ioHandler = ioHandler;
	}

	/**
	 * Runs the game until a winner can be determined.
	 * 
	 * @return the list of winning players.
	 */
	public List<Player> runGame() {
		int index = 0;

		do {
			currentPhase = phases[index];

			currentPhase.preparePhase();
			ioHandler.handleNotification(new PhasePreparedNotification(currentPhase));

			Map<Player, Player> votes = convertToPlayerVoteMap(ioHandler.gatherVotes());
			for (Entry<Player, Player> playerVoteEntry : votes.entrySet()) {
				currentPhase.registerVote(playerVoteEntry.getKey(), playerVoteEntry.getValue());
			}
			ioHandler.handleNotification(new VoteCompletedNotification(currentPhase));

			PhaseResult phaseResult = currentPhase.concludePhase();
			ioHandler.handleNotification(new PhaseConcludedNotification(currentPhase, phaseResult));

			index = (++index % phases.length);
		} while (currentPhase.getWinningPlayers().isEmpty());

		return currentPhase.getWinningPlayers();
	}

	/**
	 * Returns a copy of the list of players.
	 * 
	 * @return the copy of the list of players.
	 */
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}

	/**
	 * Returns the player object associated with a given username.
	 * 
	 * @param userName
	 *            the username.
	 * @return the player object associated with the given username,
	 */
	public Optional<Player> convertStringToPlayer(String userName) {
		Optional<Player> pl = players.stream().filter(p -> p.getName().equals(userName)).findFirst();
		return pl;
	}

	/**
	 * Converts a map of votes by usernames to a map of votes by player objects.
	 * 
	 * @param votes
	 *            a map of string votes.
	 * @return a map of the player objects associated with the votes by usernames.
	 */
	private Map<Player, Player> convertToPlayerVoteMap(Map<String, String> votes) {
		Map<Player, Player> result = new HashMap<Player, Player>();

		if (votes != null) {
			for (Entry<String, String> entry : votes.entrySet()) {
				Optional<Player> voter = convertStringToPlayer(entry.getKey());
				Optional<Player> target = convertStringToPlayer(entry.getValue());

				if (voter.isPresent() && target.isPresent()) {
					result.put(voter.get(), target.get());
				}
			}
		}

		return result;
	}
}
