package gui.chat.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Arrays;

/**
 * An IRC channel abstraction
 */
public class Channel implements Comparable<Channel> {
    private String name;
    private StringProperty topic = new SimpleStringProperty();
    private ObservableList<ChannelMessage> messages = FXCollections.observableArrayList();
    private ObservableList<String> users = FXCollections.observableArrayList();
    private ObservableMap<String, Integer> wordCount = FXCollections.observableHashMap();

    public void removeUser(String user){
        users.remove(user);
    }

    /**
     * Create a new channel by name
     *
     * @param name channel name
     */
    public Channel(String name) {
        this.name = name;
    }

    /**
     * @return the channel name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the topic property
     */
    public StringProperty topicProperty() {
        return topic;
    }

    /**
     * @return the word count property
     */
    public ObservableMap<String, Integer> wordCountProperty() {
        return wordCount;
    }

    /**
     * Add a new message to the channel's history
     * @param message the message
     */
    public void addMessage(ChannelMessage message) {
        this.messages.add(message);

        countWords(message);
    }

    /**
     * Count all incoming words
     * @param message the message
     */
    private void countWords(ChannelMessage message) {
        // TODO: implement
    }

    /**
     * @return the messages
     */
    public ObservableList<ChannelMessage> getMessages() {
        return messages;
    }

    /**
     * @return a generated channel color
     */
    public String getColor() {
        int hashCode = name.hashCode();
        int r = (hashCode & 0xFF0000) >> 16;
        int g = (hashCode & 0x00FF00) >> 8;
        int b = hashCode & 0x0000FF;
        return "#" + intTohexStringPart(r) + intTohexStringPart(g) + intTohexStringPart(b);
    }

    private String intTohexStringPart(int part) {
        String stringPart = Integer.toString(part, 16);
        return (stringPart.length() < 2 ? "0" : "") + stringPart;
    }

    /**
     * Add user information to the channel
     * @param users a set of nicknames
     */
    public void addUsers(String... users) {
        this.users.addAll(Arrays.asList(users));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Channel) {
            Channel otherChannel = (Channel) obj;
            return this.name.equals(otherChannel.name) && this.topic.equals(otherChannel.topic);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Channel otherChannel) {
        return this.name.compareTo(otherChannel.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * @return all nicknames that are in the channel
     */
    public ObservableList<String> getUsers() {
        return users;
    }

    /**
     * Replace the user list
     * @param users all user nicknames
     */
    public void setUsers(String... users) {
        this.users.setAll(Arrays.asList(users));
    }

    /**
     * Set a new topic.
     * @param topic the topic name
     */
    public void setTopic(String topic) {
        this.topic.setValue(topic);
    }
}
