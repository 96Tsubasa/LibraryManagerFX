package test.integration.application.controllers;

import application.controllers.LoginController;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginControllerTest {

    @Before
    public void reset() {
        // Ensure static currentUser reset between tests
        LoginController.currentUser = null;
    }

    @Test
    public void testInitialCurrentUserIsNull() {
        assertNull("LoginController.currentUser should be null before any login", LoginController.currentUser);
    }

    @Test
    public void testLoginEmptyInputsShowsError() throws Exception {
        // Production login logic validates credentials via LibrarySystem; calling with
        // empty inputs should result in an IllegalArgumentException (username not
        // found).
        try {
            application.logic.LibrarySystem.getInstance().handleLogin("", "");
            fail("Expected IllegalArgumentException for empty inputs");
        } catch (IllegalArgumentException ex) {
            // expected - compilation-only test update
        }
    }

    @Test
    public void testLoginInvalidUsernameShowsError() throws Exception {
        try {
            application.logic.LibrarySystem.getInstance().handleLogin("nonexistent_user_xyz", "somepass");
            fail("Expected IllegalArgumentException for invalid username");
        } catch (IllegalArgumentException ex) {
            // expected - compilation-only test update
        }
    }
}
