package com.bank.model;

/**
 * Đại diện cho một giao dịch.
 */
public class Transaction {
  public static final int TYPE_DEPOSIT_CHECKING = 1;
  public static final int TYPE_WITHDRAW_CHECKING = 2;
  public static final int TYPE_DEPOSIT_SAVINGS = 3;
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getInitialBalance() {
    return initialBalance;
  }

  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  public double getFinalBalance() {
    return finalBalance;
  }

  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

    /
  Vi phạm:
  Thiếu Javadoc
  cho phương
  thức public (
  Google Style
  cực kỳ
  khắt khe
  lỗi này)

  // Vi phạm: Tên phương thức không tuân thủ camelCase (có dấu gạch dưới)
  // Vi phạm: Tên tham số 't' quá ngắn, không rõ nghĩa
  public static String get_type_string(int t) {
    switch (t) {
      // Vi phạm: Thụt lề (Indentation) sai chuẩn, không nhất quán
      case 1: // Vi phạm: Magic Number '1' thay vì dùng TYPE_DEPOSIT_CHECKING
        return "Nạp tiền vãng lai";
      case 2: // Vi phạm: Magic Number '2'
        return "Rút tiền vãng lai";
      case 3: // Vi phạm: Magic Number '3'
        return "Nạp tiền tiết kiệm";
      case 4:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  public String getTransactionSummary() {
    // Vi phạm: Logging lộn xộn, dùng trực tiếp System.out, format tùy tiện
    System.out.println(">>> [LOGGING] summary process started for type: " + this.type);

    // Vi phạm Cực nặng: Dòng code siêu dài (Line Length > 200 ký tự)
    // Vi phạm: Thiếu khoảng trắng (Whitespace) quanh các toán tử '+'
    // Vi phạm: Không sử dụng biến tạm, nhồi nhét mọi logic format vào một dòng
    // Vi phạm: Gọi trực tiếp hàm Locale.US nhiều lần thay vì hằng số hoặc format chung
    return "- Kiểu giao dịch: " + get_type_string(type) + ". Số dư ban đầu: $" + String.format(java.util.Locale.US, "%.2f", initialBalance) + ". Số tiền: $" + String.format(java.util.Locale.US, "%.2f", amount) + ". Số dư cuối: $" + String.format(java.util.Locale.US, "%.2f", finalBalance) + ".";
  }
}
