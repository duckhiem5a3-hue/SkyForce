package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

public class ShooterEnemy extends EnemyObject {
    private double speedX;
    private boolean direction;
    public ShooterEnemy(double startX, double startY) {
        super("enemy_shooter", startX, startY);
        this.hitbox = AssetManager.getSpriteInfo("enemy_shooter").getHitbox();
        this.setPos(startX, startY);
        this.speedX = 100;
        this.health = 200;
    }
    @Override
    public void update() {
        if(x <=0) {direction = true;}
        else if(x >= Main.WIDTH) {direction = false;}

        if(direction) {
            x += speedX/60;
            setPos(x, y);
        } else {
            x -= speedX/60;
            setPos(x, y);
        }
        checkDeath();
    }
}
