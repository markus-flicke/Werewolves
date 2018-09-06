package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import game.io.WerewolfIOHandler;
import phases.Day;
import phases.Phase;
import phases.SeerNight;
import phases.WerewolfNight;
import roles.Player;
import roles.Seer;
import roles.Townie;
import roles.Werewolf;

public class DefaultGameBuilder extends GameBuilder {
	
	/* (non-Javadoc)
	 * @see game.GameFactory#createGame(java.util.List, game.io.WerewolfInputStream, game.io.WerewolfOutputStream)
	 */
	@Override
	public Game buildGame(List<String> playerNames, WerewolfIOHandler ioHandler) throws InvalidPlayerCountException {
	    List<Player> players = assignRoles(playerNames);
		return new Game(players, new Phase[]{new Day(players), new SeerNight(players), new WerewolfNight(players)}, ioHandler);
	}
	
	/* (non-Javadoc)
	 * @see game.GameFactory#assignRoles(java.util.List)
	 */
	@Override
	protected List<Player> assignRoles(List<String> playerNames) throws InvalidPlayerCountException {
		int n = playerNames.size();
		List<Player> players = new ArrayList<>();
		int wolvesN = 0;
		if(n<26){
            wolvesN = 4;
        }if(n<19){
            wolvesN = 3;
        }if(n<12){
            wolvesN = 2;
        }if(n<8){
            wolvesN = 1;
        }if(n<6 || n >= 26) {
            throw new InvalidPlayerCountException(n);
        }

        Collections.shuffle(playerNames);
		players.add(new Seer(playerNames.get(0)));

		for(int i = 1; i < wolvesN+1; i++){
            players.add(new Werewolf(playerNames.get(i)));
        }
		for(String name: playerNames.subList(1+ wolvesN,n)){
            players.add(new Townie(name));
        }
        Collections.shuffle(players);
		return players;
	}

}
