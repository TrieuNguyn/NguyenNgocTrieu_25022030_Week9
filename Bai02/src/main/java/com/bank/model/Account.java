package com.bank.model;

import com.bank.exception.BankException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidFundingAmountException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng.
 *
 * <p>Cung cấp hành vi chung cho mọi loại tài khoản: quản lý số dư,
 * ghi nhận lịch sử giao dịch, và các thao tác nạp/rút tiền cơ bản.
 * Lớp con phải implement {@link #deposit(double)} và {@link #withdraw(double)}
 * theo quy tắc riêng của từng loại tài khoản.</p>
 */
public abstract class Account {

  // FIX: Logger khai báo static final, tên theo lớp hiện tại.
  // Code gốc không có logger, dùng System.out.println tràn lan.
  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  // FIX: Hằng số đổi từ checking_type/savings_type (snake_case vi phạm)
  // sang UPPER_SNAKE_CASE đúng chuẩn Google Style.
  /**
   * Nhãn loại tài khoản vãng lai.
   */
  public static final String CHECKING_TYPE = "CHECKING";

  /**
   * Nhãn loại tài khoản tiết kiệm.
   */
  public static final String SAVINGS_TYPE = "SAVINGS";

  // FIX: Đổi _accNum (vi phạm dấu gạch dưới) → accountNumber
  //      Đổi B (quá ngắn, không rõ nghĩa) → balance
  //      Đổi list (quá mơ hồ) → transactions
  private long accountNumber;
  private double balance;
  private List<Transaction> transactions;

  /**
   * Khởi tạo tài khoản với số tài khoản và số dư ban đầu.
   *
   * @param accountNumber mã số tài khoản duy nhất
   * @param balance       số dư ban đầu (USD)
   */
  protected Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactions = new ArrayList<>();
    logger.debug("Tài khoản #{} được khởi tạo, số dư ban đầu: ${}", accountNumber, balance);
  }

  /**
   * Trả về số tài khoản.
   *
   * @return mã số tài khoản
   */
  public long getAccountNumber() {
    // FIX: Code gốc viết cả body trên cùng một dòng với khai báo hàm,
    // vi phạm quy tắc "one statement per line".
    return accountNumber;
  }

  /**
   * Cập nhật số tài khoản.
   *
   * @param accountNumber mã số tài khoản mới
   */
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Trả về số dư hiện tại.
   *
   * @return số dư (USD)
   */
  public double getBalance() {
    return balance;
  }

  /**
   * Cập nhật số dư. Chỉ dùng nội bộ trong package model.
   *
   * @param balance số dư mới
   */
  protected void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * Trả về danh sách giao dịch (bản sao không thể sửa đổi).
   *
   * @return danh sách giao dịch
   */
  public List<Transaction> getTransactions() {
    // Trả về unmodifiable view để bảo vệ tính toàn vẹn của lịch sử
    return Collections.unmodifiableList(transactions);
  }

  /**
   * Thay thế toàn bộ danh sách giao dịch.
   *
   * @param transactionList danh sách giao dịch mới; {@code null} được coi là danh sách rỗng
   */
  public void setTransactionList(List<Transaction> transactionList) {
    // FIX: Code gốc thiếu dấu ngoặc nhọn cho if/else — vi phạm Checkstyle
    // (Google Style bắt buộc braces cho mọi control statement dù chỉ 1 dòng).
    if (transactionList == null) {
      this.transactions = new ArrayList<>();
    } else {
      this.transactions = transactionList;
    }
  }

  /**
   * Nạp tiền vào tài khoản theo quy tắc của từng loại tài khoản.
   *
   * @param amount số tiền nạp (phải dương)
   */
  public abstract void deposit(double amount);

  /**
   * Rút tiền từ tài khoản theo quy tắc của từng loại tài khoản.
   *
   * @param amount số tiền rút (phải dương và hợp lệ)
   */
  public abstract void withdraw(double amount);

  /**
   * Thực hiện nghiệp vụ nạp tiền: kiểm tra hợp lệ và cộng vào số dư.
   *
   * @param amount số tiền cần nạp
   * @throws InvalidFundingAmountException nếu {@code amount <= 0}
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    // FIX: Code gốc viết amount<=0 thiếu khoảng trắng quanh toán tử.
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  /**
   * Thực hiện nghiệp vụ rút tiền: kiểm tra hợp lệ và trừ khỏi số dư.
   *
   * <p>FIX: Code gốc khai báo {@code throws Exception} — quá chung chung,
   * che khuất các lỗi thực sự. Thay bằng {@code throws BankException}
   * cho phép caller biết chính xác loại lỗi cần xử lý.</p>
   *
   * @param amount số tiền cần rút
   * @throws InvalidFundingAmountException nếu {@code amount <= 0}
   * @throws InsufficientFundsException    nếu số dư không đủ
   */
  protected void doWithdrawing(double amount) throws BankException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Thêm một giao dịch vào lịch sử tài khoản.
   *
   * @param transaction giao dịch cần thêm; bỏ qua nếu {@code null}
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
    }
  }

  /**
   * Trả về lịch sử giao dịch của tài khoản dưới dạng chuỗi.
   *
   * <p>FIX: Code gốc dùng nối chuỗi {@code s += ...} trong vòng lặp —
   * O(n²) về bộ nhớ do tạo object String mới mỗi vòng. Thay bằng
   * {@code StringBuilder} để O(n).</p>
   *
   * @return chuỗi lịch sử giao dịch, mỗi giao dịch một dòng
   */
  public String getTransactionHistory() {
    // FIX: Thay System.out.println bằng logger.debug — có thể bật/tắt
    // tùy môi trường mà không cần sửa code.
    logger.debug("Lấy lịch sử giao dịch cho tài khoản #{}", accountNumber);

    StringBuilder sb = new StringBuilder();
    sb.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");
    for (int i = 0; i < transactions.size(); i++) {
      sb.append(transactions.get(i).getTransactionSummary());
      if (i < transactions.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    return (int) (accountNumber ^ (accountNumber >>> 32));
  }
}
