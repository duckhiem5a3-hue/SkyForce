# Sky Force - Nhóm 27

## 1. Mô tả bài toán và phạm vi hệ thống

- **Sky Force - Nhóm 27** là một tựa game hành động bắn phi thuyền không gian màn hình dọc (2D Space Shooter).
- Hệ thống bao gồm một vòng lặp game hoàn chỉnh với các giao diện: Menu chính, Cửa hàng (Shop), Chọn màn chơi (Level Select) và Màn hình chiến đấu (Play Scene).
- Người chơi sẽ điều khiển phi thuyền né tránh chướng ngại vật, tiêu diệt các làn sóng kẻ địch đa dạng, thu thập vật phẩm nâng cấp và đối đầu với các trùm (Boss) ở cuối màn.

## 2. Công nghệ sử dụng, môi trường chạy và yêu cầu cài đặt

- **Ngôn ngữ lập trình:** Java.
- **Thư viện giao diện đồ họa:** JavaFX.
- **Tài nguyên hình ảnh & âm thanh:** Sử dụng bộ tài nguyên "Kenney Space Shooter Remastered" (License CC0).
- **Yêu cầu cài đặt:** Cần cài đặt Java Development Kit (JDK) phiên bản hỗ trợ JavaFX. Nếu chạy trên các phiên bản Java mới (từ Java 11 trở lên), cần cấu hình thêm JavaFX SDK vào thư viện của project.

## 3. Cấu trúc module chính

Dự án được tổ chức theo mô hình quản lý phân tầng rõ ràng:

- `com.nhom27.skyforce.main`: Chứa các lớp khởi chạy chương trình chính là `Main` và `Launcher`.
- `com.nhom27.skyforce.managers`: Nơi chứa các hệ thống quản lý lõi bao gồm vòng lặp game (`GameManager`), chuyển đổi màn hình (`SceneManager`), hiệu ứng hình ảnh (`VFXManager`) và dữ liệu người chơi (`PlayerDataManager`).
- `com.nhom27.skyforce.entities`: Quản lý toàn bộ thực thể trong game, chia thành các gói nhỏ: `player` (người chơi), `enemies` (kẻ địch/boss), `weapons` (các loại đạn), `items` (vật phẩm hỗ trợ) và `obstacles` (chướng ngại vật như thiên thạch).
- `com.nhom27.skyforce.levels`: Nơi lưu trữ kịch bản xuất hiện kẻ địch cho từng màn chơi từ Level 1 đến Level 6 thông qua `LevelScriptFactory`.
- `com.nhom27.skyforce.scenes`: Thiết kế các giao diện UI của trò chơi như `MenuScene`, `PlayScene`, `ShopScene`, và `LevelScene`.
- `com.nhom27.skyforce.audio` & `utils`: Xử lý hệ thống nhạc nền/âm thanh (`AudioManager`) và nạp tài nguyên tĩnh (`AssetManager`).

## 4. Câu lệnh dòng lệnh để chạy chương trình

Lớp chạy chính (Entry Point) của chương trình được đặt tại `com.nhom27.skyforce.main.Launcher`[cite: 3]. Tùy thuộc vào công cụ build bạn đang sử dụng (Maven/Gradle) hoặc chạy thuần, bạn có thể cấu hình IDE để chạy trực tiếp class này.

Nếu chạy bằng dòng lệnh cơ bản (đã cài đặt sẵn JavaFX trên biến môi trường):

```bash
# Biên dịch (chỉ mang tính minh họa, đường dẫn JavaFX thay đổi theo máy)
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -d bin src/com/nhom27/skyforce/main/*.java

# Chạy chương trình
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -cp bin com.nhom27.skyforce.main.Launcher
```

## 5. Danh sách chức năng đã hoàn thành

- Hệ thống điều khiển & Chiến đấu: Hỗ trợ người chơi di chuyển phi thuyền và bắn đạn tự động, bao gồm nâng cấp sức mạnh đạn và đạn đuổi (Seeker Bullet).

- Đa dạng kẻ địch: Tích hợp nhiều loại quái với AI quỹ đạo bay và cách tấn công khác nhau như Normal, Sniper, Swarm, Tanker, cùng các boss lớn (MiniBoss, MidBoss).

- Hệ thống vật phẩm (Power-ups): Quái vật rơi ra Tiền vàng (Coin), Thuốc hồi máu/XP (Pill), Khiên năng lượng (Shield) và Bùa đạn đuổi (Seeker).

- Tiến trình màn chơi: Hoàn thành 6 kịch bản Level khác nhau, tăng dần độ khó với hệ thống sinh quái vật theo đợt (Wave).

- Cửa hàng (Shop): Cho phép người dùng sử dụng số vàng thu thập được để mua và trang bị các ngoại trang (Skin) phi thuyền khác nhau như Blue, Green, Orange, Red.

- Lưu trữ dữ liệu tự động: Game có khả năng lưu và tải dữ liệu liên tục về Tổng số vàng, Điểm kỷ lục (High Score) từng màn và Skin đã mở khóa thông qua file savegame.properties.

- Hiệu ứng Nghe - Nhìn: Hoàn thiện hệ thống âm thanh (nhạc nền, SFX bắn đạn, cháy nổ, nhặt đồ) và các hiệu ứng hình ảnh (Vignette cảnh báo đỏ, viền sáng, nổ SpriteSheet).
