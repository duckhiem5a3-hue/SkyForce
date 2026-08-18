package com.nhom27.skyforce.scenes;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.ui.buttons.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelScene {
    private static LevelScene instance;
    private Scene scene;
    private HBox levelCardsContainer;
    private int selectedLevel = 1; // Mặc định chọn Level 1

    // Các biến quản lý thao tác vuốt (Swipe) và Quán tính (Inertia)
    private double mouseAnchorX;               //biến lấy vị trí con trỏ chuột trước khi kéo thả (thời điểm bắt đầu nhấn)
    private double translateAnchorX;
    private double velocityX = 300;
    private double lastMouseX = 0;
    private long lastDragTime = 0;
    private AnimationTimer inertiaTimer;


    // Biến quản lý nút bấm giữ
    private AnimationTimer buttonScrollTimer;

    private static class LevelInfo {
        int levelNumber;
        String name;
        String imageName;

        LevelInfo(int levelNumber, String name, String imageName) {
            this.levelNumber = levelNumber;
            this.name = name;
            this.imageName = imageName;
        }
    }

    private final List<LevelInfo> levelList = new ArrayList<>();

    public LevelScene() {
        instance = this;
        initLevelData();
        createLevelScene();
    }

    public static LevelScene getInstance() {
        return instance;
    }

    private void initLevelData() {
        // Tên các key hình ảnh nền cần đảm bảo đã được load trong AssetManager
        levelList.add(new LevelInfo(1, "LEVEL 1", "background_level_1"));
        levelList.add(new LevelInfo(2, "LEVEL 2", "background_level_2"));
        levelList.add(new LevelInfo(3, "LEVEL 3", "background_level_3"));
        levelList.add(new LevelInfo(4, "LEVEL 4", "background_level_4"));
        levelList.add(new LevelInfo(5, "LEVEL 5", "background_level_5"));
        levelList.add(new LevelInfo(6, "LEVEL 6", "background_level_6"));
    }

    private void createLevelScene() {
        StackPane root = new StackPane();


        // 1. Nền game (Lấy nền chung của menu)
        Image bgImage = AssetManager.getImage("background_home");
        if (bgImage != null) {
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(Main.WIDTH);
            bgImageView.setFitHeight(Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            root.setStyle("-fx-background-color: #0b131e;");
        }


        // 2. Layout tổng VBox
        VBox mainLayout = new VBox(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));    //Đẩy mọi thứ sang phải 20 ô để card đầu tiên không dính vào tường trái, card cuối không dính tường phải


        // Tiêu đề
        Label titleLabel = new Label("CHOOSE LEVEL");
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 36));
        titleLabel.setTextFill(Color.CYAN);
        titleLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 200, 255, 0.8), 12, 0.8, 0, 2));


        // Container chứa tất cả các thẻ (bao gồm các thẻ k có trên màn hình)
        levelCardsContainer = new HBox(220);     //spacing của mỗi thẻ là 180 ngang, 260 dọc
        levelCardsContainer.setAlignment(Pos.CENTER_LEFT);



        // Pane cardView cắt ra phần sẽ hiển thị trên màn hình của levelCardsContainer
        Pane cardView = new Pane(levelCardsContainer);
        cardView.setPrefSize(Main.WIDTH, 300);
        Rectangle clip = new Rectangle(Main.WIDTH, 300);        // Cắt view cho cardView
        cardView.setClip(clip);



        //Cập nhật vị trí của levelCardContainer do bị kéo
        cardView.setOnMousePressed(e -> {           //Hành động nhấn chuột chuẩn bị bắt đầu kéo thả
            inertiaTimer.stop();                                   // Dừng quán tính cũ ngay khi click chuột
            mouseAnchorX = e.getSceneX();                          // Lấy vị trí chuột thời điểm bắt đầu kéo thả
            translateAnchorX = levelCardsContainer.getTranslateX();// Lấy vị trí levelCardContainer tại thời điểm bắt đầu kéo thả
            lastMouseX = e.getSceneX();                            // Biến liên tục cập nhật vị trí chuột suốt thời gian kéo thả mỗi lần setOnMousePressed được gọi, nhưng để đây thì khá dễ gây nhầm lẫn với mouseAnchorX. Có thể xóa ở đây?
            lastDragTime = System.currentTimeMillis();             // Bắt đầu tính thời điểm kéo thả, và sẽ tính lại liên tục giữa mỗi lần setOnMousePressed được gọi
        });
        cardView.setOnMouseDragged(e -> {           //Là loại hàm tự động cập nhật liên tục 
            // Di chuyển HBox theo chuột
            double deltaX = e.getSceneX() - mouseAnchorX;                                   //vị trí mới thay đối so với cũ (cụ thể là thời điểm bắt đầu giữ chuột kéo thả)
            levelCardsContainer.setTranslateX(translateAnchorX + deltaX);                   //thay đổi vị trí của levelCardsContainer một khoảng tương tự
            
            // Tính toán vận tốc tức thời (velocity) sẽ sử dụng sự chênh lệch tọa độ và thời gian giữa 2 lần gọi setOnMouseDragged cuối cùng
            long now = System.currentTimeMillis();
            double timeDelta = (now - lastDragTime) / 1000.0; // Đổi ra giây
            if (timeDelta > 0) {
                velocityX = (e.getSceneX() - lastMouseX);
            }
            
            //liên tục cập nhật lại vị trí chuột và thời gian từ lần gọi hàm setMouseDragged trước đó (để có thể liên tục cập nhật vận tốc tức thì cho quán tính)
            lastMouseX = e.getSceneX();
            lastDragTime = now;      

            //Đảm bảo không kéo ra ngoài phạm vi các thẻ bài
            clampScroll(); 
        });
        cardView.setOnMouseReleased(e -> {
            // Kích hoạt quán tính bay tiếp khi nhả chuột
            inertiaTimer.start();
        });
        //Cập nhật vị trí của levelCardContainer do quán tính sau khi kéo
        inertiaTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (Math.abs(velocityX) > 0.5) {
                    double newX = levelCardsContainer.getTranslateX() + velocityX;
                    levelCardsContainer.setTranslateX(newX);
                    
                    // Ma sát (Friction) - Làm chậm dần
                    velocityX *= 0.96; 
                    
                    // Giới hạn không cho văng quá đà (Clamping)
                    clampScroll();
                } else {
                    velocityX = 0;
                    this.stop(); // Tắt timer khi đã dừng hẳn để tiết kiệm CPU
                }
            }
        };


        refreshLevelCards();



        // 3. Cụm nút bấm phía dưới (BACK và PLAY)
        HBox bottomButtons = new HBox(40);
        bottomButtons.setAlignment(Pos.CENTER);
        CustomButton btnBack = new CustomButton("BACK", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });
        CustomButton btnPlay = new CustomButton("PLAY", "button_green", () -> {
            PlayScene playScene = new PlayScene(selectedLevel);
            SceneManager.getInstance().switchScene(playScene.getScene());
        });
        

        // Thêm hiệu ứng Hover cho 2 button
        btnBack.setOnMouseEntered(e -> btnBack.setEffect(new Glow(0.8)));
        btnBack.setOnMouseExited(e -> btnBack.setEffect(null));
        btnPlay.setOnMouseEntered(e -> btnPlay.setEffect(new Glow(0.8)));
        btnPlay.setOnMouseExited(e -> btnPlay.setEffect(null));


        //Ghép các layout vào với nhau tạo ra hệ thống layout hoàn chỉnh để đưa vào Scene
        bottomButtons.getChildren().addAll(btnBack, btnPlay);
        mainLayout.getChildren().addAll(titleLabel, cardView, bottomButtons);
        root.getChildren().add(mainLayout);
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
    }



    private void clampScroll() {
        // Chiều rộng tổng của tất cả các thẻ + spacing (nói cách khác là chiểu rộng muốn có của levelCardsContainer
        double contentWidth = levelList.size() * 180 + (levelList.size() - 1) * 220;
        double viewCardWidth = Main.WIDTH - 40; //vì 2 card cuối phải cách biên trái/phải 20 pixel

        // Không cho phép kéo lố sang viền trái (TranslateX > 0)
        if (levelCardsContainer.getTranslateX() > 0) {
            levelCardsContainer.setTranslateX(0);
            velocityX = 0; // Đập tường thì dừng quán tính
        } 
        // Không cho phép kéo lố sang viền phải
        else if (levelCardsContainer.getTranslateX() < -(contentWidth - viewCardWidth)) { 
            // Nếu độ rộng muốn có của levelCardContainer còn nhỏ hơn cửa sổ màn hình, k cho kéo
            if (contentWidth < Main.WIDTH) {
                levelCardsContainer.setTranslateX(0);
            } else {
                levelCardsContainer.setTranslateX(-(contentWidth - viewCardWidth));
            }
            velocityX = 0;
        }
    }








    //Hàm cập nhật các thẻ
    private void refreshLevelCards() {
        levelCardsContainer.getChildren().clear();

        for (LevelInfo level : levelList) {
            // Sử dụng StackPane làm thẻ Card để hình nền nằm dưới, chữ và nút đè lên trên
            StackPane card = new StackPane();
            card.setPrefSize(180, 260);
            
            // Cắt góc tròn cho viền card
            CornerRadii radii16 = new CornerRadii(16);
            
            // 1. Cắt (Clip) hình nền level cho vừa thẻ Card và bo góc
            Image levelBgImg = AssetManager.getImage(level.imageName);
            if (levelBgImg != null) {
                ImageView bgView = new ImageView(levelBgImg);
                // Ép kích thước img nền vừa khung card
                bgView.setFitWidth(180);
                bgView.setFitHeight(260);
                
                // Crop góc bo tròn cho ảnh để không tràn viền
                Rectangle clip = new Rectangle(180, 260);
                clip.setArcWidth(32);
                clip.setArcHeight(32);
                bgView.setClip(clip);
                
                card.getChildren().add(bgView);
            } else {
                // Nếu không load được ảnh, dùng nền xám đen fallback
                card.setBackground(new Background(new BackgroundFill(Color.web("#1e2c3a"), radii16, Insets.EMPTY)));
            }

            // 2. Viền Card (Vàng nếu đang chọn, Xanh nếu chưa chọn)
            boolean isSelected = (level.levelNumber == selectedLevel);
            Color borderColor = isSelected ? Color.GOLD : Color.CYAN;
            
            Border defaultBorder = new Border(new BorderStroke(
                    borderColor, BorderStrokeStyle.SOLID, radii16, new BorderWidths(isSelected ? 3 : 2)));  //viền dày hơn xíu với card đc chọn
            Border hoverBorder = new Border(new BorderStroke(
                    Color.GOLD, BorderStrokeStyle.SOLID, radii16, new BorderWidths(3)));     //

            card.setBorder(defaultBorder);
            card.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.8), 10, 0.5, 0, 2));

            // Bố cục phần bên trên của card (Tiêu đề Level và Select Button nằm bên dưới)
            VBox cardOverlay = new VBox(15);
            cardOverlay.setAlignment(Pos.CENTER);
            
            // Phủ một lớp bóng đen mờ đằng sau chữ để chữ không bị chìm vào nền ảnh sáng
            cardOverlay.setStyle("-fx-background-color: rgba(0,0,0, 0.4); -fx-background-radius: 16;");

            Label nameLabel = new Label(level.name);
            nameLabel.setFont(AssetManager.getFont("font_kenvector_future", 16));
            nameLabel.setTextFill(isSelected ? Color.GOLD : Color.WHITE);
            nameLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 4, 1.0, 0, 1));

            CustomButton btnSelect = new CustomButton(isSelected ? "SELECTED" : "SELECT", 
                                                      130, 40, 
                                                      isSelected ? "button_yellow" : "button_blue", 
                                                      () -> {
                // Khi bấm nút select -> cập nhật biến và vẽ lại toàn bộ thẻ
                AudioManager.getInstance().playSound("sfx_click");
                selectedLevel = level.levelNumber;
                refreshLevelCards();
            });

            // 4. XỬ LÝ HOVER THAY ĐỔI VIỀN
            // Khi di chuột vào nút, thẻ card đổi viền vàng (nếu chưa select)
            btnSelect.setOnMouseEntered(e -> {
                if (!isSelected) card.setBorder(hoverBorder);
            });
            btnSelect.setOnMouseExited(e -> {
                if (!isSelected) card.setBorder(defaultBorder);
            });

            cardOverlay.getChildren().addAll(nameLabel, btnSelect);
            card.getChildren().add(cardOverlay);
            
            levelCardsContainer.getChildren().add(card);
        }
    }

    public Scene getScene() {
        return scene;
    }
}