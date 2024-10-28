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

    /* Run a query that doesn't return results on the database. */
    public static void execute(String query) {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {

            stmt.execute(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* Run a query that return results from the database. */
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

    /* Testing. */
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
