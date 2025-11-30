package application.logic;

import application.repository.BookRepository;
import application.repository.DatabaseBookRepository;
import application.repository.TransactionRepository;
import application.repository.DatabaseTransactionRepository;
import application.repository.UserRepository;
import application.repository.DatabaseUserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSystem {
    private final TransactionRepository transactionRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final List<Transaction> transactions;

    /** Default constructor (production) uses Database-backed repositories. */
    public TransactionSystem() {
        this(new DatabaseTransactionRepository(), new DatabaseBookRepository(), new DatabaseUserRepository());
    }

    /** Constructor for DI/testing. */
    public TransactionSystem(TransactionRepository transactionRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.transactions = transactionRepo.loadTransactions();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** User borrow a book, return true if successful. */
    private boolean hasUserBorrowedBook(long userId, long bookId) {
        return transactions.stream().anyMatch(transaction -> transaction.getUserId() == userId
                && transaction.getBookId() == bookId && !transaction.isReturned());
    }

    /** User borrow a book. */
    public Transaction borrowBook(User user, Book book, int days) {
        if (user == null || book == null) {
            throw new IllegalArgumentException("User or Book cannot be null.");
        }

        if (hasUserBorrowedBook(user.getUserId(), book.getBookId())) {
            throw new IllegalArgumentException("Already borrowed");
        }

        user.borrowBook();
        book.borrow();

        long transactionId = transactionRepo.createNewTransactionId();
        Transaction transaction = new Transaction(transactionId, user.getUserId(), book.getBookId(), LocalDate.now(),
                LocalDate.now().plusDays(days), null, false);

        transactions.add(transaction);
        transactionRepo.addTransaction(transaction);

        // Persist domain changes (user and book)
        userRepo.editUserById(user);
        bookRepo.editBookById(book);

        return transaction;
    }

    /** Check if there is a pending transaction for a specific user and book. */
    private boolean hasPendingTransaction(long userId, long bookId) {
        return transactions.stream().anyMatch(transaction -> transaction.getUserId() == userId
                && transaction.getBookId() == bookId && !transaction.isReturned());
    }

    /** User return a book. */
    public void returnBook(User user, Book book) {
        if (user == null || book == null) {
            throw new IllegalArgumentException("User or Book cannot be null.");
        }

        if (!hasPendingTransaction(user.getUserId(), book.getBookId())) {
            throw new IllegalArgumentException("No valid return transaction found.");
        }

        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == user.getUserId() && transaction.getBookId() == book.getBookId()
                    && !transaction.isReturned()) {
                transaction.setReturned(true);
                transaction.setReturnDate(LocalDate.now());
                book.returnBook();
                user.returnBook();
                // Persist domain changes (transaction, book, user)
                transactionRepo.editTransactionById(transaction);
                bookRepo.editBookById(book);
                userRepo.editUserById(user);
                return;
            }
        }
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
    public void editTransactionById(Transaction transaction, long userId, long bookId, LocalDate borrowDate,
            LocalDate dueDate, LocalDate returnDate, boolean isReturned) {
        transaction.setUserId(userId);
        transaction.setBook(bookId);
        transaction.setBorrowDate(borrowDate);
        transaction.setDueDate(dueDate);
        transaction.setReturnDate(returnDate);
        transaction.setReturned(isReturned);
        transactionRepo.editTransactionById(transaction);
    }

    /** Return list book user borrow. */
    public List<Book> getBookListUserBorrowing(long userId, BookSystem bookSystem) {
        List<Book> borrowing = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == userId && !transaction.isReturned()) {
                Book book = bookSystem.getBookById(transaction.getBookId());
                borrowing.add((book));
            }
        }
        return borrowing;
    }

    /** List transaction if isReturned is true. */
    public List<Transaction> isReturnedIsTrue() {
        List<Transaction> listTrue = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isReturned()) {
                listTrue.add(transaction);
            }
        }
        return listTrue;
    }

    /** List transaction if isReturned is false. */
    public List<Transaction> isReturnedIsFalse() {
        List<Transaction> listFalse = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (!transaction.isReturned()) {
                listFalse.add(transaction);
            }
        }
        return listFalse;
    }
}
