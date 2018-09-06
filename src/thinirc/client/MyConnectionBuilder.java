package thinirc.client;

import thinirc.exceptions.BuildException;
import thinirc.exceptions.NicknameAlreadyUsedException;
import thinirc.exceptions.ParamsNotSetException;

public class MyConnectionBuilder implements ThinIrcConnectionBuilder {

	private String server;
	private int port = 6667;
	private String trustStorePath;
	private String nick;
	private String password;
	private String user;
	private boolean ssl = false;

	
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setServer(java.lang.String)
	 */
	@Override
	public ThinIrcConnectionBuilder setServer(String server) {
		this.server = server;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setPort(int)
	 */
	@Override
	public ThinIrcConnectionBuilder setPort(int port) {
		this.port = port;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setNick(java.lang.String)
	 */
	@Override
	public ThinIrcConnectionBuilder setNick(String nick) {
		this.nick = nick;
		return this;
	}

	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setSSL(boolean)
	 */

	@Override
	public ThinIrcConnectionBuilder setSSL(boolean ssl) {
		this.ssl = ssl;
		return this;
	}

	@Override
	public ThinIrcConnectionBuilder setTrustStore(String trustStore) {
		this.trustStorePath = trustStore;
		return this;
	}

	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setPassword(java.lang.String)
	 */
	@Override
	public ThinIrcConnectionBuilder setPassword(String password) {
		this.password = password;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#setUser(java.lang.String)
	 */
	@Override
	public ThinIrcConnectionBuilder setUser(String user) {
		this.user = user;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnectionBuilder#build()
	 */
	@Override
	public ThinIrcConnection build() throws ParamsNotSetException, NicknameAlreadyUsedException, BuildException{
		
		if (this.server == null || this.server.equals("") || this.nick == null || this.nick.equals("") || this.user == null || this.user.equals("")) {
			throw new ParamsNotSetException();
		}
		
		MyThinIrcConnection c = new MyThinIrcConnection(this.server, this.port, this.password, this.user, this.nick, this.ssl, this.trustStorePath);
		try {
			c.connect();
		} catch (NicknameAlreadyUsedException e) {
			throw e;
		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		}
		
		return c;
	}
	
	
	
	
}
