package test.logic;

import application.logic.User;
import application.logic.UserSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import test.fakes.FakeUserRepository;

public class LibrarySystemDuplicateTest {
    private UserSystem librarySystem;
    private FakeUserRepository userRepo;
    private List<Long> createdUserIds = new ArrayList<>();

    @Before
    public void setUp() {
        userRepo = new FakeUserRepository();
        librarySystem = new UserSystem(userRepo);
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
