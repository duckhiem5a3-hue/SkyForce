package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class NormalEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 7 / 100;
    public static int sizeY = Main.WIDTH * 7 / 100;
    protected double speedY;
    protected double randomStopY;
    protected boolean hasStopped = false;
    protected boolean isStopping = false;
    protected boolean canShoot = true;
    protected boolean guaranteedDrop = false;
    protected long lastBulletTime = 0;
    protected long fireRate = 2000; // ms

    public NormalEnemy(double startX, double startY) {
        this(startX, startY, 150.0, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double startX, double startY, double speedY) {
        this(startX, startY, speedY, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double startX, double startY, double speedY, boolean canShoot, boolean isStopping, boolean guaranteedDrop, String spriteKey) {
        super(spriteKey != null ? spriteKey : "enemy_normal_blue", startX, startY);
        this.speedY = speedY;
        this.health = 30;
        this.collisionDamage = 20;
        this.canShoot = canShoot;
        this.isStopping = isStopping;
        this.guaranteedDrop = guaranteedDrop;
        if (isStopping) {
            Random random = new Random();
            this.randomStopY = 50 + random.nextDouble() * (Main.HEIGHT * 0.45);
        }
        this.setPos(startX, startY, 180);
    }

    public boolean isGuaranteedDrop() {
        return guaranteedDrop;
    }

    public void setGuaranteedDrop(boolean guaranteedDrop) {
        this.guaranteedDrop = guaranteedDrop;
    }

    public boolean canShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    public boolean timeToFire(long now) {
        if (!canShoot) return false;
        if (isStopping && !hasStopped) return false;
        if (now - lastBulletTime >= fireRate) {
            lastBulletTime = now;
            return true;
        }
        return false;
    }

    public long getFireRate() {
        return fireRate;
    }

    public void setFireRate(long fireRate) {
        this.fireRate = fireRate;
    }

    @Override
    protected void checkDeath() {
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    @Override
    public void update() {
        if (isStopping) {
            if (!hasStopped) {
                if (y <= randomStopY) {
                    y += speedY / 60.0;
                    setPos(x, y, 180);
                } else {
                    hasStopped = true;
                    setPos(x, randomStopY, 180);
                }
            }
        } else {
            y += speedY / 60.0;
            setPos(x, y, 180);
            if (y > Main.HEIGHT + 100) {
                isAlive = false;
            }
        }
        checkDeath();
    }
}
