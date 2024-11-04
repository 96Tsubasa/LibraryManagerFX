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
                + user.getJob() + "');");
    }

    /** Edit a user's info by userId. */
    public static void editUserById(User user) {
        execute("UPDATE users SET "
                + "username = " + user.getUserName() + ", "
                + "password = " + user.getPassword() + ", "
                + "email = " + user.getEmail() + ", "
                + "role = " + user.getJob() + " "
                + "WHERE userId = " + user.getUserId() + ";");
    }

    /** Get user by userId. */
    private static User getUserById(Object userId) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM users WHERE userId = '" +
                (String) userId + "';");

        return new User((String) result.getFirst().get("username"),
                (String) result.getFirst().get("userId"),
                (String) result.getFirst().get("email"),
                (String) result.getFirst().get("password"),
                null);
//                (String) result.getFirst().get("role"));
    }

    /** Edit a user's info by email. */
    public static void editUserByEmail(User user) {
        execute("UPDATE users SET "
                + "userId = " + user.getUserId() + ", "
                + "username = " + user.getUserName() + ", "
                + "password = " + user.getPassword() + ", "
                + "role = " + user.getJob() + " "
                + "WHERE email = " + user.getEmail() + ";");
    }

    /** Load all users' data from database. */
    public static List<User> loadUsers() {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM users");
        List<User> userList = new ArrayList<>();
        for (Map<String, Object> record : result) {
            User temp = new User((String) record.get("username"),
                    (String) record.get("userId"),
                    (String) record.get("email"),
                    (String) record.get("password"),
                    null);
//                    (String) record.get("role"));

            userList.add(temp);
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
                (String) result.getFirst().get("userId"),
                (String) result.getFirst().get("email"),
                (String) result.getFirst().get("password"),
                null);
//                (String) result.getFirst().get("role"));
    }

    /** Add a new author to the database. */
    private static void addAuthor(String authorName) {
        execute("INSERT INTO authors (authorName) VALUES (" + authorName + ");");
    }

    /** Get authorId by authorName. Returns -1 if not found in database. */
    private static int getAuthorIdByName(String authorName) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM authors WHERE " +
                "authorName = '" + authorName + "'");

        if (result.isEmpty()) {
            return -1;
        }

        return (int) result.getFirst().get("authorId");
    }

    /** Get authorName by authorId. Returns null if not found in database. */
    private static String getAuthorNameById(String authorId) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM authors WHERE " +
                "authorId = " + authorId);

        if (result.isEmpty()) {
            return null;
        }

        return (String) result.getFirst().get("authorName");
    }

    /** Add a new genre to the database. */
    private static void addGenre(String genreName) {
        execute("INSERT INTO genres (genreName) VALUES (" + genreName + ");");
    }

    /** Get genreId by genreName. Returns -1 if not found in database. */
    private static int getGenreIdByName(String genreName) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM genres WHERE " +
                "genreName = '" + genreName + "'");

        if (result.isEmpty()) {
            return -1;
        }

        return (int) result.getFirst().get("genreId");
    }

    /** Get genreName by genreId. Returns null if not found in database. */
    private static String getGenreNameById(String genreId) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM genres WHERE " +
                "genreId = " + genreId);

        if (result.isEmpty()) {
            return null;
        }

        return (String) result.getFirst().get("genreName");
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
    private static Book getBookById(Object bookId) {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM books WHERE bookId = '" +
                (Long) bookId + "';");

        String title = (String) result.getFirst().get("title");
        String[] authorsId = ((String) result.getFirst().get("authorsId")).split(";");
        String[] authors = new String[authorsId.length];
        for (int i = 0; i < authorsId.length; i++) {
            authors[i] = getAuthorNameById(authorsId[i]);
        }
        String publisher = (String) result.getFirst().get("publisher");
        int publicationYear = (int) result.getFirst().get("publicationYear");
        String[] genresId = ((String) result.getFirst().get("genresId")).split(";");
        String[] genres = new String[authorsId.length];
        for (int i = 0; i < authorsId.length; i++) {
            genres[i] = getAuthorNameById(genresId[i]);
        }
        int copiesAvailable = (int) result.getFirst().get("copiesAvailable");
        String description = (String) result.getFirst().get("description");

        return new Book(title, authors, publisher, publicationYear,
                genres, copiesAvailable, description);
    }

    /** Load all books from the database. */
    public static List<Book> loadBooks() {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM books");
        List<Book> bookList = new ArrayList<>();
        for (Map<String, Object> record : result) {
            String title = (String) record.get("title");
            String[] authorsId = ((String) record.get("authorsId")).split(";");
            String[] authors = new String[authorsId.length];
            for (int i = 0; i < authorsId.length; i++) {
                authors[i] = getAuthorNameById(authorsId[i]);
            }
            String publisher = (String) record.get("publisher");
            int publicationYear = (int) record.get("publicationYear");
            String[] genresId = ((String) record.get("genresId")).split(";");
            String[] genres = new String[authorsId.length];
            for (int i = 0; i < authorsId.length; i++) {
                genres[i] = getAuthorNameById(genresId[i]);
            }
            int copiesAvailable = (int) record.get("copiesAvailable");
            String description = (String) record.get("description");

            bookList.add(new Book(title, authors, publisher, publicationYear,
                    genres, copiesAvailable, description));
        }
        return bookList;
    }

    /** Load all transactions' data from database. */
    public static List<Transaction> loadTransactions() {
        List<Map<String, Object>> result = executeQuery("SELECT * FROM transactions");
        List<Transaction> transactionList = new ArrayList<>();
        for (Map<String, Object> record : result) {
            long transactionId = (Long) record.get("transactionId");
            User user = getUserById(record.get("userId"));
            Book book = getBookById(record.get("bookId"));
            LocalDate borrowDate = LocalDate.parse((String) record.get("borrowDate"));
            LocalDate dueDate = LocalDate.parse((String) record.get("dueDate"));
            LocalDate returnDate = LocalDate.parse((String) record.get("returnDate"));
            boolean isReturned = Boolean.parseBoolean((String) record.get("isReturned"));
            transactionList.add(new Transaction(transactionId, user, book,
                    borrowDate, dueDate, returnDate, isReturned));
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
//        LocalDate bday = LocalDate.of(2005, 7, 21);
//        User user1 = new User("user1", bday, "male", "Dich Vong", "1", "mail1@gmail.com", "admin");
//        User user2 = new User("user2", bday, "female", "Ton That Thuyet", "2", "mail2@gmail.com", "user");
//        user1.setPassword("12345678");
//        user2.setPassword("qwertyui");
//        addUser(user1);
//        addUser(user2);

        User currentUser = handleLogin("user2", "qwertyui");
        if (currentUser == null) {
            System.out.println("Login failed");
            return;
        }
        System.out.println("Username: " + currentUser.getUserName());
        System.out.println("Email: " + currentUser.getEmail());
    }
}
