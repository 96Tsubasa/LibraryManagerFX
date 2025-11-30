package test.fakes;

import application.logic.Book;
import application.repository.BookRepository;

import java.util.*;

public class FakeBookRepository implements BookRepository {
    private final Map<Long, Book> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public List<Book> loadBooks() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Book> findById(long bookId) {
        return Optional.ofNullable(store.get(bookId));
    }

    @Override
    public long createNewBookId() {
        return nextId++;
    }

    @Override
    public void addBook(Book book) {
        store.put(book.getBookId(), book);
    }

    @Override
    public void editBookById(Book book) {
        store.put(book.getBookId(), book);
    }

    @Override
    public void deleteBookById(long bookId) {
        store.remove(bookId);
    }

    @Override
    public List<Long> searchBookIdWithKeyword(String keyword) {
        List<Long> res = new ArrayList<>();
        for (Book b : store.values()) {
            if (b.getTitle().contains(keyword) || b.getAuthorsAsString().contains(keyword)
                    || b.getGenresAsString().contains(keyword)) {
                res.add(b.getBookId());
            }
        }
        return res;
    }
}
