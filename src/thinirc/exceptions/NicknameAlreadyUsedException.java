package thinirc.exceptions;

public class NicknameAlreadyUsedException extends RuntimeException {
	
	public NicknameAlreadyUsedException(String nickname) {
		super("Nickname \"" + nickname + "\" already used!");
	}
}
