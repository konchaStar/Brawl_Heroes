package com.example.brawlheroes.engine.weapons;

import com.example.brawlheroes.engine.Entity;
import com.example.brawlheroes.engine.Hero;
import com.example.brawlheroes.engine.Vector2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Bullet extends Entity {
    private Hero owner;
    private Vector2D direction;
    private int damage;
    private double speed;

    public Bullet(Hero owner, Point2D position, Rectangle2D hitbox, Image image, Vector2D direction, int damage, double speed) {
        super(position, hitbox, image);
        this.direction = direction;
        this.damage = damage;
        this.speed = speed;
        this.owner = owner;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public int getDamage() {
        return damage;
    }

    public double getSpeed() {
        return speed;
    }
    public void move(double deltaTime) {
        position = position.add(direction.getX() * speed * deltaTime, direction.getY() * speed * deltaTime);
    }

    public Hero getOwner() {
        return owner;
    }
}
