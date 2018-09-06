package thinirc.message;

public class MyMessage implements Message {
	MessageHeader header;
	String payload;
	
	public MyMessage(MessageHeader header, String payload) {
		this.header = header;
		this.payload = payload;
	}
	
	
	@Override
	public String toString() {
		return "ChannelMessage [header=" + header + ", payload=" + payload + "]";
	}

	/* (non-Javadoc)
	 * @see thinirc.ChannelMessage#asIrcMessageString()
	 */
	@Override
	public String asIrcMessageString() {
		
		String string = this.getHeader().asIrcMessageHeaderString();
		if (this.getPayload() != null && this.getPayload().length() > 0 )
			string += " :" + this.payload;
		return string += "\r\n";
	}
	
	
	@Override
	public MessageHeader getHeader() {
		return this.header;
	}


	@Override
	public String getPayload() {
		return this.payload;
	}


	public static Message fromIrcMessageString(final String ircMsg) {
		boolean resIsFirst = ircMsg.startsWith(":");		
		int headerSplit = ircMsg.indexOf(":", 1);
		
		System.out.println("fromIrcMessageString" + resIsFirst + " " + headerSplit);
		
		String msgPayload = "";
		String headerStr = "";
		
		if (headerSplit >= 0) {
			msgPayload = ircMsg.substring(headerSplit+1);
			headerStr = ircMsg.substring(0 , headerSplit);
		}
		else {
			headerStr = ircMsg;
		}
		
		MessageHeader header = MyMessageHeader.fromIrcMessageHeaderString(headerStr);		
		return new MyMessage(header, msgPayload);		
	}
}
