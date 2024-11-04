package application;

import javafx.scene.control.Alert;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class User {
    // Các thuộc tính của người dùng
    private String userName;
    private String userId; // userId chưa mã hóa
    private String email;
    private jobTitle job;
    private String password;

    public enum jobTitle {
        SOFTWARE_ENGINEER,        // Kỹ sư phần mềm
        WEB_DEVELOPER,            // Nhà phát triển web
        PROJECT_MANAGER,          // Quản lý dự án
        MARKETING_SPECIALIST,     // Chuyên viên marketing
        GRAPHIC_DESIGNER,         // Nhà thiết kế đồ họa
        DATA_ANALYST,             // Nhà phân tích dữ liệu
        STUDENT,                  // Sinh viên
        TEACHER,                  // Giáo viên
        LECTURER,                 // Giảng viên
        RESEARCHER,               // Nhà nghiên cứu
        ADMINISTRATOR,            // Quản trị viên
        UNIVERSITY_STAFF          // Nhân viên đại học
    }

    /** Constructor để khởi tạo dữ liệu người dùng. */
    public User(String name, String userId, String email, String password, jobTitle job) {
        this.userName = name;
        this.userId = userId; // Lưu userId chưa mã hóa
        this.email = email;
        setPassword(password); // Mã hóa mật khẩu khi khởi tạo
        this.job = job;
    }

    public User() {
    }

    /** Getter cho các thuộc tính. */
    public String getEmail() {
        return email;
    }

    /** Getter cho các thuộc tính. */
    public String getUserName() {
        return userName;
    }

    /** Getter cho các thuộc tính. */
    public String getUserId() {
        return userId;
    }

    /** Getter cho các thuộc tính. */
    public jobTitle getJob() {
        return job;
    }

    /** Getter cho các thuộc tính. */
    public String getPassword() {
        return password;
    }

    /** getter cho các thuộc tính. */
    public void setEmail(String email) {
        if (!isValidEmail(email) || email == null || email.isEmpty()) {
            showAlert("Email không hợp lệ.");
            return;
        }
        this.email = email;
    }

    /** Setter cho các thuộc tính. */
    public void setUserName(String name) {
        if (name == null || name.isEmpty()) {
            showAlert("Tên không được để trống.");
            return;
        }
        this.userName = name;
    }

    /** Setter cho các thuộc tính. */
    public void setUserId(String userId) {
        if (userId == null || userId.isEmpty() || !isValidUserId(userId)) {
            showAlert("Mã định danh sai.");
            return;
        }
        this.userId = userId;
    }

    /** Setter cho các thuộc tính. */
    public void setJob(jobTitle job) {
        if (job == null) {
            showAlert("Công việc không được để trống.");
            return;
        }
        this.job = job;
    }

    /** Setter cho các thuộc tính. */
    public void setPassword(String password) {
        if (!isValidPassword(password)) {
            showAlert("Sai định dạng mật khẩu.");
            return;
        }
        this.password = hashPassword(password);
    }

    /** Kiểm tra tính phức tạp của mật khẩu. */
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()].*");
    }

    /** Kiểm tra tính phức tạp của userId. */
    private boolean isValidUserId(String userId) {
        return userId.length() == 6;
    }

    /** Kiểm tra định dạng email hợp lệ. */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // Phương thức mã hóa mật khẩu
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu", e);
        }
    }

    // Phương thức so sánh hai người dùng
    public boolean equals(User other) {
        return this.userId.equals(other.userId);
    }

    /** Hiển thị thông báo lỗi bằng JavaFX. */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Kiểm tra mật khẩu. */
    public boolean checkPassword(String password) {
        return this.password.equals(hashPassword(password));
    }
}