package com.nhom27.skyforce.entities.weapons;

import com.nhom27.skyforce.entities.enemies.ShooterEnemy;

public class EnemyBullet extends Bullet {

    public EnemyBullet(double startX, double startY) {
        double speedY;
        int level = Math.max(1, ShooterEnemy.appearTimes);
        int damage = level * 20;
        String nameImage;
        switch (level) {
            case 1:
                nameImage = "bullet_enemy_laser";
                speedY = 250;
                break;
            case 2:
                nameImage = "bullet_enemy_laser";
                speedY = 350;
                break;
            case 3:
            default:
                nameImage = "bullet_enemy_laser";
                speedY = 450;
                break;
        }
        super(nameImage, startX, startY, 0, speedY, damage);
    }

    public EnemyBullet(double startX, double startY, double speedY, int damage) {
        super("bullet_enemy_laser", startX, startY, 0, speedY, damage);
    }

    public EnemyBullet(double startX, double startY, double speedX, double speedY, int damage) {
        this(startX, startY, speedX, speedY, damage, "bullet_enemy_laser");
    }

    public EnemyBullet(double startX, double startY, double speedX, double speedY, int damage, String imageName) {
        super(imageName != null ? imageName : "bullet_enemy_laser", startX, startY, speedX, speedY, damage);
    }
}
