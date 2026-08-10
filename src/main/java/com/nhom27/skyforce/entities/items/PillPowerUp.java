package com.nhom27.skyforce.entities.items;

import com.nhom27.skyforce.entities.player.Player;

import javafx.scene.shape.Rectangle;

public class PillPowerUp extends PowerUp {
    public PillPowerUp(double startX, double startY) {
        super("item_pill_blue", startX, startY, 100);
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.setPos(startX, startY);
    }

    @Override
    public void applyEffect(Player player) {
        player.heal(20);
        player.addXp(50); // Cộng thêm XP để thăng cấp
    }
}