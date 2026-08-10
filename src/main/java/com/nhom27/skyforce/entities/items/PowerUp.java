package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;

public abstract class PowerUp extends GameObject {
    protected double speed;

    public PowerUp(String nameImg, double startX, double startY, double speed) {
        super(nameImg, startX, startY);
        this.speed = speed;
    }

    public abstract void applyEffect(Player player);

    @Override
    public void update() {
        y += speed / 60.0;
        setPos(x, y);
        if (this.y >= Main.HEIGHT + sizeY) {
            this.isAlive = false;
        }
    }
}
