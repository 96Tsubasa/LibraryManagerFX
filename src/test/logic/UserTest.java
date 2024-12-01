package test.logic;

import application.logic.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private String username;
    private long userId;
    private String email;
    private String password;
    private byte[] image;
    private User validUser;

    @Before
    public void setUp() {
        username = "testUsername";
        userId = 100001;
        email = "testmail@hotmail.com";
        password = "strongPassword";
        image = new byte[] {1, 2, 3};
        validUser = new User(username, userId, email, password, User.NORMAL_USER, image);
    }

    @Test
    public void testConstructor_ValidArguments() {
        User user = new User(username, userId, email, password, User.NORMAL_USER, image);

        assertEquals(username, user.getUsername());
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(User.NORMAL_USER, user.getRole());
        assertArrayEquals(image, user.getImageUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmail_InvalidEmail1() {
        String invalidEmail = "invalid@@@in.va.lid*#";
        validUser.setEmail(invalidEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmail_InvalidEmail2() {
        String invalidEmail = "invalid@Email";
        validUser.setEmail(invalidEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmail_InvalidEmail3() {
        String invalidEmail = "random string@with_spaces.com";
        validUser.setEmail(invalidEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPassword_InvalidPassword1() {
        String invalidPassword = "1234";
        validUser.setPassword(invalidPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPassword_InvalidPassword2() {
        String invalidPassword = "abc def 123";
        validUser.setPassword(invalidPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPassword_InvalidPassword3() {
        String invalidPassword = "abc";
        validUser.setPassword(invalidPassword);
    }

    @Test
    public void testCheckPassword_CorrectPassword() {
        assertTrue(validUser.checkPassword(password));
    }

    @Test
    public void testCheckPassword_IncorrectPassword() {
        String incorrectPassword = "SecuredPass123";
        assertFalse(validUser.checkPassword(incorrectPassword));
    }
}
