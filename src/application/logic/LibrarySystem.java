package application.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.database.Database;

public class LibrarySystem {
    private static LibrarySystem instance;
    private List<Book> books;
    private List<User> users;
    private List<Transaction> transactions;
    private User currentUser;
    private int countAdmin;
    private int countUser;

    /** Constructor. */
    public LibrarySystem() {
        // Load data from database
        books = Database.loadBooks();
        users = Database.loadUsers();
        setCount();
        transactions = Database.loadTransactions();
    }

    /** Set countAdmin and countUser. */
    public void setCount() {
        countUser = 0;
        countAdmin = 0;
        for (User user : users) {
            if (user.getRole().equals(User.ADMIN)) {
                countAdmin ++;
            } else if (user.getRole().equals(User.NORMAL_USER)) {
                countUser ++;
            }
        }
    }

    /** Get the static instance of this class. */
    public static LibrarySystem getInstance() {
        if (instance == null) {
            instance = new LibrarySystem();
        }
        return instance;
    }

    /** Create a new user, add to users List and database. Return the user object if successful. */
    public User addUser(String name, String email, String password, String role, byte[] imageUser) {
        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        if (isUserRegistered(name)) {
            throw new IllegalArgumentException("Username is already registered.");
        }

        long userId = Database.createNewUserId();
        // Create a new user instance and add to the users list
        User user = new User(name, userId, email, password, role, imageUser);
        users.add(user);
        if (user.getRole().equals(User.ADMIN)) {
            countAdmin ++;
        } else {
            countUser ++;
        }
        Database.addUser(user);
        return user;
    }

    /** Check username and password with database, return null if no username or false password. */
    public User handleLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.checkPassword(password)) {
                    currentUser = user;
                    return user;
                }
                throw new IllegalArgumentException("Incorrect password.");
            }
        }
        throw new IllegalArgumentException("Username does not exist.");
    }

    /** Create a new book, add to books list and database. */
    public void addBook(String title, String[] authors, String publisher, int publicationYear, String[] genres, int copiesAvailable, String description, byte[] coverImage, String isbn) {
        if (!currentUser.getRole().equals(User.ADMIN)) {
            throw new IllegalArgumentException("Only admins can add books.");
        }
        long bookId = Database.createNewBookId();
        Book book = new Book(bookId, title, authors, publisher, publicationYear, genres, copiesAvailable, description, coverImage, isbn);
        books.add(book);
        Database.addBook(book);
    }

    /** Remove a book. */
    public void removeBook(Book book) {
        if (!currentUser.getRole().equals(User.ADMIN)) {
            throw new IllegalArgumentException("Only admins can remove books.");
        }

    }

    /** Search book by ISBN. */
    public Book searchBookByISBN(String isbn) {
        for(Book book1 : books) {
            if(book1.getIsbn().equals(isbn)) {
                return book1;
            }
        }
        return null;
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

    /** Getter for count admin. */
    public int getCountAdmin() {
        return countAdmin;
    }

    /** Getter for count user. */
    public int getCountUser() {
        return countUser;
    }

    /** User borrow a book, return true if successful. */
    public boolean isBorrowBook(long userId, long bookId) {
        for(Transaction transaction1 : transactions) {
            if(transaction1.getUserId() == userId && transaction1.getBookId() == bookId
                    && !transaction1.isReturned()) {
                return false;
            }
        }
        return true;
    }

    /** User borrow a book. */
    public void borrowBook(long userId, long bookId) {
        Book book = getBookById(bookId);
        User user = getUserById(userId);
        if (!isBorrowBook(userId, bookId) && !book.borrow()) {
            throw new IllegalArgumentException("The user has already borrowed this book.");
        }
        if (book == null || !book.isAvailable()) {
            throw new IllegalArgumentException("The book is not available.");
        }
        user.borrowBook();
        book.borrow();
        long transactionId = Database.createNewTransactionId();
        Transaction transaction1 = new Transaction(transactionId, userId, bookId, LocalDate.now(),
                LocalDate.now().plusMonths(6), null, false);
        transactions.add(transaction1);
        Database.addTransaction(transaction1);
    }

    /** User return a book. */
    public void returnBook(long userId, long bookId) {
        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == userId && transaction.getBookId() == bookId
                    && !transaction.isReturned()) {
                Book book = getBookById(bookId);
                User user = getUserById(userId);
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

    /** Get users list. */
    public List<User> getUsers() {
        return users;
    }

    /** Get books list. */
    public List<Book> getBooks() {
        return books;
    }

    /** Get the recently added books in the library. */
    public List<Book> getRecentBooks() {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>(); // return
        }
        int startIndex = Math.max(books.size() - 4, 0);
        List<Book> recent = books.subList(startIndex, books.size());
        return recent;
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

    /** Edit a user by userId. */
    public void editUserById(User user, String newUsername, String newEmail, String newPassword,
                             String newRole, byte[] newImageUser) {
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setRole(newRole);
        user.setImageUser(newImageUser);
        Database.editUserById(user);
    }

    /** Delete a user in the system with userId. */
    public void deleteUserById(long userId) {
        users.removeIf(user -> user.getUserId() == userId);
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
        books.removeIf(book -> book.getBookId() == bookId);
    }

    /** Log out the current user. */
    public void logOut() {
        currentUser = null;
    }

    /** Checks if the email is already registered. */
    public boolean isEmailRegistered(String email) {
        for (User user : users) {
            // If an email match is found, throw an exception indicating the email is already registered
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /** Checks if the username is already registered. */
    public boolean isUserRegistered(String username) {
        for (User user : users) {
            // If a username match is found, throw an exception indicating the email is already registered
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
