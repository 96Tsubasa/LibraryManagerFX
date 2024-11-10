package application;

import java.time.LocalDate;

public class Transaction {
    private long transactionId;
    private long userId;
    private long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;

    /**
     * Create a new transaction.
     * @param transactionId Unique ID for this transaction.
     * @param userId The user involved in this transaction.
     * @param bookId The book involved in this transaction.
     * @param borrowDate The date and time this user borrowed the book.
     * @param dueDate The date and time this user is supposed to return the book.
     * @param returnDate The date and time this user has returned the book, can be null.
     * @param isReturned Has the book been returned yet.
     */
    public Transaction(long transactionId, long userId, long bookId,
                       LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, boolean isReturned) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBook(long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    /** Update the return book status. */
    public void returnBook() {
        returnDate = LocalDate.now();
        isReturned = true;
    }

    /** Check if this transaction has been overdue. */
    public boolean isOverdue() {
        if (isReturned) {
            return false;
        }

        LocalDate now = LocalDate.now();
        return now.isAfter(dueDate);
    }
}
