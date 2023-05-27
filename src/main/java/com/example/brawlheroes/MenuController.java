package com.example.brawlheroes;

import com.example.brawlheroes.Network.Connection;
import com.example.brawlheroes.Network.Message;
import com.example.brawlheroes.Network.StartPosition;
import com.example.brawlheroes.engine.Engine;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MenuController {
    @FXML
    private Button play;
    @FXML
    private Label oponentsLabel;
    @FXML
    private Button cancel;
    private Connection connection;
    private static Settings serverSettings;
    public void onExitButton() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (IOException e) {
        }
        App.getStage().close();
    }
    public void onPlay() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverSettings.getIp(), Integer.valueOf(serverSettings.getPort())), 1000);
            play.setVisible(false);
            cancel.setVisible(true);
            oponentsLabel.setVisible(true);
            connection = new Connection(socket);
            new Thread(() -> {
                try {
                    Message message;
                    do {
                        message = connection.receive();
                        StartPosition position = (StartPosition) message.getData();
                        if(message.getType() == Message.MessageType.STARTED) {
                            Platform.runLater(() -> {
                                new Engine(connection, position).start();
                            });
                        }
                    } while (message.getType() != Message.MessageType.STARTED);
                } catch (NumberFormatException e) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Check server settings!").showAndWait();
                    });
                } catch (IOException e) {

                } catch (ClassNotFoundException e) {

                }

            }).start();
        } catch (Exception e) {

        }
    }
    public void onCancel() {
        if(connection != null) {
            try {
                connection.send(new Message(null, Message.MessageType.DISCONNECT));
                connection.close();
            } catch (IOException e) {

            }
        }
        oponentsLabel.setVisible(false);
        cancel.setVisible(false);
        play.setVisible(true);
    }
    public void onSettings() {
        onCancel();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("settings.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
        }
        scene.getStylesheets().addAll(this.getClass().getResource("settingsStyle.css").toExternalForm());
        App.getStage().setScene(scene);
    }

    public static Settings getServerSettings() {
        return serverSettings;
    }

    public static void setServerSettings(Settings serverSettings) {
        MenuController.serverSettings = serverSettings;
    }
}