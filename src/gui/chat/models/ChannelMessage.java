package gui.chat.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A text message
 */
public class ChannelMessage {
    private String from;
    private Date timestamp;
    private String text;

    /**
     * Create a new channel message
     *
     * @param from sender nickname
     * @param text text body
     */
    public ChannelMessage(String from, String text) {
        this(from, new Date(), text);
    }

    /**
     * Create a new channel message
     *
     * @param from      sender nickname
     * @param timestamp the timestamp of the message
     * @param text      text body
     */
    public ChannelMessage(String from, Date timestamp, String text) {
        this.from = from;
        this.timestamp = timestamp;
        this.text = text;
    }

    /**
     * @return the sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the message timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @return the text body
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        return String.format("%s [%s]: %s", from, dateFormat.format(timestamp), text);
    }
}
