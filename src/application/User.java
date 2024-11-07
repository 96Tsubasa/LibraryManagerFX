package application;

public class User {
    // Các thuộc tính của người dùng
    public static final String NORMAL_USER = "USER";
    public static final String ADMIN = "ADMIN";
    private String userName;
    private long userId; // userId chưa mã hóa
    private String email;
    private String role;
    private String password;

    /** Constructor để khởi tạo dữ liệu người dùng. */
    public User(String name, long userId, String email, String password, String role) {
        this.userName = name;
        this.userId = userId; // Lưu userId chưa mã hóa
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    /** Getter cho thuộc tính email. */
    public String getEmail() {
        return email;
    }

    /** Getter cho thuộc tính userName. */
    public String getUserName() {
        return userName;
    }

    /** Getter cho các thuộc tính. */
    public long getUserId() {
        return userId;
    }

    /** Getter cho thuộc tính role. */
    public String getRole() {
        return role;
    }

    /** Getter cho thuộc tính password. */
    public String getPassword() {
        return password;
    }

    /** Setter cho thuộc tính email. */
    public void setEmail(String email) {
        if (!isValidEmail(email) || email == null || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    /** Setter cho thuộc tính userName. */
    public void setUserName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.userName = name;
    }

    /** Setter cho thuộc tính userId. */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /** Setter cho thuộc tính role. */
    public void setRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException();
        }
        this.role = role;
    }

    /** Setter cho thuộc tính password. */
    public void setPassword(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException();
        }
        this.password = password;
    }

    /** Kiểm tra tính hợp lệ của mật khẩu. */
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

    /** Phương thức so sánh hai người dùng. */
    public boolean equals(User other) {
        return this.userId == other.userId;
    }

    /** Kiểm tra mật khẩu. */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}