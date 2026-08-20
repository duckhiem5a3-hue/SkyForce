# Sky Force - Nhóm 27

## I. Mô tả bài toán và phạm vi hệ thống

- **Sky Force - Nhóm 27** là một tựa game hành động bắn phi thuyền không gian màn hình dọc (2D Space Shooter).
- Hệ thống bao gồm một vòng lặp game hoàn chỉnh với các giao diện: Menu chính, Cửa hàng (Shop), Chọn màn chơi (Level Select) và Màn hình chiến đấu (Play Scene).
- Người chơi sẽ điều khiển phi thuyền né tránh chướng ngại vật, tiêu diệt các làn sóng kẻ địch đa dạng, thu thập vật phẩm nâng cấp và đối đầu với các trùm (Boss) ở cuối màn.

## II. Công nghệ sử dụng, môi trường chạy và yêu cầu cài đặt

- **Ngôn ngữ lập trình:** Java.
- **Thư viện giao diện đồ họa:** JavaFX.
- **Yêu cầu cài đặt:** Cần cài đặt Java Development Kit (JDK) phiên bản hỗ trợ JavaFX. Nếu chạy trên các phiên bản Java mới (từ Java 11 trở lên), cần cấu hình thêm JavaFX SDK vào thư viện của project.

## III. Cấu trúc module chính

Dự án được tổ chức theo mô hình quản lý phân tầng rõ ràng:

- `com.nhom27.skyforce.main`: Chứa các lớp khởi chạy chương trình chính là `Main` và `Launcher`.
- `com.nhom27.skyforce.managers`: Nơi chứa các hệ thống quản lý lõi bao gồm vòng lặp game (`GameManager`), chuyển đổi màn hình (`SceneManager`), hiệu ứng hình ảnh (`VFXManager`) và dữ liệu người chơi (`PlayerDataManager`).
- `com.nhom27.skyforce.entities`: Quản lý toàn bộ thực thể trong game, chia thành các gói nhỏ: `player` (người chơi), `enemies` (kẻ địch/boss), `weapons` (các loại đạn), `items` (vật phẩm hỗ trợ) và `obstacles` (chướng ngại vật như thiên thạch).
- `com.nhom27.skyforce.levels`: Nơi lưu trữ kịch bản xuất hiện kẻ địch cho từng màn chơi từ Level 1 đến Level 6 thông qua `LevelScriptFactory`.
- `com.nhom27.skyforce.scenes`: Thiết kế các giao diện UI của trò chơi như `MenuScene`, `PlayScene`, `ShopScene`, và `LevelScene`.
- `com.nhom27.skyforce.audio` & `utils`: Xử lý hệ thống nhạc nền/âm thanh (`AudioManager`) và nạp tài nguyên tĩnh (`AssetManager`).

## IV. Câu lệnh dòng lệnh để chạy chương trình

Lớp chạy chính (Entry Point) của chương trình được đặt tại `com.nhom27.skyforce.main.Launcher`[cite: 3]. Tùy thuộc vào công cụ build bạn đang sử dụng (Maven/Gradle) hoặc chạy thuần, bạn có thể cấu hình IDE để chạy trực tiếp class này.

Nếu chạy bằng dòng lệnh cơ bản (đã cài đặt sẵn JavaFX trên biến môi trường):

```bash
# Biên dịch (chỉ mang tính minh họa, đường dẫn JavaFX thay đổi theo máy)
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -d bin src/com/nhom27/skyforce/main/*.java

# Chạy chương trình
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -cp bin com.nhom27.skyforce.main.Launcher
```

## V. Danh sách chức năng đã hoàn thành

### 1. Cơ chế Người chơi (Player Mechanics)

- **Hệ thống điều khiển:** Áp dụng bắt sự kiện chuột (Mouse Dragged) và bàn phím (W/A/S/D, Mũi tên) để điều hướng phi thuyền mượt mà trong giới hạn không gian màn hình.
- **Hệ thống cấp độ và kinh nghiệm (Leveling System):** Phi thuyền có thanh kinh nghiệm (XP). Khi thăng cấp (tối đa Level 3), phi thuyền tự động thay đổi kết cấu đạn: từ 1 tia thẳng -> 2 tia song song -> 3 tia tỏa quạt.
- **Hệ thống Sinh tồn:** Quản lý linh hoạt thanh Máu (HP) của phi thuyền; trò chơi sẽ kết thúc (Game Over) khi máu bằng 0.

