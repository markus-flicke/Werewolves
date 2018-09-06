package thinirc.client;

import java.io.IOException;
import java.util.List;

import thinirc.MsgReceiver;
import thinirc.exceptions.InvalidChannelException;
import thinirc.message.IrcCommand;
import thinirc.message.Message;
import thinirc.message.MyMessage;
import thinirc.message.MyMessageHeader;

public interface ThinIrcConnection {

	/**
	 * registers a new {@link MsgReceiver} with the connection. The {@link MsgReceiver} will be called with each arriving {@link Message}.
	 * @param rec
	 * @return
	 */
	boolean registerMsgRec(MsgReceiver rec);

	/**
	 * Join a channel using the JOIN command.
	 * @param channel
	 * @throws IOException
	 * @throws InvalidChannelException if channel starts with "#" or contains a whitespace.
	 */
	void joinChannel(String channel) throws IOException, InvalidChannelException;
	
	/**
	 * Leave a channel using the PART command.
	 * @param channel 
	 * @param reason - a message send with the PART command.
	 * @throws IOException
	 * @throws InvalidChannelException if channel starts with "#" or contains a whitespace.
	 */
	void partChannel(String channel, String reason) throws IOException, InvalidChannelException;

	/**
	 * Set the users nickname with the NICK command.
	 * @param nick
	 * @throws IOException
	 */
	void setNick(String nick) throws IOException;

	/**
	 * Sends a text to a channel.
	 * @param channel
	 * @param text
	 */
	void sendTextToChannel(String channel, String text) throws IOException, InvalidChannelException;
	
	/**
	 * Sends a ChannelMessage to the server
	 * @param message
	 * @throws IOException
	 */
	void sendMessage(Message message) throws IOException;

	/**
	 * Uses the QUIT command to leave the server and closes _all_ resources.
	 * @param message -  message send to the server with the QUIT command.
	 */
	public void quit(String message);
	
	/**
	 * Uses the MODE command to add a mode to a channel.
	 * You must be operator to do this.
	 *
	 */
	
	
	/**
	 * Uses the MODE command to add a mode to a channel.
	 * You must be operator to do this.
	 * @param channel the channel to add the mode to.
	 * @param mode the mode to be set.
	 */
	public void addMode(String channel, IrcChannelMode mode);
	
	/**
	 * Uses the MODE command to remove a mode from a channel.
	 * You must be operator to do this.
	 * @param channel the channel to remove the mode from.
	 * @param mode the mode to be set.
	 */
	public void removeMode(String channel, IrcChannelMode mode);
	
	/**
	 * Uses the KICK command to remove a user from a channel.
	 * You must be operator to do this.
	 * @param userName the user to kick.
	 * @param channel the channel.
	 */
	public void kick(String channel, String userName);
	
	/**
	 * Uses the INVITE command to invite a player to a channel.
	 * You must be operator to do this.
	 * @param userName the user to invite.
	 * @param channel the channel.
	 */
	public void invite(String userName, String channel);

}