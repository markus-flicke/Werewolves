package thinirc;

import java.util.function.Consumer;

import thinirc.message.Message;

public interface MsgReceiver extends Consumer<Message> {
}
