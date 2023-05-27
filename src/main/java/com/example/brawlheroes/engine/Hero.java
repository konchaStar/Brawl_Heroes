package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import com.example.brawlheroes.engine.weapons.Pistol;
import com.example.brawlheroes.engine.weapons.Shotgun;
import com.example.brawlheroes.engine.weapons.Weapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hero extends Entity {
    private Vector2D direction;
    private List<Weapon> weapons;
    private int kills;
    private int death;
    private long lastStrafe;
    private int selected;
    private double healthPoints;
    private boolean isAlive;
    private boolean checkedDeath;
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
        checkedDeath = true;
        healthPoints = Consts.MAX_HEALTH;
        selected = 0;
        lastStrafe = 0;
        isAlive = true;
        kills = 0;
        death = 0;
    }
    public void move(double deltaTime, Vector2D direction) {
        position = position.add(direction.getX() * speed * deltaTime, direction.getY() * speed * deltaTime);
    }
    public void respawn(Point2D position) {
        this.position = position;
        weapons.clear();
        addWeapon(new Pistol());
        selected = 0;
        healthPoints = Consts.MAX_HEALTH;
        isAlive = true;
    }
    public void damage(int damage) {
        this.healthPoints = Math.max(Consts.MIN_HEALTH, healthPoints - damage);
        if(healthPoints == Consts.MIN_HEALTH) {
            kill();
        }
    }
    public void fire(World world) {
        weapons.get(selected).fire(world);
    }
    public void addWeapon(Weapon weapon) {
        Optional<Weapon> existed = weapons.stream()
                .filter(w -> weapon.getClass().equals(w.getClass()))
                .findAny();
        if(existed.isPresent()) {
            existed.get().setAmmo(existed.get().getAmmo() + weapon.getAmmo());
        } else {
            weapons.add(weapon);
        }
    }

    public double getSpeed() {
        return speed;
    }
    public void kill() {
        isAlive = false;
        checkedDeath = false;
        death++;
    }
    public boolean isAlive() {
        return isAlive;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getLastStrafe() {
        return lastStrafe;
    }

    public void setLastStrafe(long lastStrafe) {
        this.lastStrafe = lastStrafe;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isCheckedDeath() {
        return checkedDeath;
    }

    public void setCheckedDeath(boolean checkedDeath) {
        this.checkedDeath = checkedDeath;
    }
    public int getWeapons() {
        return weapons.size();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
    public List<Weapon> getWeaponList() {
        return weapons;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }
}
