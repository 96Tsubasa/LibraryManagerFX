package test.transaction;

import application.logic.Transaction;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class TransactionTest {

    @Test
    public void testTransactionConstructor() {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = LocalDate.now().plusMonths(1);
        LocalDate returnDate = null;

        Transaction transaction = new Transaction(1L, 100L, 200L, borrowDate, dueDate, returnDate, false);

        assertEquals(1L, transaction.getTransactionId());
        assertEquals(100L, transaction.getUserId());
        assertEquals(200L, transaction.getBookId());
        assertEquals(borrowDate, transaction.getBorrowDate());
        assertEquals(dueDate, transaction.getDueDate());
        assertNull(transaction.getReturnDate());
        assertFalse(transaction.isReturned());
    }

    @Test
    public void testSettersGetters() {
        Transaction transaction = new Transaction(1L, 100L, 200L, LocalDate.now(), LocalDate.now().plusMonths(1), null,
                false);

        transaction.setTransactionId(2L);
        assertEquals(2L, transaction.getTransactionId());

        transaction.setUserId(300L);
        assertEquals(300L, transaction.getUserId());

        transaction.setBook(400L);
        assertEquals(400L, transaction.getBookId());

        LocalDate newBorrowDate = LocalDate.now();
        transaction.setBorrowDate(newBorrowDate);
        assertEquals(newBorrowDate, transaction.getBorrowDate());

        LocalDate newDueDate = LocalDate.now().plusMonths(2);
        transaction.setDueDate(newDueDate);
        assertEquals(newDueDate, transaction.getDueDate());

        LocalDate returnDate = LocalDate.now();
        transaction.setReturnDate(returnDate);
        assertEquals(returnDate, transaction.getReturnDate());

        transaction.setReturned(true);
        assertTrue(transaction.isReturned());
    }

    @Test
    public void testReturnBook() {
        Transaction transaction = new Transaction(1L, 100L, 200L, LocalDate.now(), LocalDate.now().plusMonths(1), null,
                false);
        assertFalse(transaction.isReturned());
        assertNull(transaction.getReturnDate());

        transaction.returnBook();

        assertTrue(transaction.isReturned());
        assertNotNull(transaction.getReturnDate());
        assertEquals(LocalDate.now(), transaction.getReturnDate());
    }

    @Test
    public void testIsOverdueNotReturned() {
        LocalDate borrowDate = LocalDate.now().minusMonths(2);
        LocalDate dueDate = LocalDate.now().minusDays(1); // Due yesterday

        Transaction transaction = new Transaction(1L, 100L, 200L, borrowDate, dueDate, null, false);
        assertTrue(transaction.isOverdue());
    }

    @Test
    public void testIsNotOverdueNotReturned() {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = LocalDate.now().plusMonths(1);

        Transaction transaction = new Transaction(1L, 100L, 200L, borrowDate, dueDate, null, false);
        assertFalse(transaction.isOverdue());
    }

    @Test
    public void testIsNotOverdueReturned() {
        LocalDate borrowDate = LocalDate.now().minusMonths(2);
        LocalDate dueDate = LocalDate.now().minusDays(1);
        LocalDate returnDate = LocalDate.now().minusDays(1);

        Transaction transaction = new Transaction(1L, 100L, 200L, borrowDate, dueDate, returnDate, true);
        assertFalse(transaction.isOverdue());
    }
}
