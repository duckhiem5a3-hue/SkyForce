package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.main.Main;

public class MiniBoss extends BossObject {
    public static int sizeX = Main.WIDTH * 15 / 100;
    public static int sizeY = Main.WIDTH * 15 / 100;

    private double targetY = 150.0;
    private double speedY = 80.0;
    private boolean hasStopped = false;
    private long lastBulletTime = 0;
    private long fireRate = 2000; // ms

    public MiniBoss(double startX, double startY) {
        super("enemy_shooter", startX, startY, 600, "LEVEL 1 MINI-BOSS");
        this.collisionDamage = 50;
        this.setPos(startX, startY, 180);
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    public boolean timeToFire(long now) {
        if (hasStopped && (now - lastBulletTime >= fireRate)) {
            lastBulletTime = now;
            return true;
        }
        return false;
    }

    @Override
    protected void checkDeath() {
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    @Override
    public void update() {
        if (!hasStopped) {
            if (y < targetY) {
                y += speedY / 60.0;
                setPos(x, y, 180);
            } else {
                y = targetY;
                hasStopped = true;
                setPos(x, y, 180);
            }
        }
        checkDeath();
    }
}
