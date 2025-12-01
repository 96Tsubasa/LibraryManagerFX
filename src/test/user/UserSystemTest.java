package test.user;

import application.logic.User;
import application.logic.UserSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import test.fakes.FakeUserRepository;

import java.util.*;

public class UserSystemTest {
    // Use a lightweight in-memory fake repo and the real UserSystem
    private UserSystem userSystem;
    private FakeUserRepository fakeRepo;
    private List<Long> createdUserIds = new ArrayList<>();

    @Before
    public void setUp() {
        fakeRepo = new FakeUserRepository();
        userSystem = new UserSystem(fakeRepo);
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

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserEmptyUsername() {
        userSystem.addUser("", "emptyname@example.com", "pass", User.NORMAL_USER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserNullEmail() {
        userSystem.addUser("userTestNullEmail", null, "passTestNullEmail", User.NORMAL_USER, null);
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

    @Test(expected = IllegalArgumentException.class)
    public void testLoginWithNullUsername() {
        userSystem.handleLogin(null, "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginWithEmptyPassword() {
        userSystem.addUser("user", "email@test.com", "realpass", User.NORMAL_USER, null);
        userSystem.handleLogin("user", "");
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
    public void testEditUser_ChangeRoleToAdmin_IncreasesAdminCount() {
        User user = userSystem.addUser("userChangeRoleToAdmin", "emailChangeRoleToAdmin@gmail.com", "Password",
                User.NORMAL_USER, null);
        int adminBefore = userSystem.getCountAdmin();

        userSystem.editUserById(user, "userChangeRoleToAdmin", "emailChangeRoleToAdmin@gmail.com", "Password",
                User.ADMIN, null);

        assertEquals(adminBefore + 1, userSystem.getCountAdmin());
    }

    @Test
    public void testEditUser_ChangeRoleFromAdminToUser_DecreasesAdminCount() {
        User admin = userSystem.addUser("adminChangeRoleToUser", "adminChangeRoleToUser@gmail.com", "Password123",
                User.ADMIN, null);
        int adminBefore = userSystem.getCountAdmin();

        userSystem.editUserById(admin, "adminChangeRoleToUser", "adminChangeRoleToUser@gmail.com", "Password123",
                User.NORMAL_USER, null);

        assertEquals(adminBefore - 1, userSystem.getCountAdmin());
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
    public void testDeleteAdmin_DecreasesAdminCount() {
        User admin = userSystem.addUser("adminDeleteDecreaseCount", "adminDeleteDecreaseCount@gmail.com", "Password123",
                User.ADMIN, null);
        int adminCount = userSystem.getCountAdmin();

        userSystem.deleteUserById(admin.getUserId());

        assertEquals(adminCount - 1, userSystem.getCountAdmin());
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
