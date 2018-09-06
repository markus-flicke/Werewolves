package thinirc.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thinirc.MsgReceiver;
import thinirc.exceptions.InvalidChannelException;
import thinirc.exceptions.NicknameAlreadyUsedException;
import thinirc.message.IrcCommand;
import thinirc.message.Message;
import thinirc.message.MyMessage;
import thinirc.message.MyMessageHeader;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MyThinIrcConnection implements ThinIrcConnection {
	
	final String clientName = "propra client #2";
	final String server;
	final int port;
	final String password;
	final String login;
	final boolean ssl;
	final String trustStorePath;
	
	String nick;	
	Socket socket;

	private List<String> channels;
	private List<MsgReceiver> rec = Collections.synchronizedList(new ArrayList<>());
	private MessagePusher mp;
	
	private BufferedWriter writer;
	
	protected MyThinIrcConnection(String server, int port, String password, String login, String nick, boolean ssl, String trustStorePath) {
		this.ssl = ssl;
		this.server = server;
		this.trustStorePath = trustStorePath;
		this.port = port;
		this.password = password;
		this.nick = nick;
		this.login = login;
		this.channels = new ArrayList<>();
	}

	protected void connect() throws Exception, NicknameAlreadyUsedException {
        if(this.ssl) {
			System.setProperty("javax.net.ssl.trustStore", this.trustStorePath);
			SSLSocketFactory factory =
					(SSLSocketFactory)SSLSocketFactory.getDefault();

			this.socket = factory.createSocket(server, port);
		} else {
        	this.socket = new Socket(server, port);
		}
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        // Log on to the server.
        if (this.password != null && this.password.length() > 0) {
            this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.PASS, this.password), ""));
        }
        this.setNick(this.nick);
        this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.USER, this.login + " 8 *"), this.clientName));
                
        // Read lines from the server until it tells us we have connected.
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
        String line = null;
	    while ((line = reader.readLine()) != null) {
	    	System.out.println("login| " + line);

	        if (line.contains("004")) {
	            // We are now logged in.
	            break;
	        }
	        else if (line.contains("433")) {	            
	            throw new NicknameAlreadyUsedException(this.nick);
	        }
	    }
        
        this.mp = new MessagePusher(reader, this.rec);        
        final Thread thread = new Thread(mp);
        thread.start();                
    }
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnection#registerMsgRec(thinirc.MsgReceiver)
	 */
	@Override
	public boolean registerMsgRec(MsgReceiver rec) {
		return this.rec.add(rec);
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnection#joinChannel(java.lang.String)
	 */
	@Override
	public void joinChannel(String channel) throws IOException, InvalidChannelException {
		if (!channel.startsWith("#") || channel.contains(" ")) {
			throw new InvalidChannelException(channel);
		}
		
		this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.JOIN, channel), ""));        
		this.channels.add(channel);
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnection#setNick(java.lang.String)
	 */
	@Override
	public void setNick(String nick) throws IOException {
		this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.NICK, nick),  ""));
		this.nick = nick;
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnection#sendMessage(thinirc.message.ChannelMessage)
	 */
	@Override
	public void sendMessage(Message message) throws IOException {
		System.out.println("SENDMESSAGE " + message);
        writer.write(message.asIrcMessageString());
		writer.flush();
	}
	
	/* (non-Javadoc)
	 * @see thinirc.client.ThinIrcConnection#disconnect()
	 */
	@Override
	public void quit(String message) {
		try {
			this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.QUIT, ""), message));
		} catch (Exception e) {
			System.err.println("Could not send QUIT");
		}
		
		this.mp.stop();
		try {
			this.socket.close();
		} catch (IOException e) {
			System.err.println("Could not close Socket");
		}		
	}


	@Override
	public void partChannel(String channel, String reason) throws IOException, InvalidChannelException {
		if (this.channels.remove(channel)) {
			this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.PART, channel), reason));
		} else {
			throw new InvalidChannelException(channel);
		}
	}


	@Override
	public void sendTextToChannel(String channel, String text) throws IOException, InvalidChannelException {
		if (!channel.startsWith("#") || channel.contains(" ")) {
			throw new InvalidChannelException(channel);
		}		
		this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.PRIVMSG, channel), text));        
	}
	
	@Override
	public void invite(String userName, String channel) {
        try {
            this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.INVITE, userName + " " + channel), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
		
	@Override
	public void addMode(String channel, IrcChannelMode mode) {
        try {
            this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.MODE, channel + " " + mode.addMode()),null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@Override
	public void removeMode(String channel, IrcChannelMode mode) {
        try {
            this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.MODE, channel + " " + mode.removeMode()),null));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void kick(String channel, String userName) {
        try {
            this.sendMessage(new MyMessage(new MyMessageHeader(IrcCommand.KICK, userName + " " + channel),null));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
