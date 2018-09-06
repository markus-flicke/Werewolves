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
import java.util.concurrent.ExecutorService;
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
    private ThinIrcConnection connection;
    ExecutorService threadPool = Executors.newFixedThreadPool(5);

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
        threadPool.submit(() -> {
            try {
                connection = new MyConnectionBuilder()
                        .setServer(server.getText())
                        .setNick(nickname.getText())
                        .setPassword(password.getText())
                        .setPort(port.getValue())
                        .setSSL(false)
                        .setUser(username.getText())
                        .build();
            } catch (BuildException e) {
                Platform.runLater(()->error.setText(e.getMessage()));
            }
            Platform.runLater(() -> {
                try{
                    Main.getInstance().openChatWindow(connection, nickname.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });


    }
}
