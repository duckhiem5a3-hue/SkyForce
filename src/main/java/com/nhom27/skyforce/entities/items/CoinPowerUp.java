package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.managers.VFXManager;

import javafx.scene.shape.Rectangle;

public class CoinPowerUp extends PowerUp {
    private final int value;

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

    @Override
    public void applyEffect(Player player, VFXManager vfxManager) {
        AudioManager.getInstance().playSound("sfx_item_health_pickup");
        vfxManager.applyPlayerGlow(player, "heal");
    }
}
