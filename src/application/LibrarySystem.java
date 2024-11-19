package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LibrarySystem {
    private static LibrarySystem instance;
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private User currentUser;

    /** Constructor. */
    private LibrarySystem() {
        // Load data from database
        books = Database.loadBooks();
        users = Database.loadUsers();
        transactions = Database.loadTransactions();
    }

    /** Get the static instance of this class. */
    public static LibrarySystem getInstance() {
        if (instance == null) {
            instance = new LibrarySystem();
        }
        return instance;
    }

    /** Create a new user, add to users List and database. */
    public void addUser(String name, long userId, String email, String password, String role, byte[] imageUser) {
        // Code here
        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        if (isUserRegistered(name)) {
            throw new IllegalArgumentException("Username is already registered.");
        }

        // Create a new user instance and add to the users list
        User user = new User(name, userId, email, password, role, imageUser);
        users.add(user);
    }

    /** Check username and password with database, return null if no username or false password. */
    public User handleLogin(String username, String password) {
        // Code here
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;    // Placeholder
    }

    /** Create a new book, add to books list and database. */
    public void addBook(long bookId, String title, String[] authors, String publisher, int publicationYear, String[] genres, int copiesAvailable, String description, byte[] coverImage, String isbn) {
        // Code here
        Book book = new Book(bookId, title, authors, publisher, publicationYear, genres, copiesAvailable, description, coverImage, isbn);
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
        List<Long> checkId = Database.searchBookIdWithKeyword(keywords);
        List<Book> printBook = new ArrayList<>();
        for (Long check : checkId) {
            for (Book next : books) {
                if (next.getBookId() == check) {
                    printBook.add(next);
                }
            }
        }
        Collections.sort(printBook, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return Long.compare(b1.getBookId(), b2.getBookId());
            }
        });
        return printBook;
    }

    /** User borrow a book, return true if successful. */
    public boolean borrowBook(long userId, long bookId) {
        for(Transaction transaction1 : transactions) {
            if(transaction1.getUserId() == userId && transaction1.getBookId() == bookId && !transaction1.isReturned()) {
                return false;
            }
        }
        return true;
    }

    /** User return a book. */
    public void returnBook(long userId, long bookId) {
        for(Transaction transaction1 : transactions) {
            if(transaction1.getUserId() == userId && transaction1.getBookId() == bookId && !transaction1.isReturned()) {
                transaction1.setReturned(true);
            }
        }
    }

    /** Get users list. */
    public List<User> getUsers() {
        return users;
    }

    /** Get books list. */
    public List<Book> getBooks() {
        return books;
    }

    /** Get transactions list. */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** Return a reference to a user in the system with userId. */
    public User getUserById(long userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    /** Delete a user in the system with userId. */
    public void deleteUserById(long userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                users.remove(user);
            }
        }
    }

    /** Return a reference to a book in the system with bookId. */
    public Book getBookById(long bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    /** Delete a book in the system with bookId. */
    public void deleteBookById(long bookId) {
        // Code here
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                books.remove(book);
            }
        }
    }

    /** Log out the current user. */
    public void logOut() {
        currentUser = null;
    }

    /** Checks if the email is already registered. */
    public boolean isEmailRegistered(String email) {
        for(User user : users) {
            // If an email match is found, throw an exception indicating the email is already registered
            if(user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    /** Checks if the username is already registered. */
    public boolean isUserRegistered(String username) {
        for(User user : users) {
            // If a username match is found, throw an exception indicating the email is already registered
            if(user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
