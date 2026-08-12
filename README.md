# ✈️ Sky Force - Game Lập Trình Nâng Cao (Nhóm 27)

Dự án phát triển tựa game bắn máy bay không gian (Sky Force) sử dụng ngôn ngữ Java và thư viện đồ họa JavaFX.

## 🛠️ Công Nghệ Sử Dụng

- **Ngôn ngữ:** Java (Phiên bản 25 LTS)
- **Thư viện đồ họa:** JavaFX (Phiên bản 25)
- **Quản lý dự án & Thư viện:** Maven

## 📋 Phân Chia Công Việc

🔗 [Bảng theo dõi tiến độ công việc (Google Sheets)](https://docs.google.com/spreadsheets/d/12zK3o1sXtFvutcf5oWN5eaRMgX2s167UalnCQwqeb_g)

## 📂 Cấu Trúc Thư Mục

Dự án áp dụng mô hình phân tách rõ ràng (MVC-like) để tối ưu việc quản lý:

- `src/main/java/com/nhom27/skyforce/`: Chứa toàn bộ mã nguồn Java.
  - `main/`: Lõi chương trình, chứa launcher độc lập gọi hàm main (để Maven tải thư viện trước khi khởi tạo Application) và Game Loop.
  - `entities/`: Các thực thể như Player, Enemy, Bullet.
  - `graphics/`: Xử lý hình ảnh tĩnh, Animation.
  - `input/`: Điều khiển bàn phím, chuột.
  - `audio/`: Xử lý âm thanh, nhạc nền.
  - `utils/`: Tiện ích dùng chung, tính toán va chạm.
- `src/main/resources/com/nhom27/skyforce/`: Chứa tài nguyên vật lý.
  - `textures/`: Hình ảnh (.png, .jpg).
  - `sounds/`: Âm thanh hiệu ứng và nhạc nền (.ogg, .mp3).

## 📝 Quy Tắc Đặt Tên Tài Nguyên

**Công thức chuẩn cho Hình ảnh:**
`[nhóm]_[tên_đối_tượng]_[đặc_điểm_hoặc_cấp_độ]_[trạng_thái].png`
_(Ví dụ: `vfx_explosion_8x8_sheet.png`)_
