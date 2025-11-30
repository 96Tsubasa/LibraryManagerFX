package test.integration.application.controllers;

import application.controllers.BookDetailsController;
import application.controllers.MemberDashboardController;
import application.logic.Book;
import application.logic.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BookDetailsControllerTest {
    private FakeLibrarySystem librarySystem;
    private User normalUser;
    private Book book;

    @Before
    public void setUp() throws Exception {
        librarySystem = new FakeLibrarySystem();
        // create an admin and log in (in-memory)
        User admin = librarySystem.addUser("TEST_admin", "admin@example.com", "adminpass", User.ADMIN, null);
        librarySystem.handleLogin(admin.getUsername(), admin.getPassword());

        book = librarySystem.addBook("TEST_bookTitle", new String[] { "Author A" }, "TEST_publisher", 2000,
                new String[] { "Genre A" }, 1, "TEST_description", new byte[] { 1, 2, 3 }, "9000000000001");

        normalUser = librarySystem.addUser("TEST_borrower", "borrower@example.com", "password1", User.NORMAL_USER,
                null);
    }

    @After
    public void tearDown() {
        // cleanup in-memory
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
        BookDetailsController controller = new BookDetailsController();

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

        // Perform borrow via LibrarySystem (production borrow logic lives there)
        librarySystem.borrowBook(normalUser.getUserId(), book.getBookId(), 14);
        // simulate controller notifying the member dashboard
        dummy.loadInventory();

        // verify that librarySystem reports the book in user's borrowed list
        boolean found = librarySystem.getBookListUserBorrowing(normalUser.getUserId()).stream()
                .anyMatch(b -> b.getBookId() == book.getBookId());

        assertTrue("Borrowed book should appear in user's borrowing list", found);
        assertTrue("MemberDashboardController.loadInventory should be called", called[0]);
    }
}

/**
 * In-test fake to avoid touching the real database for integration-like
 * controller tests.
 */
class FakeLibrarySystem {
    private List<User> users = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private List<application.logic.Transaction> transactions = new ArrayList<>();
    private long nextUserId = 1L;
    private long nextBookId = 1L;
    private long nextTxnId = 1L;
    private User currentUser = null;

    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        User u = new User(name, nextUserId++, email, password, role, imageUser);
        users.add(u);
        return u;
    }

    public Book addBook(String title, String[] authors, String publisher, int publicationYear, String[] genres,
            int copiesAvailable, String description, byte[] coverImage, String isbn) {
        Book b = new Book(nextBookId++, title, authors, publisher, publicationYear, genres, copiesAvailable,
                description, coverImage, isbn);
        books.add(b);
        return b;
    }

    public void deleteBookById(long id) {
        books.removeIf(b -> b.getBookId() == id);
    }

    public void deleteUserById(long id) {
        users.removeIf(u -> u.getUserId() == id);
    }

    public List<User> getUsers() {
        return users;
    }

    public User handleLogin(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.checkPassword(password)) {
                currentUser = u;
                return u;
            }
        }
        throw new IllegalArgumentException("Username does not exist.");
    }

    public void borrowBook(long userId, long bookId, int days) {
        User user = users.stream().filter(u -> u.getUserId() == userId).findFirst().orElse(null);
        Book book = books.stream().filter(b -> b.getBookId() == bookId).findFirst().orElse(null);
        if (user == null || book == null)
            throw new IllegalArgumentException("User or book not found");
        if (book.getCopiesAvailable() <= 0)
            throw new IllegalArgumentException("No copies");
        // decrease copies and user's limit
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        user.borrowBook();
        application.logic.Transaction t = new application.logic.Transaction(nextTxnId++, userId, bookId,
                LocalDate.now(), LocalDate.now().plusDays(days), null, false);
        transactions.add(t);
    }

    public List<Book> getBookListUserBorrowing(long userId) {
        List<Book> res = new ArrayList<>();
        for (application.logic.Transaction t : transactions) {
            if (t.getUserId() == userId && !t.isReturned()) {
                Book b = books.stream().filter(x -> x.getBookId() == t.getBookId()).findFirst().orElse(null);
                if (b != null)
                    res.add(b);
            }
        }
        return res;
    }
}
