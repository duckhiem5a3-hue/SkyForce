package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.SeekerBullet;

public class PlayerLevel2 extends Player {

    public PlayerLevel2(double startX, double startY) {
        super("player_ship_lv2_blue_idle", startX, startY);
        this.level = 2;
        this.damage = 35;
        this.fireRate = 170;
        this.maxHealth = 150;
        this.health = this.maxHealth;
        this.speedBulletX = 0;
        this.speedBulletY = -500;
        this.currentBulletTexture = "bullet_player_2";
        this.xpToNextLevel = calculateXpRequirement(2);
    }

    @Override
    public List<Bullet> fireBullet(List<EnemyObject> enemies) {
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double centerX = x + sizeX / 2;
        double startY = y;

        // Level 2: Bắn 2 viên đạn song song
        double offset = sizeX * 0.25;
        Bullet b1 = new Bullet(currentBulletTexture, centerX - offset, startY, 0, speedBulletY, damage);
        Bullet b2 = new Bullet(currentBulletTexture, centerX + offset, startY, 0, speedBulletY, damage);
        bulletsToSpawn.add(b1);
        bulletsToSpawn.add(b2);

        if (isSeekerActive()) {
            SeekerBullet leftSeeker = new SeekerBullet(centerX - 25, startY, damage / 2, -1, enemies);
            SeekerBullet rightSeeker = new SeekerBullet(centerX + 25, startY, damage / 2, 1, enemies);
            bulletsToSpawn.add(leftSeeker);
            bulletsToSpawn.add(rightSeeker);
        }

        addBullet(bulletsToSpawn);
        return bulletsToSpawn;
    }
}
