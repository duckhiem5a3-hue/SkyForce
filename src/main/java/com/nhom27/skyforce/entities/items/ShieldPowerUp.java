package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.managers.VFXManager;

import javafx.scene.shape.Rectangle;

public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(double startX, double startY) {
        super("item_shield", startX, startY, 60);
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.setPos(startX, startY);
    }

    @Override
    public void applyEffect(Player player, VFXManager vfxManager) {
        player.activateShieldBuff(10000);
        AudioManager.getInstance().playSound("sfx_item_powerup_lightning");

        if (vfxManager != null) {
            vfxManager.applyPlayerGlow(player,"shield");
            vfxManager.spawnScreenShieldEffect();
        }
    }
}
