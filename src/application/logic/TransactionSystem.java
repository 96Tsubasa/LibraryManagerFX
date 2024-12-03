package application.logic;

import application.database.Database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSystem {
    private List<Transaction> transactions;

    /** Constructor for TransactionSystem. */
    public TransactionSystem() {
        transactions = Database.loadTransactions();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** User borrow a book, return true if successful. */
    private boolean isBorrowBook(long userId, long bookId) {
        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == userId && transaction.getBookId() == bookId
                    && !transaction.isReturned()) {
                return false;
            }
        }
        return true;
    }

    /** User borrow a book. */
    public void borrowBook(User user, Book book) {
        if (!isBorrowBook(user.getUserId(), book.getBookId()) && !book.borrow()) {
            throw new IllegalArgumentException("The user has already borrowed this book.");
        }
        if (book == null || !book.isAvailable()) {
            throw new IllegalArgumentException("The book is not available.");
        }
        user.borrowBook();
        book.borrow();
        long transactionId = Database.createNewTransactionId();
        Transaction transaction = new Transaction(transactionId, user.getUserId(),
                book.getBookId(), LocalDate.now(), LocalDate.now().plusMonths(6),
                null, false);
        transactions.add(transaction);
        Database.addTransaction(transaction);
    }

    /** User return a book. */
    public void returnBook(User user, Book book) {
        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == user.getUserId()
                    && transaction.getBookId() == book.getBookId()
                    && !transaction.isReturned()) {
                transaction.setReturned(true);
                transaction.setReturnDate(LocalDate.now());
                if (book != null) {
                    book.returnBook();
                    user.returnBook();
                }
                return;
            }
        }
        throw new IllegalArgumentException("No valid return transaction found.");
    }

    /** Return a reference to a transaction in the system with bookId. */
    public Transaction getTransactionById(long transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId() == transactionId) {
                return transaction;
            }
        }
        return null;
    }

    /** Edit a transaction by transactionId. */
    public void editTransactionById(Transaction transaction, long userId, long bookId,
                                    LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, boolean isReturned) {
        transaction.setUserId(userId);
        transaction.setBook(bookId);
        transaction.setBorrowDate(borrowDate);
        transaction.setDueDate(dueDate);
        transaction.setReturnDate(returnDate);
        transaction.setReturned(isReturned);
        Database.editTransactionById(transaction);
    }

    /** Return list book user borrow. */
    public List<Book> getBookListUserBorrowing(long userId, BookSystem bookSystem) {
        List<Book> borrowing = new ArrayList<>();
        for(Transaction transaction : transactions) {
            if(transaction.getUserId() == userId && transaction.isReturned() == false) {
                Book book = bookSystem.getBookById(transaction.getBookId());
                borrowing.add((book));
            }
        }
        return borrowing;
    }
}
