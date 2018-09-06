package game;

public class InvalidPlayerCountException extends Exception {

	public InvalidPlayerCountException(int playerCount) {
		super("Invalid playercount: " + playerCount + ".");
	}

}
