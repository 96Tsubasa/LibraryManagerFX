package test.user;

import application.logic.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserConstructor() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        assertEquals("TestUser", user.getUsername());
        assertEquals(1L, user.getUserId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Password123", user.getPassword());
        assertEquals(User.NORMAL_USER, user.getRole());
        assertNull(user.getImageUser());
        assertEquals(10, user.getLimitBook());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmailConstructor() {
        new User("TestUser", 1L, "invalid-email", "Password123", User.NORMAL_USER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEmailConstructor() {
        new User("TestUser", 1L, "", "Password123", User.NORMAL_USER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUsernameConstructor() {
        new User("", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeakPasswordConstructor() {
        new User("TestUser", 1L, "test@example.com", "weak", User.NORMAL_USER, null);
    }

    @Test
    public void testSetUsername() {
        User user = new User("OriginalUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setUsername("NewUser");
        assertEquals("NewUser", user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidUsername() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setUsername("");
    }

    @Test
    public void testSetEmail() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidEmail() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setEmail("invalid-email");
    }

    @Test
    public void testSetPassword() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setPassword("NewPassword456");
        assertEquals("NewPassword456", user.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWeakPassword() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setPassword("weak");
    }

    @Test
    public void testSetRole() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setRole(User.ADMIN);
        assertEquals(User.ADMIN, user.getRole());
    }

    @Test
    public void testSetImageUser() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        byte[] image = { 1, 2, 3 };
        user.setImageUser(image);
        assertArrayEquals(image, user.getImageUser());
    }

    @Test
    public void testSetLimitBook() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setLimitBook(5);
        assertEquals(5, user.getLimitBook());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidLimitBook() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setLimitBook(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLimitBookExceedLimit() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setLimitBook(11);
    }

    @Test
    public void testCheckPassword() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        assertTrue(user.checkPassword("Password123"));
        assertFalse(user.checkPassword("WrongPassword"));
    }

    @Test
    public void testUserEquality() {
        User user1 = new User("User1", 1L, "user1@example.com", "Password123", User.NORMAL_USER, null);
        User user2 = new User("User2", 1L, "user2@example.com", "Password123", User.NORMAL_USER, null);
        User user3 = new User("User3", 2L, "user3@example.com", "Password123", User.NORMAL_USER, null);

        assertEquals(user1, user2); // Same userId
        assertNotEquals(user1, user3); // Different userId
    }

    @Test
    public void testBorrowBook() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        int limitBefore = user.getLimitBook();
        user.borrowBook();
        assertEquals(limitBefore - 1, user.getLimitBook());
    }

    @Test
    public void testReturnBook() {
        User user = new User("TestUser", 1L, "test@example.com", "Password123", User.NORMAL_USER, null);
        user.setLimitBook(5);
        int limitBefore = user.getLimitBook();
        user.returnBook();
        assertEquals(limitBefore + 1, user.getLimitBook());
    }
}
