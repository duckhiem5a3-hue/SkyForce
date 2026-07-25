package com.nhom27.skyforce.entities.player;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.main.Main;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Player extends GameObject {
    protected int health;
    protected int maxHealth;
    protected boolean gettingBuffed;
    protected int timeInBuff;
    protected int timeSinceLastBullet;
    public static int sizeX = Main.WIDTH / 12;
    public static int sizeY = Main.WIDTH / 16;

    private void setDefault() {
        this.maxHealth = 100;
        this.health = maxHealth;
        this.gettingBuffed = false;
        this.timeInBuff = 0;
        this.timeSinceLastBullet = 0;
    }

    public Player(Shape shape) {
        super("player_ship_1", Main.WIDTH / 2, Main.HEIGHT * (3 / 4), sizeX, sizeY);
        setDefault();
        this.hitbox = shape != null ? shape : new Rectangle(sizeX, sizeY);
        this.setPos(Main.WIDTH / 2, Main.HEIGHT * (3 / 4));
    }

    public Player(double startX, double startY) {
        super("player_ship_1", startX, startY, sizeX, sizeY);
        setDefault();
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.setPos(startX, startY);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
    }

    public void heal(int amount) {
        this.health = Math.min(maxHealth, this.health + amount);
    }

    public boolean isGettingBuffed() {
        return gettingBuffed;
    }

    public void setGettingBuffed(boolean buffed) {
        this.gettingBuffed = buffed;
        if (buffed) {
            this.timeInBuff = 600; // 10s at 60 FPS
        }
    }

    public int getTimeInBuff() {
        return timeInBuff;
    }

    public void setTimeInBuff(int timeInBuff) {
        this.timeInBuff = timeInBuff;
    }

    public int getTimeSinceLastBullet() {
        return timeSinceLastBullet;
    }

    public void setTimeSinceLastBullet(int timeSinceLastBullet) {
        this.timeSinceLastBullet = timeSinceLastBullet;
    }

    @Override
    public void update() {
        timeSinceLastBullet++;
        if (gettingBuffed) {
            timeInBuff--;
            if (timeInBuff <= 0) {
                gettingBuffed = false;
                timeInBuff = 0;
            }
        }
    }
}