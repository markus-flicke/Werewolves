package gui.chat.render;

import gui.chat.models.ChannelMessage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A channel message cell
 */
public class ChannelMessageListCell extends ListCell<ChannelMessage> {
    private VBox vbox = new VBox();
    private TextField dateLabel = new TextField();
    private TextField nicknameLabel = new TextField();
    private TextField messageLabel = new TextField();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    private String ownNickname;

    public ChannelMessageListCell(String ownNickname) {
        super();
        this.ownNickname = ownNickname;

        HBox hbox = new HBox();
        Pane pane = new Pane();
        hbox.getChildren().addAll(nicknameLabel, pane, dateLabel);
        HBox.setHgrow(pane, Priority.ALWAYS);

        dateLabel.setEditable(false);
        nicknameLabel.setEditable(false);
        messageLabel.setEditable(false);

        nicknameLabel.getStyleClass().add("tiny");
        dateLabel.getStyleClass().add("tiny");

        vbox.getChildren().addAll(hbox, messageLabel);
    }

    @Override
    protected void updateItem(ChannelMessage channelMessage, boolean empty) {
        super.updateItem(channelMessage, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            Insets insets = channelMessage.getFrom().equals(ownNickname) ?
                    new Insets(0, 0, 0, 40)
                    : new Insets(0, 40, 0, 0);
            vbox.setBackground(new Background(
                    new BackgroundFill(
                            channelMessage.getFrom().equals(ownNickname) ? Color.ALICEBLUE : Color.WHITESMOKE,
                            new CornerRadii(8),
                            insets
                    )
            ));
            vbox.setPadding(new Insets(
                    insets.getTop() + 8,
                    insets.getRight() + 8,
                    insets.getBottom() + 8,
                    insets.getLeft() + 8
            ));

            dateLabel.setText(dateFormat.format(channelMessage.getTimestamp()));
            nicknameLabel.setText(channelMessage.getFrom());
            messageLabel.setText(channelMessage.getText());

            setGraphic(vbox);
        }
    }

}
