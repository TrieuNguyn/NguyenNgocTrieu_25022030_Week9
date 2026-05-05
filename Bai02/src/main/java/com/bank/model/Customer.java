package com.bank.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một khách hàng của ngân hàng.
 *
 * <p>Mỗi khách hàng được xác định bởi số CMND ({@code idNumber}) và có thể
 * sở hữu nhiều tài khoản ngân hàng ({@link Account}) thuộc các loại khác nhau.</p>
 */
public class Customer {

  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /**
   * Tạo khách hàng với giá trị mặc định (dùng cho deserialization).
   */
  public Customer() {
    this(0L, "");
  }

  /**
   * Tạo khách hàng với số CMND và họ tên.
   *
   * @param idNumber số CMND (9 chữ số)
   * @param fullName họ và tên đầy đủ
   */
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<>();
  }

  /**
   * Trả về số CMND.
   *
   * @return số CMND
   */
  public long getIdNumber() {
    return idNumber;
  }

  /**
   * Cập nhật số CMND.
   *
   * @param idNumber số CMND mới
   */
  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  /**
   * Trả về họ tên đầy đủ.
   *
   * @return họ và tên
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Cập nhật họ tên.
   *
   * @param fullName họ và tên mới
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Trả về danh sách tài khoản của khách hàng.
   *
   * @return danh sách tài khoản
   */
  public List<Account> getAccountList() {
    return accountList;
  }

  /**
   * Thay thế toàn bộ danh sách tài khoản.
   *
   * @param accountList danh sách tài khoản mới; {@code null} được coi là danh sách rỗng
   */
  public void setAccountList(List<Account> accountList) {
    if (accountList == null) {
      this.accountList = new ArrayList<>();
    } else {
      this.accountList = accountList;
    }
  }

  /**
   * Thêm một tài khoản cho khách hàng nếu chưa tồn tại.
   *
   * @param account tài khoản cần thêm; bỏ qua nếu {@code null}
   */
  public void addAccount(Account account) {
    if (account == null) {
      return;
    }
    if (!accountList.contains(account)) {
      accountList.add(account);
    }
  }

  /**
   * Xóa một tài khoản khỏi danh sách của khách hàng.
   *
   * @param account tài khoản cần xóa; bỏ qua nếu {@code null}
   */
  public void removeAccount(Account account) {
    if (account == null) {
      return;
    }
    accountList.remove(account);
  }

  /**
   * Trả về thông tin tóm tắt của khách hàng.
   *
   * @return chuỗi gồm số CMND và họ tên
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
