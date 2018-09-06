package thinirc.client;

import java.io.BufferedReader;
import java.io.IOException;

import thinirc.MsgReceiver;
import thinirc.message.MyMessage;

class MessagePusher implements Runnable {
	
	private boolean keepRunning = true;
    final private BufferedReader reader;
    final private Iterable<MsgReceiver> recs;
    
			
	MessagePusher(BufferedReader istr, Iterable<MsgReceiver> r) throws IOException {
		reader = istr;
		recs = r;
	}

	@Override
	public void run() {		
		try {			
			while (this.getKeepRunning()) {
				if (!reader.ready()) {
					continue;
				}
				final String readLine = reader.readLine();
				if (readLine == null)
					return;
	            recs.forEach(x -> x.accept(MyMessage.fromIrcMessageString(readLine)));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}			
	}	
	
	synchronized boolean getKeepRunning() {
		return this.keepRunning;
	}

	synchronized void stop() {
		this.keepRunning = false;
	}
	
	
}
