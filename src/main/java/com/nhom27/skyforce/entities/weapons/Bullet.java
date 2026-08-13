package com.nhom27.skyforce.entities.weapons;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.main.Main;
import javafx.scene.shape.Rectangle;

public class Bullet extends GameObject {
    protected int damage;
    // Vector vận tốc của đạn
    protected double speedX;
    protected double speedY;

    // Tối ưu
    public Bullet(String nameImage, double startX, double startY, double speedX, double speedY, int damage) {
        super(nameImage, startX, startY);
        this.speedX = speedX;
        this.speedY = speedY;
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.damage = damage;

        double angle = Math.toDegrees(Math.atan2(speedY, speedX)) + 90; // Ảnh viên đạn hướng lên
        this.setPos(startX - sizeX / 2, startY - sizeY, angle);
    }

    public int getDamage() {
        return damage;
    }

    public void update() {
        x += speedX / 60;
        y += speedY / 60;
        this.setPos(x, y);
        if (this.x < 0 || this.x > Main.WIDTH || this.y < 0 || this.y > Main.HEIGHT) {
            this.isAlive = false;
        }
        ;
    }
    /*
     * các class con StraightBullet và DiagonalBullet bị xóa bỏ
     * Field incline được sinh ra để tạo độ nghiêng cho đạn
     * incline dương: nghiêng sang phải
     * incline âm: nghiêng sang trái
     * incline bằng 0: không nghiêng (straight bullet)
     */
}
