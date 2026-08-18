package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.BossObject;

public class MiniBoss extends BossObject {
    private double targetY;
    private double speedY;
    private boolean hasStopped;
    private long lastBulletTime;
    private long fireRate;

    public MiniBoss() {
        super("boss_mini_red", 600, "LEVEL 1 MINI-BOSS");
        this.collisionDamage = 500;
        this.targetY = 100.0;
        this.speedY = 80.0;
        this.hasStopped = false;
        this.lastBulletTime = 0;
        this.fireRate = 2000;
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
    public void update() {
        if (!hasStopped) {
            if (y < targetY) {
                y += speedY / 60.0;
                setPos(x, y);
            } else {
                y = targetY;
                hasStopped = true;
                setPos(x, y);
            }
        }
        checkDeath();
    }
}
