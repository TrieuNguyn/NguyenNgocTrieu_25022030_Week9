package com.bank.exception;

/**
 * Ngoại lệ gốc cho tất cả lỗi nghiệp vụ trong hệ thống ngân hàng.
 *
 * <p>Mọi exception cụ thể trong hệ thống đều kế thừa từ lớp này,
 * cho phép caller bắt {@code BankException} để xử lý toàn bộ lỗi nghiệp vụ
 * mà không cần liệt kê từng loại.</p>
 */
public class BankException extends Exception {

  /**
   * Tạo ngoại lệ với thông điệp mô tả lỗi.
   *
   * @param message thông điệp mô tả nguyên nhân lỗi
   */
  public BankException(String message) {
    super(message);
  }
}
