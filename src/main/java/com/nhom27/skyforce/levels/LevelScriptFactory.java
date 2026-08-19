package com.nhom27.skyforce.levels;

/**
 * Class Factory chịu trách nhiệm khởi tạo class LevelScript tương ứng với Level chỉ định (Factory Pattern).
 */
public class LevelScriptFactory {

    /**
     * Khởi tạo đối tượng LevelScript dựa vào cấp độ màn chơi (stage level).
     *
     * @param stageLevel Chỉ số Level (1, 2, 3, 4, 5, ...)
     * @return Đối tượng LevelScript phù hợp
     */
    public static LevelScript createLevelScript(int stageLevel) {
        return switch (stageLevel) {
            case 1 -> new Level1Script();
            case 2 -> new Level2Script();
            case 3 -> new Level3Script();
            case 4 -> new Level4Script();
            case 5 -> new Level5Script();
            default -> new Level1Script();
        };
    }
}
