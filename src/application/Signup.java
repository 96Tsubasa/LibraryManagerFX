package org.example.library;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;


public class Signup {
    private final String userDir = "users"; // Thư mục chứa thông tin người dùng
    private final String userFilePath = "users.txt"; // Đường dẫn tới file lưu thông tin người dùng
    private final Map<String, String> userMap = new HashMap<>();

    /** Constructor: Tải dữ liệu người dùng từ file khi khởi tạo đối tượng. */
    public Signup() {
        createUserFileIfNotExists();
        loadUserData();
    }

    /** Phương thức đăng ký tài khoản mới. */
    public void registerNewUser(String name, LocalDate birthday, String sex, String address, String userId,
                                String email, String job, String password, String confirmPassword) {

        InfoAreaManager infoAreaManager = InfoAreaManager.getInstance(); // Sử dụng InfoAreaManager để hiển thị thông báo

        // Kiểm tra tính hợp lệ của email
        if (email == null || email.isEmpty()) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Email cannot be empty.");
            return;
        }
        if (!isValidEmail(email)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Invalid email format.");
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu
        if (password == null || password.length() < 6) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Password must be at least 6 characters long.");
            return;
        }
        if (!isValidPassword(password)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Password must be at least 8 characters long, include uppercase, lowercase, a digit, and a special character.");
            return;
        }

        // Kiểm tra xem mật khẩu nhập lại có trùng khớp không
        if (!password.equals(confirmPassword)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
            return;
        }

        // Kiểm tra xem email đã được đăng ký hay chưa
        if (userMap.containsKey(email)) {
            infoAreaManager.showAlert(Alert.AlertType.ERROR, "Email is already registered.");
            return;
        }

        User newUser = new User(name, birthday, sex, address, userId, email, job);

        // Băm mật khẩu và lưu vào bản đồ người dùng
        String hashedPassword = hashPassword(password);
        userMap.put(email, hashedPassword);

        saveUserData(newUser, hashedPassword);

        infoAreaManager.showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
        infoAreaManager.appendText("User registered: " + email + "\n");
    }

    /** Phương thức lưu dữ liệu người dùng vào file. */
    private void saveUserData(User user, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            // Ghi thông tin người dùng và mật khẩu băm vào file
            writer.write(user.getUserId() + ":" + user.getName() + ":" + user.getBirthday() + ":" + user.getSex() + ":"
                    + user.getAddress() + ":" + user.getEmail() + ":" + user.getJob() + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Error saving user data. Please try again.");
        }
    }

    /** Tạo file nếu chưa tồn tại. */
    private void createUserFileIfNotExists() {
        try {
            // Tạo thư mục nếu chưa tồn tại
            File userDirectory = new File(userDir);
            if (!userDirectory.exists()) {
                userDirectory.mkdirs(); // Tạo tất cả các thư mục nếu chưa tồn tại
            }
    
            // Tạo file nếu chưa tồn tại trong thư mục
            File file = new File(userDirectory, userFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Error creating user file.");
        }
    }

    /** Tải dữ liệu người dùng từ file. */
    private void loadUserData() {
        if (!Files.exists(Paths.get(userFilePath))) {
            return; // Nếu file không tồn tại, không cần tải dữ liệu
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 9) {
                    String email = parts[5];
                    String password = parts[8];
                    userMap.put(email, password); // Thêm email và mật khẩu vào map
                }
            }
        } catch (IOException e) {
            InfoAreaManager.getInstance().showAlert(Alert.AlertType.ERROR, "Error loading user data.");
        }
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
            throw new RuntimeException(e);
        }
    }
}
