package application.logic;

import application.repository.DatabaseUserRepository;
import application.repository.UserRepository;

import java.util.List;

public class UserSystem {
    private final UserRepository repo;
    private final List<User> users;
    private User currentUser;
    private int countAdmin;
    private int countUser;

    /** Default constructor (production) uses Database-backed repository. */
    public UserSystem() {
        this(new DatabaseUserRepository());
    }

    /** Constructor for dependency injection (tests can pass a fake repo). */
    public UserSystem(UserRepository repo) {
        this.repo = repo;
        users = repo.loadUsers();
        setCount();
    }

    /** Set countAdmin and countUser. */
    private void setCount() {
        countUser = 0;
        countAdmin = 0;
        for (User user : users) {
            if (user.getRole().equals(User.ADMIN)) {
                countAdmin++;
            } else if (user.getRole().equals(User.NORMAL_USER)) {
                countUser++;
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getCountAdmin() {
        return countAdmin;
    }

    public int getCountUser() {
        return countUser;
    }

    /**
     * Create a new user, add to users List and database. Return the user object if
     * successful.
     */
    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        if (isUserRegistered(name)) {
            throw new IllegalArgumentException("Username is already registered.");
        }

        long userId = repo.createNewUserId();
        // Create a new user instance and add to the users list
        User user = new User(name, userId, email, password, role, imageUser);
        users.add(user);
        if (user.getRole().equals(User.ADMIN)) {
            countAdmin++;
        } else {
            countUser++;
        }
        repo.addUser(user);
        return user;
    }

    /** Checks if the email is already registered. */
    private boolean isEmailRegistered(String email) {
        for (User user : users) {
            // If an email match is found, throw an exception indicating the email is
            // already registered
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /** Checks if the username is already registered. */
    private boolean isUserRegistered(String username) {
        for (User user : users) {
            // If a username match is found, throw an exception indicating the email is
            // already registered
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check username and password with the users list, return null if no username
     * or false password.
     */
    public User handleLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.checkPassword(password)) {
                    currentUser = user;
                    return user;
                }
                throw new IllegalArgumentException("Incorrect password.");
            }
        }
        throw new IllegalArgumentException("Username does not exist.");
    }

    /** Return a reference to a user in the system with userId. */
    public User getUserById(long userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    /** Edit a user by userId. */
    public void editUserById(User user, String newUsername, String newEmail, String newPassword, String newRole,
            byte[] newImageUser) {
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setRole(newRole);
        user.setImageUser(newImageUser);
        repo.editUserById(user);
    }

    /** Delete a user in the system with userId. */
    public void deleteUserById(long userId) {
        users.removeIf(user -> user.getUserId() == userId);
        repo.deleteUserById(userId);
    }
}
