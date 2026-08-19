package com.nhom27.skyforce.ui;

import com.nhom27.skyforce.utils.AssetManager;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class CustomButton extends Button {
    private Label textLabel;
    private ImageView buttonImageView;

    public CustomButton(String text, String nameImage, Runnable action) {
        this(text, 333, 58, nameImage, action);

    }

    private void setupButtonStyle() {
        this.setBackground(Background.EMPTY); // Bỏ nền xám mặc định
        this.setPadding(Insets.EMPTY); // Bỏ viền lề mặc định
        this.setCursor(Cursor.HAND); // Đổi con trỏ chuột thành bàn tay khi hover
    }

    public CustomButton(Integer width, Integer height, String nameImage, Runnable action) {
        setupButtonStyle();
        Image buttonImage = AssetManager.getImage(nameImage);
        buttonImageView = new ImageView(buttonImage);

        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false); // Ảnh co dãn kích bằng với kích thước

        StackPane graphicContainer = new StackPane();
        graphicContainer.getChildren().addAll(buttonImageView);
        this.setGraphic(graphicContainer);

        this.setOnAction(event -> {
            if (action != null) {
                action.run();
            }
        });

        applyJuicyHoverEffect(nameImage);
    }

    public CustomButton(String text, Integer width, Integer height, String nameImage, Runnable action) {
        setupButtonStyle();
        Image buttonImage = AssetManager.getImage(nameImage);
        buttonImageView = new ImageView(buttonImage);

        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false); // Ảnh co dãn kích bằng với kích thước

        textLabel = new Label(text);
        textLabel.setTextFill(Color.BLACK);
        Font customFont = AssetManager.getFont("font_kenvector_future_thin", 24);
        textLabel.setFont(customFont);

        StackPane graphicContainer = new StackPane();
        graphicContainer.getChildren().addAll(buttonImageView, textLabel);
        this.setGraphic(graphicContainer);

        this.setOnAction(event -> {
            if (action != null) {
                action.run();
            }
        });

        applyJuicyHoverEffect(nameImage);
    }

    public void updateLabel(String newText) {
        if (textLabel != null) {
            textLabel.setText(newText);
        }
    }

    // ==========================================================
    // HÀM HIỆU ỨNG TỰ ĐỘNG (JUICY HOVER EFFECT)
    // ==========================================================
    private void applyJuicyHoverEffect(String nameImage) {
        // 1. Tự động quét tên file ảnh để chọn màu Hào quang tương ứng
        Color glowColor = Color.WHITE; // Màu mặc định
        if (nameImage != null) {
            String lowerName = nameImage.toLowerCase();
            if (lowerName.contains("button_blue"))
                glowColor = Color.rgb(0, 191, 255, 0.8); // Xanh dương
            else if (lowerName.contains("button_green"))
                glowColor = Color.rgb(46, 204, 113, 0.8); // Xanh lá
            else if (lowerName.contains("button_yellow"))
                glowColor = Color.rgb(255, 215, 0, 0.8); // Vàng
            else if (lowerName.contains("button_red"))
                glowColor = Color.rgb(255, 60, 0, 0.8); // Đỏ cam
        }

        DropShadow neonGlow = new DropShadow(BlurType.GAUSSIAN, glowColor, 25, 0.6, 0, 0);

        // 2. Hiệu ứng phóng to (Scale Up) lên 110% trong 150ms
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), this);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        // 3. Hiệu ứng thu nhỏ (Scale Down) về 100% trong 150ms
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), this);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // 4. Bắt sự kiện chuột di vào
        this.setOnMouseEntered(e -> {
            this.setEffect(neonGlow); // Bật hào quang
            scaleDown.stop(); // Phanh gấp hiệu ứng thu nhỏ (nếu có)
            scaleUp.play(); // Bật nảy to
        });

        // 5. Bắt sự kiện chuột đi ra
        this.setOnMouseExited(e -> {
            this.setEffect(null); // Tắt hào quang
            scaleUp.stop(); // Phanh gấp hiệu ứng phóng to (nếu có)
            scaleDown.play(); // Thu về bình thường
        });
    }
}