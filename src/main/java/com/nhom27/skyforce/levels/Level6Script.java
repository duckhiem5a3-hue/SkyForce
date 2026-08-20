package com.nhom27.skyforce.levels;

import java.util.Random;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.enemies.*;
import com.nhom27.skyforce.entities.items.*;
import com.nhom27.skyforce.entities.obstacles.Asteroid;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 6 (Endless Void - Chế độ Vô Tận Ngẫu
 * Nhiên).
 * Được nâng cấp với thuật toán sinh quái ngẫu nhiên đa dạng (Procedural
 * Generation):
 * - 10 kịch bản sóng quái độc lập & sóng kết hợp (Hybrid Waves).
 * - Biến thiên ngẫu nhiên về số lượng, tốc độ, góc di chuyển, biên độ sóng
 * sine.
 * - Sóng thả hòm tiếp tế ngẫu nhiên (Supply Drop Wave).
 * - Cảnh báo Boss ngẫu nhiên vị trí & hỗ trợ quái nhỏ.
 */
public class Level6Script implements LevelScript {

    private int currentWave = 0;
    private long lastWaveTime = 0;
    private long waveInterval = 5000; // ms
    private boolean isBossFight = false;
    private boolean isWarningBoss = false;
    private long warningStartTime = 0;

    public Level6Script() {
        setup();
    }

    @Override
    public void setup() {
        currentWave = 0;
        lastWaveTime = 0;
        waveInterval = 4000;
        isBossFight = false;
        isWarningBoss = false;
        warningStartTime = 0;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        if (isWarningBoss) {
            if (now - warningStartTime >= 3000) {
                isWarningBoss = false;
                if (gameManager.getPlayScene() != null) {
                    gameManager.getPlayScene().showWarningBanner(false);
                }
                spawnBossWave(gameManager);
            }
            return;
        }

        // Đợi Boss chết mới gọi đợt tiếp theo
        if (isBossFight)
            return;

        if (lastWaveTime == 0) {
            lastWaveTime = now;
        }

        if (now - lastWaveTime >= waveInterval) {
            currentWave++;
            lastWaveTime = now;

            // Thời gian nghỉ giữa các đợt biến thiên ngẫu nhiên (từ 1.8s đến 4.5s)
            Random rand = gameManager.getRandom();
            int randomIntervalOffset = rand.nextInt(1200) - 400; // -400ms đến +800ms
            int baseInterval = Math.max(2000, 4500 - (currentWave * 70));
            waveInterval = Math.max(1800, baseInterval + randomIntervalOffset);

            // Cứ mỗi 10 Wave thì cảnh báo và gọi Boss
            if (currentWave % 10 == 0) {
                isWarningBoss = true;
                warningStartTime = now;
                if (gameManager.getPlayScene() != null) {
                    gameManager.getPlayScene().showWarningBanner(true);
                }
                AudioManager.getInstance().playSound("sfx_zap");
                return;
            }

            // Đổ xúc xắc chọn 1 trong 10 kịch bản ngẫu nhiên
            int pattern = rand.nextInt(10);
            switch (pattern) {
                case 0:
                    spawnRandomAsteroidStorm(gameManager);
                    break;
                case 1:
                    spawnDynamicSwarmWave(gameManager);
                    break;
                case 2:
                    spawnRandomTankerFormation(gameManager);
                    break;
                case 3:
                    spawnRandomSniperAmbush(gameManager);
                    break;
                case 4:
                    spawnFighterSquadron(gameManager);
                    break;
                case 5:
                    spawnHybridMeteorAndSwarm(gameManager);
                    break;
                case 6:
                    spawnEliteArmoredConvoy(gameManager);
                    break;
                case 7:
                    spawnSineSwarmCrossfire(gameManager);
                    break;
                case 8:
                    spawnSupplyDropRaid(gameManager);
                    break;
                case 9:
                    spawnAllOutChaosRaid(gameManager);
                    break;
                default:
                    spawnFighterSquadron(gameManager);
                    break;
            }
        }
    }

    /**
     * Pattern 0: Mưa thiên thạch ngẫu nhiên với kích thước, vị trí và tốc độ lệch
     * nhau.
     */
    private void spawnRandomAsteroidStorm(GameManager gm) {
        Random r = gm.getRandom();
        int count = 3 + r.nextInt(3) + Math.min(3, currentWave / 6); // 3 đến 8 cục
        for (int i = 0; i < count; i++) {
            double startX = r.nextDouble() * (Main.WIDTH - 60);
            double startY = -80 - (i * (60 + r.nextInt(60)));
            Asteroid a = new Asteroid(startX, startY);
            gm.spawnEnemy(a);
        }
    }

