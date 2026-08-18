package com.nhom27.skyforce.entities.weapons;

public class EnemyBullet extends Bullet {

    public EnemyBullet(double startX, double startY) {
        super("bullet_enemy_laser", startX, startY, 0, 250.0, 20);
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
