package com.nhom27.skyforce.entities.weapons;

import com.nhom27.skyforce.entities.enemies.ShooterEnemy;
import com.nhom27.skyforce.utils.AssetManager;

public class EnemyBullet extends Bullet{

    public EnemyBullet(double startX, double startY) {
        double speedY;
        int damage = ShooterEnemy.appearTimes*40;
        String nameImage;
        switch (ShooterEnemy.appearTimes) {
            case 1:
                nameImage = "bullet_enemy_1";
                speedY = 150;
                break;
            case 2:
                nameImage = "bullet_enemy_2";
                speedY = 300;
                break;
            case 3:
                nameImage = "bullet_enemy_3";
                speedY = 450;
                break;
            default:
                nameImage = "bullet_enemy_3";
                speedY = 600;
                break;
        }
        super(nameImage, startX, startY, 0, speedY, damage);
    }
}
