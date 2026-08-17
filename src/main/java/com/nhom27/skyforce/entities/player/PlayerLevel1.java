package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.SeekerBullet;

public class PlayerLevel1 extends Player {

    public PlayerLevel1(double startX, double startY) {
        this(startX, startY, "blue");
    }

    public PlayerLevel1(double startX, double startY, String skinId) {
        super("player_ship_lv1_" + (skinId != null && !skinId.isEmpty() ? skinId : "blue") + "_idle", startX, startY);
        this.skinId = skinId != null && !skinId.isEmpty() ? skinId : "blue";
        this.level = 1;
        this.damage = 10;
        this.fireRate = 200; // 5 shots/sec => 50 DPS
        this.maxHealth = 100;
        this.health = this.maxHealth;
        this.speedBulletX = 0;
        this.speedBulletY = -900;
        this.currentBulletTexture = getBulletTexture();
        this.xpToNextLevel = calculateXpRequirement(1);
    }

    @Override
    public List<Bullet> fireBullet(List<EnemyObject> enemies) {
        shotCount++;
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double centerX = x + sizeX / 2;
        double startY = y;

        // Level 1: Bắn 1 viên đạn thẳng (10 dmg * 5 shots/s = 50 DPS)
        String bulletTex = getBulletTexture();
        Bullet b = new Bullet(bulletTex, centerX, startY, speedBulletX, speedBulletY, damage);
        bulletsToSpawn.add(b);

        if (shouldFireSeeker()) {
            String seekerTex = getSeekerBulletTexture();
            SeekerBullet leftSeeker = new SeekerBullet(seekerTex, centerX - 25, startY, 30, -1, enemies);
            SeekerBullet rightSeeker = new SeekerBullet(seekerTex, centerX + 25, startY, 30, 1, enemies);
            bulletsToSpawn.add(leftSeeker);
            bulletsToSpawn.add(rightSeeker);
        }

        addBullet(bulletsToSpawn);
        return bulletsToSpawn;
    }
}
