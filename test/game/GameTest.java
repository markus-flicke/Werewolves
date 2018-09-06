package test.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import game.DefaultGameBuilder;
import game.Game;
import game.GameBuilder;
import game.InvalidPlayerCountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import roles.Player;
import roles.Townie;
import roles.Werewolf;
import util.VoteTowniesTestInputListener;
import util.VoteWerewolvesTestInputListener;
import util.WerewolfTestIOHandler;
import util.WerewolfTestUtility;

class GameTest {

	GameBuilder fac;
	WerewolfTestIOHandler ioHandler;
	Game game;

	@BeforeEach
	void setUp() throws Exception {
		fac = new DefaultGameBuilder();
		ioHandler = new WerewolfTestIOHandler();
	}

	@Test
	void testGameTownieWin() throws InvalidPlayerCountException {
		for (int i = 6; i <= 25; i++) {
			game = fac.buildGame(WerewolfTestUtility.createPlayerNameList(i), ioHandler);
			ioHandler.registerInputListener(new VoteWerewolvesTestInputListener(game, ioHandler));

			List<Player> winners = game.runGame();

			for (Player player : winners) {
				assertTrue(player.hasWon(game.getPlayers()));
				assertTrue(player instanceof Townie);
			}
		}
	}

	@Test
	void testGameWerewolfWin() throws InvalidPlayerCountException {
		for (int i = 6; i <= 25; i++) {
			game = fac.buildGame(WerewolfTestUtility.createPlayerNameList(i), ioHandler);
			ioHandler.registerInputListener(new VoteTowniesTestInputListener(game, ioHandler));

			List<Player> winners = game.runGame();

			for (Player player : winners) {
				assertTrue(player.hasWon(game.getPlayers()));
				assertTrue(player instanceof Werewolf);
			}
		}
	}

}
