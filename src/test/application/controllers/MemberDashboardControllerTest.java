package test.application.controllers;

import application.controllers.MemberDashboardController;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@Ignore("Requires JavaFX runtime - considered an integration/UI test and skipped in unit test runs")
public class MemberDashboardControllerTest {
    private MemberDashboardController controller;

    @Before
    public void setUp() throws Exception {
        controller = new MemberDashboardController();

        // Set private FXML fields via reflection
        setField(controller, "browseBook", new AnchorPane());
        setField(controller, "profile", new AnchorPane());
        setField(controller, "inventory", new AnchorPane());

        // ensure visible defaults
        getField(controller, "browseBook").setVisible(true);
        getField(controller, "profile").setVisible(false);
        getField(controller, "inventory").setVisible(false);

        // default currentPane is "browseBook" but set explicitly via reflection
        setField(controller, "currentPane", "browseBook");
    }

    @Test
    public void testSwitchToProfile() throws Exception {
        controller.switchToProfile();

        AnchorPane browse = getField(controller, "browseBook");
        AnchorPane profile = getField(controller, "profile");
        AnchorPane inventory = getField(controller, "inventory");

        assertFalse("browse should be hidden", browse.isVisible());
        assertTrue("profile should be visible", profile.isVisible());
        assertFalse("inventory should be hidden", inventory.isVisible());
    }

    @Test
    public void testDisablePane() throws Exception {
        // Set currentPane to profile and ensure profile visible
        setField(controller, "currentPane", "profile");
        getField(controller, "profile").setVisible(true);

        controller.disablePane();

        AnchorPane profile = getField(controller, "profile");
        assertFalse("profile should be hidden after disablePane", profile.isVisible());
    }

    // --- Reflection helpers ---
    private static AnchorPane getField(MemberDashboardController controller, String name) throws Exception {
        Field f = MemberDashboardController.class.getDeclaredField(name);
        f.setAccessible(true);
        return (AnchorPane) f.get(controller);
    }

    private static void setField(MemberDashboardController controller, String name, Object value) throws Exception {
        Field f = MemberDashboardController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
}