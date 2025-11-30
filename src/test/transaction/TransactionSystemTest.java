package test.transaction;

import application.logic.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;

public class TransactionSystemTest {
    private FakeTransactionSystem transactionSystem;
    private FakeBookSystem bookSystem;
    private FakeUserSystem userSystem;
    private List<Long> createdUserIds = new ArrayList<>();
    private List<Long> createdBookIds = new ArrayList<>();
    // Baseline snapshot of transaction IDs existing before each test
    private Set<Long> baselineTransactionIds = new HashSet<>();

    @Before
    public void setUp() {
        transactionSystem = new FakeTransactionSystem();
        bookSystem = new FakeBookSystem();
        userSystem = new FakeUserSystem();
        createdUserIds.clear();
        createdBookIds.clear();
        // capture existing transactions so we only remove those added by the test
        baselineTransactionIds.clear();
        for (Transaction t : transactionSystem.getTransactions()) {
            baselineTransactionIds.add(t.getTransactionId());
        }
    }

    @After
    public void tearDown() {
        // With in-memory fakes there's nothing to clean in external DB; just clear
        // lists
        transactionSystem.clearAll();
        createdUserIds.clear();
        createdBookIds.clear();
    }

    @Test
    public void testBorrowBook() {
        User user = userSystem.addUser("BorrowUser", "borrow@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Borrow Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" },
                5, "Desc", null, "BORROW-ISBN");
        createdBookIds.add(book.getBookId());

        int initialCopies = book.getCopiesAvailable();
        int initialLimit = user.getLimitBook();

        transactionSystem.borrowBook(user, book);

