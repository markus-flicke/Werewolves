package gui;

import gui.chat.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thinirc.client.MyConnectionBuilder;
import thinirc.client.ThinIrcConnection;

/**
 * The entry class of this application
 */
public class Main extends Application {
    private static Main self;
    private Stage primaryStage;

    /**
     * @return the applications `Main` instance
     */
    public static Main getInstance() {
        return self;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.self = this;
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("login/login.fxml"));
        primaryStage.setTitle("IRC Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    /**
     * Open up the main chat window.
     * @param ircConnection a valid irc connection
     * @param nickname the user's nickname
     * @throws Exception we hope for the best
     */
    public void openChatWindow(ThinIrcConnection ircConnection, String nickname) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat/chat.fxml"));
        Parent root = fxmlLoader.load();
        ((ChatController) fxmlLoader.getController()).setIrcConnection(ircConnection, nickname);

        primaryStage.setTitle("MaRC - Marburg's Relay-Chat Client");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            ((ChatController) fxmlLoader.getController()).closeConnection();
        });
    }
}
