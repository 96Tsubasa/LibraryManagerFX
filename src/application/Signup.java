package application;

import javafx.scene.control.Alert;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;

class InfoAreaManager {
    private static InfoAreaManager instance;

    private InfoAreaManager() {
        // Constructor riêng tư để thực hiện mẫu Singleton
    }

    public static InfoAreaManager getInstance() {
        if (instance == null) {
            instance = new InfoAreaManager();
        }
        return instance;
    }

    /** Phương thức hiển thị thông báo */
    public void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

public class Signup extends User {
    private final String url = "jdbc:mysql://localhost:3306/user_management"; // Đường dẫn đến cơ sở dữ liệu
    private final String user = "your_username"; // Tên người dùng MySQL
    private final String password = "your_password"; // Mật khẩu MySQL

    /** Constructor: Tải dữ liệu người dùng từ cơ sở dữ liệu khi khởi tạo đối tượng. */
    Signup() {}

    /** Phương thức đăng ký tài khoản mới. */
    public void registerNewUser(String name, LocalDate birthday, GenderSex sex, String address, String userId,
                                String email, jobTitle job, String password, String confirmPassword) {

        InfoAreaManager infoAreaManager = InfoAreaManager.getInstance(); // Sử dụng InfoAreaManager để hiển thị thông báo

        // Kiểm tra tính hợp lệ của email
        if (email == null || email.isEmpty()) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Email không được để trống.");
            return;
        }
        if (!isValidEmail(email)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Định dạng email không hợp lệ.");
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu
        if (password == null || password.length() < 6) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }
        if (!isValidPassword(password)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, một chữ số và một ký tự đặc biệt.");
            return;
        }

        // Kiểm tra xem mật khẩu nhập lại có trùng khớp không
        if (!password.equals(confirmPassword)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Mật khẩu không trùng khớp.");
            return;
        }

        // Kiểm tra xem email đã được đăng ký hay chưa
        if (isEmailRegistered(email)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Email đã được đăng ký.");
            return;
        }

        // Băm mật khẩu
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return; // Nếu có lỗi trong việc băm mật khẩu, dừng lại
        }

        // Lưu thông tin người dùng vào cơ sở dữ liệu
        saveUserData(new User(name, birthday, sex, address, userId, email, job, hashedPassword));

        infoAreaManager.showAlert(Alert.AlertType.INFORMATION, "Đăng ký thành công!");
    }

    /** Phương thức lưu dữ liệu người dùng vào cơ sở dữ liệu. */
    private void saveUserData(User user) {
        String query = "INSERT INTO users (userId, name, birthday, sex, address, email, job, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setDate(3, java.sql.Date.valueOf(user.getBirthday()));
            pstmt.setString(4, user.getSex().name());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getJob().name());
            pstmt.setString(8, user.getPassword()); // Đã băm trước đó
            pstmt.executeUpdate();
        } catch (SQLException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Lỗi khi lưu dữ liệu người dùng vào cơ sở dữ liệu: " + e.getMessage());
        }
    }

    /** Kiểm tra xem email đã được đăng ký hay chưa. */
    private boolean isEmailRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có ít nhất một bản ghi thì email đã được đăng ký
            }
        } catch (SQLException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Lỗi khi kiểm tra email: " + e.getMessage());
        }
        return false;
    }

    /** Kiểm tra định dạng email hợp lệ. */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /** Kiểm tra tính phức tạp của mật khẩu. */
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()].*");
    }

    /** Băm mật khẩu bằng SHA-256. */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Lỗi khi băm mật khẩu: " + e.getMessage());
            return null; // Trả về null nếu có lỗi
        }
    }
}
