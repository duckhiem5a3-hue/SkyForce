package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.SeekerBullet;

public class PlayerLevel3 extends Player {

    public PlayerLevel3(double startX, double startY) {
        this(startX, startY, "blue");
    }

    public PlayerLevel3(double startX, double startY, String skinId) {
        super("player_ship_lv3_" + (skinId != null && !skinId.isEmpty() ? skinId : "blue") + "_idle", startX, startY);
        this.skinId = skinId != null && !skinId.isEmpty() ? skinId : "blue";
        this.level = 3;
        this.damage = 50;
        this.fireRate = 140;
        this.maxHealth = 200;
        this.health = this.maxHealth;
        this.speedBulletX = 0;
        this.speedBulletY = -550;
        this.currentBulletTexture = getBulletTexture();
        this.xpToNextLevel = calculateXpRequirement(3);
    }

    @Override
    public List<Bullet> fireBullet(List<EnemyObject> enemies) {
        shotCount++;
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double centerX = x + sizeX / 2;
        double startY = y;

        // Level 3: Bắn 3 viên đạn tỏa quạt (1 thẳng, 2 nghiêng ±15°)
        String bulletTex = getBulletTexture();
        double speed = Math.abs(speedBulletY);
        double angleRad = Math.toRadians(15);
        double vxLeft = -speed * Math.sin(angleRad);
        double vyLeft = -speed * Math.cos(angleRad);
        double vxRight = speed * Math.sin(angleRad);
        double vyRight = -speed * Math.cos(angleRad);

        Bullet bCenter = new Bullet(bulletTex, centerX, startY, 0, speedBulletY, damage);
        Bullet bLeft = new Bullet(bulletTex, centerX - 12, startY, vxLeft, vyLeft, damage);
        Bullet bRight = new Bullet(bulletTex, centerX + 12, startY, vxRight, vyRight, damage);

        bulletsToSpawn.add(bCenter);
        bulletsToSpawn.add(bLeft);
        bulletsToSpawn.add(bRight);

        if (shouldFireSeeker()) {
            String seekerTex = getSeekerBulletTexture();
            SeekerBullet leftSeeker = new SeekerBullet(seekerTex, centerX - 25, startY, damage / 2, -1, enemies);
            SeekerBullet rightSeeker = new SeekerBullet(seekerTex, centerX + 25, startY, damage / 2, 1, enemies);
            bulletsToSpawn.add(leftSeeker);
            bulletsToSpawn.add(rightSeeker);
        }

        addBullet(bulletsToSpawn);
        return bulletsToSpawn;
    }
}
