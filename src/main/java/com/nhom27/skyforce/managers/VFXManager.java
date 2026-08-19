package com.nhom27.skyforce.managers;

import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class VFXManager {
    private Pane gamePane;

    public VFXManager(Pane gamePane) {
        this.gamePane = gamePane;
    }

    // Hiệu ứng tia lửa khi đạn trúng quái (tự động đổi theo skin của người chơi)
    public void spawnImpactEffect(double x, double y, String skinId) {
        String skin = (skinId != null && !skinId.isEmpty()) ? skinId.toLowerCase() : "blue";
        Image img1 = AssetManager.getImage("vfx_hit_player_" + skin + "_1");
        Image img2 = AssetManager.getImage("vfx_hit_player_" + skin + "_2");

        if (img1 == null) {
            img1 = AssetManager.getImage("vfx_hit_player_blue_1");
            img2 = AssetManager.getImage("vfx_hit_player_blue_2");
        }

        if (img1 == null)
            return;

        ImageView effectView = new ImageView(img1);

        // Tự động căn tâm ảnh vào đúng tọa độ x, y
        double width = img1.getWidth();
        double height = img1.getHeight();
        effectView.setLayoutX(x - width / 2);
        effectView.setLayoutY(y - height / 2);

        gamePane.getChildren().add(effectView);

        Image finalImg2 = img2;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    if (finalImg2 != null) {
                        effectView.setImage(finalImg2);
                    }
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

    // Lớp quản lý 1 vụ nổ riêng biệt (chuẩn Hướng đối tượng OOP cơ bản)
    private static class ExplosionAnimation {
        private int currentFrame = 0;

        public ExplosionAnimation(Pane gamePane, Image sheet, double x, double y, double targetWidth,
                double targetHeight) {
            int cols = 8;
            int rows = 8;
            int totalFrames = cols * rows;

            double frameWidth = sheet.getWidth() / cols;
            double frameHeight = sheet.getHeight() / rows;

            ImageView effectView = new ImageView(sheet);
            effectView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
            effectView.setFitWidth(targetWidth);
            effectView.setFitHeight(targetHeight);
            effectView.setLayoutX(x - targetWidth / 2);
            effectView.setLayoutY(y - targetHeight / 2);

            gamePane.getChildren().add(effectView);

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
                if (currentFrame < totalFrames) {
                    int col = currentFrame % cols;
                    int row = currentFrame / cols;

                    double frameX = col * frameWidth;
                    double frameY = row * frameHeight;

                    effectView.setViewport(new Rectangle2D(frameX, frameY, frameWidth, frameHeight));
                }
                currentFrame++;
            }));

            timeline.setCycleCount(totalFrames);
            timeline.setOnFinished(e -> gamePane.getChildren().remove(effectView));
            timeline.play();
        }
    }

    public void spawnExplosionSpriteSheet(double x, double y, double targetWidth, double targetHeight) {
        Image sheet = AssetManager.getImage("vfx_explosion_8x8_sheet");
        if (sheet == null)
            return; // Tránh lỗi nếu chưa có ảnh

        // Mỗi lần nổ sẽ tạo ra 1 đối tượng vụ nổ riêng biệt
        new ExplosionAnimation(gamePane, sheet, x, y, targetWidth, targetHeight);
    }

    public void applyPlayerGlow(Player player, String cases) {
        if (player.getView() == null)
            return;

        // Dừng hiệu ứng cũ nếu có (Cơ chế Đè hiệu ứng ưu tiên cái mới nhất)
        if (player.getGlowTimer() != null) {
            player.getGlowTimer().stop();
        }

        DropShadow glow = new DropShadow();
        double durations = 800; // default cho heal và damaged

        // Cài đặt màu sắc và thời gian
        if (cases.equals("heal")) {
            glow.setColor(Color.LIMEGREEN);
        } else if (cases.equals("damaged")) {
            glow.setColor(Color.RED);
        } else if (cases.equals("shield")) {
            glow.setColor(Color.DEEPSKYBLUE);
            durations = player.getShieldBuffTimeRemaining();
        } else if (cases.equals("buffed")) {
            glow.setColor(Color.GOLD);
            durations = player.getSeekerBuffTimeRemaining();
        }

        // Cài đặt thông số tỏa sáng
        glow.setRadius(25);
        glow.setSpread(0.6);

        player.getView().setEffect(glow);

        // Xử lý logic khôi phục sau khi hết thời gian
        PauseTransition delay = new PauseTransition(Duration.millis(durations));
        delay.setOnFinished(e -> {
            // Luôn kiểm tra trạng thái thực tế của Player thay vì chỉ dựa vào chuỗi "cases"
            if (player.isSeekerActive() && !cases.equals("buffed")) {
                applyPlayerGlow(player, "buffed");
            } else {
                // Trả về nguyên trạng
                DropShadow defaultOutline = new DropShadow();
                defaultOutline.setColor(Color.WHITE);
                defaultOutline.setRadius(5);
                defaultOutline.setSpread(0.6);
                player.getView().setEffect(defaultOutline);
            }
        });

        player.setGlowTimer(delay);
        delay.play();
    }

    public void spawnScreenEffect(String effectType) {
        double width = (gamePane != null && gamePane.getWidth() > 0) ? gamePane.getWidth() : Main.WIDTH;
        double height = (gamePane != null && gamePane.getHeight() > 0) ? gamePane.getHeight() : Main.HEIGHT;

        // 1. Tạo hình chữ nhật kích thước bằng màn hình
        Rectangle screenOverlay = new Rectangle(width, height);

        // 2. Tạo hiệu ứng viền xanh lá (Vignette) bằng RadialGradient
        // Trong suốt ở tâm, tỏa màu xanh lá mượt ra phía mép màn hình
        // viền đỏ nếu nhận sát thương

        RadialGradient healGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(46, 204, 113, 0.75)) // Màu xanh lá neon hồi máu
        );
        RadialGradient damageGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(255, 60, 0, 0.75)) // Màu đỏ báo động trúng đạn
        );
        RadialGradient shieldGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(0, 191, 255, 0.8))); // Màu xanh báo hiệu nhặt khiên
        RadialGradient buffGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(0, 255, 255, 0.8))); // Màu xanh báo hiệu cường hóa

        switch (effectType) {
            case "heal":
                screenOverlay.setFill(healGradient);
                break;
            case "damaged":
                screenOverlay.setFill(damageGradient);
                break;
            case "shield":
                screenOverlay.setFill(shieldGradient);
                break;
            case "buffed":
                screenOverlay.setFill(buffGradient);
                break;
            default:
                System.out.println("Lỗi hiệu ứng: " + effectType);
                return;
        }

        // Đảm bảo không cản trở tương tác chuột
        screenOverlay.setMouseTransparent(true);

        // Thêm lớp phủ vào Pane màn hình
        gamePane.getChildren().add(screenOverlay);

        // 3. Hiệu ứng mờ dần FadeOut
        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), screenOverlay);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> gamePane.getChildren().remove(screenOverlay));
        fadeOut.play();
    }
}