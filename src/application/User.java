package org.example.library;

import java.time.LocalDate;
import java.time.Period;

public class User {
    private final String userDir = "users";
    private final String userFilePath = "users.txt";

    private String name;
    private LocalDate birthday;
    private String sex;
    private String address;
    private int userId;
    private String email;
    private String job;
    private String code;

    // Constructor để khởi tạo dữ liệu người dùng
    public User(String name, LocalDate birthday, String sex, String address, int userId, String email, String job, String code) {
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

    public int getUserId() {
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

    public int age(LocalDate birth, LocalDate currentDate) {
        Period period = Period.between(birth, currentDate);
        return period.getYears();
    }
}
