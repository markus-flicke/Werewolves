package game;

import java.util.List;

import game.io.WerewolfIOHandler;
import roles.Player;

public abstract class GameBuilder {
	
	/**
	 * Generates a new instance of Game.
	 * @param playerNames the list of usernames in the game.
	 * @param input the input stream to read player inputs from.
	 * @param output the output stream to write messages to.
	 * @return a game object.
	 * @throws InvalidPlayerCountException if the total amount of players is invalid.
	 */
	public abstract Game buildGame(List<String> playerNames, WerewolfIOHandler ioHandler) throws InvalidPlayerCountException;
	
	/**
	 * Assigns the players their roles.
	 * @param playerNames the list of usernames in the game.
	 * @return a list of Player objects.
	 * @throws InvalidPlayerCountException if the total amount of players is invalid.
	 */
	protected abstract List<Player> assignRoles(List<String> playerNames) throws InvalidPlayerCountException;

	/**
	 * Checks if a given number is in a certain range.
	 * @param x the number to check.
	 * @param lower the inclusive lower bound of the range.
	 * @param upper the inclusive upper bound of the range.
	 * @return true if x is in the given range, false otherwise.
	 */
	protected boolean isBetween(int x, int lower, int upper) {
		return lower <= x && x <= upper;
	}
}
