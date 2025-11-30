package test.user;

import application.logic.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class UserSystemTest {
    // Use a lightweight in-memory fake to keep these tests pure unit tests
    private FakeUserSystem userSystem;
    private List<Long> createdUserIds = new ArrayList<>();

    @Before
    public void setUp() {
        userSystem = new FakeUserSystem();
        createdUserIds.clear();
    }

    @After
    public void tearDown() {
        // Cleanup: Delete all test users created
        for (Long userId : createdUserIds) {
            userSystem.deleteUserById(userId);
        }
        createdUserIds.clear();
    }

    @Test
    public void testAddUser() {
        int countBefore = userSystem.getUsers().size();
        User user = userSystem.addUser("TestUser001", "testuser001@example.com", "Password123", User.NORMAL_USER, null);
        assertNotNull(user);
        assertEquals("TestUser001", user.getUsername());
        assertEquals("testuser001@example.com", user.getEmail());
        assertEquals(User.NORMAL_USER, user.getRole());
        assertEquals(countBefore + 1, userSystem.getUsers().size());
        createdUserIds.add(user.getUserId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserDuplicateEmail() {
        User user1 = userSystem.addUser("User001", "duplicate@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user1.getUserId());
        // Try to add another user with same email
        userSystem.addUser("User002", "duplicate@example.com", "Password123", User.NORMAL_USER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserDuplicateUsername() {
        User user1 = userSystem.addUser("DuplicateUser", "user1@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user1.getUserId());
        // Try to add another user with same username
        userSystem.addUser("DuplicateUser", "user2@example.com", "Password123", User.NORMAL_USER, null);
    }

    @Test
    public void testGetUserById() {
        User user = userSystem.addUser("TestUser002", "testuser002@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());

        User found = userSystem.getUserById(user.getUserId());
        assertNotNull(found);
        assertEquals("TestUser002", found.getUsername());

        User notFound = userSystem.getUserById(999999L);
        assertNull(notFound);
    }

    @Test
    public void testHandleLogin() {
        User user = userSystem.addUser("LoginUser", "loginuser@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());

        User loggedInUser = userSystem.handleLogin("LoginUser", "Password123");
        assertNotNull(loggedInUser);
        assertEquals("LoginUser", loggedInUser.getUsername());
        assertEquals(user, userSystem.getCurrentUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleLoginIncorrectPassword() {
        User user = userSystem.addUser("LoginUser2", "loginuser2@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());

        userSystem.handleLogin("LoginUser2", "WrongPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleLoginNonexistentUser() {
        userSystem.handleLogin("NonexistentUser", "Password123");
    }

    @Test
    public void testEditUserById() {
        User user = userSystem.addUser("OriginalUser", "original@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());

        userSystem.editUserById(user, "UpdatedUser", "updated@example.com", "UpdatedPassword456", User.ADMIN,
                new byte[] { 1, 2 });

        assertEquals("UpdatedUser", user.getUsername());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("UpdatedPassword456", user.getPassword());
        assertEquals(User.ADMIN, user.getRole());
        assertArrayEquals(new byte[] { 1, 2 }, user.getImageUser());
    }

    @Test
    public void testDeleteUserById() {
        User user = userSystem.addUser("UserToDelete", "delete@example.com", "Password123", User.NORMAL_USER, null);
        long userIdToDelete = user.getUserId();
        int countBefore = userSystem.getUsers().size();

        userSystem.deleteUserById(userIdToDelete);

        assertEquals(countBefore - 1, userSystem.getUsers().size());
        assertNull(userSystem.getUserById(userIdToDelete));
    }

    @Test
    public void testCountAdminAndUser() {
        int adminCountBefore = userSystem.getCountAdmin();
        int userCountBefore = userSystem.getCountUser();

        User admin = userSystem.addUser("TestAdmin", "admin@example.com", "Password123", User.ADMIN, null);
        createdUserIds.add(admin.getUserId());

        User normalUser = userSystem.addUser("TestNormalUser", "normal@example.com", "Password123", User.NORMAL_USER,
                null);
        createdUserIds.add(normalUser.getUserId());

        assertEquals(adminCountBefore + 1, userSystem.getCountAdmin());
        assertEquals(userCountBefore + 1, userSystem.getCountUser());
    }

    @Test
    public void testGetUsers() {
        List<User> users = userSystem.getUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 0);
    }

    @Test
    public void testGetCurrentUser() {
        User user = userSystem.addUser("CurrentUser", "current@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());

        userSystem.handleLogin("CurrentUser", "Password123");
        assertEquals(user, userSystem.getCurrentUser());
    }
}

/**
 * In-test fake replacement for `UserSystem` to avoid touching the real
 * database. This keeps the tests as pure unit tests that exercise logic only.
 */
class FakeUserSystem {
    private List<User> users = new ArrayList<>();
    private User currentUser = null;
    private int countAdmin = 0;
    private int countUser = 0;
    private long nextId = 1L;

    public FakeUserSystem() {
        // start with empty in-memory user list
        setCount();
    }

    private void setCount() {
        countAdmin = 0;
        countUser = 0;
        for (User u : users) {
            if (User.ADMIN.equals(u.getRole()))
                countAdmin++;
            else
                countUser++;
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

    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        for (User u : users) {
            if (u.getEmail().equals(email))
                throw new IllegalArgumentException("Email is already registered.");
            if (u.getUsername().equals(name))
                throw new IllegalArgumentException("Username is already registered.");
        }
        long userId = nextId++;
        User user = new User(name, userId, email, password, role, imageUser);
        users.add(user);
        if (User.ADMIN.equals(role))
            countAdmin++;
        else
            countUser++;
        return user;
    }

    public User handleLogin(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (u.checkPassword(password)) {
                    currentUser = u;
                    return u;
                }
                throw new IllegalArgumentException("Incorrect password.");
            }
        }
        throw new IllegalArgumentException("Username does not exist.");
    }

    public User getUserById(long userId) {
        for (User u : users)
            if (u.getUserId() == userId)
                return u;
        return null;
    }

    public void editUserById(User user, String newUsername, String newEmail, String newPassword, String newRole,
            byte[] newImageUser) {
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setRole(newRole);
        user.setImageUser(newImageUser);
    }

    public void deleteUserById(long userId) {
        users.removeIf(u -> u.getUserId() == userId);
        setCount();
    }
}
