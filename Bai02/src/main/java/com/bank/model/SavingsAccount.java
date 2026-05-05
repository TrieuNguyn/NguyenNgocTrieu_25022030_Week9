package com.bank.model;

import com.bank.exception.BankException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidFundingAmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm (Savings Account).
 *
 * <p>Áp dụng hai quy tắc nghiệp vụ khác với tài khoản vãng lai:</p>
 * <ul>
 *   <li>Số tiền rút tối đa mỗi lần: {@value #MAX_SINGLE_WITHDRAWAL} USD</li>
 *   <li>Số dư tối thiểu bắt buộc duy trì: {@value #MIN_REQUIRED_BALANCE} USD</li>
 * </ul>
 */
// FIX: Code gốc đặt dấu ngoặc nhọn mở trên dòng riêng — vi phạm Google Style
// (yêu cầu dấu { cùng dòng với khai báo class/method).
public class SavingsAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

  // FIX: Code gốc dùng magic number 1000.0 và 5000.0 trực tiếp trong code.
  // Đặt thành hằng số có tên rõ nghĩa: (1) dễ tìm kiếm khi cần đổi, (2) tự
  // document — đọc code hiểu ngay ý nghĩa mà không cần comment.
  /**
   * Số tiền tối đa được phép rút trong một giao dịch (USD).
   */
  public static final double MAX_SINGLE_WITHDRAWAL = 1000.0;

  /**
   * Số dư tối thiểu bắt buộc phải duy trì trong tài khoản (USD).
   */
  public static final double MIN_REQUIRED_BALANCE = 5000.0;

  /**
   * Tạo tài khoản tiết kiệm.
   *
   * <p>FIX: Code gốc dùng tham số {@code n} và {@code b} — quá ngắn,
   * người đọc phải đoán. Đổi thành {@code accountNumber} và {@code balance}.</p>
   *
   * @param accountNumber mã số tài khoản duy nhất
   * @param balance       số dư ban đầu (USD)
   */
  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
    logger.info("Tạo tài khoản tiết kiệm #{}, số dư ban đầu: ${}", accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản tiết kiệm.
   *
   * <p>FIX: Code gốc dùng {@code System.err.println} để "log" trạng thái xử lý,
   * dùng tham số {@code a} và biến {@code iB}/{@code fB} quá ngắn,
   * và magic number {@code 3} thay vì {@code Transaction.TYPE_DEPOSIT_SAVINGS}.</p>
   *
   * @param amount số tiền nạp (phải dương)
   */
  @Override
  public void deposit(double amount) {
    logger.debug("Yêu cầu nạp tiền tiết kiệm #{}: ${}", getAccountNumber(), amount);
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS,
          amount,
          initialBalance,
          getBalance());
      addTransaction(transaction);
      logger.info("Nạp tiền tiết kiệm #{} thành công: +${} | Số dư: ${} → ${}",
          getAccountNumber(), amount, initialBalance, getBalance());
    } catch (BankException e) {
      // FIX: Code gốc catch Exception (quá chung) và print message không đủ context.
      logger.warn("Nạp tiền tiết kiệm #{} thất bại: {}", getAccountNumber(), e.getMessage());
    }
  }

  /**
   * Rút tiền từ tài khoản tiết kiệm, tuân theo giới hạn nghiệp vụ.
   *
   * <p>FIX chính của method này:</p>
   * <ul>
   *   <li>Tham số đổi từ {@code a} → {@code amount}</li>
   *   <li>Biến tạm đổi từ {@code iB}/{@code fB} → {@code initialBalance}/{@code finalBalance}</li>
   *   <li>Magic number 1000.0 và 5000.0 → hằng số có tên</li>
   *   <li>Magic number 4 → {@code Transaction.TYPE_WITHDRAW_SAVINGS}</li>
   *   <li>Catch {@code Exception} → catch cụ thể từng loại để xử lý đúng</li>
   *   <li>Khối catch thiếu dấu ngoặc nhọn → thêm đầy đủ</li>
   *   <li>System.out.println → logger với đúng level</li>
   * </ul>
   *
   * @param amount số tiền rút
   */
  @Override
  public void withdraw(double amount) {
    logger.debug("Yêu cầu rút tiền tiết kiệm #{}: ${}", getAccountNumber(), amount);
    double initialBalance = getBalance();
    try {
      if (amount > MAX_SINGLE_WITHDRAWAL) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_REQUIRED_BALANCE) {
        throw new InsufficientFundsException(amount);
      }
      doWithdrawing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(transaction);
      logger.info("Rút tiền tiết kiệm #{} thành công: -${} | Số dư: ${} → ${}",
          getAccountNumber(), amount, initialBalance, finalBalance);
    } catch (InvalidFundingAmountException e) {
      // WARN: vi phạm quy tắc nghiệp vụ — vượt hạn mức rút
      logger.warn("Rút tiền tiết kiệm #{} vượt hạn mức ${}: {}",
          getAccountNumber(), MAX_SINGLE_WITHDRAWAL, e.getMessage());
    } catch (InsufficientFundsException e) {
      // WARN: vi phạm quy tắc nghiệp vụ — số dư sẽ xuống dưới mức tối thiểu
      logger.warn("Rút tiền tiết kiệm #{} vi phạm số dư tối thiểu ${}: {}",
          getAccountNumber(), MIN_REQUIRED_BALANCE, e.getMessage());
    } catch (BankException e) {
      // ERROR: lỗi không lường trước trong hệ thống — cần điều tra
      logger.error("Lỗi không xác định khi rút tiền tiết kiệm #{}", getAccountNumber(), e);
    }
  }
}
