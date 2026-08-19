package com.nhom27.skyforce.levels;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Interface đại diện cho kịch bản của một Level trong trò chơi (State/Strategy Pattern).
 * Mỗi class triển khai interface này chịu trách nhiệm quản lý toàn bộ các biến trạng thái,
 * thời gian và kịch bản xuất hiện kẻ địch (enemy wave) của Level đó.
 */
public interface LevelScript {

    /**
     * Khởi tạo hoặc thiết lập lại trạng thái ban đầu của kịch bản màn chơi.
     */
    void setup();

    /**
     * Cập nhật kịch bản sinh quái vật theo thời gian thực trong vòng lặp game.
     *
     * @param now Timestamp hiện tại (tính bằng mili giây)
     * @param elapsedSec Thời gian đã trôi qua kể từ khi bắt đầu màn chơi (tính bằng giây)
     * @param gameManager Tham chiếu tới GameManager để tương tác với môi trường game
     */
    void update(long now, double elapsedSec, GameManager gameManager);

    /**
     * Kiểm tra xem màn chơi đã hoàn thành hay chưa.
     *
     * @return true nếu màn chơi đã hoàn thành (chiến thắng)
     */
    boolean isCompleted();

    /**
     * Callback hỗ trợ khi một kẻ địch bị tiêu diệt (tùy chọn).
     * Có thể được sử dụng để xử lý logic khi MiniBoss hoặc MidBoss bị hạ gục.
     *
     * @param enemy Kẻ địch vừa bị tiêu diệt
     * @param gameManager Tham chiếu tới GameManager
     */
    default void onEnemyKilled(EnemyObject enemy, GameManager gameManager) {}
}
