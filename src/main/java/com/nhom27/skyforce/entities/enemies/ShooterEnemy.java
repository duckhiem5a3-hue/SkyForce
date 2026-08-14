package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShooterEnemy extends EnemyObject {
    public static double startHeight = 100;
    private double speedX;
    private boolean direction;      //true: go right (x+) . False: go left (x-)
    private double margins;
    private Shape closeBox;
    private double dodgeCooldown;
    private boolean canDodge;
    public ShooterEnemy(double startX, double startY) {
        super("enemy_shooter", startX, startY);
        this.hitbox = AssetManager.getSpriteInfo("enemy_shooter").getHitbox();
        this.setPos(startX, startY);
        this.speedX = 200;
        this.health = 200;
        this.margins = 40;
        this.closeBox = new Rectangle(sizeX + margins*2 , 300);
    }
    public Shape getCloseBox() {
        return closeBox;
    }
    public void setDirection(boolean direction) {
        this.direction = direction;
    }
    public boolean getDirection() {
        return direction;
    }
    public double getDodgeCooldown() {
        return this.dodgeCooldown;
    }
    public void setCoolDown() {
        this.dodgeCooldown = 500 + System.currentTimeMillis();
    }
    public boolean getCanDodge() {
        return canDodge;
    }
    public void lockDodge() {
        this.canDodge = false;
    }

    @Override
    public void setPos(double currentX, double currentY){
        super.setPos(currentX, currentY);
        if (this.closeBox != null) {
            this.closeBox.setLayoutX(currentX - margins);
            this.closeBox.setLayoutY(y); 
        }
    }
    @Override
    public void update() {
        if(dodgeCooldown - System.currentTimeMillis() < 0) {   //hết thời gian cấm đổi hướng 
            canDodge = true;
        }
    
        if(x <=0) {setPos(Main.WIDTH - sizeX,startHeight);}
        else if(x >= Main.WIDTH - sizeX) {setPos(0,startHeight);}

        if(direction) {
            x += speedX/60;
            setPos(x, y);
        } else {
            x -= speedX/60;
            setPos(x, y);
        }
        checkDeath();
    }
}
