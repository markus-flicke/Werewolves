package util;

import java.util.List;
import java.util.stream.Collectors;

import game.Game;
import roles.Player;
import roles.Werewolf;

public class VoteWerewolvesTestInputListener implements WerewolfTestInputListener {

	private final Game game;
	private final WerewolfTestIOHandler ioHandler;
	
	public VoteWerewolvesTestInputListener(Game game, WerewolfTestIOHandler ioHandler) {
		this.game = game;
		this.ioHandler = ioHandler;
	}

	@Override
	public void handleInputNotification() {
		List<Werewolf> werewolves = game.getPlayers().stream().filter(p -> p instanceof Werewolf && p.isAlive()).map(p -> (Werewolf) p).collect(Collectors.toList());
		for (Player p : game.getPlayers()) {
			ioHandler.queueVote(p.getName(), werewolves.get(0).getName());
		}
	}

}