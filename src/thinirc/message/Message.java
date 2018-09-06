package thinirc.message;

public interface Message {

	String asIrcMessageString();	
	MessageHeader getHeader();
	String getPayload();

}