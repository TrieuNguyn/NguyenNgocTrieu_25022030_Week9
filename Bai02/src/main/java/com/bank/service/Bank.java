package com.bank.service;

import com.bank.model.Account;
import com.bank.model.CheckingAccount;
import com.bank.model.Customer;
import com.bank.model.SavingsAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dịch vụ quản lý trung tâm của ngân hàng.
 *
 * <p>Chịu trách nhiệm quản lý danh sách khách hàng, nạp dữ liệu từ file,
 * và cung cấp các truy vấn thông tin theo nhiều thứ tự sắp xếp khác nhau.</p>
 */
public class Bank {

  private static final Logger logger = LoggerFactory.getLogger(Bank.class);

  // FIX: Đổi c_list (snake_case vi phạm) → customerList (camelCase đúng chuẩn).
  private List<Customer> customerList;

  // FIX: Magic String pattern số CMND trở thành hằng số có tên.
  // Lý do: (1) dễ thay đổi sau này, (2) tự document — tên hằng giải thích ý nghĩa.
  private static final String ID_NUMBER_PATTERN = "\\d{9}";

  // FIX: Magic String "CHECKING"/"SAVINGS" → dùng hằng số từ Account
  // để tránh typo và đảm bảo nhất quán giữa parse và so sánh.
  private static final String ACCOUNT_TYPE_CHECKING = Account.CHECKING_TYPE;
  private static final String ACCOUNT_TYPE_SAVINGS = Account.SAVINGS_TYPE;

  // Số phần tử tối thiểu trên một dòng tài khoản: [accountNum] [type] [balance]
  private static final int ACCOUNT_LINE_PARTS = 3;

  /**
   * Khởi tạo ngân hàng với danh sách khách hàng rỗng.
   */
  public Bank() {
    this.customerList = new ArrayList<>();
    logger.info("Hệ thống ngân hàng khởi động");
  }

