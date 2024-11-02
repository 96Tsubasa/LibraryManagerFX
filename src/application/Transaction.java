package application;

import java.time.LocalDateTime;

public class Transaction {
    private long transactionId;
    private User user;
    private Book book;
    private LocalDateTime borrowDateTime;
    private LocalDateTime dueDateTime;
    private LocalDateTime returnDateTime;
    private boolean isReturned = false;

    /**
     * Create a new transaction.
     * @param transactionId Unique ID for this transaction.
     * @param user The user involved in this transaction.
     * @param book The book involved in this transaction.
     * @param borrowDateTime The date and time this user borrowed the book.
     * @param dueDateTime The date and time this user is supposed to return the book.
     */
    public Transaction(long transactionId, User user, Book book, LocalDateTime borrowDateTime, LocalDateTime dueDateTime) {
        this.transactionId = transactionId;
        this.user = user;
        this.book = book;
        this.borrowDateTime = borrowDateTime;
        this.dueDateTime = dueDateTime;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowDateTime() {
        return borrowDateTime;
    }

    public void setBorrowDateTime(LocalDateTime borrowDateTime) {
        this.borrowDateTime = borrowDateTime;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public LocalDateTime getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    /** Update the return book status. */
    public void returnBook() {
        returnDateTime = LocalDateTime.now();
        isReturned = true;
    }

    /** Check if this transaction has been overdue. */
    public boolean isOverdue() {
        if (isReturned) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dueDateTime);
    }
}
