package com.example.brawlheroes;

import com.example.brawlheroes.Network.Connection;
import com.example.brawlheroes.engine.Engine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.Socket;

public class MenuController {
    @FXML
    private Button exitButton;
    public void onExitButton() {
        App.getStage().close();
    }
    public void onPlay() {
        try {
            Socket socket = new Socket("localhost", 1234);
            Connection connection = new Connection(socket);
            new Engine(connection).start();
        } catch (Exception e) {

        }
    }
    public void onCancel() {

    }
}