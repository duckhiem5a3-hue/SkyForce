package com.nhom27.skyforce.entities.base;

import javafx.scene.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import com.nhom27.skyforce.utils.AssetManager;

public abstract class GameObject {
    // 1. Các thuộc tính Logic (Data)
    protected double x, y; // đa số các method làm việc với Node như setLayout, setFitWidth/Height đều nhận
                           // kiểu double
    protected ImageView view; // 2. Thuộc tính Giao diện (View)
    protected boolean isAlive;
    protected int timeLived;
    protected Shape hitbox; // Thuộc tính đa giác (dùng để kiểm tra va chạm)
    protected double sizeX;
    protected double sizeY;

    public Node getView() {
        return this.view;
    }

    public void setView(String skin) {
        Image img = AssetManager.getImage(skin);
        if (img != null) {
            this.view = new ImageView(img);
        }
    }

    public Shape getHitbox() {
        return this.hitbox;
    }

    public boolean isAlive() {
        return this.isAlive == true;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    // (Đã dùng trong Bullet)
    public void setPos(double currentX, double currentY, double rotateAngle) {
        setPos(currentX, currentY);
        if (this.view != null) {
            this.view.setRotate(rotateAngle);
        }
        if (this.hitbox != null) {
            this.hitbox.setRotate(rotateAngle);
        }
    }

    public void setPos(double currentX, double currentY) {
        this.x = currentX;
        this.y = currentY;
        // Cập nhật vị trí của ảnh
        if (this.view != null) {
            this.view.setLayoutX(x);
            this.view.setLayoutY(y);
        }
        // Cập nhật vị trí hình đa giác
        if (this.hitbox != null) {
            this.hitbox.setLayoutX(x);
            this.hitbox.setLayoutY(y);
        }
    }

    // Tối ưu
    public GameObject(String nameImage, double startX, double startY) {
        Image img = AssetManager.getImage(nameImage);
        if (img != null) {
            this.view = new ImageView(img);
            sizeX = img.getWidth();
            sizeY = img.getHeight();
            this.view.setFitHeight(sizeY);
            this.view.setFitWidth(sizeX);

            DropShadow outline = new DropShadow();
            outline.setColor(Color.WHITE);
            outline.setRadius(5); // Độ nhòe của viền
            outline.setSpread(0.6); // Độ đậm đặc của viền (0.0 đến 1.0)
            this.view.setEffect(outline);
        } else {
            this.view = new ImageView();
        }
        this.isAlive = true;
    }

    public abstract void update();
}
