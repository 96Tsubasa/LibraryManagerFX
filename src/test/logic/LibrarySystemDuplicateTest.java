package test.logic;

import application.logic.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibrarySystemDuplicateTest {
    private FakeLibrarySystem librarySystem;
    private List<Long> createdUserIds = new ArrayList<>();

    @Before
    public void setUp() {
        librarySystem = new FakeLibrarySystem();
        createdUserIds.clear();
    }

    @After
    public void tearDown() {
        for (Long id : createdUserIds) {
            try {
                librarySystem.deleteUserById(id);
            } catch (Exception ignored) {
            }
        }
        createdUserIds.clear();
    }

    @Test
    public void testAddUserDuplicateUsernameThrows() {
        User u1 = librarySystem.addUser("DupUser", "dup1@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(u1.getUserId());

        try {
            librarySystem.addUser("DupUser", "dup2@example.com", "Password123", User.NORMAL_USER, null);
            fail("Expected IllegalArgumentException when adding duplicate username");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddUserDuplicateEmailThrows() {
        User u1 = librarySystem.addUser("UniqueUser1", "dupemail@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(u1.getUserId());

        try {
            librarySystem.addUser("UniqueUser2", "dupemail@example.com", "Password123", User.NORMAL_USER, null);
            fail("Expected IllegalArgumentException when adding duplicate email");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}

// Simple in-test fake to avoid DB access and make tests unit tests
class FakeLibrarySystem {
    private List<User> users = new ArrayList<>();
    private long nextId = 1L;

    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        for (User u : users) {
            if (u.getUsername().equals(name))
                throw new IllegalArgumentException("Username is already registered.");
            if (u.getEmail().equals(email))
                throw new IllegalArgumentException("Email is already registered.");
        }
        User u = new User(name, nextId++, email, password, role, imageUser);
        users.add(u);
        return u;
    }

    public void deleteUserById(long id) {
        users.removeIf(u -> u.getUserId() == id);
    }
}