  /**
   * Trả về danh sách toàn bộ khách hàng.
   *
   * @return danh sách khách hàng
   */
  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Thay thế danh sách khách hàng.
   *
   * @param customerList danh sách mới; {@code null} được coi là danh sách rỗng
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
    logger.debug("Danh sách khách hàng được cập nhật: {} khách hàng", this.customerList.size());
  }

  /**
   * Nạp danh sách khách hàng và tài khoản từ {@link InputStream}.
   *
   * <p>Định dạng file:</p>
   * <ul>
   *   <li>Dòng khách hàng: {@code [Họ tên] [9 chữ số CMND]}</li>
   *   <li>Dòng tài khoản: {@code [Số TK] [CHECKING|SAVINGS] [Số dư]}</li>
   * </ul>
   *
   * <p>FIX: Code gốc có nhiều vi phạm nghiêm trọng trong method này:</p>
   * <ul>
   *   <li>Catch {@code Exception} (quá chung) → tách thành {@code IOException}
   *       và {@code NumberFormatException} để xử lý đúng từng trường hợp</li>
   *   <li>System.out.println → logger với đúng level</li>
   *   <li>If lồng 5 cấp → tách thành các method phụ {@code parseCustomerLine}
   *       và {@code parseAccountLine} để giảm Cyclomatic Complexity</li>
   *   <li>Magic String pattern regex trực tiếp → hằng số {@code ID_NUMBER_PATTERN}</li>
   * </ul>
   *
   * @param inputStream luồng dữ liệu đầu vào; bỏ qua nếu {@code null}
   */
  public void readCustomerList(InputStream inputStream) {
    if (inputStream == null) {
      logger.warn("readCustomerList nhận InputStream null, bỏ qua");
      return;
    }
    logger.info("Bắt đầu đọc dữ liệu khách hàng từ InputStream");
    // FIX: Thêm charset tường minh StandardCharsets.UTF_8 —
    // dùng InputStreamReader không có charset phụ thuộc vào platform default.
    try (BufferedReader reader =
             new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      Customer currentCustomer = null;
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }
        currentCustomer = processLine(line, currentCustomer);
      }
      logger.info("Đọc dữ liệu hoàn tất: {} khách hàng", customerList.size());
    } catch (IOException e) {
      // ERROR: lỗi I/O là lỗi hệ thống, không phải lỗi nghiệp vụ — dùng ERROR level
      // và ghi kèm exception object để có đầy đủ stack trace
      logger.error("Lỗi I/O khi đọc dữ liệu khách hàng", e);
    }
  }

  /**
   * Xử lý một dòng dữ liệu: xác định đây là dòng khách hàng hay dòng tài khoản.
   *
   * @param line            dòng dữ liệu (đã được trim)
   * @param currentCustomer khách hàng đang được xử lý, có thể {@code null}
   * @return khách hàng hiện tại (cập nhật nếu đây là dòng khách hàng mới)
   */
  private Customer processLine(String line, Customer currentCustomer) {
    int lastSpace = line.lastIndexOf(' ');
    if (lastSpace <= 0) {
      return currentCustomer;
    }
    String lastToken = line.substring(lastSpace + 1).trim();
    if (lastToken.matches(ID_NUMBER_PATTERN)) {
      return parseCustomerLine(line, lastToken, lastSpace);
    }
    parseAccountLine(line, currentCustomer);
    return currentCustomer;
  }

  /**
   * Phân tích dòng khách hàng và thêm vào danh sách.
   *
   * @param line       dòng đầy đủ
   * @param idToken    token số CMND đã trích xuất
   * @param nameEndIdx chỉ số kết thúc của phần họ tên trong dòng
   * @return đối tượng {@link Customer} mới được tạo
   */
  private Customer parseCustomerLine(String line, String idToken, int nameEndIdx) {
    try {
      String name = line.substring(0, nameEndIdx).trim();
      long idNumber = Long.parseLong(idToken);
      Customer customer = new Customer(idNumber, name);
      customerList.add(customer);
      // INFO: thêm khách hàng mới là sự kiện nghiệp vụ quan trọng
      logger.info("Thêm khách hàng: '{}' CMND #{}", name, idNumber);
      return customer;
    } catch (NumberFormatException e) {
      logger.warn("Không thể parse số CMND từ dòng: '{}'", line);
      return null;
    }
  }

  /**
   * Phân tích dòng tài khoản và gắn vào khách hàng hiện tại.
   *
   * @param line            dòng tài khoản
   * @param currentCustomer khách hàng sở hữu tài khoản; bỏ qua nếu {@code null}
   */
  private void parseAccountLine(String line, Customer currentCustomer) {
    if (currentCustomer == null) {
      logger.warn("Tìm thấy dòng tài khoản nhưng chưa có khách hàng: '{}'", line);
      return;
    }
    String[] parts = line.split("\\s+");
    if (parts.length < ACCOUNT_LINE_PARTS) {
      logger.warn("Dòng tài khoản không đủ thông tin (cần {}): '{}'", ACCOUNT_LINE_PARTS, line);
      return;
    }
    try {
      long accountNumber = Long.parseLong(parts[0]);
      String accountType = parts[1];
      double balance = Double.parseDouble(parts[2]);
      Account account = createAccount(accountNumber, accountType, balance);
      if (account != null) {
        currentCustomer.addAccount(account);
        logger.debug("Gắn tài khoản #{} ({}) vào khách hàng CMND #{}",
            accountNumber, accountType, currentCustomer.getIdNumber());
      }
    } catch (NumberFormatException e) {
      logger.warn("Dòng tài khoản chứa giá trị số không hợp lệ: '{}'", line);
    }
  }

  /**
   * Tạo đối tượng tài khoản phù hợp dựa trên loại tài khoản.
   *
   * @param accountNumber mã số tài khoản
   * @param accountType   loại tài khoản ({@code CHECKING} hoặc {@code SAVINGS})
   * @param balance       số dư ban đầu
   * @return tài khoản phù hợp, hoặc {@code null} nếu loại không hợp lệ
   */
  private Account createAccount(long accountNumber, String accountType, double balance) {
    if (ACCOUNT_TYPE_CHECKING.equals(accountType)) {
      return new CheckingAccount(accountNumber, balance);
    }
    if (ACCOUNT_TYPE_SAVINGS.equals(accountType)) {
      return new SavingsAccount(accountNumber, balance);
    }
    logger.warn("Loại tài khoản không hợp lệ: '{}' cho tài khoản #{}", accountType, accountNumber);
    return null;
  }

  /**
   * Trả về thông tin tất cả khách hàng, sắp xếp theo số CMND tăng dần.
   *
   * <p>FIX: Code gốc dùng {@code Collections.sort} với Anonymous class thay vì
   * lambda (verbose không cần thiết từ Java 8+), và nối chuỗi trong vòng lặp.
   * Bản refactor dùng {@code List.sort} với method reference và {@link StringBuilder}.</p>
   *
   * @return chuỗi thông tin khách hàng, mỗi khách hàng một dòng
   */
  public String getCustomersInfoByIdOrder() {
    logger.debug("Lấy danh sách khách hàng theo thứ tự CMND");
    // FIX: Thay Anonymous class → Comparator method reference (ngắn gọn hơn)
    customerList.sort(Comparator.comparingLong(Customer::getIdNumber));
    return buildCustomerInfoString(customerList);
  }

  /**
   * Trả về thông tin tất cả khách hàng, sắp xếp theo tên rồi CMND.
   *
   * <p>FIX: Code gốc có logic sort và build string giống hệt
   * {@code getCustomersInfoByIdOrder} — vi phạm DRY (Don't Repeat Yourself).
   * Tách phần build string ra method {@code buildCustomerInfoString} dùng chung.</p>
   *
   * @return chuỗi thông tin khách hàng, sắp xếp theo tên
   */
  public String getCustomersInfoByNameOrder() {
    logger.debug("Lấy danh sách khách hàng theo thứ tự tên");
    List<Customer> sorted = new ArrayList<>(customerList);
    sorted.sort(Comparator.comparing(Customer::getFullName)
        .thenComparingLong(Customer::getIdNumber));
    return buildCustomerInfoString(sorted);
  }

  /**
   * Xây dựng chuỗi thông tin từ danh sách khách hàng.
   *
   * <p>Method helper tránh trùng lặp giữa các phương thức sắp xếp khác nhau.</p>
   *
   * @param customers danh sách khách hàng đã sắp xếp
   * @return chuỗi thông tin, mỗi khách hàng một dòng
   */
  private String buildCustomerInfoString(List<Customer> customers) {
    // FIX: Code gốc dùng res += ... trong vòng lặp → O(n²) string allocation.
    // StringBuilder giải quyết bằng cách append trực tiếp vào buffer → O(n).
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < customers.size(); i++) {
      sb.append(customers.get(i).getCustomerInfo());
      if (i < customers.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
