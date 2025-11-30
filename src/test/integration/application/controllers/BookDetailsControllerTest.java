package test.integration.application.controllers;

import application.controllers.BookDetailsController;
import application.controllers.MemberDashboardController;
import application.logic.Book;
import application.logic.LibrarySystem;
import application.logic.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class BookDetailsControllerTest {
    private LibrarySystem librarySystem;
    private User normalUser;
    private Book book;

    @Before
    public void setUp() throws Exception {
        librarySystem = LibrarySystem.getInstance();
        // login as admin to add a book
        User admin = librarySystem.getUsers().get(0);
        librarySystem.handleLogin(admin.getUsername(), admin.getPassword());

        book = librarySystem.addBook("TEST_bookTitle", new String[] { "Author A" }, "TEST_publisher", 2000,
                new String[] { "Genre A" }, 1, "TEST_description", new byte[] { 1, 2, 3 }, "9000000000001");

        normalUser = librarySystem.addUser("TEST_borrower", "borrower@example.com", "password1", User.NORMAL_USER,
                new byte[] { 1 });
    }

    @After
    public void tearDown() {
        try {
            librarySystem.deleteBookById(book.getBookId());
        } catch (Exception ignored) {
        }
        try {
            librarySystem.deleteUserById(normalUser.getUserId());
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testBorrowBookInvokesLibraryAndReloadsInventory() throws Exception {
        BookDetailsController controller = new BookDetailsController() {
            @Override
            protected void showAlert(javafx.scene.control.Alert.AlertType alertType, String title, String message) {
                // noop for test
            }
        };

        // set book field
        Field bookField = BookDetailsController.class.getDeclaredField("book");
        bookField.setAccessible(true);
        bookField.set(controller, book);

        // set memberDashboardController with a dummy that records calls
        final boolean[] called = { false };
        MemberDashboardController dummy = new MemberDashboardController() {
            @Override
            public void loadInventory() {
                called[0] = true;
            }
        };
        Field mdcField = BookDetailsController.class.getDeclaredField("memberDashboardController");
        mdcField.setAccessible(true);
        mdcField.set(controller, dummy);

        // set current user to the normal user
        application.controllers.LoginController.currentUser = normalUser;

        // call borrowBook
        controller.borrowBook();

        // verify that librarySystem reports the book in user's borrowed list
        boolean found = librarySystem.getBookListUserBorrowing(normalUser.getUserId()).stream()
                .anyMatch(b -> b.getBookId() == book.getBookId());

        assertTrue("Borrowed book should appear in user's borrowing list", found);
        assertTrue("MemberDashboardController.loadInventory should be called", called[0]);
    }
}
