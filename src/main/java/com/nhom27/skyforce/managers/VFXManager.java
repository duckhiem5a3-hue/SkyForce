package com.nhom27.skyforce.managers;

import com.nhom27.skyforce.utils.AssetManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class VFXManager {
    private Pane gamePane;

    public VFXManager(Pane gamePane) {
        this.gamePane = gamePane;
    }

    // Hiệu ứng tia lửa khi đạn trúng quái
    public void spawnImpactEffect(double x, double y) {
        ImageView effectView = new ImageView(AssetManager.getImage("vfx_impact_blue_01"));

        // Tự động căn tâm ảnh vào đúng tọa độ x, y
        double width = effectView.getImage().getWidth();
        double height = effectView.getImage().getHeight();
        effectView.setLayoutX(x - width / 2);
        effectView.setLayoutY(y - height / 2);

        gamePane.getChildren().add(effectView);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    effectView.setImage(AssetManager.getImage("vfx_impact_blue_02"));
                }),
                new KeyFrame(Duration.millis(80), e -> {
                    effectView.setScaleX(1.5); // Phóng to gấp rưỡi
                    effectView.setScaleY(1.5);
                    effectView.setOpacity(0.4); // Làm mờ đi chỉ còn 40%
                }),
                new KeyFrame(Duration.millis(100), e -> {
                    gamePane.getChildren().remove(effectView);
                }));
        timeline.setCycleCount(1); // Yêu cầu hiệu ứng chạy 1 lần
        timeline.play();
    }
}