package com.nhom27.skyforce.utils;

import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class SpriteInfo {
    private final Image image;
    private final double[] hitboxPoints;

    public SpriteInfo(Image image, double[] hitboxPoints) {
        this.image = image;
        this.hitboxPoints = hitboxPoints;
    }

    public SpriteInfo(Image image) {
        this.image = image;
        this.hitboxPoints = null;
    }

    public Image getImage() {
        return image;
    }

    public Shape getHitbox() {
        if (hitboxPoints == null)
            return null;
        return new Polygon(hitboxPoints);
    }
}
