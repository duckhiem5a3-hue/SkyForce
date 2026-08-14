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
import javafx.scene.effect.Effect;
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
    int currentFrame;

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

    public void spawnExplosionSpriteSheet(double x, double y, double targetWidth, double targetHeight) {
        Image sheet = AssetManager.getImage("vfx_explosion_8x8_sheet");
        if (sheet == null)
            return; // Tránh lỗi nếu chưa có ảnh

        // 1. Khai báo thông số của Sprite Sheet
        int cols = 8;
        int rows = 8;
        int totalFrames = cols * rows; // Tổng cộng 64 khung hình

        // 2. Tính toán kích thước của 1 khung hình (1 ô vuông)
        double frameWidth = sheet.getWidth() / cols;
        double frameHeight = sheet.getHeight() / rows;

        ImageView effectView = new ImageView(sheet);

        // Cắt lấy ô đầu tiên (cột 0, hàng 0) để hiển thị lúc mới sinh ra
        effectView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        effectView.setFitWidth(targetWidth);
        effectView.setFitHeight(targetHeight);

        // Căn tâm vụ nổ vào tọa độ x, y
        effectView.setLayoutX(x - targetWidth / 2);
        effectView.setLayoutY(y - targetHeight / 2);
        gamePane.getChildren().add(effectView);

        // 3. Biến đếm khung hình hiện tại
        currentFrame = 0;

        // 4. Tạo Timeline lật trang
        // Tốc độ 16ms/frame tương đương khoảng 60 FPS. Bạn có thể tăng lên 20ms-30ms
        // nếu muốn nổ chậm hơn.
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            if (currentFrame < totalFrames) {
                // Công thức toán học đỉnh cao để tính vị trí cột và hàng từ 1 con số
                int col = currentFrame % cols;
                int row = currentFrame / cols;

                // Tính tọa độ X, Y của khung cửa sổ trên tấm ảnh lớn
                double frameX = col * frameWidth;
                double frameY = row * frameHeight;

                // Dịch chuyển khung cửa sổ
                effectView.setViewport(new Rectangle2D(frameX, frameY, frameWidth, frameHeight));
            }
            currentFrame++;
        }));

        // Cho Timeline chạy đúng 64 lần (64 khung hình)
        timeline.setCycleCount(totalFrames);

        // Khi chạy xong toàn bộ, tự động xóa ảnh để dọn rác bộ nhớ
        timeline.setOnFinished(e -> gamePane.getChildren().remove(effectView));

        timeline.play();
    }

    public void applyPlayerGlow(Player player, String cases) {
        if (player.getView() == null) return;


        if (player.getGlowTimer() != null) { //nếu đang ở trong 1 trong 3 trạng thái (đc buff/ hồi máu/nhận dame)
            player.getGlowTimer().stop();    //cắt trạng thái (delay) đó, vì đã nhận được trạng thái mới (đồng nghĩa với bỏ hành động khi kết thúc đếm ngược) 
        }                                    

        DropShadow glow = new DropShadow();
        double durations = 800; // default

        if (cases.equals("heal")) {
            glow.setColor(Color.LIMEGREEN);
        } else if (cases.equals("damaged")) {
            glow.setColor(Color.RED);
        } else if (cases.equals("shield")) {
            glow.setColor(Color.DEEPSKYBLUE);    
            durations = 1200;
        } else if (cases.equals("buffed")) {
            glow.setColor(Color.GOLD);

            /*Lấy thời gian trực tiếp từ Player. 
            -> Nếu được gọi độc lập khi đang không trong trạng thái buff vàng để kích buff vàng mới:
               -> Hàm activateSeekerBuff sẽ luôn đc gọi trước nó (trong class SeekerPowerUp, hàm activateSeekerBuff)
                  và reset thời gian của getSeekerBuffTimeRemaining() về 10000

            -> Nếu được gọi dưới tư cách 1 hàm đệ quy (bởi 1 hàm gọi hiệu ứng màu xanh/đỏ, khi mà 1 hàm gọi buff vàng trước đó vẫn chưa hết thời gian (dòng 157)
               -> Lấy thời gian hiện có của hàm gọi buff vàng trước đó 
            */
            durations = player.getSeekerBuffTimeRemaining(); 
        }

        glow.setRadius(25);
        if(cases == "shield") {glow.setRadius(35);}
        glow.setSpread(0.6);
        if(cases == "shield") {glow.setSpread(0.8);}

        player.getView().setEffect(glow);

        PauseTransition delay = new PauseTransition(Duration.millis(durations));
        delay.setOnFinished(e -> {
            if (!cases.equals("buffed") && player.isSeekerActive()) {
                applyPlayerGlow(player, "buffed"); 
            } else {
                player.getView().setEffect(null); //tiền đề cho trạng thái dòng 122
            }
        });

        //lưu timer mới, trường hợp có hiệu ứng khác chen ngang thì hủy 
        player.setGlowTimer(delay);
        delay.play();

    }

    public void spawnScreenEffect(boolean addHealth) {
        double width = (gamePane != null && gamePane.getWidth() > 0) ? gamePane.getWidth() : Main.WIDTH;
        double height = (gamePane != null && gamePane.getHeight() > 0) ? gamePane.getHeight() : Main.HEIGHT;

        // 1. Tạo hình chữ nhật kích thước bằng màn hình
        Rectangle screenOverlay = new Rectangle(width, height);

        // 2. Tạo hiệu ứng viền xanh lá (Vignette) bằng RadialGradient
        // Trong suốt ở tâm, tỏa màu xanh lá mượt ra phía mép màn hình
        //viền đỏ nếu nhận sát thương 

        RadialGradient vignetteGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(46, 204, 113, 0.75)) // Màu xanh lá neon hồi máu
        );
        RadialGradient vignetteGradient2 = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(255, 60, 0, 0.75)) // Màu xanh lá neon hồi máu
        );
        if(addHealth) {
            screenOverlay.setFill(vignetteGradient);
        } else {
            screenOverlay.setFill(vignetteGradient2);
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



    public void spawnScreenSeekerEffect() {
        double width = (gamePane != null && gamePane.getWidth() > 0) ? gamePane.getWidth() : Main.WIDTH;
        double height = (gamePane != null && gamePane.getHeight() > 0) ? gamePane.getHeight() : Main.HEIGHT;

        Rectangle screenOverlay = new Rectangle(width, height);

        RadialGradient vignetteGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(0, 225, 255, 0.75)));
        screenOverlay.setFill(vignetteGradient);
        screenOverlay.setMouseTransparent(true);

        gamePane.getChildren().add(screenOverlay);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), screenOverlay);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> gamePane.getChildren().remove(screenOverlay));
        fadeOut.play();
    }

    

    public void spawnScreenShieldEffect() {
        double width = (gamePane != null && gamePane.getWidth() > 0) ? gamePane.getWidth() : Main.WIDTH;
        double height = (gamePane != null && gamePane.getHeight() > 0) ? gamePane.getHeight() : Main.HEIGHT;

        Rectangle screenOverlay = new Rectangle(width, height);

        RadialGradient vignetteGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.4, Color.TRANSPARENT),
                new Stop(1.0, Color.rgb(0, 191, 255, 0.8)));
        screenOverlay.setFill(vignetteGradient);
        screenOverlay.setMouseTransparent(true);

        gamePane.getChildren().add(screenOverlay);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), screenOverlay);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> gamePane.getChildren().remove(screenOverlay));
        fadeOut.play();
    }
}