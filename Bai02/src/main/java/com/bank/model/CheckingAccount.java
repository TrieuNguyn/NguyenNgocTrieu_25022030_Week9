package com.bank.model;

import com.bank.exception.BankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản vãng lai (Checking Account).
 *
 * <p>Không giới hạn số tiền rút tối đa và không yêu cầu số dư tối thiểu,
 * ngoại trừ điều kiện số dư phải đủ để thực hiện giao dịch.</p>
 */
public class CheckingAccount extends Account {
  private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

  /**
   * Tạo tài khoản vãng lai.
   *
   * @param accountNumber mã số tài khoản duy nhất
   * @param balance       số dư ban đầu (USD)
   */
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
    logger.info("Tạo tài khoản vãng lai #{}, số dư ban đầu: ${}", accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản vãng lai.
   *
   * <p>Ghi nhận giao dịch nếu thành công.
   * Ghi log WARN nếu số tiền không hợp lệ.</p>
   *
   * @param amount số tiền nạp (phải dương)
   */
  @Override
  public void deposit(double amount) {
    logger.debug("Yêu cầu nạp tiền vãng lai #{}: ${}", getAccountNumber(), amount);
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_CHECKING,
          amount,
          initialBalance,
          getBalance());
      addTransaction(transaction);
      // INFO: nghiệp vụ quan trọng hoàn thành thành công — cần ghi để audit
      logger.info("Nạp tiền vãng lai #{} thành công: +${} | Số dư: ${} → ${}",
          getAccountNumber(), amount, initialBalance, getBalance());
    } catch (BankException e) {
      // WARN: lỗi do input người dùng, không phải lỗi hệ thống
      logger.warn("Nạp tiền vãng lai #{} thất bại: {}", getAccountNumber(), e.getMessage());
    }
  }

  /**
   * Rút tiền từ tài khoản vãng lai.
   *
   * <p>FIX: Code gốc bắt {@code BankException} rồi in {@code System.out.println(e.getMessage())}
   * — mất hoàn toàn context (tài khoản nào? số tiền bao nhiêu?).
   * Logger giải quyết bằng cách đính kèm đầy đủ thông tin vào mỗi dòng log.</p>
   *
   * @param amount số tiền rút (phải dương và không vượt số dư)
   */
  @Override
  public void withdraw(double amount) {
    logger.debug("Yêu cầu rút tiền vãng lai #{}: ${}", getAccountNumber(), amount);
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING,
          amount,
          initialBalance,
          getBalance());
      addTransaction(transaction);
      logger.info("Rút tiền vãng lai #{} thành công: -${} | Số dư: ${} → ${}",
          getAccountNumber(), amount, initialBalance, getBalance());
    } catch (BankException e) {
      logger.warn("Rút tiền vãng lai #{} thất bại: {}", getAccountNumber(), e.getMessage());
    }
  }
}
