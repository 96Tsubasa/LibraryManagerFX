package test.logic;

import application.logic.Book;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    private long bookId;
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private int publicationYear;
    private String[] genres;
    private int copiesAvailable;
    private String description;
    private byte[] coverImage;
    private Book validBook;

    @Before
    public void setUp() {
        bookId = 900000001;
        isbn = "9000000000001";
        title = "Super Awesome Book Title";
        authors = new String[] {"Author A"};
        publisher = "Publisher Future";
        publicationYear = 2000;
        genres = new String[] {"Genre A"};
        copiesAvailable = 100;
        description = "Awesome description.";
        coverImage = new byte[] {1, 2, 3, 4};
        validBook = new Book(bookId, title, authors, publisher, publicationYear, genres,
                copiesAvailable, description, coverImage, isbn);
    }

    @Test
    public void testConstructor_ValidArguments() {
        Book book = new Book(bookId, title, authors, publisher, publicationYear, genres,
                copiesAvailable, description, coverImage, isbn);

        assertEquals(bookId, book.getBookId());
        assertEquals(title, book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals(publisher, book.getPublisher());
        assertEquals(publicationYear, book.getPublicationYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(copiesAvailable, book.getCopiesAvailable());
        assertEquals(description, book.getDescription());
        assertArrayEquals(coverImage, book.getCoverImage());
        assertEquals(isbn, book.getIsbn());
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

    @Test
    public void testIsAvailable_True() {
        assertTrue(validBook.isAvailable());
    }

    @Test
    public void testIsAvailable_False() {
        validBook.setCopiesAvailable(0);
        assertFalse(validBook.isAvailable());
    }

    @Test
    public void testBorrow_Success() {
        assertTrue(validBook.borrow());
        assertEquals(99, validBook.getCopiesAvailable());
    }

    @Test
    public void testBorrow_Failed() {
        validBook.setCopiesAvailable(0);
        assertFalse(validBook.borrow());
    }

    @Test
    public void testReturnBook() {
        validBook.setCopiesAvailable(45);
        validBook.returnBook();
        assertEquals(46, validBook.getCopiesAvailable());
    }
}
