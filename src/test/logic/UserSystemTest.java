package test.logic;

import application.database.Database;
import application.logic.User;
import application.logic.UserSystem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserSystemTest {
    private static UserSystem userSystem;
    private static User admin;
    private static List<Long> userIdAdded;

    @BeforeClass
    public static void setUpBeforeClass() {
        userSystem = new UserSystem();
        admin = userSystem.getUsers().get(0);
        userSystem.handleLogin(admin.getUsername(), admin.getPassword());
        userIdAdded = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        for (Long userId : userIdAdded) {
            try {
                Database.deleteUserById(userId);
            } catch (Exception e) {
                // Ignore this exception
            }
        }
    }

    @Test
    public void testHandleLogin_Success() {
        User newUser = userSystem.handleLogin(admin.getUsername(), admin.getPassword());

        assertNotNull(newUser);
    }

    @Test
    public void testHandleLogin_UsernameNotFound() {
        boolean failed = true;

        try {
            userSystem.handleLogin("TESTUsernameThatDoesntExist92811390", "12345678");
        } catch (Exception e) {
            assertEquals("Username does not exist.", e.getMessage());
            failed = false;
        }

        userSystem.handleLogin(admin.getUsername(), admin.getPassword());

        if (failed) {
            fail("testHandleLogin_UsernameNotFound() failed because either the username being " +
                    "used in the test actually exists in the system or the method being test " +
                    "does not behave as expected, please double check.");
        }
    }

    @Test
    public void testHandleLogin_IncorrectPassword() {
        boolean failed = true;

        try {
            userSystem.handleLogin(admin.getUsername(), admin.getPassword() + "no");
        } catch (Exception e) {
            assertEquals("Incorrect password.", e.getMessage());
            failed = false;
        }

        userSystem.handleLogin(admin.getUsername(), admin.getPassword());

        if (failed) {
            fail("testHandleLogin_IncorrectPassword() failed because the method being test " +
                    "does not behave as expected, please double check.");
        }
    }

    @Test
    public void testAddUser_Success1() {
        String username = "TESTUsernameThatDoesntExist12467142";
        String email = "TESTEmailThatDoesntExist85217852@abcxyz.net";
        String password = "12345678";
        byte[] image = {1, 2, 3, 4};

        int userCount = userSystem.getUsers().size();
        int normalUserCount = userSystem.getCountUser();

        User newUser = null;
        try {
            newUser = userSystem.addUser(username, email, password, User.NORMAL_USER, image);
            userIdAdded.add(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("testAddUser_Success() got an exception: " + e.getMessage());
            System.out.println("It is possible that the username or email being used in the " +
                    "test unit have been registered in the system before (Somehow), please " +
                    "double check.");
        }

        assertNotNull(newUser);
        assertEquals(userCount + 1, userSystem.getUsers().size());
        assertEquals(normalUserCount + 1, userSystem.getCountUser());

        assertEquals(username, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals(password, newUser.getPassword());
        assertEquals(User.NORMAL_USER, newUser.getRole());
        assertArrayEquals(image, newUser.getImageUser());
    }

    @Test
    public void testAddUser_Success2() {
        String username = "TESTUsernameThatDoesntExist12859142";
        String email = "TESTEmailThatDoesntExist85295722@abcxyz.net";
        String password = "12345678";
        byte[] image = {1, 2, 3, 4};

        int userCount = userSystem.getUsers().size();
        int adminCount = userSystem.getCountAdmin();

        User newUser = null;
        try {
            newUser = userSystem.addUser(username, email, password, User.ADMIN, image);
            userIdAdded.add(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("testAddUser_Success2() got an exception: " + e.getMessage());
            System.out.println("It is possible that the username or email being used in the " +
                    "test unit have been registered in the system before (Somehow), please " +
                    "double check.");
        }

        assertNotNull(newUser);
        assertEquals(userCount + 1, userSystem.getUsers().size());
        assertEquals(adminCount + 1, userSystem.getCountAdmin());

        assertEquals(username, newUser.getUsername());
        assertEquals(email, newUser.getEmail());
        assertEquals(password, newUser.getPassword());
        assertEquals(User.ADMIN, newUser.getRole());
        assertArrayEquals(image, newUser.getImageUser());
    }

    @Test
    public void testAddUser_UsernameRegistered() {
        String username = admin.getUsername();
        String email = "TESTEmailThatDoesntExist89182732@abcxyz.net";
        String password = "12345678";
        byte[] image = {1, 2, 3, 4};

        User newUser = null;
        try {
            newUser = userSystem.addUser(username, email, password, User.NORMAL_USER, image);
            userIdAdded.add(newUser.getUserId());

            fail("testAddUser_UsernameRegistered() failed because userSystem.addUser did not " +
                    "check if the username has been registered.");
        } catch (Exception e) {
            assertEquals("Username is already registered.", e.getMessage());
        }
    }

    @Test
    public void testAddUser_EmailRegistered() {
        String username = "TESTUsernameThatDoesntExist28572842";
        String email = admin.getEmail();
        String password = "12345678";
        byte[] image = {1, 2, 3, 4};

        User newUser = null;
        try {
            newUser = userSystem.addUser(username, email, password, User.NORMAL_USER, image);
            userIdAdded.add(newUser.getUserId());

            fail("testAddUser_EmailRegistered() failed because userSystem.addUser did not " +
                    "check if the email has been registered.");
        } catch (Exception e) {
            assertEquals("Email is already registered.", e.getMessage());
        }
    }

    @Test
    public void testGetUserById() {
        assertEquals(admin, userSystem.getUserById(admin.getUserId()));
    }
}
