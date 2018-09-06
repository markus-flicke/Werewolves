package thinirc.exceptions;

public class InvalidChannelException extends Exception {

	public InvalidChannelException(String channelName) {
		super(channelName + " is not a valid channel name.");
	}
}
