# CookABook Backend

Ứng dụng CookABook Backend được đóng gói sẵn trong Docker để dễ dàng khởi chạy.

## Cách chạy nhanh (với cấu hình mặc định)

1. Tải file `compose.yaml` từ repository này

2. Mở terminal/command prompt, di chuyển đến thư mục chứa file `compose.yaml` và chạy lệnh:

```bash
docker compose -p cookabook_backend up -d
```

## Cách chạy với biến môi trường tùy chỉnh

1. Tạo file `.env` từ file mẫu:
   ```bash
   cp .env.example .env
   ```

## Cách chạy dự án khi clone repo về máy

1. Tạo file `.env` từ file mẫu: `.env.example` (thông tin bắt buộc phải hợp lệ để kết nối được với DB, chi tiết xem trong file `application.properties`)

2. Chạy lệnh: `mvn install` để tải các dependencies cần thiết

3. Có thể chạy dự án bằng extension spring-boot hoặc chạy bằng lệnh: `mvn run:spring-boot`
