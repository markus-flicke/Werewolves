package thinirc.message;

public interface MessageBuilder {
	Message message(MessageHeader header, String messagePayload);
}
