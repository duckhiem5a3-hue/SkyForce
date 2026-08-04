package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.main.Main;

import javafx.scene.shape.Rectangle;

public class PowerUp extends GameObject {
    protected double speed;

    public PowerUp(String nameImg, double startX, double startY) {
        super(nameImg, startX, startY);
        this.speed = 220;
        this.hitbox = new Rectangle(40, 40);
        this.setPos(startX, startY);
    }

    public PowerUp(String nameImg, double startX, double startY, double speed) {
        super(nameImg, startX, startY);
        this.speed = speed;
        this.hitbox = new Rectangle(40, 40);
        this.setPos(startX, startY);
    }

    @Override
    public void update() {
        y += speed / 60.0;
        setPos(x, y);
        if (this.y >= Main.HEIGHT + 50) {
            this.isAlive = false;
        }
    }
}