    /**
     * Pattern 1: Bầy quái Swarm biến hình với các kiểu quỹ đạo ngẫu nhiên (Sine,
     * Straight, Diagonal).
     */
    private void spawnDynamicSwarmWave(GameManager gm) {
        Random r = gm.getRandom();
        int count = 5 + r.nextInt(4) + Math.min(5, currentWave / 4);
        SwarmEnemy.TrajectoryType[] types = SwarmEnemy.TrajectoryType.values();
        SwarmEnemy.TrajectoryType selectedType = types[r.nextInt(types.length)];

        double baseSpeedY = 200.0 + r.nextDouble() * 120.0 + (currentWave * 3);
        double speedX = (r.nextDouble() - 0.5) * 160.0;
        double amp = 40.0 + r.nextDouble() * 80.0;
        double freq = 0.015 + r.nextDouble() * 0.02;
        double startX = 60 + r.nextDouble() * (Main.WIDTH - 220);

        for (int i = 0; i < count; i++) {
            double phase = (i * 0.4);
            SwarmEnemy swarm = new SwarmEnemy(selectedType, baseSpeedY, speedX, amp, freq, phase);
            swarm.spawnAt(startX, -swarm.getSizeY() - (i * swarm.getSizeY()));
            gm.spawnEnemy(swarm);
        }
    }

    /**
     * Pattern 2: Bức tường Tanker bọc thép ngẫu nhiên về khoảng hở và tốc độ bay.
     */
    private void spawnRandomTankerFormation(GameManager gm) {
        Random r = gm.getRandom();
        int tankerCount = 2 + r.nextInt(2); // 2 hoặc 3 con
        double speed = 60.0 + r.nextDouble() * 40.0 + Math.min(50, currentWave * 2);

        for (int i = 0; i < tankerCount; i++) {
            boolean canShoot = r.nextBoolean();
            TankerEnemy t = new TankerEnemy(speed, canShoot);
            double posX = r.nextDouble() * (Main.WIDTH - t.getSizeX());
            double posY = -t.getSizeY() - (i * 70);
            t.setPos(posX, posY);
            gm.spawnEnemy(t);
        }
    }

    /**
     * Pattern 3: Phục kích Laser Sniper ngẫu nhiên vị trí xuất hiện.
     */
    private void spawnRandomSniperAmbush(GameManager gm) {
        Random r = gm.getRandom();
        int count = 2 + r.nextInt(2) + Math.min(2, currentWave / 8);
        for (int i = 0; i < count; i++) {
            double speed = 140.0 + r.nextDouble() * 60.0;
            long pauseMs = 800 + r.nextInt(600);
            SniperEnemy s = new SniperEnemy(speed, pauseMs, "AUTO");
            double spawnX = 30 + r.nextDouble() * (Main.WIDTH - 100);
            double spawnY = -s.getSizeY() * (i + 1) - (r.nextInt(50));
            s.setPos(spawnX, spawnY);
            gm.spawnEnemy(s);
        }
    }

    /**
     * Pattern 4: Tập đoàn Phi cơ Chiến đấu (Blue / Red Normal Enemy) bay theo
     * V-Shape hoặc Hàng Ngang.
     */
    private void spawnFighterSquadron(GameManager gm) {
        Random r = gm.getRandom();
        int count = 4 + r.nextInt(4);
        boolean isVShape = r.nextBoolean();
        double centerX = 80 + r.nextDouble() * (Main.WIDTH - 160);
        double speed = 100.0 + r.nextDouble() * 60.0;

        for (int i = 0; i < count; i++) {
            boolean isRed = r.nextDouble() < 0.35; // 35% ra quái đỏ biết bắn
            boolean isStopping = r.nextDouble() < 0.25; // 25% quái dừng giữa đường xả đạn
            String sprite = isRed ? "enemy_normal_red" : "enemy_normal_blue";

            NormalEnemy enemy = new NormalEnemy(speed, isRed, isStopping, isRed, sprite);
            double offsetX = isVShape ? (i - count / 2.0) * 55.0 : (i * 65.0) - (count * 30.0);
            double offsetY = isVShape ? -Math.abs(i - count / 2.0) * 45.0 - enemy.getSizeY()
                    : -i * 50.0 - enemy.getSizeY();

            double spawnX = Math.max(20, Math.min(Main.WIDTH - enemy.getSizeX() - 20, centerX + offsetX));
            enemy.setPos(spawnX, offsetY);
            gm.spawnEnemy(enemy);
        }
    }

