package test.logic;

import application.logic.DigitalBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DigitalBookTest {
    private long bookId;
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private int publicationYear;
    private String[] genres;
    private String description;
    private byte[] coverImage;
    private String bookUrl;
    private DigitalBook validBook;

    @Before
    public void setUp() {
        bookId = 900000001;
        isbn = "9000000000001";
        title = "Super Awesome Book Title";
        authors = new String[] {"Author A"};
        publisher = "Publisher Future";
        publicationYear = 2000;
        genres = new String[] {"Genre A"};
        description = "Awesome description.";
        coverImage = new byte[] {1, 2, 3, 4};
        bookUrl = "library.com/book/1";
        validBook = new DigitalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, bookUrl);
    }

    @Test
    public void testConstructor_ValidArguments() {
        DigitalBook book = new DigitalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, bookUrl);

        assertEquals(bookId, book.getBookId());
        assertEquals(title, book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals(publisher, book.getPublisher());
        assertEquals(publicationYear, book.getPublicationYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(description, book.getDescription());
        assertArrayEquals(coverImage, book.getCoverImage());
        assertEquals(isbn, book.getIsbn());
        assertEquals(bookUrl, book.getBookUrl());
    }

    @Test
    public void testGetAuthorsAsString_OneAuthor() {
        assertEquals(authors[0], validBook.getAuthorsAsString());
    }

    @Test
    public void testGetAuthorsAsString_ManyAuthors() {
        validBook.setAuthors(new String[] {"Author A", "Author B", "Author C"});
        assertEquals("Author A, Author B, Author C", validBook.getAuthorsAsString());
    }

    @Test
    public void testGetGenresAsString_OneGenre() {
        assertEquals(genres[0], validBook.getGenresAsString());
    }

    @Test
    public void testGetGenresAsString_ManyGenres() {
        validBook.setGenres(new String[] {"Genre A", "Genre B", "Genre C"});
        assertEquals("Genre A, Genre B, Genre C", validBook.getGenresAsString());
    }
}
