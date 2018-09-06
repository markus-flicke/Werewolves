package thinirc.message;

public enum IrcCommand {
	JOIN, 
	PING, 
	PONG, 
	QUIT, 
	PRIVMSG, 
	NICK, 
	USER, 
	UNKNOWN, 
	PASS, 
	PART, 
	INVITE, 
	NAMES, 
	LIST, 
	KICK, 
	MODE,
	RPL_LUSERCLIENT(251),
	RPL_LISTSTART(321),
	RPL_LIST(322),
	RPL_LISTEND(323),
	RPL_CHANNELMODEIS(324),
	RPL_NOTOPIC(331),
	RPL_NAMREPLY(353),
	RPL_ENDOFNAMES(366);	
	final int replyCode;
	
	IrcCommand() {
		this.replyCode = 0;
	}
	
	IrcCommand(int replyCode) {
		this.replyCode = replyCode;
	}
	
	public static boolean isReply(String messageString) {
		return messageString.matches("^[0-9].*$");
	}
		
	public static IrcCommand getByReplyCode(int code) {
		IrcCommand result = UNKNOWN;
		for (IrcCommand command : IrcCommand.values()) {
			if (command.replyCode == code) {
				result = command;
				break;
			}
		} 
		return result;
	}
	
}
