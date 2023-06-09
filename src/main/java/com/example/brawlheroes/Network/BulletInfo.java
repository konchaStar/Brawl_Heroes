package com.example.brawlheroes.Network;

import java.io.Serializable;

public class BulletInfo implements Serializable {
    private int damage;
    private double speed;
    private double positionX;
    private double positionY;
    private double directionX;
    private double directionY;

    public BulletInfo(int damage, double speed, double positionX, double positionY, double directionX, double directionY) {
        this.damage = damage;
        this.positionX = positionX;
        this.positionY = positionY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
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

    public double getSpeed() {
        return speed;
    }
}
