package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import com.example.brawlheroes.Network.BulletInfo;
import com.example.brawlheroes.Network.Connection;
import com.example.brawlheroes.Network.Message;
import com.example.brawlheroes.engine.weapons.Bullet;
import com.example.brawlheroes.engine.weapons.Pistol;
import com.example.brawlheroes.engine.weapons.Shotgun;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World {
    private ResourceLoader loader;
    private List<Entity> objects = new ArrayList<>();
    private List<Spawn> spawns = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private Image floor;
    private Point2D mapSize;
    private Hero mainHero;
    private Hero enemy;
    private Connection connection;

    public World(Connection connection) {
        loader = new ResourceLoader();
        mainHero = new Hero(new Point2D(70,70), new Rectangle2D(0,0, Consts.TILE_SIZE - 2,Consts.TILE_SIZE - 2),
                loader.getHeroImage());
        mainHero.addWeapon(new Pistol());
        enemy = new Hero(new Point2D(110,110), new Rectangle2D(0,0, Consts.TILE_SIZE,Consts.TILE_SIZE),
                loader.getHeroImage());
        enemy.setDirection(new Vector2D(1, 0));
        this.connection = connection;
    }
    public void deleteBullet(Bullet bullet) {
        bullets.remove(bullet);
    }
    public List<Entity> getObjects() {
        return objects;
    }

    public void setObjects(List<Entity> objects) {
        this.objects = objects;
    }

    public List<Spawn> getSpawns() {
        return spawns;
    }

    public void setSpawns(List<Spawn> spawns) {
        this.spawns = spawns;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Hero getMainHero() {
        return mainHero;
    }

    public void setMainHero(Hero mainHero) {
        this.mainHero = mainHero;
    }

    public Hero getEnemy() {
        return enemy;
    }

    public void setEnemy(Hero enemy) {
        this.enemy = enemy;
    }
    public void addBullet(Bullet bullet, boolean local) {
        bullets.add(bullet);
        if(local) {
            BulletInfo info = new BulletInfo(bullet.getDamage(), bullet.getSpeed(), bullet.getPosition().getX(), bullet.getPosition().getY(),
                    bullet.getDirection().getX(), bullet.getDirection().getY());
            try {
                connection.send(new Message(info, Message.MessageType.FIRE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResourceLoader getLoader() {
        return loader;
    }

    public Image getFloor() {
        return floor;
    }

    public void setFloor(Image floor) {
        this.floor = floor;
    }

    public Point2D getMapSize() {
        return mapSize;
    }

    public void setMapSize(Point2D mapSize) {
        this.mapSize = mapSize;
    }
}
