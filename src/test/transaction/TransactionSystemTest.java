package test.transaction;

import application.logic.Book;
import application.logic.BookSystem;
import application.logic.Transaction;
import application.logic.TransactionSystem;
import application.logic.User;
import application.logic.UserSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;

import test.fakes.FakeBookRepository;
import test.fakes.FakeTransactionRepository;
import test.fakes.FakeUserRepository;

public class TransactionSystemTest {
    private TransactionSystem transactionSystem;
    private BookSystem bookSystem;
    private UserSystem userSystem;
    private FakeTransactionRepository txnRepo;
    private FakeBookRepository bookRepo;
    private FakeUserRepository userRepo;

    @Before
    public void setUp() {
        txnRepo = new FakeTransactionRepository();
        bookRepo = new FakeBookRepository();
        userRepo = new FakeUserRepository();

        bookSystem = new BookSystem(bookRepo);
        userSystem = new UserSystem(userRepo);
        transactionSystem = new TransactionSystem(txnRepo, bookRepo, userRepo);
    }

    @After
    public void tearDown() {
        // reset by re-creating repos/systems next test
        transactionSystem = null;
        bookSystem = null;
        userSystem = null;
        txnRepo = null;
        bookRepo = null;
        userRepo = null;
    }

    @Test
    public void testBorrowBook() {
        User user = userSystem.addUser("BorrowUser", "borrow@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Borrow Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" },
                5, "Desc", null, "BORROW-ISBN");

        int initialCopies = book.getCopiesAvailable();
        int initialLimit = user.getLimitBook();

        transactionSystem.borrowBook(user, book, 14);

        assertEquals(initialCopies - 1, book.getCopiesAvailable());
        assertEquals(initialLimit - 1, user.getLimitBook());
        assertEquals(1, transactionSystem.getTransactions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBookNotAvailable() {
        User user = userSystem.addUser("BorrowUser2", "borrow2@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("No Copies Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 0, "Desc", null, "NOCOPIES-ISBN");

        transactionSystem.borrowBook(user, book, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowSameBookTwice() {
        User user = userSystem.addUser("BorrowUser3", "borrow3@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Borrow Twice Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "TWICE-ISBN");

        transactionSystem.borrowBook(user, book, 14);
        // Try to borrow the same book again without returning
        transactionSystem.borrowBook(user, book, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBookInvalidDays() {
        User user = userSystem.addUser("BorrowUser4", "borrow4@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Invalid Days Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 0, "Desc", null, "INVALIDDAYS-ISBN");

        transactionSystem.borrowBook(user, book, 65535);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBook_ExceedBorrowLimit_ThrowsException() {
        User user = userSystem.addUser("LimitUser", "limitUser@gmail.com", "Password123", User.NORMAL_USER, null);

        for (int i = 0; i < 10; i++) {
            Book book = bookSystem.addBook("Book " + i, new String[] { "A" }, "P", 2024, new String[] { "G" }, 1, "",
                    null, "ISBN" + i);
            transactionSystem.borrowBook(user, book, 14);
        }

        Book extraBook = bookSystem.addBook("Extra Book", new String[] { "A" }, "P", 2024, new String[] { "G" }, 1, "",
                null, "EXTRA");
        transactionSystem.borrowBook(user, extraBook, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBook_NullUser_ThrowsException() {
        Book book = bookSystem.addBook("Book", new String[] { "A" }, "P", 2024, new String[] { "G" }, 5, "", null,
                "ISBN1");
        transactionSystem.borrowBook(null, book, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBook_NullBook_ThrowsException() {
        User user = userSystem.addUser("userBorrowNullBook", "userBorrowNullBook@gmail.com", "Password987",
                User.NORMAL_USER, null);
        transactionSystem.borrowBook(user, null, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBook_NullUserAndBook_ThrowsException() {
        transactionSystem.borrowBook(null, null, 14);
    }

    @Test
    public void testReturnBook() {
        User user = userSystem.addUser("ReturnUser", "return@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Return Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" },
                5, "Desc", null, "RETURN-ISBN");

        transactionSystem.borrowBook(user, book, 14);
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
        Book book = bookSystem.addBook("Return Not Borrowed Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "RETURNNOTBORR-ISBN");

        transactionSystem.returnBook(user, book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnBookTwice() {
        User user = userSystem.addUser("ReturnUser3", "return3@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Return Book Twice", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "RETURNNOTBORR-ISBN");

        transactionSystem.returnBook(user, book);
        transactionSystem.returnBook(user, book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnBook_NullUser_ThrowsException() {
        Book book = bookSystem.addBook("B", new String[] { "A" }, "P", 2024, new String[] { "G" }, 5, "", null, "1");
        transactionSystem.returnBook(null, book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnBook_NullBook_ThrowsException() {
        User user = userSystem.addUser("userReturnNullBook", "userReturnNullBook@gmail.com", "Password123",
                User.NORMAL_USER, null);
        transactionSystem.returnBook(user, null);
    }

    @Test
    public void testGetTransactionById() {
        User user = userSystem.addUser("TxnUser", "txn@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Txn Book", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "TXN-ISBN");

        transactionSystem.borrowBook(user, book, 14);
        Transaction addedTxn = transactionSystem.getTransactions().get(0);

        Transaction found = transactionSystem.getTransactionById(addedTxn.getTransactionId());
        assertNotNull(found);
        assertEquals(addedTxn.getTransactionId(), found.getTransactionId());

        Transaction notFound = transactionSystem.getTransactionById(999999L);
        assertNull(notFound);
    }

    @Test
    public void testGetTransactionById_NonExistentId_ReturnsNull() {
        assertNull(transactionSystem.getTransactionById(-1L));
        assertNull(transactionSystem.getTransactionById(0L));
    }

    @Test
    public void testEditTransactionById() {
        User user = userSystem.addUser("EditTxnUser", "edittxn@example.com", "Password123", User.NORMAL_USER, null);
        Book book = bookSystem.addBook("Edit Txn Book", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 5, "Desc", null, "EDITTXN-ISBN");

        transactionSystem.borrowBook(user, book, 14);
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
        Book book1 = bookSystem.addBook("Book 1", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "ISBN1");
        Book book2 = bookSystem.addBook("Book 2", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5,
                "Desc", null, "ISBN2");

        transactionSystem.borrowBook(user, book1, 14);
        transactionSystem.borrowBook(user, book2, 14);

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
    public void testGetBookListUserBorrowing_NoBorrowing_ReturnsEmptyList() {
        User user = userSystem.addUser("EmptyUser", "empty@gmail.com", "Password123", User.NORMAL_USER, null);
        List<Book> result = transactionSystem.getBookListUserBorrowing(user.getUserId(), bookSystem);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetBookListUserBorrowing_UserNotExist_ReturnsEmpty() {
        List<Book> result = transactionSystem.getBookListUserBorrowing(999999L, bookSystem);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTransactions() {
        List<Transaction> transactions = transactionSystem.getTransactions();
        assertNotNull(transactions);
        assertTrue(transactions.size() >= 0);
    }
}
