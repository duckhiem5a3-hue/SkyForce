package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class TankerEnemy extends EnemyObject {
    private double speedY = 70.0;
    private long lastFireTime = 0;
    private long fireRate = 2500; // ms
    private boolean canShoot = true;

    public TankerEnemy() {
        this(70.0, true);
    }

    public TankerEnemy(double speedY, boolean canShoot) {
        super("enemy_tanker_red", 200, 50);
        this.speedY = speedY;
        this.canShoot = canShoot;
    }

    public boolean timeToFire(long now) {
        if (canShoot && (now - lastFireTime >= fireRate)) {
            lastFireTime = now;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        y += speedY / 60.0;
        setPos(x, y);
        if (y > Main.HEIGHT) {
            isAlive = false;
        }
        checkDeath();
    }
}
