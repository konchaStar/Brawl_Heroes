package com.example.brawlheroes.engine.weapons;

import com.example.brawlheroes.App;
import com.example.brawlheroes.engine.Vector2D;
import com.example.brawlheroes.engine.World;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

public class Shotgun extends Weapon {
    public Shotgun() {
        delay = 700;
        lastShot = 0;
        ammo = 15;
    }
    @Override
    public void fire(World world) {
        if(System.currentTimeMillis() - lastShot > delay && ammo > 0) {
            new MediaPlayer(world.getLoader().getShotgun()).play();
            ammo--;
            Bullet bullet = new Bullet(world.getMainHero(), world.getMainHero().getPosition(), new Rectangle2D(0, 0, 15, 10),
                    world.getLoader().getBulletImage(), world.getMainHero().getDirection(),
                    10, 600);
            Bullet[] bullets = new Bullet[5];
            bullets[0] = bullet;
            Vector2D direction = bullet.getDirection();
            Random random = new Random();
            for(int i = 0; i < 4; i++) {
                int angle = random.nextInt(40) - 20;
                double cos = Math.cos(angle * Math.PI / 180.0);
                double sin = Math.sin(angle * Math.PI / 180.0);
                Vector2D newDirection = new Vector2D(direction.getX() * cos - direction.getY() * sin,
                        direction.getX() * sin + direction.getY() * cos);
                bullets[i + 1] = new Bullet(world.getMainHero(), world.getMainHero().getPosition(), new Rectangle2D(0, 0, 15, 10),
                        world.getLoader().getBulletImage(), newDirection,
                        10, 700);
            }
            for(int i = 0; i < 5; i++) {
                world.addBullet(bullets[i], true);
            }
            lastShot = System.currentTimeMillis();
        }
    }
}