    /**
     * Pattern 5: Sóng hỗn hợp Mưa Thiên Thạch + Quái Swarm lao từ trên xuống.
     */
    private void spawnHybridMeteorAndSwarm(GameManager gm) {
        Random r = gm.getRandom();
        // 2-3 Cục Thiên thạch
        int astCount = 2 + r.nextInt(2);
        for (int i = 0; i < astCount; i++) {
            Asteroid a = new Asteroid(r.nextDouble() * (Main.WIDTH - 60), -100 - (i * 120));
            gm.spawnEnemy(a);
        }

        // 4-6 Con Swarm bám đuôi
        int swarmCount = 4 + r.nextInt(3);
        double startX = r.nextBoolean() ? 40 : Main.WIDTH - 80;
        for (int i = 0; i < swarmCount; i++) {
            SwarmEnemy s = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0, 70.0, 0.02, i * 0.5);
            s.spawnAt(startX, -s.getSizeY() - 150 - (i * 40));
            gm.spawnEnemy(s);
        }
    }

    /**
     * Pattern 6: Đội hình Tinh nhuệ (Tanker đi trước che chắn, Quái đỏ bắn laser đi
     * sau).
     */
    private void spawnEliteArmoredConvoy(GameManager gm) {
        Random r = gm.getRandom();
        double speed = 80.0 + r.nextDouble() * 30.0;

        // 2 Tankers làm lá chắn phía trước
        TankerEnemy t1 = new TankerEnemy(speed, true);
        t1.setPos(Main.WIDTH * 0.25 - t1.getSizeX() / 2.0, -t1.getSizeY());

        TankerEnemy t2 = new TankerEnemy(speed, true);
        t2.setPos(Main.WIDTH * 0.75 - t2.getSizeX() / 2.0, -t2.getSizeY());

        gm.spawnEnemy(t1);
        gm.spawnEnemy(t2);

        // 2 Quái đỏ nấp phía sau Tankers
        NormalEnemy r1 = new NormalEnemy(speed, true, false, true, "enemy_normal_red");
        r1.setPos(t1.getX(), -t1.getSizeY() - 80);

        NormalEnemy r2 = new NormalEnemy(speed, true, false, true, "enemy_normal_red");
        r2.setPos(t2.getX(), -t2.getSizeY() - 80);

        gm.spawnEnemy(r1);
        gm.spawnEnemy(r2);
    }

    /**
     * Pattern 7: Cơn bão Swarm Zic-Zac (2 toán quái Swarm đan chéo từ trái và
     * phải).
     */
    private void spawnSineSwarmCrossfire(GameManager gm) {
        int count = 4;
        for (int i = 0; i < count; i++) {
            SwarmEnemy sLeft = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 260.0, 0, 90.0, 0.02, 0);
            sLeft.spawnAt(80, -sLeft.getSizeY() - (i * 50));

            SwarmEnemy sRight = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 260.0, 0, 90.0, 0.02, Math.PI);
            sRight.spawnAt(Main.WIDTH - 80, -sRight.getSizeY() - (i * 50));

            gm.spawnEnemy(sLeft);
            gm.spawnEnemy(sRight);
        }
    }

    /**
     * Pattern 8: Chuyến hàng tiếp tế đặc biệt (Quái đỏ mang 100% PowerUp rớt vật
     * phẩm quý).
     */
    private void spawnSupplyDropRaid(GameManager gm) {
        Random r = gm.getRandom();
        double speed = 110.0;
        double spawnX = 50 + r.nextDouble() * (Main.WIDTH - 100);

        // Quái tiếp tế rớt chắc chắn vật phẩm
        NormalEnemy supplyCarrier = new NormalEnemy(speed, false, true, true, "enemy_normal_red");
        supplyCarrier.setPos(spawnX, -supplyCarrier.getSizeY());
        gm.spawnEnemy(supplyCarrier);

        // 2 quái xanh bảo vệ xung quanh
        NormalEnemy guard1 = new NormalEnemy(speed, false, false, false, "enemy_normal_blue");
        guard1.setPos(Math.max(10, spawnX - 60), -supplyCarrier.getSizeY() - 40);

        NormalEnemy guard2 = new NormalEnemy(speed, false, false, false, "enemy_normal_blue");
        guard2.setPos(Math.min(Main.WIDTH - guard2.getSizeX() - 10, spawnX + 60), -supplyCarrier.getSizeY() - 40);

        gm.spawnEnemy(guard1);
        gm.spawnEnemy(guard2);
    }

    /**
     * Pattern 9: Đột kích tổng hợp (Sniper + Normal Enemies + Asteroid).
     */
    private void spawnAllOutChaosRaid(GameManager gm) {
        Random r = gm.getRandom();

        // 1 Sniper
        SniperEnemy sniper = new SniperEnemy(160.0, 900, "AUTO");
        sniper.setPos(r.nextDouble() * (Main.WIDTH - sniper.getSizeX()), -sniper.getSizeY());
        gm.spawnEnemy(sniper);

        // 1 Asteroid
        Asteroid ast = new Asteroid(r.nextDouble() * (Main.WIDTH - 60), -120);
        gm.spawnEnemy(ast);

        // 3 Normal Enemies
        for (int i = 0; i < 3; i++) {
            NormalEnemy n = new NormalEnemy(130.0, r.nextBoolean(), false, false, "enemy_normal_blue");
            n.setPos(r.nextDouble() * (Main.WIDTH - n.getSizeX()), -n.getSizeY() - (i * 60));
            gm.spawnEnemy(n);
        }
    }

    /**
     * Triệu hồi Boss ở Wave 10, 20, 30... với vị trí ngẫu nhiên nhẹ.
     */
    private void spawnBossWave(GameManager gm) {
        isBossFight = true;
        if (gm.getPlayScene() != null) {
            gm.getPlayScene().showWarningBanner(false);
        }

        Random r = gm.getRandom();

        // Wave chẵn chục (20, 40) ra MidBoss, lẻ (10, 30) ra MiniBoss
        if (currentWave % 20 == 0) {
            MidBoss boss = new MidBoss();
            double spawnX = Math.max(20, Math.min(Main.WIDTH - boss.getSizeX() - 20,
                    Main.WIDTH / 2.0 - boss.getSizeX() / 2.0 + (r.nextDouble() - 0.5) * 100));
            boss.setPos(spawnX, -boss.getSizeY());
            gm.spawnEnemy(boss);
        } else {
            MiniBoss boss = new MiniBoss();
            double spawnX = Math.max(20, Math.min(Main.WIDTH - boss.getSizeX() - 20,
                    Main.WIDTH / 2.0 - boss.getSizeX() / 2.0 + (r.nextDouble() - 0.5) * 100));
            boss.setPos(spawnX, -boss.getSizeY());
            gm.spawnEnemy(boss);
        }
    }

    @Override
    public void onEnemyKilled(EnemyObject enemy, GameManager gameManager) {
        if (enemy instanceof MidBoss || enemy instanceof MiniBoss) {
            isBossFight = false;
            lastWaveTime = 0; // Reset lại timer để ra wave mới
            if (gameManager.getPlayScene() != null) {
                gameManager.getPlayScene().showWarningBanner(false);
            }

            // Boss rớt ngẫu nhiên 15-25 đồng Vàng + 1 PowerUp hỗ trợ đặc biệt
            // (Shield/Pill/Seeker)
            Random r = gameManager.getRandom();
            int coinCount = 15 + r.nextInt(11);
            for (int c = 0; c < coinCount; c++) {
                double dropX = enemy.getX() + enemy.getSizeX() / 2.0 + (r.nextDouble() - 0.5) * 200;
                double dropY = enemy.getY() + enemy.getSizeY() / 2.0 + (r.nextDouble() - 0.5) * 150;
                CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                gameManager.addPowerUp(coin);
            }

            // 100% Drop 1 PowerUp ngẫu nhiên cho người chơi khi hạ Boss
            double bossCenterX = enemy.getX() + enemy.getSizeX() / 2.0;
            double bossCenterY = enemy.getY() + enemy.getSizeY() / 2.0;
            int powerType = r.nextInt(3);
            PowerUp item = switch (powerType) {
                case 0 -> new ShieldPowerUp(bossCenterX, bossCenterY);
                case 1 -> new PillPowerUp(bossCenterX, bossCenterY);
                default -> new SeekerPowerUp(bossCenterX, bossCenterY);
            };
            gameManager.addPowerUp(item);
        }
    }

    @Override
    public boolean isCompleted() {
        // Chế độ vô tận không bao giờ tự kết thúc, chỉ kết thúc khi Player hết máu
        return false;
    }
}