package thinirc.message;

public interface MessageHeader {

	String asIrcMessageHeaderString();	
	String getRessource();
	IrcCommand getIrcCommand();
	String getParams();

}