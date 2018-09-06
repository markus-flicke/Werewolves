package thinirc.message;

public class MyMessageHeader implements MessageHeader {
	@Override
	public String getRessource() {
		return this.ressource;
	}

	@Override
	public IrcCommand getIrcCommand() {
		return this.command;
	}

	@Override
	public String getParams() {		
		return this.params;
	}

	final String ressource;
	final IrcCommand command;
	final String params;
	
	public MyMessageHeader(String ressource, IrcCommand command, String params) {
		this.ressource = ressource;
		this.command = command;
		this.params = params;
	}
	
	public MyMessageHeader(IrcCommand command, String suffix) {
		this("", command, suffix);
	}
	
	@Override
	public String toString() {
		return "MessageHeader [ressource=" + ressource + ", command=" + command + ", params=" + params + "]";
	}
	
	/* (non-Javadoc)
	 * @see thinirc.MessageHeader#asIrcMessageHeaderString()
	 */
	@Override
	public String asIrcMessageHeaderString() {
		return this.command.name() + " " + this.params;
	}

	public static MessageHeader fromIrcMessageHeaderString(String ircHeader) {
		boolean resIsFirst = ircHeader.startsWith(":");
		int end = ircHeader.indexOf(" ");
		
		String prefix = "";		
		if (resIsFirst) {
			prefix = ircHeader.substring(1, end).trim();
			ircHeader = ircHeader.substring(end+1).trim();
		}
		System.out.println("HEADER COMMAND " + ircHeader);
		end = ircHeader.indexOf(" ");
		
		IrcCommand cmd;
		String params;
		try {
			if (IrcCommand.isReply(ircHeader)) {
				cmd = IrcCommand.getByReplyCode(Integer.parseInt(ircHeader.split(" ")[0]));
				params = ircHeader.substring(end).trim();
			} else {
				cmd = IrcCommand.valueOf(ircHeader.substring(0, end));
				params = ircHeader.substring(end).trim();
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			cmd = IrcCommand.UNKNOWN;
			params = ircHeader.trim();
		}
				
		return new MyMessageHeader(prefix, cmd, params);
	}
	
}
