package application.logic;

import java.time.LocalDate;
import java.util.List;

public class LibrarySystem {
    private static LibrarySystem instance;

    private final UserSystem userSystem;
    private final BookSystem bookSystem;
    private final TransactionSystem transactionSystem;
    private final RatingSystem ratingSystem;

    /** Constructor. */
    private LibrarySystem() {
        // Load data from database
        bookSystem = new BookSystem();
        userSystem = new UserSystem();
        transactionSystem = new TransactionSystem();
        ratingSystem = new RatingSystem();
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
        if (!userSystem.getCurrentUser().getRole().equals(User.ADMIN)) {
            throw new IllegalArgumentException("Only admins can add user.");
        }
        return userSystem.addUser(name, email, password, role, imageUser);
    }

    /** Check username and password with the system, return null if no username or false password. */
    public User handleLogin(String username, String password) {
        return userSystem.handleLogin(username, password);
    }

    /** Create a new book, add to books list and database. */
    public Book addBook(String title, String[] authors, String publisher, int publicationYear, String[] genres, int copiesAvailable, String description, byte[] coverImage, String isbn) {
        if (!userSystem.getCurrentUser().getRole().equals(User.ADMIN)) {
            throw new RuntimeException("Only admins can add books.");
        }

        return bookSystem.addBook(title, authors, publisher, publicationYear, genres, copiesAvailable, description, coverImage, isbn);
    }

    /** Search book by ISBN. */
    public Book searchBookByISBN(String isbn) {
        return bookSystem.searchBookByISBN(isbn);
    }

    /** Search books by keywords. */
    public List<Book> searchBooks(String keywords) {
        return bookSystem.searchBooks(keywords);
    }

    /** Getter for count admin. */
    public int getCountAdmin() {
        return userSystem.getCountAdmin();
    }

    /** Getter for count user. */
    public int getCountUser() {
        return userSystem.getCountUser();
    }

    /** User borrow a book. */
    public Transaction borrowBook(long userId, long bookId, int days) {
        Book book = getBookById(bookId);
        User user = getUserById(userId);
        return transactionSystem.borrowBook(user, book, days);
    }

    /** User return a book. */
    public void returnBook(long userId, long bookId) {
        Book book = getBookById(bookId);
        User user = getUserById(userId);
        transactionSystem.returnBook(user, book);
    }

    /** Get users list. */
    public List<User> getUsers() {
        return userSystem.getUsers();
    }

    /** Get books list. */
    public List<Book> getBooks() {
        return bookSystem.getBooks();
    }

    /** Get the recently added books in the library. */
    public List<Book> getRecentBooks() {
        return bookSystem.getRecentBooks();
    }

    /** Get transactions list. */
    public List<Transaction> getTransactions() {
        return transactionSystem.getTransactions();
    }

    /** Return a reference to a user in the system with userId. */
    public User getUserById(long userId) {
        return userSystem.getUserById(userId);
    }

    /** Edit a user by userId. */
    public void editUserById(User user, String newUsername, String newEmail, String newPassword,
                             String newRole, byte[] newImageUser) {
        User currentUser = userSystem.getCurrentUser();

        // If the current user is not an admin, ensure they can only edit their own account
        if (!currentUser.getRole().equals(User.ADMIN)) {
            if (!currentUser.equals(user)) {
                // If the user being edited is not the same as the current user, throw an exception
                throw new IllegalArgumentException("You can only edit your own account.");
            }
        }

        userSystem.editUserById(user, newUsername, newEmail, newPassword, newRole, newImageUser);
    }

    /** Delete a user in the system with userId. */
    public void deleteUserById(long userId) {
        userSystem.deleteUserById(userId);
    }

    /** Return a reference to a book in the system with bookId. */
    public Book getBookById(long bookId) {
        return bookSystem.getBookById(bookId);
    }

    /** Edit a book by bookId. */
    public void editBookById(Book book, String newTitle, String[] newAuthors, String newPublisher,
                             int newPublicationYear, String[] newGenres, int newCopiesAvailable,
                             String newDescription, byte[] newCoverImage, String newIsbn) {
        if (!userSystem.getCurrentUser().getRole().equals(User.ADMIN)) {
            throw new IllegalArgumentException("Only admins can edit books.");
        }

        bookSystem.editBookById(book, newTitle, newAuthors, newPublisher, newPublicationYear,
                newGenres, newCopiesAvailable, newDescription, newCoverImage, newIsbn);
    }

    /** Delete a book in the system with bookId. */
    public void deleteBookById(long bookId) {
        bookSystem.deleteBookById(bookId);
    }

    /** Edit a transaction by transactionId. */
    public void editTransactionById(Transaction transaction, long userId, long bookId,
                                    LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, boolean isReturned) {
        if (!userSystem.getCurrentUser().getRole().equals(User.ADMIN)) {
            throw new IllegalArgumentException("Only admins can edit transactions.");
        }

        transactionSystem.editTransactionById(transaction, userId, bookId, borrowDate,
                dueDate, returnDate, isReturned);
    }

    /** Return a reference to a transaction in the system with bookId. */
    public Transaction getTransactionById(long transactionId) {
        return transactionSystem.getTransactionById(transactionId);
    }

    /** Return average rating of a book. */
    public double getAvgBookRating(long bookId) {
        return ratingSystem.getAvgBookRating(bookId);
    }

    /** User changes rating. */
    public void editRatingByUserId(Rating rating, int star, String comment) {
        User currentUser = userSystem.getCurrentUser();

        // If the current user is not an admin, ensure they can only edit their own rating
        if (!currentUser.getRole().equals(User.ADMIN)) {
            if (currentUser.getUserId() != rating.getUserId()) {
                // If the user being edited is not the same as the current user, throw an exception
                throw new IllegalArgumentException("You can only edit your own rating.");
            }
        }
        ratingSystem.editRatingByUserId(rating, star, comment);
    }

    /** Add rating. */
    public void addRating(long userId, long bookId, int star, LocalDate ratingDate, String comment) {
        ratingSystem.addRating(userId, bookId, star, ratingDate, comment);
    }

    /** Delete rating.*/
    public void deleteRatingById(long ratingId) {
        User currentUser = userSystem.getCurrentUser();
        Rating rating = ratingSystem.getRatingByRatingId(ratingId);

        // If the current user is not an admin, ensure they can only edit their own rating
        if (!currentUser.getRole().equals(User.ADMIN)) {
            if (currentUser.getUserId() != rating.getUserId()) {
                // If the user being edited is not the same as the current user, throw an exception
                throw new IllegalArgumentException("You can only delete your own rating.");
            }
        }
        ratingSystem.deleteRatingById(ratingId);
    }

    /** Return ratings. */
    public List<Rating> getRatings() {
        return ratingSystem.getRatings();
    }

    /** Return Rating for User. */
    public List<Rating> getRatingForUserId(long userId) {
        return ratingSystem.getRatingForUserId(userId);
    }

    /** Return Rating for Book. */
    public List<Rating> getRatingForBookId(long bookId) {
        return ratingSystem.getRatingForBookId(bookId);
    }

    /** Return Rating for news. */
    public List<Rating> getRecentRating() {
        return ratingSystem.getRecentRating();
    }

    /** Get rating for search. */
    public Rating getRatingByRatingId(long ratingId) {
        return ratingSystem.getRatingByRatingId(ratingId);
    }

    /** Return list book user borrow. */
    public List<Book> getBookListUserBorrowing(long userId) {
        return transactionSystem.getBookListUserBorrowing(userId, bookSystem);
    }
}