package com.example.brawlheroes.Network;

import java.io.Serializable;

public class StartPosition implements Serializable {
    private double positionX;
    private double positionY;

    public StartPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}
