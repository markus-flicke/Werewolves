package gui.chat.render;

import gui.chat.models.Channel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

/**
 * A channel list cell
 */
public class ChannelListCell extends ListCell<Channel> {
    private HBox hbox = new HBox();
    private Pane pane = new Pane();
    private Label label = new Label();
    private Button button = new Button();

    private Consumer<Channel> buttonAction;

    public ChannelListCell(Consumer<Channel> buttonAction) {
        super();
        this.buttonAction = buttonAction;
        hbox.getChildren().addAll(pane, label, button);
        HBox.setHgrow(label, Priority.ALWAYS);

        button.setText("x");

        label.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateItem(Channel channel, boolean empty) {
        super.updateItem(channel, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            pane.setBackground(new Background(
                    new BackgroundFill(Color.web(channel.getColor()), new CornerRadii(8), Insets.EMPTY)
            ));
            label.setText(channel.getName());
            button.setOnAction(action -> buttonAction.accept(channel));
            setGraphic(hbox);
        }
    }

}
