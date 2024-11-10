package application;

public class User {
    // User attributes
    public static final String NORMAL_USER = "USER";
    public static final String ADMIN = "ADMIN";
    private String username;
    private long userId;
    private String email;
    private String role;
    private String password;
    private boolean loggedIn;

    /** Constructor to initialize user data. */
    public User(String name, long userId, String email, String password, String role) {
        setUsername(name);       // Validates and sets username
        setUserId(userId);       // Directly sets userId (additional validation can be added here if needed)
        setEmail(email);         // Validates and sets email
        setPassword(password);   // Validates and sets password
        setRole(role);           // Validates and sets role
        this.loggedIn = false;
    }

    public User() {}

    /** Getter for email attribute. */
    public String getEmail() {
        return email;
    }

    /** Getter for username attribute. */
    public String getUsername() {
        return username;
    }

    /** Getter for userId attribute. */
    public long getUserId() {
        return userId;
    }

    /** Getter for role attribute. */
    public String getRole() {
        return role;
    }

    /** Getter for password attribute. */
    public String getPassword() {
        return password;
    }

    /** Setter for email attribute. */
    public void setEmail(String email) {
        if (!isValidEmail(email) || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid or empty email.");
        }
        this.email = email;
    }

    /** Setter for username attribute. */
    public void setUsername(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid or empty username.");
        }
        this.username = name;
    }

    /** Setter for userId attribute. */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /** Setter for role attribute. */
    public void setRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        this.role = role;
    }

    /** Setter for password attribute. */
    public void setPassword(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password does not meet complexity requirements.");
        }
        this.password = password;
    }

    /** Validates password complexity. */
    private boolean isValidPassword(String password) {
        // The password must be at least 8 characters long.
        // It must contain at least one uppercase letter.
        // It must contain at least one lowercase letter.
        // It must contain at least one numeric digit.
        // It must contain at least one special character from the set !@#$%^&*().
        // It must not contain any whitespace characters.
        return password.length() >= 8 && !password.matches(".*[!@#$%^&*()].*")
                && !password.contains(" ");
    }

    /** Validates email format. */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /** Checks if two users are equal based on userId. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return userId == other.userId;
    }

    /** Verifies password. */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /** Checks if user is logged in. */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /** Sets login status. */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
