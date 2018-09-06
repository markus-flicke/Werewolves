package test.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import game.DefaultGameBuilder;
import game.Game;
import game.GameBuilder;
import game.InvalidPlayerCountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.io.WerewolfIOHandler;
import roles.Player;
import roles.Seer;
import roles.Townie;
import roles.Werewolf;
import util.WerewolfTestIOHandler;
import util.WerewolfTestUtility;

import java.util.List;

class DefaultGameBuilderTest {
	
	GameBuilder fac = new DefaultGameBuilder();
	WerewolfIOHandler ioHandler;
	
	@BeforeEach
	void setUp() throws Exception {
		ioHandler = new WerewolfTestIOHandler();
	}

	@Test
	void testInvalidPlayerCount() {
        Assertions.assertThrows(InvalidPlayerCountException.class, ()->{
            fac.buildGame(WerewolfTestUtility.createPlayerNameList(5), ioHandler);
        });
        Assertions.assertThrows(InvalidPlayerCountException.class, ()->{
            fac.buildGame(WerewolfTestUtility.createPlayerNameList(26), ioHandler);
        });
	}
	
	@Test
	void testValidPlayercount() throws InvalidPlayerCountException {
        Game game = fac.buildGame(WerewolfTestUtility.createPlayerNameList(6), ioHandler);
        List<Player> players = game.getPlayers();
        int werCount = 0;
        int seerCount = 0;
        int townieCount = 0;
        for(Player p: players){
            if(p instanceof Seer){
                seerCount++;
            }else if(p instanceof Werewolf){
                werCount++;
            }else if(p instanceof Townie){
                townieCount++;
            }
        }
        assertTrue(seerCount == 1 && werCount == 1 && townieCount == 4);
	}
}
