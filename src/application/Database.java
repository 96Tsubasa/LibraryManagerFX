package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String databaseUrl = "jdbc:sqlite:library.db";

    /** Add a new user to the database. */
    public static void addUser(User user) {
        execute("INSERT INTO users (username, password, email, role) VALUES ('"
                + user.getName() + "', '"
                + user.getPassword() + "', '"
                + user.getEmail() + "', '"
                + user.getJob() + "');");
    }

    /** Edit a user's info by userId. */
    public static void editUserById(User user) {
        execute("UPDATE users SET "
                + "username = " + user.getName() + ", "
                + "password = " + user.getPassword() + ", "
                + "email = " + user.getEmail() + ", "
                + "role = " + user.getJob() + " "
                + "WHERE userId = " + user.getUserId() + ";");
    }

    /** Edit a user's info by email. */
    public static void editUserByEmail(User user) {
        execute("UPDATE users SET "
                + "userId = " + user.getUserId() + ", "
                + "username = " + user.getName() + ", "
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
                    null,
                    null,
                    null,
                    (String) record.get("userId"),
                    (String) record.get("email"),
                    (String) record.get("role"));

            userList.add(temp);
        }
        return userList;
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
        execute("INSERT INTO books (title, authorsId, publicationYear) VALUES " +
                "('Hitorigoto', '1', 2018), " +
                "('Bake no Hana', '2', 2024);");
        List<Map<String, Object>> result = executeQuery("SELECT * FROM books");

        for (Map<String, Object> it : result) {
            System.out.println(it.get("bookId") + " " + it.get("title") + " " + it.get("publicationYear"));
        }
    }
}
