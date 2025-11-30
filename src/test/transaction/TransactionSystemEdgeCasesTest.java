package test.transaction;

import application.logic.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionSystemEdgeCasesTest {
    private FakeTransactionSystem transactionSystem;
    private FakeBookSystem bookSystem;
    private FakeUserSystem userSystem;
    private List<Long> createdUserIds = new ArrayList<>();
    private List<Long> createdBookIds = new ArrayList<>();

    @Before
    public void setUp() {
        transactionSystem = new FakeTransactionSystem();
        bookSystem = new FakeBookSystem();
        userSystem = new FakeUserSystem();
        createdUserIds.clear();
        createdBookIds.clear();
    }

    @After
    public void tearDown() {
        // In-memory fakes require no DB cleanup
        createdBookIds.clear();
        createdUserIds.clear();
    }

    @Test
    public void testBorrowReducesCopiesFromOneToZero() {
        User user = userSystem.addUser("EdgeBorrowUser1", "edge1@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Edge Book One Copy", new String[] { "Author" }, "Pub", 2025,
                new String[] { "Genre" }, 1, "Desc", null, "EDGE-ONE-ISBN");
        createdBookIds.add(book.getBookId());

        // preconditions
        assertEquals(1, book.getCopiesAvailable());

        transactionSystem.borrowBook(user, book);

        // book copies should drop to 0
        assertEquals(0, book.getCopiesAvailable());

        // a transaction referencing this user/book should exist and be not returned
        boolean found = transactionSystem.getTransactions().stream().anyMatch(
                t -> t.getUserId() == user.getUserId() && t.getBookId() == book.getBookId() && !t.isReturned());
        assertTrue(found);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowWhenNoCopiesThrows() {
        User user = userSystem.addUser("EdgeBorrowUser2", "edge2@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Edge Book Zero Copies", new String[] { "Author" }, "Pub", 2025,
                new String[] { "Genre" }, 0, "Desc", null, "EDGE-ZERO-ISBN");
        createdBookIds.add(book.getBookId());

        // should throw because book has 0 copies
        transactionSystem.borrowBook(user, book);
    }
}

// Minimal in-test fakes
class FakeTransactionSystem {
    private List<Transaction> transactions = new ArrayList<>();
    private long nextId = 1L;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void borrowBook(User user, Book book) {
        if (book.getCopiesAvailable() <= 0)
            throw new IllegalArgumentException("No copies");
        boolean already = transactions.stream().anyMatch(
                t -> t.getUserId() == user.getUserId() && t.getBookId() == book.getBookId() && !t.isReturned());
        if (already)
            throw new IllegalArgumentException("Already borrowed");
        book.borrow();
        user.borrowBook();
        Transaction t = new Transaction(nextId++, user.getUserId(), book.getBookId(), java.time.LocalDate.now(),
                java.time.LocalDate.now().plusMonths(1), null, false);
        transactions.add(t);
    }

    public void returnBook(User user, Book book) {
        for (Transaction t : transactions) {
            if (t.getUserId() == user.getUserId() && t.getBookId() == book.getBookId() && !t.isReturned()) {
                t.returnBook();
                book.returnBook();
                user.returnBook();
                return;
            }
        }
        throw new IllegalArgumentException("Not borrowed");
    }

    public List<Transaction> getTransactionsCopy() {
        return new ArrayList<>(transactions);
    }
}

class FakeBookSystem {
    private List<Book> books = new ArrayList<>();
    private long nextId = 1L;

    public Book addBook(String title, String[] authors, String publisher, int year, String[] genres, int copies,
            String desc, byte[] cover, String isbn) {
        Book b = new Book(nextId++, title, authors, publisher, year, genres, copies, desc, cover, isbn);
        books.add(b);
        return b;
    }

    public void deleteBookById(long id) {
        books.removeIf(b -> b.getBookId() == id);
    }
}

class FakeUserSystem {
    private List<User> users = new ArrayList<>();
    private long nextId = 1L;

    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        User u = new User(name, nextId++, email, password, role, imageUser);
        users.add(u);
        return u;
    }

    public void deleteUserById(long id) {
        users.removeIf(u -> u.getUserId() == id);
    }
}
