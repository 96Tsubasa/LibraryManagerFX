package application;

import java.time.LocalDate;

public class Transaction {
    private long transactionId;
    private User user;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned = false;

    /**
     * Create a new transaction.
     * @param transactionId Unique ID for this transaction.
     * @param user The user involved in this transaction.
     * @param book The book involved in this transaction.
     * @param borrowDate The date and time this user borrowed the book.
     * @param dueDate The date and time this user is supposed to return the book.
     */
    public Transaction(long transactionId, User user, Book book, LocalDate borrowDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
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
