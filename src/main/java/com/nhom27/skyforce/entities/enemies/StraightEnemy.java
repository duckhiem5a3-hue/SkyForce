package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class StraightEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 7 / 100;
    public static int sizeY = Main.WIDTH * 7 / 100;
    private double speedY;

    public StraightEnemy(double startX, double startY) {
        super("enemy_straight", startX, startY, sizeX, sizeY, new double[] {
                0, 0,
                70, 0,
                70, 70,
                0, 70
        });
        this.speedY = 100.0;
        this.health = 50;
    }

    public StraightEnemy(double startX, double startY, double speedY) {
        this(startX, startY);
        this.speedY = speedY;
    }

    @Override
    public void update() {
        y += speedY / 60.0;
        setPos(x, y, 180);
        if (y > Main.HEIGHT + 100) {
            this.isAlive = false;
        }
        checkDeath();
    }
}
