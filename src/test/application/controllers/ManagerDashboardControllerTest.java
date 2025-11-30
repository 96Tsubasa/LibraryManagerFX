package test.application.controllers;

import application.controllers.ManagerDashboardController;
import application.logic.Book;
import application.logic.User;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Unit tests for pure logic methods in ManagerDashboardController. These tests
 * avoid JavaFX by calling private helper methods via reflection.
 */
public class ManagerDashboardControllerTest {
    private ManagerDashboardController controller;

    @Before
    public void setUp() {
        controller = new ManagerDashboardController();
    }

    @Test
    public void testGetUserDetail() throws Exception {
        User user = new User("u", 100L, "e@x.com", "password1", User.NORMAL_USER, new byte[] { 1, 2 });

        Method m = ManagerDashboardController.class.getDeclaredMethod("getUserDetail", User.class);
        m.setAccessible(true);
        String result = (String) m.invoke(controller, user);

        assertTrue(result.contains("Username: " + user.getUsername()));
        assertTrue(result.contains("Email: " + user.getEmail()));
        assertTrue(result.contains("Role: " + user.getRole()));
    }

    @Test
    public void testGetBookDetail() throws Exception {
        Book book = new Book(123L, "title", new String[] { "a" }, "publisher", 2020, new String[] { "g" }, 1, "desc",
                new byte[] {}, "isbn");

        Method m = ManagerDashboardController.class.getDeclaredMethod("getBookDetail", Book.class);
        m.setAccessible(true);
        String result = (String) m.invoke(controller, book);

        assertTrue(result.contains("ID: " + book.getBookId()));
        assertTrue(result.contains("Title: " + book.getTitle()));
        assertTrue(result.contains("Author: " + book.getAuthorsAsString()));
    }
}
