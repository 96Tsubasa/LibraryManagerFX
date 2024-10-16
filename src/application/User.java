package org.example.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String userDir = "users";
    private final String userFilePath = "users.txt";
    private Map<String, Integer> userMap = new HashMap<>();

    private String name;
    private LocalDate birthday;
    private String sex;
    private String address;
    private String userId;
    private String email;
    private String job;
    private String code;

    // Constructor để khởi tạo dữ liệu người dùng
    public User(String name, LocalDate birthday, String sex, String address, String userId, String email, String job, String code) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.address = address;
        this.userId = userId;
        this.email = email;
        this.job = job;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getJob() {
        return job;
    }

    public String getSex() {
        return sex;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getUserFilePath() {
        return userFilePath;
    }

    public int age(LocalDate birth, LocalDate currentDate) {
        Period period = Period.between(birth, currentDate);
        return period.getYears();
    }

    // Mã hóa userId sử dụng SHA-256
    private String hashUserId(String userId) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(String.valueOf(userId).getBytes());
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Kiểm tra thông tin đăng nhập (dựa vào userId)
    public boolean checkUserLogin(String username, String userId) {
        if (userMap.containsKey(username)) {
            try {
                String hashedUserId = hashUserId(userId);
                String storedHashedUserId = String.valueOf(userMap.get(username));
                return storedHashedUserId.equals(hashedUserId);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
