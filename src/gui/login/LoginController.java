package gui.login;

import gui.Main;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import thinirc.client.MyConnectionBuilder;
import thinirc.client.ThinIrcConnection;
import thinirc.exceptions.BuildException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class LoginController implements Initializable {
    @FXML
    private TextField server;
    @FXML
    private Spinner<Integer> port;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private TextField nickname;
    @FXML
    private Button loginButton;
    @FXML
    private Label error;
    private ThinIrcConnection thinIrcConnection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        server.setText("dbsvm.mathematik.uni-marburg.de");
        port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 65535, 7778));
        port.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                port.getValueFactory().setValue(oldValue);
            }
        });
        password.setText("propra2018");
        username.setText("");
        nickname.setText("");

        setupButtonBindings();
    }

    /**
     * Create button bindings for disabling the button on insufficient user input
     */
    private void setupButtonBindings() {
        loginButton.disableProperty().bind(Bindings.or(nickname.textProperty().isEmpty(),username.textProperty().isEmpty()));
    }

    /**
     * The action when the login button is clicked
     */
    public void login() {
        MyConnectionBuilder myConnectionBuilder = new MyConnectionBuilder();
        String nick = nickname.getText();
        myConnectionBuilder
                .setNick(nick)
                .setPassword(password.getText())
                .setPort(port.getValue())
                .setServer(server.getText())
                .setSSL(false)
                .setUser(username.getText());

        Thread loginThread = new Thread(()-> {
            try{
                thinIrcConnection = myConnectionBuilder.build();
            }catch(Exception e){
                error.setText("Connection failed.");
            }
                Platform.runLater(() -> {
                    try{
                        Main.getInstance().openChatWindow(thinIrcConnection, nick);
                    }catch(Exception e){
                        throw new RuntimeException(e);
                    }
                });
        });
        loginThread.start();
    }
}
