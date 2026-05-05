# Bai5 - Test Coverage & Quality Enforcement (JaCoCo)

## Cấu hình JaCoCo
- **Ngưỡng**: LINE coverage >= 80%
- **Báo cáo**: `target/site/jacoco/index.html`
- **Fail build** tự động nếu coverage dưới ngưỡng

## Chạy
```bash
mvn clean verify           # build + test + check coverage + tạo report
```

## Xem báo cáo
Mở `target/site/jacoco/index.html` trong browser sau khi chạy.

## Trong CI
- `mvn clean verify` chạy đủ vòng đời
- Artifact `jacoco-report` được upload và giữ 14 ngày
