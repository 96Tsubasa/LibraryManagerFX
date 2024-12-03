package test.logic;

import application.logic.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TransactionTest {
    private long transactionId;
    private long userId;
    private long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private Transaction transaction;

    @Before
    public void setUp() {
        transactionId = 900000001;
        userId = 900000002;
        bookId = 900000003;
        borrowDate = LocalDate.of(2024, 11, 1);
        dueDate = LocalDate.of(2024, 11, 8);
        returnDate = LocalDate.of(2024, 11, 7);
        isReturned = true;
        transaction = new Transaction(transactionId, userId, bookId,
                borrowDate, dueDate, returnDate, isReturned);
    }

    @Test
    public void testConstructor() {
        Transaction newTransaction = new Transaction(transactionId, userId, bookId,
                borrowDate, dueDate, returnDate, isReturned);

        assertEquals(transactionId, newTransaction.getTransactionId());
        assertEquals(userId, newTransaction.getUserId());
        assertEquals(bookId, newTransaction.getBookId());
        assertEquals(borrowDate, newTransaction.getBorrowDate());
        assertEquals(dueDate, newTransaction.getDueDate());
        assertEquals(returnDate, newTransaction.getReturnDate());
        assertEquals(isReturned, newTransaction.isReturned());
    }

    @Test
    public void testReturnBook() {
        transaction.setDueDate(LocalDate.now().plusDays(2));
        transaction.setReturnDate(null);
        transaction.setReturned(false);

        transaction.returnBook();

        assertEquals(LocalDate.now(), transaction.getReturnDate());
        assertTrue(transaction.isReturned());
    }

    @Test
    public void testOverDue_True() {
        transaction.setDueDate(LocalDate.now().minusDays(3));
        transaction.setReturnDate(null);
        transaction.setReturned(false);

        assertTrue(transaction.isOverdue());
    }

    @Test
    public void testOverDue_False() {
        transaction.setDueDate(LocalDate.now().plusDays(5));
        transaction.setReturnDate(null);
        transaction.setReturned(false);

        assertFalse(transaction.isOverdue());
    }
}
