package test.logic;

import application.database.Database;
import application.logic.Book;
import application.logic.BookSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BookSystemTest {
    private static BookSystem bookSystem;
    private static List<Long> bookIdAdded;

    @BeforeClass
    public static void setUpBeforeClass() {
        bookSystem = new BookSystem();
        bookIdAdded = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        for (Long bookId : bookIdAdded) {
            try {
                Database.deleteBookById(bookId);
            } catch (Exception e) {
                // Ignore this exception
            }
        }
    }

    @Test
    public void testAddBook_Success() {
        String title = "TEST_BookTitle12345";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "9780000000001";

        int bookCount = bookSystem.getBooks().size();

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("testAddBook_Success() got an exception: " + e.getMessage());
        }

        assertNotNull(book);
        assertEquals(bookCount + 1, bookSystem.getBooks().size());

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
    public void testSearchBookByISBN_Success() {
        String title = "TEST_BookTitle42512";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123456789123";

        try {
            Book book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        Book bookFound = bookSystem.searchBookByISBN(isbn);

        assertNotNull(bookFound);

        assertEquals(title, bookFound.getTitle());
        assertArrayEquals(authors, bookFound.getAuthors());
        assertEquals(publisher, bookFound.getPublisher());
        assertEquals(publicationYear, bookFound.getPublicationYear());
        assertArrayEquals(genres, bookFound.getGenres());
        assertEquals(copiesAvailable, bookFound.getCopiesAvailable());
        assertEquals(description, bookFound.getDescription());
        assertArrayEquals(coverImage, bookFound.getCoverImage());
        assertEquals(isbn, bookFound.getIsbn());
    }

    @Test
    public void testSearchBookByISBN_Fail() {
        Book bookFound = bookSystem.searchBookByISBN("123123123123123");

        assertNull(bookFound);
    }

    @Test
    public void testSearchBooks() {
        String title = "Pneumonoultramicroscopicsilicovolcanoconiosis is the longest word!";
        String[] authors = {"Author A"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123888789123";

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        List<Book> result = bookSystem.searchBooks("Pneumonoultramicroscopicsilicovolcanoconiosis");

        if (!result.contains(book)) {
            fail("testSearchBooks() failed.");
        }
    }

    @Test
    public void testGetBookById_Success() {
        String title = "TEST_BookTitle92849";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123456987123";

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        assertEquals(book, bookSystem.getBookById(book.getBookId()));
    }

    @Test
    public void testGetBookById_Fail() {
        Book book = bookSystem.getBookById(-123);

        assertNull(book);
    }

    @Test
    public void testEditBookById() {
        String title = "TEST_BookTitle894278";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "121116922223";

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        title = "TEST_BookTitleThatIsDifferentFromBefore";
        authors = new String[] {"Author B"};
        publisher = "TEST_Publisher123123";
        publicationYear = 2020;
        genres = new String[] {"Genre A", "Genre C"};
        copiesAvailable = 50;
        description = "TEST_Description123123";
        coverImage = new byte[] {1, 3, 12, 4, 5};
        isbn = "121194832223";

        long bookId = book.getBookId();

        bookSystem.editBookById(book, title, authors, publisher, publicationYear, genres,
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
    public void testDeleteBookById() {
        String title = "TEST_BookTitle427857";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "121194722229";

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        int bookCount = bookSystem.getBooks().size();

        long bookId = book.getBookId();
        bookSystem.deleteBookById(bookId);
        book = bookSystem.getBookById(bookId);

        assertEquals(bookCount - 1, bookSystem.getBooks().size());
        assertNull(book);
    }

    @Test
    public void testGetRecentBooks() {
        String title = "TEST_BookTitle7287773";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher2345";
        int publicationYear = 2024;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        int copiesAvailable = 25;
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "451948286229";

        Book book1 = null;
        try {
            book1 = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book1.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        title = "TEST_BookTitle7999923";
        authors = new String[] {"Author A"};
        publisher = "TEST_Publisher7532";
        publicationYear = 2022;
        genres = new String[] {"Genre A", "Genre B"};
        copiesAvailable = 15;
        description = "TEST_Description999";
        coverImage = new byte[] {1, 2, 2, 2, 2};
        isbn = "211948387469";

        Book book2 = null;
        try {
            book2 = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    copiesAvailable, description, coverImage, isbn);
            bookIdAdded.add(book2.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        List<Book> result = bookSystem.getRecentBooks();

        if (!result.contains(book1) || !result.contains(book2)) {
            fail("testGetRecentBooks() failed.");
        }
    }
}
