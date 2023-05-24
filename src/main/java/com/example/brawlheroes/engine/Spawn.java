package com.example.brawlheroes.engine;

import com.example.brawlheroes.engine.weapons.Rifle;
import com.example.brawlheroes.engine.weapons.Shotgun;
import com.example.brawlheroes.engine.weapons.Weapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public class Spawn {
    private long delay;
    private boolean isSpawned;
    private Point2D position;
    private int id;
    private Class<? extends Weapon> weapon;
    private Weapon heroWeapon;
    private long lastSpawn;

    public Spawn(long delay, int id, Class<? extends Weapon> weapon, Point2D position) {
        this.delay = delay;
        this.id = id;
        this.weapon = weapon;
        this.position = position;
        isSpawned = false;
        lastSpawn = 0;
    }
    public void spawn(World world) {
        ResourceLoader loader = new ResourceLoader();
        if(weapon.equals(Shotgun.class)) {
            world.getItems().add(new Item(position, new Rectangle2D(0, 0, 60, 20),
                    loader.getShotgunImage(), id));
        } else if (weapon.equals(Rifle.class)) {
            world.getItems().add(new Item(position, new Rectangle2D(0, 0, 60, 20),
                    loader.getRifleImage(), id));
        }
        isSpawned = true;
        if(weapon.equals(Shotgun.class)) {
            heroWeapon = new Shotgun();
        } else if (weapon.equals(Rifle.class)) {
            heroWeapon = new Rifle();
        }
    }
    public int pick(World world) {
        world.getMainHero().addWeapon(heroWeapon);
        lastSpawn = System.currentTimeMillis();
        isSpawned = false;
        return world.getItems().stream()
                .filter(i -> i.getSpawnerId() == id)
                .findAny().get().getSpawnerId();
    }

    public long getDelay() {
        return delay;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public int getId() {
        return id;
    }

    public Class<? extends Weapon> getWeapon() {
        return weapon;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setLastSpawn(long lastSpawn) {
        this.lastSpawn = lastSpawn;
    }

    public long getLastSpawn() {
        return lastSpawn;
    }

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }
}
