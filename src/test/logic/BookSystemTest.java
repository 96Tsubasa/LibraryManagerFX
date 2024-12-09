package test.logic;

import application.database.Database;
import application.logic.Book;
import application.logic.BookSystem;
import application.logic.DigitalBook;
import application.logic.PhysicalBook;
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
    public void testAddBook1_Success() {
        String title = "TEST_BookTitle12345";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "9780000000001";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        int bookCount = bookSystem.getBooks().size();

        PhysicalBook book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, status, shelfNumber, copiesAvailable);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("testAddBook1_Success() failed: " + e.getMessage());
        }

        assertNotNull(book);
        assertEquals(bookCount + 1, bookSystem.getBooks().size());

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
    public void testAddBook2_Success() {
        String title = "TEST_BookTitle765432";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 4, 4, 4, 5};
        String isbn = "9874827854001";
        String bookUrl = "library.net/book/765432";

        int bookCount = bookSystem.getBooks().size();

        DigitalBook book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, bookUrl);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("testAddBook2_Success() failed: " + e.getMessage());
        }

        assertNotNull(book);
        assertEquals(bookCount + 1, bookSystem.getBooks().size());

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
    public void testSearchBookByISBN_Success() {
        String title = "TEST_BookTitle42512";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123456789123";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        try {
            Book book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                description, coverImage, isbn, status, shelfNumber, copiesAvailable);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        Book bookFound = bookSystem.searchBookByISBN(isbn);

        assertNotNull(bookFound);

        if (bookFound instanceof PhysicalBook phyBook) {
            assertEquals(title, phyBook.getTitle());
            assertArrayEquals(authors, phyBook.getAuthors());
            assertEquals(publisher, phyBook.getPublisher());
            assertEquals(publicationYear, phyBook.getPublicationYear());
            assertArrayEquals(genres, phyBook.getGenres());
            assertEquals(description, phyBook.getDescription());
            assertArrayEquals(coverImage, phyBook.getCoverImage());
            assertEquals(isbn, phyBook.getIsbn());
            assertEquals(status, phyBook.getStatus());
            assertEquals(shelfNumber, phyBook.getShelfNumber());
            assertEquals(copiesAvailable, phyBook.getCopiesAvailable());
        } else {
            fail("testSearchBookByISBN_Success() failed: Expected to find a physical book.");
        }
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
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123888789123";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, status, shelfNumber, copiesAvailable);
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
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "123456987123";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, status, shelfNumber, copiesAvailable);
            bookIdAdded.add(book.getBookId());
        } catch (Exception e) {
            System.out.println("bookSystem.addBook() got an exception: " + e.getMessage());
            System.out.println("Please double check this method.");
        }

        assertNotNull(book);
        assertEquals(book, bookSystem.getBookById(book.getBookId()));
    }

    @Test
    public void testGetBookById_Fail() {
        Book book = bookSystem.getBookById(-123);

        assertNull(book);
    }

    @Test
    public void testEditBookById1() {
        String title = "TEST_BookTitle894278";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "121116922223";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        PhysicalBook book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, status, shelfNumber, copiesAvailable);
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
        description = "TEST_Description123123";
        coverImage = new byte[] {1, 3, 12, 4, 5};
        isbn = "121194832223";
        status = PhysicalBook.STATUS_NEW;
        shelfNumber = 2;
        copiesAvailable = 50;

        long bookId = book.getBookId();

        bookSystem.editBookById(book, title, authors, publisher, publicationYear, genres,
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
    public void testEditBookById2() {
        String title = "TEST_BookTitle809499";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "121189534523";
        String bookUrl = "library.net/book/809499";

        DigitalBook book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, bookUrl);
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
        description = "TEST_Description123123";
        coverImage = new byte[] {1, 3, 12, 4, 5};
        isbn = "121194832223";
        bookUrl = "library.net/book/128947998";

        long bookId = book.getBookId();

        bookSystem.editBookById(book, title, authors, publisher, publicationYear, genres,
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
    public void testDeleteBookById() {
        String title = "TEST_BookTitle427857";
        String[] authors = {"Author A", "Author B"};
        String publisher = "TEST_Publisher";
        int publicationYear = 2000;
        String[] genres = {"Genre A", "Genre B", "Genre C"};
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "121194722229";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        Book book = null;
        try {
            book = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, status, shelfNumber, copiesAvailable);
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
        String description = "TEST_Description";
        byte[] coverImage = {1, 2, 3, 4, 5};
        String isbn = "451948286229";
        String status = PhysicalBook.STATUS_GOOD;
        int shelfNumber = 1;
        int copiesAvailable = 25;

        Book book1 = null;
        try {
            book1 = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                   description, coverImage, isbn, status, shelfNumber, copiesAvailable);
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
        description = "TEST_Description999";
        coverImage = new byte[] {1, 2, 2, 2, 2};
        isbn = "211948387469";
        String bookUrl = "library.net/book/7999923";

        Book book2 = null;
        try {
            book2 = bookSystem.addBook(title, authors, publisher, publicationYear, genres,
                    description, coverImage, isbn, bookUrl);
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
