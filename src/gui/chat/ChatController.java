package gui.chat;

import csv.*;
import gui.chat.models.Channel;
import gui.chat.models.ChannelMessage;
import gui.chat.render.ChannelListCell;
import gui.chat.render.ChannelMessageListCell;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import thinirc.client.ThinIrcConnection;
import thinirc.exceptions.InvalidChannelException;
import thinirc.message.*;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The controller for the main chat window
 */
public class ChatController implements Initializable {
    @FXML
    private TextField channelInput;
    @FXML
    private Button channelAddButton;
    @FXML
    private ListView<Channel> channelListView;
    @FXML
    private Label channelTopic;
    @FXML
    private ListView<ChannelMessage> channelMessagesView;
    @FXML
    private ListView<String> userView;
    @FXML
    private TextField textInput;
    @FXML
    private Button sendButton;
    @FXML
    private BarChart<String, Integer> wordCountHistogram;

    @FXML
    private ListView<String> wordList;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private ObjectProperty<ThinIrcConnection> ircConnection = new SimpleObjectProperty<>();
    private String myNickname = "";

    private ObservableMap<String, Channel> channels = FXCollections.observableHashMap();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ircConnection.addListener((observable, oldValue, newValue) -> {
            handleIrcConnection(newValue);
        });

        channelListView.setCellFactory(param -> new ChannelListCell(channel -> {
            removeChannel(channel);
        }));
        channelMessagesView.setCellFactory(param -> new ChannelMessageListCell(myNickname));


        channelListView.itemsProperty().bind(Bindings.createObjectBinding(
                () -> {
                    List<Channel> channels = new ArrayList<>(this.channels.values());
                    Collections.sort(channels);
                    return FXCollections.observableList(channels);
                },
                channels));

        channelListView.getSelectionModel().selectedItemProperty().addListener(listener -> {
            if (channelListView.getSelectionModel().isEmpty()) {
                channelTopic.textProperty().unbind();
                channelTopic.textProperty().bind(new SimpleStringProperty());
                channelMessagesView.itemsProperty().unbind();
                channelMessagesView.itemsProperty().bind(new SimpleListProperty<>());
                userView.itemsProperty().unbind();
                userView.itemsProperty().bind(new SimpleListProperty<>());

                wordCountHistogram.dataProperty().unbind();

            } else {
                Channel selectedChannel = channelListView.getSelectionModel().getSelectedItem();

                channelTopic.textProperty().unbind();
                channelTopic.textProperty().bind(selectedChannel.topicProperty());
                channelMessagesView.itemsProperty().unbind();
                channelMessagesView.itemsProperty().bind(
                        new SimpleListProperty<>(selectedChannel.getMessages())
                );


                userView.itemsProperty().unbind();
                userView.itemsProperty().bind(
                        new SimpleListProperty<>(selectedChannel.getUsers())
                );

                // word count
                final int numberOfResults = 10;
                wordCountHistogram.dataProperty().bind(Bindings.createObjectBinding(
                        () -> {
                            XYChart.Series<String, Integer> series = new XYChart.Series<>();
                            series.setName("Word Count");
                            selectedChannel.wordCountProperty()
                                    .entrySet()
                                    .stream()
                                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                                    .limit(numberOfResults)
                                    .map(entry -> new XYChart.Data<>(entry.getKey(), entry.getValue()))
                                    .forEach(data -> series.getData().add(data));
                            return FXCollections.observableArrayList(series);
                        },
                        selectedChannel.wordCountProperty()
                ));
            }
        });

        channelInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            channelAddButton.setDisable(isChannelAddButtonDisabled(newValue));
        });
        channelInput.setText("#pingpong");
        channels.addListener((InvalidationListener) ivs -> {
            // ugly hack
            String value = channelInput.getText();
            channelInput.setText("");
            channelInput.setText(value);
        });

        textInput.disableProperty().bind(channelListView.getSelectionModel().selectedItemProperty().isNull());
        sendButton.disableProperty().bind(channelListView.getSelectionModel().selectedItemProperty().isNull());

        userView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && userView.getSelectionModel().selectedItemProperty().isNotNull().get()) {
                String selectedNickname = userView.getSelectionModel().getSelectedItem();
                Channel newChannel = new Channel(selectedNickname);
                channels.put(selectedNickname, newChannel);
                channelListView.getSelectionModel().select(newChannel);
            }
        });

        wordList.setItems(FXCollections.observableList(Arrays.asList("☺", "♡", "❀", "☕", "(╯°□°）╯︵ ┻━┻")));
        wordList.setOrientation(Orientation.HORIZONTAL);
    }

    /**
     * This method is necessary to set parameters from other stages.
     *
     * @param ircConnection a valid irc connection
     * @param myNickName    the own nickname
     */
    public void setIrcConnection(ThinIrcConnection ircConnection, String myNickName) {
        this.ircConnection.setValue(ircConnection);
        this.myNickname = myNickName;
    }

    /**
     * Check if the add button for channels should be disabled
     *
     * @param newValue the new value of the user input.
     * @return true if it should be disabled, else false
     */
    private boolean isChannelAddButtonDisabled(String newValue) {
        if(newValue.startsWith("#")){
            return false;
        }
        return true;
    }

    /**
     * Join (add) a new channel
     */
    public void addChannel() {
        if(ircConnection.isNull().getValue()){
            throw new RuntimeException("Connection doesnt exist");
        }
        String channelInputText = channelInput.getText();
        Channel channel = new Channel(channelInputText);
        channels.put(channelInputText, channel);
        try{
            ircConnection.get().joinChannel(channelInputText);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove the channel
     *
     * @param channel the channel
     */
    private void removeChannel(Channel channel) {
        channels.remove(channel.getName());
        try{
            ircConnection.get().partChannel(channel.getName(), "CU later alligator");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle all messages from the irc connection.
     *
     * @param ircConnection a valid irc connection
     */
    private void handleIrcConnection(ThinIrcConnection ircConnection) {
        ircConnection.registerMsgRec(m -> {
            IrcCommand cmd = m.getHeader().getIrcCommand();
            String channel;
            try {
                switch (cmd) {
                    case PING:
                        ircConnection.sendMessage(MyMessage.fromIrcMessageString("PONG :"+ m.getPayload()));
                        break;
                    case JOIN:
                        String nick = m.getHeader().getRessource().split("!")[0];
                        channel = m.getHeader().getParams();
                        channels.get(channel).addUsers(nick);
                        break;
                    case PRIVMSG:
                        System.out.println(m);
                        String sender = m.getHeader().getRessource().split("!")[0];
                        channel = m.getHeader().getParams();
//                                asIrcMessageHeaderString().split(" ")[1];
                        if(channel.equals(myNickname)){
                            if(!channels.containsKey(channel)){
                                channels.get(sender).addMessage(new ChannelMessage(channel, m.getPayload()));
                            }else{
                                channels.put(channel, new Channel(channel));
                            }
                        }
                        String message = m.getPayload();
                        channels.get(channel).addMessage(new ChannelMessage(sender, message));
                        break;
                    case UNKNOWN:
                        String params = m.getHeader().getParams();
                        if(params.startsWith("353")){
                            channel = m.getHeader().asIrcMessageHeaderString().split(" ")[4];
                            String[] users = m.getPayload().split(" ");
                            channels.get(channel).setUsers(users);
                        } else if(params.contains("331")||params.contains("332")||params.contains("TOPIC")) {
                        } else if(params.contains("QUIT")){
                            String user = m.getHeader().getRessource().split("!")[1];
                            channels.forEach((String key, Channel ch) -> {
                                ObservableList<String> users = ch.getUsers();
                                users.remove(user);
                                ch.setUsers((String[]) users.toArray());
                            });
                        }
                        break;
                    case KICK:
                        String user = m.getHeader().getParams().split(" ")[0];
                        channel = m.getHeader().getParams().split(" ")[1];
                        if(user.equals(myNickname)){
                            channels.remove(channel);
                        }else{
                            channels.get(channel).removeUser(user);
                        }
                        break;
                    case INVITE:
                        System.out.println("Params: " + m.getHeader().getParams());
                        System.out.println("Resource: " + m.getHeader().getRessource());
                        System.out.println("Payload: " + m.getPayload());
                        user = m.getHeader().getParams().split(" ")[0];
                        channel = "#" + m.getPayload().replace(".", "").split("#")[1];
                        if(user.equals(myNickname)){
                            channels.put(channel, new Channel(channel));
                        }
                        try {
                            ircConnection.joinChannel(channel);
                        } catch (InvalidChannelException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });}

    /**
     * Close the connection to clear up resources at program end
     */
    public void closeConnection() {
        ircConnection.get().quit("CU later alligator O:O;");
    }

    /**
     * Send a message
     */
    public void send() {
        if(channelListView.getSelectionModel().selectedItemProperty().isNull().get()){
            System.out.println("No channel selected!");
            return;
        }
        if(textInput.textProperty().isEmpty().get()){
            System.out.println("No message entered!");
            return;
        }
        try{
            String channel = channelListView.getSelectionModel().getSelectedItem().getName();
            String text = textInput.getText();
            if(text.equals(":sendPoem")){
                sendPoem();
            }else{
                ircConnection.get().sendTextToChannel(channel,text);
                channels.get(channel).addMessage(new ChannelMessage(myNickname, text));
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
//        TODO: Fehler als Channel Nachricht verpacken
        textInput.setText("");
    }

    /**
     * Handle key input from text field
     *
     * @param keyEvent a keyboard event
     */
    public void inputKeyTyped(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            send();
        }
    }

    List<ChannelMessage> readChannelHistory(String channelName, FilterPredicate<ChannelMessage> pred) {
        List<ChannelMessage> historyList = new ArrayList<>();
        // todo: implement
        return historyList;
    }

    public void historyRefresh() {
        System.out.println("Klick auf historyRefresh");
        // todo: implement
    }

	/**
	 * Handle exit item in file menu
	 */
	public void exit(ActionEvent e) {
    	closeConnection();
    	Platform.exit();
    }

    public void lvMouseClick(MouseEvent e){
        textInput.setText(textInput.getText() + wordList.getSelectionModel().getSelectedItem());
    }

    private void sendPoem(){
	    String channel = channelListView.getSelectionModel().getSelectedItem().getName();
        Thread storyThread = new Thread(() -> {
            BufferedReader bufferedReader = null;
            String nextLine = "";
            try {
                bufferedReader = new BufferedReader(new FileReader("/Users/m/Uni/Praktikum/irc_B2b/src/gui/chat/Mond"));
                nextLine = bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 3; i++) {
                while (nextLine != null) {
                    try {
                        ircConnection.get().sendTextToChannel(channel, nextLine);
                        Thread.sleep(800);
                        nextLine = bufferedReader.readLine();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        storyThread.start();
    }

    public static void main(String[] args){

    }
}
