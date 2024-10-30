package org.example.library;

import java.time.LocalDate;
import java.time.Period;

public class User {
    // Các thuộc tính của người dùng
    private String name;
    private LocalDate birthday;
    private String sex;
    private String address;
    private String userId; // userId chưa mã hóa
    private String email;
    private String job;

    /** Constructor để khởi tạo dữ liệu người dùng. */
    public User(String name, LocalDate birthday, String sex, String address, String userId, String email, String job) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.address = address;
        this.userId = userId; // Lưu userId chưa mã hóa
        this.email = email;
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
    public String getAddress() {
        return address;
    }

    /** Getter cho các thuộc tính. */
    public String getJob() {
        return job;
    }

    /** Getter cho các thuộc tính. */
    public String getSex() {
        return sex;
    }

    /** Getter cho các thuộc tính. */
    public LocalDate getBirthday() {
        return birthday;
    }

    /** Getter cho các thuộc tính. */
    public void setEmail() {
        this.email = email;
    }

    /** Getter cho các thuộc tính. */
    public void setName() {
        this.name = name;
    }

    /** Getter cho các thuộc tính. */
    public void setUserId() {
        this.userId = userId;
    }

    /** Getter cho các thuộc tính. */
    public void setAddress() {
        this.address = address;
    }

    /** Getter cho các thuộc tính. */
    public void setJob() {
        this.job = job;
    }

    /** Getter cho các thuộc tính. */
    public void setSex() {
        this.sex = sex;
    }

    /** Getter cho các thuộc tính. */
    public void setBirthday() {
        this.birthday = birthday;
    }

    /** Phương thức tính tuổi dựa vào ngày sinh. */
    public int age() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
