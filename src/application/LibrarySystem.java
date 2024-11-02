package application;

import java.util.List;

public class LibrarySystem {
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;

    /** Constructor. */
    public LibrarySystem() {
        // Load data from database
    }

    /** Create a new user, add to users List and database (Need to add/change parameters). */
    public void addUser(String username, String password, String email) {
        // Code here
    }

    /** Check username and password with database, return null if no username or false password. */
    public User handleLogin(String username, String password) {
        // Code here
        return null;    // Placeholder
    }

    /** Create a new book, add to books list and database (Need to add parameters). */
    public void addBook() {
        // Code here
    }

    /** Remove a book. */
    public void removeBook(Book book) {
        // Code here
    }

    /** Search book by ISBN. */
    public Book searchBookByISBN(String isbn) {
        // Code here
        return null;    // Placeholder
    }

    /** Search books by keywords. */
    public List<Book> searchBooks(String keywords) {
        // Code here
        return null;    // Placeholder
    }

    /** User borrow a book, return true if successful. */
    public boolean borrowBook(User user, Book book) {
        // Code here
        return true;    // Placeholder
    }

    /** User return a book. */
    public void returnBook(User user, Book book) {
        // Code here
    }
}
