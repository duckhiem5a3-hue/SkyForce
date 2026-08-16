package com.nhom27.skyforce.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PlayerDataManager {
    private static PlayerDataManager instance;
    private final Properties props;

    // Tên file save sẽ nằm cùng thư mục với game
    private final String SAVE_FILE_PATH = "savegame.properties";

    private static final String KEY_TOTAL_GOLD = "total_gold";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_EQUIPPED_SKIN = "equipped_skin";
    private static final String KEY_UNLOCKED_SKINS = "unlocked_skins";

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

    public void checkAndUpdateHighScore(int currentScore) {
        int highestScore = getHighScore();
        if (currentScore > highestScore) {
            props.setProperty(KEY_HIGH_SCORE, String.valueOf(currentScore));
            saveData();
            System.out.println("Kỷ lục mới thiết lập: " + currentScore);
        }
    }

    // ==========================================
    // CÁC HÀM QUẢN LÝ SKIN
    // ==========================================
    public String getEquippedSkin() {
        return props.getProperty(KEY_EQUIPPED_SKIN, "blue");
    }

    public void setEquippedSkin(String skinId) {
        if (skinId == null || skinId.isEmpty())
            return;
        props.setProperty(KEY_EQUIPPED_SKIN, skinId);
        saveData();
    }

    public Set<String> getUnlockedSkins() {
        String raw = props.getProperty(KEY_UNLOCKED_SKINS, "blue");
        String[] split = raw.split(",");
        Set<String> set = new HashSet<>();
        for (String s : split) {
            String trimmed = s.trim(); // xóa khoảng trống " "
            if (!trimmed.isEmpty()) {
                set.add(trimmed);
            }
        }
        set.add("blue"); // blue luôn được mở khóa mặc định
        return set;
    }

    public boolean isSkinUnlocked(String skinId) {
        if ("blue".equalsIgnoreCase(skinId))
            return true;
        return getUnlockedSkins().contains(skinId);
    }

    public boolean buySkin(String skinId, int price) {
        if (isSkinUnlocked(skinId)) {
            setEquippedSkin(skinId);
            return true;
        }
        int currentGold = getTotalGold();
        if (currentGold >= price) {
            int newGold = currentGold - price;
            props.setProperty(KEY_TOTAL_GOLD, String.valueOf(newGold));

            Set<String> unlocked = getUnlockedSkins();
            unlocked.add(skinId);
            props.setProperty(KEY_UNLOCKED_SKINS, String.join(",", unlocked));
            props.setProperty(KEY_EQUIPPED_SKIN, skinId);
            saveData();
            System.out.println("Đã mua thành công skin: " + skinId + " với giá " + price + " gold");
            return true;
        }
        System.out.println("Không đủ gold để mua skin: " + skinId);
        return false;
    }
}