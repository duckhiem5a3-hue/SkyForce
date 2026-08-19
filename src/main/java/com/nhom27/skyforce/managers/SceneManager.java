package com.nhom27.skyforce.managers;

import java.util.HashMap;
import java.util.Map;

import com.nhom27.skyforce.scenes.MenuScene;
import com.nhom27.skyforce.scenes.ShopScene;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    // Đảm bảo được khởi tạo 1 lần duy nhất
    private static SceneManager instance;
    private Stage primaryStage;
    private final Map<String, Scene> scenes = new HashMap<>();

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public void switchScene(String name) {
        if ("MenuScene".equals(name)) {
            com.nhom27.skyforce.audio.AudioManager.getInstance().playMusic("background_home_music");
            if (MenuScene.getInstance() != null) {
                MenuScene.getInstance().onShown();
            }
        } else if ("ShopScene".equals(name)) {
            com.nhom27.skyforce.audio.AudioManager.getInstance().playMusic("background_home_music");
            if (ShopScene.getInstance() == null) {
                ShopScene shopScene = ShopScene.getInstance();
                addScene("ShopScene", shopScene.getScene());
            }
            ShopScene.getInstance().onShown();
        } else if ("PlayScene".equals(name)) {
            com.nhom27.skyforce.audio.AudioManager.getInstance().playMusic("background_play_music");
        }

        if (scenes.containsKey(name)) {
            primaryStage.setScene(scenes.get(name));
        }
    }

}
