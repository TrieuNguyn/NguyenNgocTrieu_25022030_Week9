package com.bank.exception;

import java.util.Locale;

/**
 * Ngoại lệ khi số tiền giao dịch không hợp lệ (âm, bằng không,
 * hoặc vượt giới hạn cho phép).
 */
public class InvalidFundingAmountException extends BankException {

  /**
   * Tạo ngoại lệ với số tiền không hợp lệ.
   *
   * @param amount số tiền bị từ chối
   */
  public InvalidFundingAmountException(double amount) {
    super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
  }
}
