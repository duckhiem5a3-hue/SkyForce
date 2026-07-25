package com.nhom27.skyforce.entities.base;

import javafx.scene.shape.Polygon;

public abstract class EnemyObject extends GameObject {
    protected int health;

    public EnemyObject(String nameImage, double startX, double startY, double sizeX, double sizeY, double[] hitbox) {
        super(nameImage, startX, startY, sizeX, sizeY);
        this.health = 100;
        if (hitbox != null && hitbox.length > 0) {
            this.hitbox = new Polygon(hitbox);
        }
        this.setPos(startX, startY);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        checkDeath();
    }

    public static double[] getBulletShape(double sizeX, double sizeY) {
        double[] bullet1 = new double[] {
                0, 0,
                0, sizeY,
                sizeX, sizeY,
                sizeX, 0,
        };
        return bullet1;
    }

    public double[] getShapePlane1(double sizeX, double sizeY) {
        double[] plane1 = new double[] {
                0.425 * sizeX, 0.225 * sizeY,
                0.575 * sizeX, 0.225 * sizeY,
                0.575 * sizeX, 0.45 * sizeY,
                0.9 * sizeX, 0.55 * sizeY,
                0.5 * sizeX, 0.8 * sizeY,
                0.1 * sizeX, 0.55 * sizeY,
                0.425 * sizeX, 0.45 * sizeY,
                0.425 * sizeX, 0.225 * sizeY,
        };
        return plane1;
    }

    protected void checkDeath() {
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    public abstract void update();
}