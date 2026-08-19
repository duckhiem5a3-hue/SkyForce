package com.nhom27.skyforce.levels;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.enemies.MidBoss;
import com.nhom27.skyforce.entities.enemies.NormalEnemy;
import com.nhom27.skyforce.entities.enemies.SwarmEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 5.
 * Chứa toàn bộ cờ trạng thái, kịch bản cảnh báo đỏ, xuất hiện MidBoss, đạn
 * Phase 1 / Phase 2 của Boss,
 * vụ nổ liên hoàn khi diệt Boss, lực hút từ tính thu gom vàng và màn hình chiến
 * thắng.
 */
public class Level5Script implements LevelScript {

    private boolean lvl5_spawnedSwarm = false;
    private boolean lvl5_spawnedRedEnemies = false;
    private boolean lvl5_warningTriggered = false;
    private boolean lvl5_bossSpawned = false;
    private MidBoss lvl5_midBoss = null;
    private boolean lvl5_victoryTriggered = false;
    private boolean lvl5_coinsSpawned = false;
    private long lvl5_clearTime = 0;
    private boolean lvl5_magneticActive = false;
    private boolean isVictory = false;

    public Level5Script() {
        setup();
    }

    @Override
    public void setup() {
        lvl5_spawnedSwarm = false;
        lvl5_spawnedRedEnemies = false;
        lvl5_warningTriggered = false;
        lvl5_bossSpawned = false;
        lvl5_midBoss = null;
        lvl5_victoryTriggered = false;
        lvl5_coinsSpawned = false;
        lvl5_clearTime = 0;
        lvl5_magneticActive = false;
        isVictory = false;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        // 🎬 Giai đoạn 1: Giao hàng tiếp tế (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl5_spawnedSwarm) {
            for (int i = 0; i < 4; i++) {
                SwarmEnemy s1 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0, 60.0, 0.025, 0);
                s1.spawnAt(80.0 + (i * 40), -s1.getSizeY() - (i * 30));

                SwarmEnemy s2 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0, 60.0, 0.025, Math.PI);
                s2.spawnAt(Main.WIDTH - 80.0 - (i * 40), -s2.getSizeY() - (i * 30));

                gameManager.spawnEnemy(s1);
                gameManager.spawnEnemy(s2);
            }
            lvl5_spawnedSwarm = true;
        }

        // 12s: 2 quái Đỏ rớt 1 ShieldPowerUp và 1 PillPowerUp 100%
        if (elapsedSec >= 10.0 && !lvl5_spawnedRedEnemies) {
            double centerX = Main.WIDTH / 2.0;

            NormalEnemy rShield = new NormalEnemy(90.0, false, false, true, "enemy_normal_red");
            rShield.setPos(centerX - 90 - rShield.getSizeX() / 2.0, -rShield.getSizeY());

            NormalEnemy rPill = new NormalEnemy(90.0, false, false, true, "enemy_normal_red");
            rPill.setPos(centerX + 90 - rPill.getSizeX() / 2.0, -rPill.getSizeY());

            gameManager.spawnEnemy(rShield);
            gameManager.spawnEnemy(rPill);
            lvl5_spawnedRedEnemies = true;
        }

        // 🚨 Giai đoạn 2: Cảnh Báo Đỏ (Giây 15 - 20)
        if (elapsedSec >= 15.0 && elapsedSec < 19.0 && !lvl5_warningTriggered) {
            if (gameManager.getPlayScene() != null) {
                gameManager.getPlayScene().showWarningBanner(true);
            }
            AudioManager.getInstance().playSound("sfx_zap");
            lvl5_warningTriggered = true;
        }

        // 😈 Giai đoạn 3 & 4: Boss MidBoss Xuất Hiện (Giây 20+)
        if (elapsedSec >= 19.0 && !lvl5_bossSpawned) {
            if (gameManager.getPlayScene() != null) {
                gameManager.getPlayScene().showWarningBanner(false);
            }
            MidBoss boss = new MidBoss();
            boss.setPos(Main.WIDTH / 2.0 - boss.getSizeX() / 2.0, -boss.getSizeY());
            lvl5_midBoss = boss;
            gameManager.spawnEnemy(lvl5_midBoss);
            lvl5_bossSpawned = true;
        }

        // 🎆 Giai đoạn 5: Sau khi Boss bị tiêu diệt (Lực hút từ tính -> Bảng Win)
        if (lvl5_victoryTriggered && !isVictory) {
            long elapsedSinceClear = System.currentTimeMillis() - lvl5_clearTime;
            // 1s: Kích hoạt lực hút từ tính sau 1s từ khi MidBoss nổ
            if (elapsedSinceClear >= 1000 && !lvl5_magneticActive) {
                lvl5_magneticActive = true;
            }

            if (lvl5_magneticActive) {
                for (PowerUp p : gameManager.getPowerUps()) {
                    if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                        coin.setMagnetized(true, gameManager.getPlayer());
                    }
                }
            }

            // Sau khi toàn bộ vàng đã được hút hết (hoặc quá 3s), hiển thị bảng chiến thắng
            // WinMenu
            boolean hasCoinsRemaining = gameManager.getPowerUps().stream()
                    .anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
            if (!hasCoinsRemaining || elapsedSinceClear >= 3000) {
                isVictory = true;
                gameManager.handleVictory();
            }
        }
    }

    @Override
    public void onEnemyKilled(EnemyObject enemy, GameManager gameManager) {
        if (enemy instanceof MidBoss midBoss && !lvl5_victoryTriggered) {
            lvl5_victoryTriggered = true;
            gameManager.getVFXManager().spawnScreenEffect("buffed");

            // Vụ nổ liên hoàn tại vị trí của MidBoss
            for (int i = 0; i < 8; i++) {
                double expX = midBoss.getX() + gameManager.getRandom().nextDouble() * midBoss.getSizeX();
                double expY = midBoss.getY() + gameManager.getRandom().nextDouble() * midBoss.getSizeY();
                gameManager.getVFXManager().spawnExplosionSpriteSheet(expX, expY, 120, 120);
            }
            AudioManager.getInstance().playSound("sfx_explosion_enemy");

            // Rớt đại tiệc 30 đồng Vàng xung quanh vị trí nổ của Boss!
            for (int c = 0; c < 30; c++) {
                double dropX = midBoss.getX() + midBoss.getSizeX() / 2.0
                        + (gameManager.getRandom().nextDouble() - 0.5) * 260;
                double dropY = midBoss.getY() + midBoss.getSizeY() / 2.0
                        + (gameManager.getRandom().nextDouble() - 0.5) * 150;
                CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                gameManager.addPowerUp(coin);
            }
            lvl5_coinsSpawned = true;
            lvl5_clearTime = System.currentTimeMillis();
            lvl5_magneticActive = false;
        }
    }

    @Override
    public boolean isCompleted() {
        return isVictory;
    }
}
