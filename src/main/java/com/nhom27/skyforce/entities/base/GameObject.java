package com.nhom27.skyforce.entities.base;

import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

import com.nhom27.skyforce.utils.AssetManager;

public abstract class GameObject {
    // 1. Các thuộc tính Logic (Data)
    protected double x, y; // đa số các method làm việc với Node như setLayout, setFitWidth/Height đều nhận
                           // kiểu double
    protected int timeLived;
    protected ImageView view; // 2. Thuộc tính Giao diện (View)
    protected Boolean isAlive;
    protected Shape hitbox; // Thuộc tính đa giác (dùng để kiểm tra va chạm)

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
        return Boolean.TRUE.equals(this.isAlive);
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

    public GameObject(String nameImage, double startX, double startY, double sizeX, double sizeY) {
        this(nameImage, startX, startY);
        if (this.view != null) {
            this.view.setFitHeight(sizeY);
            this.view.setFitWidth(sizeX);
        }
    }

    public GameObject(String nameImage, double startX, double startY) {
        Image img = AssetManager.getImage(nameImage);
        if (img != null) {
            this.view = new ImageView(img);
        } else {
            this.view = new ImageView();
        }
        this.x = startX;
        this.y = startY;
        this.isAlive = true;
        this.view.setLayoutX(x);
        this.view.setLayoutY(y);
    }

    public abstract void update();
}
