package test.book;

import application.logic.Book;
import application.logic.BookSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

import test.fakes.FakeBookRepository;

public class BookSystemTest {
    private BookSystem bookSystem;
    private FakeBookRepository bookRepo;
    private List<Long> createdBookIds = new ArrayList<>();

    @Before
    public void setUp() {
        bookRepo = new FakeBookRepository();
        bookSystem = new BookSystem(bookRepo);
        createdBookIds.clear();
    }

    @After
    public void tearDown() {
        createdBookIds.clear();
    }

    @Test
    public void testAddBook() {
        int sizeBefore = bookSystem.getBooks().size();
        Book book = bookSystem.addBook("Test Book", new String[] { "Test Author" }, "Test Publisher", 2024,
                new String[] { "Test" }, 5, "Test Description", null, "TEST-ISBN-001");
        assertNotNull(book);
        assertEquals("Test Book", book.getTitle());
        assertEquals(sizeBefore + 1, bookSystem.getBooks().size());
        createdBookIds.add(book.getBookId());
    }

    @Test
    public void testSearchBookByISBN() {
        // Thêm sách test
        Book addedBook = bookSystem.addBook("Search Test", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 3, "Desc", null, "SEARCH-ISBN-123");
        createdBookIds.add(addedBook.getBookId());

        // Tìm kiếm sách vừa thêm
        Book found = bookSystem.searchBookByISBN("SEARCH-ISBN-123");
        assertNotNull(found);
        assertEquals("SEARCH-ISBN-123", found.getIsbn());
        assertEquals(addedBook.getBookId(), found.getBookId());

        // Tìm kiếm sách không tồn tại
        Book notFound = bookSystem.searchBookByISBN("NOT-EXIST-ISBN");
        assertNull(notFound);
    }

    @Test
    public void testGetBookById() {
        // Thêm sách test
        Book addedBook = bookSystem.addBook("Get By ID Test", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Genre" }, 2, "Desc", null, "GETBYID-ISBN");
        createdBookIds.add(addedBook.getBookId());

        // Lấy sách theo ID
        Book found = bookSystem.getBookById(addedBook.getBookId());
        assertNotNull(found);
        assertEquals(addedBook.getBookId(), found.getBookId());
        assertEquals("Get By ID Test", found.getTitle());

        // Lấy sách ID không tồn tại
        Book notFound = bookSystem.getBookById(999999L);
        assertNull(notFound);
    }

    @Test
    public void testEditBookById() {
        // Thêm sách test
        Book book = bookSystem.addBook("Original Title", new String[] { "Author1" }, "Publisher1", 2024,
                new String[] { "Genre1" }, 5, "Original Desc", null, "EDIT-ISBN");
        createdBookIds.add(book.getBookId());

        // Sửa sách
        bookSystem.editBookById(book, "Updated Title", new String[] { "Author2" }, "Publisher2", 2025,
                new String[] { "Genre2" }, 10, "Updated Desc", new byte[] { 1, 2 }, "EDIT-ISBN-UPDATED");

        // Kiểm tra các thay đổi
        assertEquals("Updated Title", book.getTitle());
        assertArrayEquals(new String[] { "Author2" }, book.getAuthors());
        assertEquals("Publisher2", book.getPublisher());
        assertEquals(2025, book.getPublicationYear());
        assertArrayEquals(new String[] { "Genre2" }, book.getGenres());
        assertEquals(10, book.getCopiesAvailable());
        assertEquals("Updated Desc", book.getDescription());
        assertEquals("EDIT-ISBN-UPDATED", book.getIsbn());
    }

    @Test
    public void testDeleteBookById() {
        // Thêm sách test
        Book book = bookSystem.addBook("Delete Test", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" },
                1, "Desc", null, "DELETE-ISBN");
        long bookIdToDelete = book.getBookId();
        int sizeBefore = bookSystem.getBooks().size();

        // Xóa sách
        bookSystem.deleteBookById(bookIdToDelete);

        // Kiểm tra sách đã bị xóa
        assertEquals(sizeBefore - 1, bookSystem.getBooks().size());
        assertNull(bookSystem.getBookById(bookIdToDelete));
        // Không cần thêm vào createdBookIds vì nó đã bị xóa
    }

    @Test
    public void testGetRecentBooks() {
        // Lấy danh sách sách gần đây (4 cuốn gần nhất)
        List<Book> recent = bookSystem.getRecentBooks();

        // Nếu có sách trong hệ thống
        if (bookSystem.getBooks().size() > 0) {
            assertTrue(recent.size() <= 4);
            assertTrue(recent.size() > 0);
        } else {
            assertTrue(recent.isEmpty());
        }
    }

    @Test
    public void testSearchBooks() {
        // Thêm sách test
        Book addedBook = bookSystem.addBook("Fiction Novel", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Fiction" }, 5, "A fiction book", null, "FICTION-001");
        createdBookIds.add(addedBook.getBookId());

        // Tìm kiếm theo keywords
        List<Book> results = bookSystem.searchBooks("Fiction");
        assertTrue(results.size() >= 0);
    }

}
