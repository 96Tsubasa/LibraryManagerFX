package application;

import java.util.List;

public class LibrarySystem {
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;

    /** Constructor. */
    public LibrarySystem() {
        // Load data from database
        books = Database.loadBooks();
        users = Database.loadUsers();
        transactions = Database.loadTransactions();
    }

    /** Create a new user, add to users List and database (Need to add/change parameters). */
    public void addUser(String name, long userId, String email, String password, String role) {
        // Code here
        User user = new User(name, userId, email, password, role);
        user.setUserId(userId);
        user.setUserName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        users.add(user);
    }

    /** Check username and password with database, return null if no username or false password. */
    public User handleLogin(String username, String password) {
        // Code here
        for (User user : users) {
            if (user.getUserName().equals(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;    // Placeholder
    }

    /** Create a new book, add to books list and database (Need to add parameters). */
    public void addBook(String title, String[] authors, String publisher, int publicationYear, String[] genres, int copiesAvailable, String description) {
        // Code here
        Book book = new Book(title, authors, publisher, publicationYear, genres, copiesAvailable, description);
        books.add(book);
    }

    /** Remove a book. */
    public void removeBook(Book book) {
        // Code here
        books.remove(book);
    }

    /** Search book by ISBN. */
    public Book searchBookByISBN(String isbn) {
        // Code here
        for(Book book1 : books) {
            if(book1.getIsbn().equals(isbn)) {
                return book1;
            }
        }
        return null;    // Placeholder
    }

    /** Search books by keywords. */
    public List<Book> searchBooks(String keywords) {
        // Code here
        for(Book book1 : books) {
            if(book1.getIsbn().equals(keywords) || book1.getIsbn().equals(keywords) || )
        }
        return null;    // Placeholder
    }

    /** User borrow a book, return true if successful. */
    public boolean borrowBook(User user, Book book) {
        // Code here
        for(Transaction transaction1 : transactions) {
            if(transaction1.getUser().equals(user) && transaction1.getBook().equals(book) && !transaction1.isReturned()) {
                return false;
            }
        }
        return true;    // Placeholder
    }

    /** User return a book. */
    public void returnBook(User user, Book book) {
        // Code here
        for(Transaction transaction1 : transactions) {
            if(transaction1.getUser().equals(user) && transaction1.getBook().equals(book) && !transaction1.isReturned()) {
                transaction1.setReturned(true);
            }
        }
    }
}
