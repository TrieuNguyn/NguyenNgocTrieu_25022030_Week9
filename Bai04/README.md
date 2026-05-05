# Bai4 - Matrix Strategy: Kiểm thử đa hệ điều hành

## Vấn đề "It works on my machine"
Hardcoded path separator gây lỗi:
```java
// ❌ SAI - chỉ chạy trên Linux/Mac
assertEquals("data/reports/file.txt", utils.buildPath("data","reports","file.txt"));

// ✅ ĐÚNG - dùng Paths.get() để lấy separator đúng theo OS
String expected = Paths.get("data", "reports", "file.txt").toString();
assertEquals(expected, utils.buildPath(...));
```

## Matrix workflow
Workflow chạy 3 job song song:
- `ubuntu-latest` → Linux (separator: `/`)
- `windows-latest` → Windows (separator: `\`)
- `macos-latest` → macOS (separator: `/`)

`fail-fast: false` cho phép xem kết quả toàn bộ trước khi kết luận.

## Giải pháp cross-platform
Dùng `java.nio.file.Path` / `Paths.get()` / `File.separator` thay vì hardcode `/` hoặc `\`.
