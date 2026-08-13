package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.managers.VFXManager;

import javafx.scene.shape.Rectangle;

public class SeekerPowerUp extends PowerUp {
    public SeekerPowerUp(double startX, double startY) {
        super("item_powerup_lightning", startX, startY, 100);
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.setPos(startX, startY);
    }

    @Override
    public void applyEffect(Player player, VFXManager vfxManager) {
        player.activateSeekerBuff(10000);
        AudioManager.getInstance().playSound("sfx_item_powerup_lightning");

        if (vfxManager != null) {
            vfxManager.applyPlayerSeekerGlow(player);
            vfxManager.spawnScreenSeekerEffect();
        }
    }
}
