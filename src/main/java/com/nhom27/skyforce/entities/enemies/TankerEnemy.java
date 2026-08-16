package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class TankerEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 12 / 100;
    public static int sizeY = Main.WIDTH * 12 / 100;
    private double speedY = 70.0; // Cực chậm
    private long lastFireTime = 0;
    private long fireRate = 2500; // ms
    private boolean canShoot = true;

    public TankerEnemy(double startX, double startY) {
        this(startX, startY, 70.0, true);
    }

    public TankerEnemy(double startX, double startY, double speedY, boolean canShoot) {
        super("enemy_straight", startX, startY);
        this.health = 200;
        this.collisionDamage = 50; // Nửa cây máu (50/100 HP)
        this.speedY = speedY;
        this.canShoot = canShoot;
        this.setPos(startX, startY, 180);
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
        setPos(x, y, 180);
        if (y > Main.HEIGHT + 100) {
            isAlive = false;
        }
        checkDeath();
    }
}
