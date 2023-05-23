package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import com.example.brawlheroes.engine.weapons.Pistol;
import com.example.brawlheroes.engine.weapons.Weapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Hero extends Entity {
    private Vector2D direction;
    private List<Weapon> weapons;
    private int selected;
    private double healthPoints;
    private boolean isAlive;
    private double speed = 120.f;

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }
    public Hero(Point2D position, Rectangle2D hitbox, Image image) {
        super(position, hitbox, image);
        weapons = new ArrayList<>();
        selected = 0;
    }
    public void move(double deltaTime, Vector2D direction) {
        position = position.add(direction.getX() * speed * deltaTime, direction.getY() * speed * deltaTime);
    }
    public void respawn(Point2D position) {
        this.position = position;
        weapons.clear();
        selected = 0;
        healthPoints = Consts.MAX_HEALTH;
    }
    public void damage(int damage) {
        this.healthPoints = Math.max(Consts.MIN_HEALTH, healthPoints - damage);
    }
    public void fire(World world) {
        weapons.get(selected).fire(world);
    }
    public void addWeapon(Weapon weapon) {
        weapons.add(0, weapon);
    }

    public double getSpeed() {
        return speed;
    }
}
