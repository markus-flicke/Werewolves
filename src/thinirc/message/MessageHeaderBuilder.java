package thinirc.message;

public interface MessageHeaderBuilder {
	MessageHeader header(IrcCommand cmd, String headerParams); 
}
