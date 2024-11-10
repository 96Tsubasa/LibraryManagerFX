package application;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String databaseUrl = "jdbc:sqlite:library.db";

    /** Create User object from Result set. */
    private static User createUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getLong("userId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")
        );
    }

    /** Add a new user to the database. */
    public static void addUser(User user) {
        String query = "INSERT INTO users (userId, username, password, email, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Edit a user's info by userId. */
    public static void editUserById(User user) {
        String query = "UPDATE users SET username = ?, password = ?, email = ?, role = ? " +
                "WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.setLong(5, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Get user by userId. */
    private static User getUserById(long userId) {
        String query = "SELECT * FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setLong(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Edit a user's info by email. */
    public static void editUserByEmail(User user) {
        String query = "UPDATE users SET userId = ?, username = ?, password = ?, role = ? " +
                "WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Load all users' data from database. */
    public static List<User> loadUsers() {
        List<User> userList = new ArrayList<>();

        String query = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                userList.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    /** Generate a new unique userId. */
    public static long createNewUserId() {
        String query = "SELECT MAX(userId) AS max FROM users";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("max") + 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }

    /** Handle login. Return User object with given username and password, null if no such user found.
     * This has been changed to private method and will be removed soon. */
    private static User handleLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"),
                            rs.getLong("userId"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Add a new author to the database. */
    private static void addAuthor(String authorName) {
        String query = "INSERT INTO authors (authorName) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Get authorId by authorName. Returns -1 if not found in database. */
    private static long getAuthorIdByName(String authorName) {
        String query = "SELECT * FROM authors WHERE authorName = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, authorName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("authorId");
                } else {
                    addAuthor(authorName);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return getAuthorIdByName(authorName);
    }

    /** Get authorName by authorId. Returns null if not found in database. */
    private static String getAuthorNameById(long authorId) {
        String query = "SELECT * FROM authors WHERE authorId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, authorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("authorName");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Add a new genre to the database. */
    private static void addGenre(String genreName) {
        String query = "INSERT INTO genres (genreName) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, genreName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Get genreId by genreName. Returns -1 if not found in database. */
    private static long getGenreIdByName(String genreName) {
        String query = "SELECT * FROM genres WHERE genreName = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, genreName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("genreId");
                } else {
                    addGenre(genreName);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return getGenreIdByName(genreName);
    }

    /** Get genreName by genreId. Returns null if not found in database. */
    private static String getGenreNameById(long genreId) {
        String query = "SELECT * FROM genres WHERE genreId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, genreId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("genreName");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Create Book object from Result set. */
    private static Book createBookFromResultSet(ResultSet rs) throws SQLException {
        long bookId = rs.getLong("bookId");
        String isbn = rs.getString("isbn");
        String title = rs.getString("title");
        String[] authorsId = rs.getString("authorsId").split(";");
        String[] authors = new String[authorsId.length];
        for (int i = 0; i < authorsId.length; i++) {
            authors[i] = getAuthorNameById(Long.parseLong(authorsId[i]));
        }
        String publisher = rs.getString("publisher");
        int publicationYear = rs.getInt("publicationYear");
        String[] genresId = rs.getString("genresId").split(";");
        String[] genres = new String[genresId.length];
        for (int i = 0; i < genresId.length; i++) {
            genres[i] = getGenreNameById(Long.parseLong(genresId[i]));
        }
        int copiesAvailable = rs.getInt("copiesAvailable");
        String description = rs.getString("description");

        return new Book(bookId,
                title,
                authors,
                publisher,
                publicationYear,
                genres,
                copiesAvailable,
                description);
    }

    /** Add a new book to the database. */
    public static void addBook(Book book) {
        String query = "INSERT INTO books (bookId, isbn, authorsId, title," +
                " genresId, publisher, publicationYear, copiesAvailable, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, book.getBookId());
            pstmt.setString(2, book.getIsbn());
            StringBuilder authorsId = new StringBuilder();
            for (String author : book.getAuthors()) {
                authorsId.append(getAuthorIdByName(author)).append(";");
            }
            pstmt.setString(3, authorsId.toString());
            pstmt.setString(4, book.getTitle());
            StringBuilder genresId = new StringBuilder();
            for (String genre : book.getGenres()) {
                genresId.append(getGenreIdByName(genre)).append(";");
            }
            pstmt.setString(5, genresId.toString());
            pstmt.setString(6, book.getPublisher());
            pstmt.setInt(7, book.getPublicationYear());
            pstmt.setInt(8, book.getCopiesAvailable());
            pstmt.setString(9, book.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Get book by bookId. */
    public static Book getBookById(long bookId) {
        String query = "SELECT * FROM books WHERE bookId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setLong(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createBookFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** Edit a book's info by bookId. */
    public static void editBookById(Book book) {
        String query = "UPDATE books SET isbn = ?, authorsId = ?, title = ?, genresId = ?, " +
                "publisher = ?, publicationYear = ?, copiesAvailable = ?, description = ? " +
                "WHERE bookId = ?";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getIsbn());
            StringBuilder authorsId = new StringBuilder();
            for (String author : book.getAuthors()) {
                authorsId.append(getAuthorIdByName(author)).append(";");
            }
            pstmt.setString(2, authorsId.toString());
            pstmt.setString(3, book.getTitle());
            StringBuilder genresId = new StringBuilder();
            for (String genre : book.getGenres()) {
                genresId.append(getGenreIdByName(genre)).append(";");
            }
            pstmt.setString(4, genresId.toString());
            pstmt.setString(5, book.getPublisher());
            pstmt.setInt(6, book.getPublicationYear());
            pstmt.setInt(7, book.getCopiesAvailable());
            pstmt.setString(8, book.getDescription());
            pstmt.setLong(9, book.getBookId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Generate a new unique bookId. */
    public static long createNewBookId() {
        String query = "SELECT MAX(bookId) AS max FROM books";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("max") + 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1;
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

        String query = "SELECT * FROM books";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                bookList.add(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookList;
    }

    /** Add a new transaction to the database. */
    public static void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (transactionId, userId, bookId, borrowDate, " +
                "dueDate, returnDate, isReturned) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, transaction.getTransactionId());
            pstmt.setLong(2, transaction.getUserId());
            pstmt.setLong(3, transaction.getBookId());
            pstmt.setString(4, transaction.getBorrowDate().toString());
            pstmt.setString(5, transaction.getDueDate().toString());
            if (transaction.getReturnDate() == null) {
                pstmt.setNull(6, Types.VARCHAR);
            } else {
                pstmt.setString(6, transaction.getReturnDate().toString());
            }
            pstmt.setBoolean(7, transaction.isReturned());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Generate a new unique transactionId. */
    public static long createNewTransactionId() {
        String query = "SELECT MAX(transactionId) AS max FROM transactions";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("max") + 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }

    /** Load all transactions' data from database. */
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactionList = new ArrayList<>();

        String query = "SELECT * FROM transactions";
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LocalDate returnDate = null;
                if (rs.getString("returnDate") != null) {
                    returnDate = LocalDate.parse(rs.getString("returnDate"));
                }
                transactionList.add(new Transaction(rs.getLong("transactionId"),
                        rs.getLong("userId"),
                        rs.getLong("bookId"),
                        LocalDate.parse(rs.getString("borrowDate")),
                        LocalDate.parse(rs.getString("dueDate")),
                        returnDate,
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

            conn.setAutoCommit(true);
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


        Transaction tran1 = new Transaction(createNewTransactionId(),
                libSys.users.get(1).getUserId(),
                libSys.books.get(0).getBookId(),
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 8),
                LocalDate.of(2024, 11, 7),
                true);
        addTransaction(tran1);

        Transaction tran2 = new Transaction(createNewTransactionId(),
                libSys.users.get(3).getUserId(),
                libSys.books.get(1).getBookId(),
                LocalDate.of(2024, 11, 3),
                LocalDate.now(),
                null,
                false);
        addTransaction(tran2);

        Transaction tran3 = new Transaction(createNewTransactionId(),
                libSys.users.get(4).getUserId(),
                libSys.books.get(1).getBookId(),
                LocalDate.of(2024, 11, 8),
                LocalDate.of(2024, 11, 15),
                null,
                false);
        addTransaction(tran3);

        libSys.transactions = loadTransactions();
        for (Transaction transaction : libSys.transactions) {
            System.out.println("transactionID = " + transaction.getTransactionId());
            System.out.println("userID = " + transaction.getUserId());
            System.out.println("user = " + getUserById(transaction.getUserId()).getUsername());
            System.out.println("bookID = " + transaction.getBookId());
            System.out.println("book = " + getBookById(transaction.getBookId()).getTitle());
            System.out.println("borrowDate = " + transaction.getBorrowDate().toString());
            System.out.println("dueDate = " + transaction.getDueDate().toString());
            if (transaction.getReturnDate() != null)
                System.out.println("returnDate = " + transaction.getReturnDate().toString());
            System.out.println("isReturned = " + transaction.isReturned());
            System.out.println("---------------");
        }

    }
}
