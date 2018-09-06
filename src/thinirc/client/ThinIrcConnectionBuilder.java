package thinirc.client;

import thinirc.exceptions.BuildException;
import thinirc.exceptions.NicknameAlreadyUsedException;
import thinirc.exceptions.ParamsNotSetException;

public interface ThinIrcConnectionBuilder {

	/**
	 * Sets "server" address.
	 * @param server
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setServer(String server);

	/**
	 * Sets server port.
	 * @param port
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setPort(int port);

	/**
	 * Sets users nickname
	 * @param nick
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setNick(String nick);

	/**
	 * Set if a ssl connection is used or not
	 * @param ssl
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setSSL(boolean ssl);


	/**
	 * Sets trustStore for System Property "javax.net.ssl.trustStore".
	 * @param trustStore
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setTrustStore(String trustStore);

	/**
	 * Sets user password. Allows "" or null to be used if there is none.
	 * @param password
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setPassword(String password);

	/**
	 * Sets user name.
	 * @param user name
	 * @return the builder
	 */
	ThinIrcConnectionBuilder setUser(String user);

	/**
	 * Builds a new ThinIrcConnection which is already connected to the server. Additionally the user is logged in and the nick is set.
	 * @return ThinIrcConnection
	 * @throws ParamsNotSetException if a param is not set properly
	 * @throws NicknameAlreadyUsedException if the nickname is already in use
	 * @throws BuildException if any other exception or error happens while building or connecting. 
	 */
	ThinIrcConnection build() throws ParamsNotSetException, NicknameAlreadyUsedException, BuildException;

}