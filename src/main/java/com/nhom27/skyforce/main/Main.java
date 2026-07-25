package com.nhom27.skyforce.main;

import com.nhom27.skyforce.audio.AudioManager;

import com.nhom27.skyforce.scenes.MenuScene;
import com.nhom27.skyforce.scenes.SceneManager;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // Kích thước của cửa sổ game
    public static final int WIDTH = 1200;
    public static final int HEIGHT = WIDTH * 3 / 4; // = 900

    @Override
    public void start(Stage primaryStage) {

        // Đưa "gói 2D" lên cửa sổ hiển thị trò chơi
        primaryStage.setTitle("Sky Force - Nhóm 27");

        // Tải tài nguyên (Ảnh & Âm Thanh) -- Chưa code load âm thanh
        AssetManager.loadAllAsset();

        // Bật Nhạc nền
        AudioManager.getInstance();

        // Giao cửa sổ cho SceneManager quản lý
        SceneManager.getInstance().setPrimaryStage(primaryStage);

        // Bật Menu trang chủ
        MenuScene menuScene = new MenuScene();
        SceneManager.getInstance().addScene("MenuScene", menuScene.getScene());
        SceneManager.getInstance().switchScene("MenuScene");

        // Không cho người dùng thay đổi kích thước cửa sổ
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}