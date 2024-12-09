package test.logic;

import application.database.Database;
import application.logic.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionSystemTest {
    private static TransactionSystem transactionSystem;
    private static List<Long> transactionIdAdded;

    @BeforeClass
    public static void setUpBeforeClass() {
        transactionSystem = new TransactionSystem();
        transactionIdAdded = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        for (Long transactionId : transactionIdAdded) {
            try {
                Database.deleteTransactionById(transactionId);
            } catch (Exception e) {
                // Ignore this exception
            }
        }
    }

    @Test
    public void testBorrowBook_Success() {
        int transactionCount = transactionSystem.getTransactions().size();

        String username = "TESTUsernameThatDoesntExist19827489";
        long userId = 94000001;
        String email = "TESTEmailThatDoesntExist85949772@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {1, 2, 3, 4, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);
        int limitBook = user.getLimitBook();

        long bookId = 98000001;
        String title = "TEST_BookTitle992287";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 2, 3, 4, 1};
        String isbn = "9780000000001";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        int days = 7;
        transactionSystem.borrowBook(user, book, days);

        Transaction transaction = transactionSystem.getTransactions().getLast();
        transactionIdAdded.add(transaction.getTransactionId());

        assertEquals(transactionCount + 1, transactionSystem.getTransactions().size());

        assertEquals(userId, transaction.getUserId());
        assertEquals(bookId, transaction.getBookId());
        assertEquals(LocalDate.now(), transaction.getBorrowDate());
        assertEquals(LocalDate.now().plusDays(days), transaction.getDueDate());
        assertNull(transaction.getReturnDate());
        assertFalse(transaction.isReturned());

        assertEquals(limitBook - 1, user.getLimitBook());
        assertEquals(copiesAvailable - 1, book.getCopiesAvailable());
    }

    @Test
    public void testBorrowBook_Fail_NullUser() {
        long bookId = 98000002;
        String title = "TEST_BookTitle949027";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 2, 3, 4, 1};
        String isbn = "9780000000002";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(null, book, 5);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());

            fail("testBorrowBook_Fail_NullUser() failed: Expected to throw an exception.");
        } catch (Exception e) {
            assertEquals("User or Book cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testBorrowBook_Fail_NullBook() {
        String username = "TESTUsernameThatDoesntExist19092289";
        long userId = 94000002;
        String email = "TESTEmailThatDoesntExist52667772@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {1, 2, 6, 4, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        try {
            transactionSystem.borrowBook(user, null, 6);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());

            fail("testBorrowBook_Fail_NullBook() failed: Expected to throw an exception.");
        } catch (Exception e) {
            assertEquals("User or Book cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testBorrowBook_Fail_BookNotAvailable() {
        String username = "TESTUsernameThatDoesntExist29091889";
        long userId = 94000003;
        String email = "TESTEmailThatDoesntExist89287372@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {1, 2, 3, 4, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        long bookId = 98000003;
        String title = "TEST_BookTitle9958727";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 2, 3, 4, 1};
        String isbn = "9780000000003";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 0;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(user, book, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());

            fail("testBorrowBook_Fail_BookNotAvailable() failed: Expected to throw an exception.");
        } catch (Exception e) {
            assertEquals("The book is not available.", e.getMessage());
        }
    }

    @Test
    public void testBorrowBook_Fail_UserAlreadyBorrowedThisBook() {
        String username = "TESTUsernameThatDoesntExist18675992";
        long userId = 94000004;
        String email = "TESTEmailThatDoesntExist89580722@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {4, 4, 3, 4, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        long bookId = 98000004;
        String title = "TEST_BookTitle9009482";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 2, 3, 4, 1};
        String isbn = "9780000000004";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(user, book, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());
        } catch (Exception e) {
            fail("transactionSystem.borrowBook() didn't work as expected. Please double check.");
        }

        try {
            transactionSystem.borrowBook(user, book, 5);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());

            fail("testBorrowBook_Fail_UserAlreadyBorrowedThisBook() failed: Expected to " +
                    "throw an exception.");
        } catch (Exception e) {
            assertEquals("The user has already borrowed this book.", e.getMessage());
        }
    }

    @Test
    public void testReturnBook_Success() {
        String username = "TESTUsernameThatDoesntExist142908792";
        long userId = 94000005;
        String email = "TESTEmailThatDoesntExist50192922@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {4, 4, 3, 12, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);
        int limitBook = user.getLimitBook();

        long bookId = 98000005;
        String title = "TEST_BookTitle3748289";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 21, 3, 4, 1};
        String isbn = "9780000000005";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(user, book, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());
        } catch (Exception e) {
            fail("transactionSystem.borrowBook() didn't work as expected. Please double check.");
        }

        transactionSystem.returnBook(user, book);

        Transaction transaction = transactionSystem.getTransactions().getLast();

        assertEquals(LocalDate.now(), transaction.getReturnDate());
        assertTrue(transaction.isReturned());

        assertEquals(limitBook, user.getLimitBook());
        assertEquals(copiesAvailable, book.getCopiesAvailable());
    }

    @Test
    public void testReturnBook_Fail_NullUser() {
        long bookId = 98000006;
        String title = "TEST_BookTitle30927739";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 21, 34, 4, 1};
        String isbn = "9780000000006";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.returnBook(null, book);

            fail("testReturnBook_Fail_NullUser() failed: Expected an exception.");
        } catch (Exception e) {
            assertEquals("User or Book cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testReturnBook_Fail_NullBook() {
        String username = "TESTUsernameThatDoesntExist185927692";
        long userId = 94000006;
        String email = "TESTEmailThatDoesntExist59805922@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {4, 4, 35, 12, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        try {
            transactionSystem.returnBook(user, null);

            fail("testReturnBook_Fail_NullBook() failed: Expected an exception.");
        } catch (Exception e) {
            assertEquals("User or Book cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testReturnBook_Fail_NoValidTransactionFound() {
        String username = "TESTUsernameThatDoesntExist149587212";
        long userId = 94000007;
        String email = "TESTEmailThatDoesntExist54267822@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {48, 4, 3, 12, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        long bookId = 98000007;
        String title = "TEST_BookTitle3092089";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 21, 3, 4, 13};
        String isbn = "9780000000007";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.returnBook(user, book);

            fail("testReturnBook_Fail_NoValidTransactionFound() failed: Expected an exception.");
        } catch (Exception e) {
            assertEquals("No valid return transaction found.", e.getMessage());
        }
    }

    @Test
    public void testEditTransactionById() {
        String username = "TESTUsernameThatDoesntExist570009212";
        long userId = 94000008;
        String email = "TESTEmailThatDoesntExist90281122@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {48, 45, 3, 12, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        long bookId = 98000008;
        String title = "TEST_BookTitle5980250";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {5, 21, 37, 4, 13};
        String isbn = "9780000000008";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(user, book, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());
        } catch (Exception e) {
            fail("transactionSystem.borrowBook() didn't work as expected. Please double check.");
        }

        Transaction transaction = transactionSystem.getTransactions().getLast();
        int transactionCount = transactionSystem.getTransactions().size();

        userId = 94000009;
        bookId = 98000009;
        LocalDate borrowDate = LocalDate.now().minusDays(7);
        LocalDate dueDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().minusDays(1);
        boolean isReturned = true;

        transactionSystem.editTransactionById(transaction, userId, bookId,
                borrowDate, dueDate, returnDate, isReturned);

        Transaction editedTransaction = transactionSystem.getTransactions().getLast();

        assertEquals(transactionCount, transactionSystem.getTransactions().size());

        assertEquals(userId, editedTransaction.getUserId());
        assertEquals(bookId, editedTransaction.getBookId());
        assertEquals(borrowDate, editedTransaction.getBorrowDate());
        assertEquals(dueDate, editedTransaction.getDueDate());
        assertEquals(returnDate, editedTransaction.getReturnDate());
        assertTrue(editedTransaction.isReturned());
    }

    @Test
    public void testGetBookListUserBorrowing() {
        BookSystem bookSystem = new BookSystem();

        String username = "TESTUsernameThatDoesntExist509182972";
        long userId = 94000010;
        String email = "TESTEmailThatDoesntExist90950022@abcxyz.com";
        String password = "12345678";
        byte[] imageUser = new byte[] {48, 45, 35, 12, 5};
        User user = new User(username, userId, email, password, User.NORMAL_USER, imageUser);

        String title = "TEST_BookTitle98247870";
        String[] authors = new String[] {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = new String[] {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {51, 21, 37, 4, 13};
        String isbn = "9780000000010";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 35;
        PhysicalBook book1 = bookSystem.addBook(title, authors, publisher, publicationYear,
                genres, description, coverImage, isbn, status, shelfNumber, copiesAvailable);
        PhysicalBook book2 = bookSystem.addBook(title + "0", authors, publisher, publicationYear,
                genres, description, coverImage, isbn + "0", status, shelfNumber, copiesAvailable);

        try {
            transactionSystem.borrowBook(user, book1, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());

            transactionSystem.borrowBook(user, book2, 7);
            transactionIdAdded.add(transactionSystem.getTransactions()
                    .getLast()
                    .getTransactionId());
        } catch (Exception e) {
            fail("transactionSystem.borrowBook() didn't work as expected. Please double check.");
        }

        List<Book> result = new ArrayList<>();
        try {
            result = transactionSystem.getBookListUserBorrowing(userId, bookSystem);
        } catch (Exception e) {
            fail("testGetBookListUserBorrowing() failed.");
        }

        bookSystem.deleteBookById(book1.getBookId());
        bookSystem.deleteBookById(book2.getBookId());

        if (!result.contains(book1) || !result.contains(book2)) {
            fail("testGetBookListUserBorrowing() failed.");
        }
    }
}
