package util;

import java.util.List;
import java.util.stream.Collectors;

import game.Game;
import game.io.WerewolfIOHandler;
import roles.Player;
import roles.Townie;

public class VoteTowniesTestInputListener implements WerewolfTestInputListener {

	private final Game game;
	private final WerewolfTestIOHandler ioHandler;
	
	public VoteTowniesTestInputListener(Game game, WerewolfTestIOHandler ioHandler) {
		this.game = game;
		this.ioHandler = ioHandler;
	}

	@Override
	public void handleInputNotification() {
		List<Townie> townies = game.getPlayers().stream().filter(p -> p instanceof Townie && p.isAlive()).map(p -> (Townie) p).collect(Collectors.toList());
		for (Player p : game.getPlayers()) {
			ioHandler.queueVote(p.getName(), townies.get(0).getName());
		}
	}

}