### 2. Kẻ địch (Enemy)

- **Đa dạng chủng loại:** Tích hợp nhiều loại kẻ địch mang đặc tính riêng biệt: NormalEnemy (bay thẳng), TankerEnemy (đóng vai trò lá chắn máu cao), SwarmEnemy (bay lượn sóng) và Asteroid (thiên thạch rớt ngẫu nhiên).
- **Địch Ngắm bắn (Sniper Enemy):** Lập trình cho kẻ địch có khả năng dừng lại giữa không trung, tính toán khoảng cách và góc độ để nhắm bắn trực tiếp vào tọa độ hiện tại của người chơi.
- **Hệ thống Trùm (Boss Mechanics):** Thiết kế MiniBoss và MidBoss với lượng máu lớn. MidBoss được lập trình 2 giai đoạn (Phase 1: Bắn đạn tỏa 360 độ; Phase 2: Nhả tia laser kép cường độ cao).

### 3. Hệ thống Vật phẩm và Cường hóa (Items & Buffs)

- **Rớt vật phẩm:** Kẻ địch bị tiêu diệt có tỷ lệ rơi ra Tiền vàng (Coin) và các bùa lợi.
- **Lực hút từ tính (Magnetic Pull):** Thuật toán tự động thu gom toàn bộ tiền vàng bay về phía phi thuyền khi kết thúc màn chơi hoặc khi diệt xong Boss (đối với các màn 1, 5).
- **Đa dạng bùa lợi (Power-ups):**
  - **Pill:** Hồi phục HP và tăng điểm XP.
  - **Shield:** Kích hoạt lớp khiên năng lượng chặn hoàn toàn 1 lần sát thương.
  - **Seeker:** Kích hoạt hệ thống "Đạn thông minh", tự động dò tìm và chuyển hướng bay về phía kẻ địch gần nhất.

### 4. Hệ thống Màn chơi và Tiến trình (Level Design)

- **Kịch bản thời gian thực (Campaign):** Cung cấp 5 màn chơi có cốt truyện. Các đợt lính (Wave) được lập trình xuất hiện chính xác theo từng mốc giây (Ví dụ: Đội hình chữ V, Bức tường xe tăng).
- **Sinh tồn vô tận (Endless Mode):** Màn 6 áp dụng thuật toán sinh cấu trúc ngẫu nhiên (Structured Randomness). Trò chơi tự động bốc ngẫu nhiên các đội hình lính, tăng dần tốc độ xuất hiện và gọi Boss mỗi 10 đợt.
- **Cảnh báo Động:** Xuất hiện băng rôn cảnh báo đỏ nhấp nháy trên màn hình trước khi Boss xuất hiện.

### 5. Lưu trữ Dữ liệu và Cửa hàng (Data & Shop)

- **Lưu trữ bền vững (Persistence):** Ứng dụng lớp Properties để đọc/ghi dữ liệu. Trò chơi tự động lưu Tổng số tiền Vàng, Điểm kỷ lục của từng màn và danh sách Skin đã mở khóa xuống tệp vật lý `savegame.properties`.
- **Cửa hàng Ngoại trang:** Cung cấp hệ thống Shop để người chơi dùng Vàng mua các loại phi thuyền mới (Blue, Green, Orange, Red), trực tiếp thay đổi ngoại hình máy bay và màu đạn khi chiến đấu.

### 6. Trải nghiệm Đa phương tiện (Audio & VFX)

- **Xử lý Âm thanh:** Quản lý độc lập luồng Nhạc nền (MediaPlayer) và Hiệu ứng SFX (AudioClip). Tích hợp tính năng Bật/Tắt âm thanh toàn cục.
- **Kỹ xảo Hình ảnh (VFX):**
  - Cơ chế đổi viền màn hình (Vignette) khi nhận sát thương hoặc nhận hiệu ứng có lợi.
  - Hiệu ứng chuyển động (AnimationTimer) cho các vụ nổ SpriteSheet và tia lửa va chạm.
  - Các nút bấm UI được tích hợp hiệu ứng Scale (nảy lên) và Neon Glow (tỏa sáng) khi tương tác chuột.
