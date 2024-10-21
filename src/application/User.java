package org.example.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class User {
    // Các thuộc tính của người dùng
    private String name;
    private LocalDate birthday;
    private String sex;
    private String address;
    private String userId; // userId chưa mã hóa
    private String email;
    private String job;

    // Constructor để khởi tạo dữ liệu người dùng
    public User(String name, LocalDate birthday, String sex, String address, String userId, String email, String job) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.address = address;
        this.userId = userId; // Lưu userId chưa mã hóa
        this.email = email;
    }

    // Getter cho các thuộc tính
    public String getEmail() {
        return email;
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

    // Phương thức tính tuổi dựa vào ngày sinh
    public int age() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
