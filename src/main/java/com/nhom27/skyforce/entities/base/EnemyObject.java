package com.nhom27.skyforce.entities.base;

import com.nhom27.skyforce.main.Main;

public abstract class EnemyObject extends GameObject {
    public static int sizeX = Main.WIDTH * 7 / 100;
    public static int sizeY = Main.WIDTH * 7 / 100;
    protected int health;
    protected int collisionDamage = 20;
    protected boolean debug;
    public EnemyObject(String nameImage, double startX, double startY) {
        super(nameImage, startX, startY);
        debug = true;
    }

    public int getCollisionDamage() {
        return collisionDamage;
    }

    public void setCollisionDamage(int collisionDamage) {
        this.collisionDamage = collisionDamage;
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

    protected void checkDeath() {
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    public abstract void update();
}