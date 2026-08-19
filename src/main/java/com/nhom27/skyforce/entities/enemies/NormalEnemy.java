package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

public class NormalEnemy extends EnemyObject {
    protected double speedY;
    protected double randomStopY;
    protected boolean hasStopped;
    protected boolean isStopping; // Có tính năng dừng hay không
    protected boolean canShoot; // Có thể bắn hay không
    protected boolean guaranteedDrop;
    protected long lastBulletTime = 0;
    protected long fireRate = 2000; // ms

    public NormalEnemy() {
        this(150.0, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double speedY) {
        this(speedY, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double startX, double startY) {
        this(startX, startY, 150.0, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double startX, double startY, double speedY) {
        this(startX, startY, speedY, false, false, false, "enemy_normal_blue");
    }

    public NormalEnemy(double speedY, boolean canShoot, boolean isStopping,
            boolean guaranteedDrop, String spriteKey) {
        super(spriteKey != null ? spriteKey : "enemy_normal_blue");

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
    }

    public NormalEnemy(double startX, double startY, double speedY, boolean canShoot, boolean isStopping,
            boolean guaranteedDrop, String spriteKey) {
        this(speedY, canShoot, isStopping, guaranteedDrop, spriteKey);
        setPos(startX, startY);
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
        if (!canShoot)
            return false;
        if (isStopping && !hasStopped)
            return false;
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
    public void attack(GameManager gm, long now, Player player) {
        if (gm != null && timeToFire(now)) {
            double startX = getX() + getSizeX() / 2.0 - 5;
            double startY = getY() + getSizeY();
            EnemyBullet eBullet = new EnemyBullet(startX, startY, 0, 120.0, 15, "bullet_enemy_round_purple");
            gm.spawnEnemyBullet(eBullet);
        }
    }

    @Override
    public void update() {
        if (isStopping) {
            if (!hasStopped) {
                if (y <= randomStopY) {
                    y += speedY / 60.0;
                    setPos(x, y);
                } else {
                    hasStopped = true;
                    setPos(x, randomStopY);
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
