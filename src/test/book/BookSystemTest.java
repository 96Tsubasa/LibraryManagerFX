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

    @Test(expected = IllegalArgumentException.class)
    public void testAddBook_NullTitle_ThrowsException() {
        bookSystem.addBook(null, new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5, "Desc", null,
                "ISBN123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBook_EmptyTitle_ThrowsException() {
        bookSystem.addBook("", new String[] { "Author" }, "Pub", 2024, new String[] { "Genre" }, 5, "Desc", null,
                "ISBN123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBook_NullAuthors_ThrowsException() {
        bookSystem.addBook("Title", null, "Pub", 2024, new String[] { "Genre" }, 5, "Desc", null, "ISBN123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBook_EmptyAuthorsArray_ThrowsException() {
        bookSystem.addBook("Title", new String[] {}, "Pub", 2024, new String[] { "Genre" }, 5, "Desc", null, "ISBN123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBook_NegativeCopiesAvailable_ThrowsException() {
        bookSystem.addBook("Title", new String[] { "A" }, "Pub", 2024, new String[] { "G" }, -1, "Desc", null,
                "ISBN123");
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
    public void testSearchBookByISBN_EmptyString_ReturnsNull() {
        Book found = bookSystem.searchBookByISBN("");
        assertNull(found);
    }

    @Test
    public void testSearchBookByISBN_Null_ReturnsNull() {
        Book found = bookSystem.searchBookByISBN(null);
        assertNull(found);
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
    public void testGetRecentBooks_EmptyList_ReturnsEmpty() {
        List<Book> recent = bookSystem.getRecentBooks();
        assertTrue(recent.isEmpty());
    }

    @Test
    public void testGetRecentBooks_LessThan4Books_ReturnsAll() {
        bookSystem.addBook("Book1", new String[] { "A" }, "P", 2020, new String[] { "G" }, 1, "", null, "1");
        bookSystem.addBook("Book2", new String[] { "B" }, "P", 2021, new String[] { "G" }, 1, "", null, "2");
        createdBookIds.addAll(
                Arrays.asList(bookSystem.getBooks().get(0).getBookId(), bookSystem.getBooks().get(1).getBookId()));

        List<Book> recent = bookSystem.getRecentBooks();
        assertEquals(2, recent.size());
    }

    @Test
    public void testGetRecentBooks_MoreThan4Books_ReturnsLast4() {
        for (int i = 1; i <= 6; i++) {
            Book b = bookSystem.addBook("Book " + i, new String[] { "A" }, "P", 2020, new String[] { "G" }, 1, "", null,
                    "ISBN" + i);
            createdBookIds.add(b.getBookId());
        }

        List<Book> recent = bookSystem.getRecentBooks();
        assertEquals(4, recent.size());

        assertEquals("Book 6", recent.get(3).getTitle());
        assertEquals("Book 3", recent.get(0).getTitle());
    }

    @Test
    public void testSearchBooks() {
        // Thêm sách test
        Book addedBook = bookSystem.addBook("Fiction Novel", new String[] { "Author" }, "Pub", 2024,
                new String[] { "Fiction" }, 5, "A fiction book", null, "FICTION-001");
        createdBookIds.add(addedBook.getBookId());

        // Tìm kiếm theo keywords
        List<Book> results = bookSystem.searchBooks("Fiction");
        assertTrue(results.size() >= 1);
    }

    @Test
    public void testSearchBooks_NoMatch_ReturnsEmptyList() {
        bookSystem.addBook("Harry Potter", new String[] { "JK" }, "Bloomsbury", 1997, new String[] { "Fantasy" }, 10,
                "Wizard", null, "1234567890");
        List<Book> results = bookSystem.searchBooks("NonExistentKeyword12345");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchBooks_MultipleMatches_ReturnsSortedById() {
        Book b1 = bookSystem.addBook("Java Book", new String[] { "A" }, "Pub", 2020, new String[] { "Tech" }, 5, "Java",
                null, "JAVA001");
        Book b2 = bookSystem.addBook("Advanced Java", new String[] { "B" }, "Pub", 2021, new String[] { "Tech" }, 3,
                "More Java", null, "JAVA002");

        createdBookIds.add(b1.getBookId());
        createdBookIds.add(b2.getBookId());

        List<Book> results = bookSystem.searchBooks("Java");
        assertEquals(2, results.size());
        assertTrue(results.get(0).getBookId() < results.get(1).getBookId());
    }
}