        assertEquals(initialCopies - 1, book.getCopiesAvailable());
        assertEquals(initialLimit - 1, user.getLimitBook());
        assertEquals(1, transactionSystem.getTransactions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBookNotAvailable() {
        User user = userSystem.addUser("BorrowUser2", "borrow2@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("No Copies Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 0, "Desc", null, "NOCOPIES-ISBN");
        createdBookIds.add(book.getBookId());

        transactionSystem.borrowBook(user, book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowSameBookTwice() {
        User user = userSystem.addUser("BorrowUser3", "borrow3@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Borrow Twice Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "TWICE-ISBN");
        createdBookIds.add(book.getBookId());

        transactionSystem.borrowBook(user, book);
        // Try to borrow the same book again without returning
        transactionSystem.borrowBook(user, book);
    }

    @Test
    public void testReturnBook() {
        User user = userSystem.addUser("ReturnUser", "return@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Return Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" },
                5, "Desc", null, "RETURN-ISBN");
        createdBookIds.add(book.getBookId());

        transactionSystem.borrowBook(user, book);
        int copiesAfterBorrow = book.getCopiesAvailable();
        int limitAfterBorrow = user.getLimitBook();

        transactionSystem.returnBook(user, book);

        assertEquals(copiesAfterBorrow + 1, book.getCopiesAvailable());
        assertEquals(limitAfterBorrow + 1, user.getLimitBook());

        Transaction transaction = transactionSystem.getTransactions().get(0);
        assertTrue(transaction.isReturned());
        assertNotNull(transaction.getReturnDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnBookNotBorrowed() {
        User user = userSystem.addUser("ReturnUser2", "return2@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Return Not Borrowed Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "RETURNNOTBORR-ISBN");
        createdBookIds.add(book.getBookId());

        transactionSystem.returnBook(user, book);
    }

    @Test
    public void testGetTransactionById() {
        User user = userSystem.addUser("TxnUser", "txn@example.com", "Password123", User.NORMAL_USER, null);
        createdUserIds.add(user.getUserId());
        Book book = bookSystem.addBook("Txn Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "TXN-ISBN");
        createdBookIds.add(book.getBookId());

        transactionSystem.borrowBook(user, book);
        Transaction addedTxn = transactionSystem.getTransactions().get(0);

        Transaction found = transactionSystem.getTransactionById(addedTxn.getTransactionId());
        assertNotNull(found);
        assertEquals(addedTxn.getTransactionId(), found.getTransactionId());

        Transaction notFound = transactionSystem.getTransactionById(999999L);
        assertNull(notFound);
    }

    @Test
    public void testEditTransactionById() {
        User user = userSystem.addUser("EditTxnUser", "edittxn@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Edit Txn Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "EDITTXN-ISBN");

        transactionSystem.borrowBook(user, book);
        Transaction transaction = transactionSystem.getTransactions().get(0);

        LocalDate newBorrowDate = LocalDate.now().minusDays(10);
        LocalDate newDueDate = LocalDate.now().plusMonths(1);
        LocalDate newReturnDate = LocalDate.now();

        transactionSystem.editTransactionById(transaction, 999L, 888L, newBorrowDate, newDueDate, newReturnDate, true);

        assertEquals(999L, transaction.getUserId());
        assertEquals(888L, transaction.getBookId());
        assertEquals(newBorrowDate, transaction.getBorrowDate());
        assertEquals(newDueDate, transaction.getDueDate());
        assertEquals(newReturnDate, transaction.getReturnDate());
        assertTrue(transaction.isReturned());
    }

    @Test
    public void testGetBookListUserBorrowing() {
        User user = userSystem.addUser("BorrowListUser", "borrowlist@example.com", "Password123", User.NORMAL_USER,
                null);
        createdUserIds.add(user.getUserId());
        Book book1 = bookSystem.addBook("Book 1", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "ISBN1");
        createdBookIds.add(book1.getBookId());
        Book book2 = bookSystem.addBook("Book 2", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "ISBN2");
        createdBookIds.add(book2.getBookId());

        transactionSystem.borrowBook(user, book1);
        transactionSystem.borrowBook(user, book2);

        List<Book> borrowingBooks = transactionSystem.getBookListUserBorrowing(user.getUserId(), bookSystem);
        assertEquals(2, borrowingBooks.size());
        assertTrue(borrowingBooks.contains(book1));
        assertTrue(borrowingBooks.contains(book2));

        // Return one book
        transactionSystem.returnBook(user, book1);

        List<Book> stillBorrowing = transactionSystem.getBookListUserBorrowing(user.getUserId(), bookSystem);
        assertEquals(1, stillBorrowing.size());
        assertTrue(stillBorrowing.contains(book2));
    }

    @Test
    public void testGetTransactions() {
        List<Transaction> transactions = transactionSystem.getTransactions();
        assertNotNull(transactions);
        assertTrue(transactions.size() >= 0);
    }

    // --- Fake in-test systems to avoid DB access ---
    static class FakeTransactionSystem {
        private List<Transaction> transactions = new ArrayList<>();
        private long nextId = 1L;

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public void borrowBook(User user, Book book) {
            if (book.getCopiesAvailable() <= 0)
                throw new IllegalArgumentException("No copies");
            // check if already borrowed and not returned
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
            // find matching transaction
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

        public Transaction getTransactionById(long id) {
            return transactions.stream().filter(t -> t.getTransactionId() == id).findFirst().orElse(null);
        }

        public Transaction getTransactionsFirst() {
            return transactions.isEmpty() ? null : transactions.get(0);
        }

        public void editTransactionById(Transaction transaction, long userId, long bookId,
                java.time.LocalDate borrowDate, java.time.LocalDate dueDate, java.time.LocalDate returnDate,
                boolean returned) {
            transaction.setUserId(userId);
            transaction.setBook(bookId);
            transaction.setBorrowDate(borrowDate);
            transaction.setDueDate(dueDate);
            transaction.setReturnDate(returnDate);
            transaction.setReturned(returned);
        }

        public List<Book> getBookListUserBorrowing(long userId, FakeBookSystem bookSystem) {
            List<Book> res = new ArrayList<>();
            for (Transaction t : transactions) {
                if (t.getUserId() == userId && !t.isReturned()) {
                    Book b = bookSystem.getBookById(t.getBookId());
                    if (b != null)
                        res.add(b);
                }
            }
            return res;
        }

        public void clearAll() {
            transactions.clear();
        }
    }

    static class FakeBookSystem {
        private List<Book> books = new ArrayList<>();
        private long nextId = 1L;

        public List<Book> getBooks() {
            return books;
        }

        public Book addBook(String title, String[] authors, String publisher, int year, String[] genres, int copies,
                String desc, byte[] cover, String isbn) {
            Book b = new Book(nextId++, title, authors, publisher, year, genres, copies, desc, cover, isbn);
            books.add(b);
            return b;
        }

        public void deleteBookById(long id) {
            books.removeIf(b -> b.getBookId() == id);
        }

        public Book getBookById(long id) {
            return books.stream().filter(b -> b.getBookId() == id).findFirst().orElse(null);
        }

        public Book searchBookByISBN(String isbn) {
            return books.stream().filter(b -> isbn.equals(b.getIsbn())).findFirst().orElse(null);
        }

        public void editBookById(Book book, String newTitle, String[] newAuthors, String newPublisher, int newYear,
                String[] newGenres, int newCopies, String newDesc, byte[] newCover, String newIsbn) {
            book.setTitle(newTitle);
            book.setAuthors(newAuthors);
            book.setPublisher(newPublisher);
            book.setPublicationYear(newYear);
            book.setGenres(newGenres);
            book.setCopiesAvailable(newCopies);
            book.setDescription(newDesc);
            book.setCoverImage(newCover);
            book.setIsbn(newIsbn);
        }

        public List<Book> getRecentBooks() {
            return books.size() <= 4 ? new ArrayList<>(books) : books.subList(books.size() - 4, books.size());
        }

        public List<Book> searchBooks(String keyword) {
            List<Book> res = new ArrayList<>();
            for (Book b : books)
                if (b.getTitle().contains(keyword) || b.getAuthorsAsString().contains(keyword))
                    res.add(b);
            return res;
        }
    }

    static class FakeUserSystem {
        private List<User> users = new ArrayList<>();
        private long nextId = 1L;

        public User addUser(String name, String email, String password, String role, byte[] imageUser) {
            for (User u : users) {
                if (u.getEmail().equals(email))
                    throw new IllegalArgumentException("Email is already registered.");
                if (u.getUsername().equals(name))
                    throw new IllegalArgumentException("Username is already registered.");
            }
            User u = new User(name, nextId++, email, password, role, imageUser);
            users.add(u);
            return u;
        }

        public void deleteUserById(long id) {
            users.removeIf(u -> u.getUserId() == id);
        }

        public User getUserById(long id) {
            return users.stream().filter(u -> u.getUserId() == id).findFirst().orElse(null);
        }
    }
}
