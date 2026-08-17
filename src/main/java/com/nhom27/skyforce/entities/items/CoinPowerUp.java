package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.managers.VFXManager;

import javafx.scene.shape.Rectangle;

public class CoinPowerUp extends PowerUp {
    private final int value;

    private boolean isMagnetized = false;
    private Player targetPlayer = null;
    private double magnetSpeed = 800.0;

    public CoinPowerUp(double startX, double startY) {
        this(startX, startY, 1);
    }

    public CoinPowerUp(double startX, double startY, int value) {
        super("item_coin_gold", startX, startY, 120);
        this.value = value;
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.setPos(startX, startY);
    }

    public int getValue() {
        return value;
    }

    public void setMagnetized(boolean magnetized, Player targetPlayer) {
        this.isMagnetized = magnetized;
        this.targetPlayer = targetPlayer;
    }

    public boolean isMagnetized() {
        return isMagnetized;
    }

    @Override
    public void update() {
        if (isMagnetized && targetPlayer != null && targetPlayer.isAlive()) {
            double targetX = targetPlayer.getX() + targetPlayer.getSizeX() / 2.0 - sizeX / 2.0;
            double targetY = targetPlayer.getY() + targetPlayer.getSizeY() / 2.0 - sizeY / 2.0;
            double dx = targetX - x;
            double dy = targetY - y;
            double dist = Math.hypot(dx, dy);
            if (dist > 0) {
                double moveDist = (magnetSpeed / 60.0);
                if (moveDist >= dist) {
                    x = targetX;
                    y = targetY;
                } else {
                    x += (dx / dist) * moveDist;
                    y += (dy / dist) * moveDist;
                }
            }
            setPos(x, y);
        } else {
            super.update();
        }
    }

    @Override
    public void applyEffect(Player player, VFXManager vfxManager) {
        AudioManager.getInstance().playSound("sfx_item_health_pickup");
        vfxManager.applyPlayerGlow(player, "heal");
    }
}

