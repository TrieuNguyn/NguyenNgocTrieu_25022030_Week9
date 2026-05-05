# Bai3 - CI/CD Automation với GitHub Actions

## Workflow: ci.yml
- **Trigger**: push và pull_request
- **Steps**: checkout → setup JDK 17 → `mvn clean test package` → upload artifact
- **Artifact**: JAR được lưu 7 ngày sau mỗi build thành công

## Demo lỗi build
Để kiểm thử CI, cố tình sửa test thành assertion sai:
```java
assertEquals(99, calc.add(2, 3)); // 2+3 ≠ 99 → build đỏ
```
Sau đó xem log trên GitHub Actions tab → sửa lại → pipeline xanh.

## Cách push lên GitHub
```bash
git init
git add .
git commit -m "feat: Bai3 CI/CD automation"
git remote add origin https://github.com/<username>/week9-bai3.git
git push -u origin main
```
