package com.example.brawlheroes.engine.weapons;

import com.example.brawlheroes.engine.Item;
import com.example.brawlheroes.engine.World;

public abstract class Weapon {
    protected long delay;
    protected long lastShot;
    protected int ammo;
    public abstract void fire(World world);

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }
}
