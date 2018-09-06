package thinirc.exceptions;

public class InviteOnlyException extends RuntimeException {
	
	public InviteOnlyException(String channel) {
		super("Channel \"" + channel + "\" is invite only!");
	}

}
