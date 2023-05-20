package com.example.brawlheroes;

import com.example.brawlheroes.Network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    private Client client;
    @FXML
    private Button exitButton;
    public void onExitButton() {
        App.getStage().close();
    }
    public void onPlay() {
        client = new Client();
        client.start();
    }
    public void onCancel() {
        client.setRunning(false);

    }
}