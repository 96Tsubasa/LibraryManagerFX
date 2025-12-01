package test.book;

import application.logic.Book;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookTest {
    @Test
    public void testDefaultConstructor() {
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    public void testFullConstructorAndGetters() {
        String[] authors = { "Author1", "Author2" };
        String[] genres = { "Genre1", "Genre2" };
        byte[] cover = { 1, 2, 3 };
        Book book = new Book(1L, "Title", authors, "Publisher", 2020, genres, 5, "Desc", cover, "ISBN123");
        assertEquals(1L, book.getBookId());
        assertEquals("Title", book.getTitle());
        assertArrayEquals(authors, book.getAuthors());
        assertEquals("Publisher", book.getPublisher());
        assertEquals(2020, book.getPublicationYear());
        assertArrayEquals(genres, book.getGenres());
        assertEquals(5, book.getCopiesAvailable());
        assertEquals("Desc", book.getDescription());
        assertArrayEquals(cover, book.getCoverImage());
        assertEquals("ISBN123", book.getIsbn());
    }

    @Test
    public void testSetters() {
        Book book = new Book();
        book.setBookId(2L);
        book.setTitle("NewTitle");
        book.setAuthors(new String[] { "A" });
        book.setPublisher("Pub");
        book.setPublicationYear(2021);
        book.setGenres(new String[] { "G" });
        book.setCopiesAvailable(3);
        book.setDescription("D");
        book.setCoverImage(new byte[] { 4, 5 });
        book.setIsbn("ISBN2");
        assertEquals(2L, book.getBookId());
        assertEquals("NewTitle", book.getTitle());
        assertArrayEquals(new String[] { "A" }, book.getAuthors());
        assertEquals("Pub", book.getPublisher());
        assertEquals(2021, book.getPublicationYear());
        assertArrayEquals(new String[] { "G" }, book.getGenres());
        assertEquals(3, book.getCopiesAvailable());
        assertEquals("D", book.getDescription());
        assertArrayEquals(new byte[] { 4, 5 }, book.getCoverImage());
        assertEquals("ISBN2", book.getIsbn());
    }

    @Test
    public void testIsAvailable() {
        Book book = new Book();
        book.setCopiesAvailable(1);
        assertTrue(book.isAvailable());
        book.setCopiesAvailable(0);
        assertFalse(book.isAvailable());
    }

    @Test
    public void testBorrowAndReturnBook() {
        Book book = new Book();
        book.setCopiesAvailable(2);

        book.borrow();
        assertEquals(1, book.getCopiesAvailable());
        book.borrow();
        assertEquals(0, book.getCopiesAvailable());
        try {
            book.borrow();
            fail("Expected RuntimeException when borrowing with no copies available");
        } catch (RuntimeException ex) {
            // expected
        }
        book.returnBook();
        assertEquals(1, book.getCopiesAvailable());
    }

    @Test
    public void testGetAuthorsAsString() {
        Book book = new Book();
        book.setAuthors(new String[] { "A1", "A2" });
        assertEquals("A1, A2", book.getAuthorsAsString());
    }

    @Test
    public void testGetGenresAsString() {
        Book book = new Book();
        book.setGenres(new String[] { "G1", "G2" });
        assertEquals("G1, G2", book.getGenresAsString());
    }

    @Test
    public void testGetAuthorsAsString_NullAuthors_ReturnsEmptyOrSafeString() {
        Book book = new Book();
        book.setAuthors(null);

        assertEquals("", book.getAuthorsAsString());
    }

    @Test
    public void testGetGenresAsString_NullGenres_ReturnsEmpty() {
        Book book = new Book();
        book.setGenres(null);
        assertEquals("", book.getGenresAsString());
    }

    @Test
    public void testGetBookInfo() {
        Book book = new Book(1L, "Title", new String[] { "A" }, "Pub", 2022, new String[] { "G" }, 1, "Desc", null,
                "ISBN");

        assertEquals(1L, book.getBookId());
        assertEquals("Title", book.getTitle());
        assertArrayEquals(new String[] { "A" }, book.getAuthors());
        assertEquals("Pub", book.getPublisher());
        assertEquals(2022, book.getPublicationYear());
        assertArrayEquals(new String[] { "G" }, book.getGenres());
        assertEquals(1, book.getCopiesAvailable());
        assertEquals("Desc", book.getDescription());
        assertEquals("ISBN", book.getIsbn());
        assertEquals("A", book.getAuthorsAsString());
        assertEquals("G", book.getGenresAsString());
    }
}
