package test.integration.application.controllers;

import application.controllers.LoginController;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

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
        final AtomicBoolean alerted = new AtomicBoolean(false);
        LoginController controller = new LoginController() {
            @Override
            protected void showAlert(javafx.scene.control.Alert.AlertType alertType, String title, String message) {
                alerted.set(true);
            }
        };

        // call the test-friendly login method (avoids JavaFX toolkit)
        controller.loginWithCredentialsForTest("", "");

        assertTrue("showAlert should be invoked for empty inputs", alerted.get());
        assertNull(LoginController.currentUser);
    }

    @Test
    public void testLoginInvalidUsernameShowsError() throws Exception {
        final AtomicBoolean alerted = new AtomicBoolean(false);
        LoginController controller = new LoginController() {
            @Override
            protected void showAlert(javafx.scene.control.Alert.AlertType alertType, String title, String message) {
                alerted.set(true);
            }
        };

        // call the test-friendly login method (avoids JavaFX toolkit)
        controller.loginWithCredentialsForTest("nonexistent_user_xyz", "somepass");

        assertTrue("showAlert should be invoked for invalid username", alerted.get());
        assertNull(LoginController.currentUser);
    }
}
