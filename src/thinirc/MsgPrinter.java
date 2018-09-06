package thinirc;

import thinirc.message.Message;

public class MsgPrinter implements MsgReceiver {

	@Override
	public void accept(Message msg) {
		System.out.println("Received MSG: " + msg);
	}

}
