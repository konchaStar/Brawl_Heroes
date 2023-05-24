package com.example.brawlheroes.Network;

import java.io.Serializable;

public class DeathInfo implements Serializable {
    private double positionX;
    private double positionY;

    public DeathInfo(double positionX, double positionY) {
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
