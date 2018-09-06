package thinirc.client;

public enum IrcChannelMode {
	INVITE("i"),
	QUIET("q"),
	PRIVATE("p"),
	NOEXTERNAL("n");
	
	private String flag;
	
	private IrcChannelMode(String flag) {
		this.flag = flag;
	}
	
	public String addMode() {
		return  "+" + this.flag;
	}
	
	public String removeMode() {
		return "-" + this.flag;
	}
}
