package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String databaseUrl = "jdbc:sqlite:library.db";

    /** Add a new user to the database. */
    public static void addUser(User user) {
        execute("INSERT INTO users (userId, username, password, email, role) VALUES ('"
                + user.getUserId() + "', '"
                + user.getUserName() + "', '"
                + user.getPassword() + "', '"
                + user.getEmail() + "', '"
                + user.getRole() + "');");
    }

    /** Edit a user's info by userId. */
    public static void editUserById(User user) {
        execute("UPDATE users SET "
                + "username = " + user.getUserName() + ", "
                + "password = " + user.getPassword() + ", "
                + "email = " + user.getEmail() + ", "
                + "role = " + user.getRole() + " "
                + "WHERE userId = " + user.getUserId() + ";");
    }

    /** Get user by userId. */
    private static User getUserById(long userId) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userId = '" +
                     userId + "';")) {

            rs.next();
            return new User(rs.getString("username"),
                    rs.getLong("userId"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Edit a user's info by email. */
    public static void editUserByEmail(User user) {
        execute("UPDATE users SET "
                + "userId = " + user.getUserId() + ", "
                + "username = " + user.getUserName() + ", "
                + "password = " + user.getPassword() + ", "
                + "role = " + user.getRole() + " "
                + "WHERE email = " + user.getEmail() + ";");
    }

    /** Load all users' data from database. */
    public static List<User> loadUsers() {
        List<User> userList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                userList.add(new User(rs.getString("username"),
                        rs.getLong("userId"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    /** Handle login. Return User object with given username and password, null if no such user found.
     * This has been changed to private method and will be removed soon. */
    private static User handleLogin(String username, String password) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM users WHERE username = '" +
                username + "' AND password = '" +
                password + "';");

        if (result.isEmpty()) {
            return null;
        }

        return new User((String) result.getFirst().get("username"),
                (Long) result.getFirst().get("userId"),
                (String) result.getFirst().get("email"),
                (String) result.getFirst().get("password"),
                (String) result.getFirst().get("role"));
    }

    /** Add a new author to the database. */
    private static void addAuthor(String authorName) {
        execute("INSERT INTO authors (authorName) VALUES (" + authorName + ");");
    }

    /** Get authorId by authorName. Returns -1 if not found in database. */
    private static long getAuthorIdByName(String authorName) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM authors WHERE " +
                     "authorName = '" + authorName + "'")) {

            rs.next();
            return rs.getLong("authorId");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /** Get authorName by authorId. Returns null if not found in database. */
    private static String getAuthorNameById(String authorId) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM authors WHERE " +
                     "authorId = " + authorId)) {

            rs.next();
            return rs.getString("authorName");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Add a new genre to the database. */
    private static void addGenre(String genreName) {
        execute("INSERT INTO genres (genreName) VALUES (" + genreName + ");");
    }

    /** Get genreId by genreName. Returns -1 if not found in database. */
    private static long getGenreIdByName(String genreName) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM genres WHERE " +
                     "genreName = '" + genreName + "'")) {

            rs.next();
            return rs.getLong("genreId");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /** Get genreName by genreId. Returns null if not found in database. */
    private static String getGenreNameById(String genreId) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM genres WHERE " +
                     "genreId = " + genreId)) {

            rs.next();
            return rs.getString("genreName");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Add a new book to the database. */
    public static void addBook(Book book) {
        StringBuilder query = new StringBuilder("INSERT INTO books (isbn, authorsId, title," +
                " genresId, publisher, publicationYear, copiesAvailable, description) VALUES (");
        query.append("'").append(book.getIsbn()).append("', '");
        for (String author : book.getAuthors()) {
            query.append(getAuthorIdByName(author)).append(";");
        }
        query.append("', '").append(book.getTitle()).append("', '");
        for (String genre : book.getGenres()) {
            query.append(getAuthorIdByName(genre)).append(";");
        }
        query.append("', '").append(book.getPublisher()).append("', ");
        query.append("'").append(book.getPublicationYear()).append("', ");
        query.append("'").append(book.getCopiesAvailable()).append("', ");
        query.append("'").append(book.getDescription()).append("');");
        execute(query.toString());
    }

    /** Get book by bookId. */
//    private static Book getBookById(Object bookId) {
//        List<Map<String, Object>> result = executeQuery("SELECT * FROM books WHERE bookId = '" +
//                String.valueOf(bookId) + "';");
//
//        String title = (String) result.getFirst().get("title");
//        String[] authorsId = ((String) result.getFirst().get("authorsId")).split(";");
//        String[] authors = new String[authorsId.length];
//        for (int i = 0; i < authorsId.length; i++) {
//            authors[i] = getAuthorNameById(authorsId[i]);
//        }
//        String publisher = (String) result.getFirst().get("publisher");
//        int publicationYear = Integer.parseInt((String) result.getFirst().get("publicationYear"));
//        String[] genresId = ((String) result.getFirst().get("genresId")).split(";");
//        String[] genres = new String[authorsId.length];
//        for (int i = 0; i < authorsId.length; i++) {
//            genres[i] = getAuthorNameById(genresId[i]);
//        }
//        int copiesAvailable = Integer.parseInt((String) result.getFirst().get("copiesAvailable"));
//        String description = (String) result.getFirst().get("description");
//
//        return new Book(Long.parseLong((String) bookId), title, authors, publisher, publicationYear,
//                genres, copiesAvailable, description);
//    }

    /** Load all books from the database. */
    public static List<Book> loadBooks() {
        List<Book> bookList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                long bookId = rs.getLong("bookId");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String[] authorsId = rs.getString("authorsId").split(";");
                String[] authors = new String[authorsId.length];
                for (int i = 0; i < authorsId.length; i++) {
                    authors[i] = getAuthorNameById(authorsId[i]);
                }
                String publisher = rs.getString("publisher");
                int publicationYear = rs.getInt("publicationYear");
                String[] genresId = rs.getString("genresId").split(";");
                String[] genres = new String[genresId.length];
                for (int i = 0; i < genresId.length; i++) {
                    genres[i] = getAuthorNameById(genresId[i]);
                }
                int copiesAvailable = rs.getInt("copiesAvailable");
                String description = rs.getString("description");

                bookList.add(new Book(bookId,
                        title,
                        authors,
                        publisher,
                        publicationYear,
                        genres,
                        copiesAvailable,
                        description));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookList;
    }

    /** Add a new transaction to the database. */
    public static void addTransaction(Transaction transaction) {
        execute("INSERT INTO transactions (transactionId, userId, bookId, borrowDate, " +
                "dueDate, returnDate, isReturned) VALUES ('"
                + transaction.getTransactionId() + "', '"
                + transaction.getUserId() + "', '"
                + transaction.getBookId() + "', '"
                + transaction.getBorrowDate().toString() + "', '"
                + transaction.getDueDate().toString() + "', '"
                + transaction.getReturnDate().toString() + "', '"
                + transaction.isReturned() + "');");
    }

    /** Load all transactions' data from database. */
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactionList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transactions")) {

            while (rs.next()) {
                transactionList.add(new Transaction(rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getLong("bookId"),
                        LocalDate.parse(rs.getString("borrowDate")),
                        LocalDate.parse(rs.getString("dueDate")),
                        LocalDate.parse(rs.getString("returnDate")),
                        rs.getBoolean("isReturned")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactionList;
    }

    /** Run a query that doesn't return results on the database. */
    public static void execute(String query) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {

            stmt.execute(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Run a query that return results from the database. */
    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    /** Testing. */
    public static void main(String[] args) {
        LibrarySystem libSys = new LibrarySystem();
//        User currentUser = new User("user1", "100000", "mail1@gmail.com", "12345678", User.NORMAL_USER);
//        addUser(currentUser);

//        System.out.println(libSys.users.getFirst().getPassword());

        User currUser = libSys.handleLogin("user1", "12345678");

        if (currUser == null) {
            System.out.println("Login failed");
        } else {
            System.out.println("Username: " + currUser.getUserName() + "\nEmail: " + currUser.getEmail());
        }
    }
}
