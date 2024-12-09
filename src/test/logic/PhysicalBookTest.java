package test.logic;

import application.logic.PhysicalBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhysicalBookTest {
    private long bookId;
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private int publicationYear;
    private String[] genres;
    private String description;
    private byte[] coverImage;
    private String status;
    private int shelfNumber;
    private int copiesAvailable;
    private PhysicalBook validBook;

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
        status = PhysicalBook.STATUS_NEW;
        shelfNumber = 1;
        copiesAvailable = 100;
        validBook = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);
    }

    @Test
    public void testConstructor_ValidArguments() {
        PhysicalBook book = new PhysicalBook(bookId, title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);

        assertEquals(bookId, book.getBookId());
        assertEquals(title, book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals(publisher, book.getPublisher());
        assertEquals(publicationYear, book.getPublicationYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(description, book.getDescription());
        assertArrayEquals(coverImage, book.getCoverImage());
        assertEquals(isbn, book.getIsbn());
        assertEquals(status, book.getStatus());
        assertEquals(shelfNumber, book.getShelfNumber());
        assertEquals(copiesAvailable, book.getCopiesAvailable());
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
        validBook.borrow();
        assertEquals(99, validBook.getCopiesAvailable());
    }

    @Test
    public void testBorrow_Failed() {
        validBook.setCopiesAvailable(0);

        try {
            validBook.borrow();

            fail("testBorrow_Failed() failed: Expected an exception when borrowing a book " +
                    "that is not available.");
        } catch (Exception e) {
            assertEquals("This book is not available for borrowing at the moment.",
                    e.getMessage());
        }
    }

    @Test
    public void testReturnBook() {
        validBook.setCopiesAvailable(45);
        validBook.returnBook();
        assertEquals(46, validBook.getCopiesAvailable());
    }
}
