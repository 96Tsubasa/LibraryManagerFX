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
    private String name;
    private String userId; // userId chưa mã hóa
    private String email;
    private jobTitle job;
    private String password;
    private final Map<String, String> userMap = new HashMap<>();

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
    public User(String name, String userId, String email, jobTitle job, String password) {
        this.name = name;
        this.userId = userId; // Lưu userId chưa mã hóa
        this.email = email;
        setPassword(password); // Mã hóa mật khẩu khi khởi tạo
        this.job = job;
        userMap.put(userId, this.password);
    }

    public User() {
    }

    /** Getter cho các thuộc tính. */
    public String getEmail() {
        return email;
    }

    /** Getter cho các thuộc tính. */
    public String getName() {
        return name;
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
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            showAlert("Tên không được để trống.");
            return;
        }
        this.name = name;
    }

    /** Setter cho các thuộc tính. */
    public void setUserId(String userId) {
        if (userId == null || userId.isEmpty() || userId.length() != 6) {
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
        if (password == null || password.length() < 6) {
            showAlert("Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }
        this.password = hashPassword(password);
    }

    /** Phương thức toString để hiển thị thông tin người dùng. */
    @Override
    public String toString() {
        return String.format("User ID: %s, Name: %s, Email: %s, Job: %s",
                userId, name, email, job);
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