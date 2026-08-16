package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

public class StraightEnemy extends NormalEnemy {

    public StraightEnemy(double startX, double startY) {
        super(startX, startY);
    }

    public StraightEnemy(double startX, double startY, double speedY) {
        super(startX, startY, speedY);
    }
}
