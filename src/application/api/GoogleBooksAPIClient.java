package application.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import application.database.Database;
import application.logic.Book;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksAPIClient {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    /**
     * Fetches book information from Google Books API by ISBN.
     * @param isbn The ISBN of the book.
     */
    public static Book getBookInfoByISBN(String isbn) throws IOException, InterruptedException {
        String url = API_URL + isbn + "&key=" + Config.GOOGLE_BOOKS_API_KEY;

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray items = jsonResponse.optJSONArray("items");

            if (items != null && !items.isEmpty()) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title", "Unknown Title");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                String[] authors = authorsArray != null
                        ? authorsArray.toList().stream().map(Object::toString).toArray(String[]::new)
                        : new String[] { "Unknown Author" };
                String publisher = volumeInfo.optString("publisher", "Unknown Publisher");
                int publicationYear = volumeInfo.optInt("publishedDate", 0);
                JSONArray genresArray = volumeInfo.optJSONArray("categories");
                String[] genres = genresArray != null
                        ? genresArray.toList().stream().map(Object::toString).toArray(String[]::new)
                        : new String[] {};
                String description = volumeInfo.optString("description", "No description available.");
                byte[] coverImage = null;

                if (volumeInfo.has("imageLinks")) {
                    String coverImageUrl = volumeInfo.getJSONObject("imageLinks").optString("thumbnail", null);
                    if (coverImageUrl != null) {
                        coverImage = fetchCoverImage(coverImageUrl);
                    }
                }

                return new Book(Database.createNewBookId(),
                        title,
                        authors,
                        publisher,
                        publicationYear,
                        genres,
                        0,
                        description,
                        coverImage,
                        isbn
                );
            }
        } else {
            throw new IOException("Failed to fetch data from Google Books API. HTTP status code: " + response.statusCode());
        }
        return null;
    }

    private static byte[] fetchCoverImage(String imageUrl) {
        try {
            HttpResponse<byte[]> response;
            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(imageUrl))
                        .GET()
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            }

            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch cover image: " + e.getMessage());
        }
        return null;
    }
}