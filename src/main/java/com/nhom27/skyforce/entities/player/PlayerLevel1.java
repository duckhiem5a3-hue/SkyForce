package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.SeekerBullet;

public class PlayerLevel1 extends Player {

    public PlayerLevel1(double startX, double startY) {
        super("player_ship_lv1_blue_idle", startX, startY);
        this.level = 1;
        this.damage = 25;
        this.fireRate = 200;
        this.maxHealth = 100;
        this.health = this.maxHealth;
        this.speedBulletX = 0;
        this.speedBulletY = -450;
        this.currentBulletTexture = "bullet_player_1";
        this.xpToNextLevel = calculateXpRequirement(1);
    }

    @Override
    public List<Bullet> fireBullet(List<EnemyObject> enemies) {
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double centerX = x + sizeX / 2;
        double startY = y;

        // Level 1: Bắn 1 viên đạn thẳng
        Bullet b = new Bullet(currentBulletTexture, centerX, startY, speedBulletX, speedBulletY, damage);
        bulletsToSpawn.add(b);

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
