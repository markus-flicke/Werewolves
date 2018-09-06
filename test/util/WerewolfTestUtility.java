package util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class WerewolfTestUtility {
	
	public static List<String> createPlayerNameList(int playerCount) {
		List<String> players = new ArrayList<String>();
		IntStream.rangeClosed(1, playerCount).forEachOrdered(i -> players.add("Player" + i));
		return players;
	}

}
