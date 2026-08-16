package com.nhom27.skyforce.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShooterEnemy extends EnemyObject {
    public static int appearTimes = 0;
    public static final double startHeight = 10;

    private double speedX;
    private boolean direction;      //true: go right (x+) . False: go left (x-)

    private double margins;
    private Shape closeBox;
    private long dodgeCooldown;
    private boolean canDodge;

    private static List<EnemyBullet> enemyBulletsList = new ArrayList<>(); //đặt là static để việc cập nhật đạn không phụ thuộc vào việc enemy đã chết chưa
    private long updatePassed;
    private long fireSpeed;     //amount of update needed to fire bullet

    public ShooterEnemy(double startX, double startY) {
        super("enemy_shooter", startX, startY);
        this.hitbox = AssetManager.getSpriteInfo("enemy_shooter").getHitbox();
        this.setPos(startX, startY);
        this.speedX = 150;
        this.health = 200;
        this.margins = 40;
        this.closeBox = new Rectangle(sizeX + margins*2 , 300);
        appearTimes++;
        this.updatePassed = 0;
        this.fireSpeed = 50;
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
    public long getDodgeCooldown() {
        return this.dodgeCooldown;
    }
    public void setDodgeCoolDown() {
        this.dodgeCooldown = 500 + System.currentTimeMillis();
    }
    public boolean getCanDodge() {
        return canDodge;
    }
    public void lockDodge() {
        this.canDodge = false;
    }
    public static List<EnemyBullet> getBulletList() {
        return enemyBulletsList;
    }
    public static void addBullet(EnemyBullet eBullet) {
        enemyBulletsList.add(eBullet);
    }
    public boolean timeToFire() {
        if(updatePassed == fireSpeed) {
            updatePassed -= fireSpeed;
            return true;
        }
        return false;
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
        updatePassed++;
        if(dodgeCooldown < System.currentTimeMillis()) {   //hết thời gian cấm đổi hướng 
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
