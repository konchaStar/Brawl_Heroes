package com.example.brawlheroes.Network;

import java.io.Serializable;

public class HeroInfo implements Serializable {
    private double positionX;
    private double positionY;
    private double directionX;
    private double directionY;

    public HeroInfo(double positionX, double positionY, double directionX, double directionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirectionX() {
        return directionX;
    }

    public double getDirectionY() {
        return directionY;
    }
}
