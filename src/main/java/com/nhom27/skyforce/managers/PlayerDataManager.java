package com.nhom27.skyforce.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PlayerDataManager {
    private static PlayerDataManager instance;
    private final Properties props;

    // Tên file save sẽ nằm cùng thư mục với game
    private final String SAVE_FILE_PATH = "savegame.properties";

    private static final String KEY_TOTAL_GOLD = "total_gold";
    private static final String KEY_HIGH_SCORE = "high_score";

    private PlayerDataManager() {
        props = new Properties();
        loadData(); // Tự động load dữ liệu từ file khi khởi động
    }

    public static synchronized PlayerDataManager getInstance() {
        if (instance == null) {
            instance = new PlayerDataManager();
        }
        return instance;
    }

    // ==========================================
    // CÁC HÀM XỬ LÝ FILE (ĐỌC / GHI)
    // ==========================================
    private void loadData() {
        File file = new File(SAVE_FILE_PATH);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (Exception e) {
                System.err.println("Lỗi đọc file save: " + e.getMessage());
            }
        }
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(SAVE_FILE_PATH)) {
            props.store(fos, "Sky Force - Player Save Data");
        } catch (Exception e) {
            System.err.println("Lỗi ghi file save: " + e.getMessage());
        }
    }

    // ==========================================
    // CÁC HÀM QUẢN LÝ VÀNG (GOLD)
    // ==========================================
    public int getTotalGold() {
        // Nếu không tìm thấy, mặc định là 0
        return Integer.parseInt(props.getProperty(KEY_TOTAL_GOLD, "0"));
    }

    public void addGold(int amount) {
        if (amount <= 0)
            return;
        int newTotal = getTotalGold() + amount;
        props.setProperty(KEY_TOTAL_GOLD, String.valueOf(newTotal));
        saveData(); // Ghi ngay ra file
    }

    public void setTotalGold(int amount) {
        props.setProperty(KEY_TOTAL_GOLD, String.valueOf(Math.max(0, amount)));
        saveData();
    }

    // ==========================================
    // CÁC HÀM QUẢN LÝ ĐIỂM CAO NHẤT (HIGH SCORE)
    // ==========================================
    public int getHighScore() {
        return Integer.parseInt(props.getProperty(KEY_HIGH_SCORE, "0"));
    }

    // Hàm này rất hay: Bạn cứ ném điểm khi qua màn vào đây, nó tự kiểm tra
    public void checkAndUpdateHighScore(int currentScore) {
        int highestScore = getHighScore();
        if (currentScore > highestScore) {
            props.setProperty(KEY_HIGH_SCORE, String.valueOf(currentScore));
            saveData();
            System.out.println("Kỷ lục mới thiết lập: " + currentScore);
        }
    }
}