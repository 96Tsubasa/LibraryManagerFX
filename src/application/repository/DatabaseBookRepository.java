package application.repository;

import application.database.Database;
import application.logic.Book;

import java.util.List;
import java.util.Optional;

public class DatabaseBookRepository implements BookRepository {
    @Override
    public List<Book> loadBooks() {
        return Database.loadBooks();
    }

    @Override
    public Optional<Book> findById(long bookId) {
        return Database.loadBooks().stream().filter(b -> b.getBookId() == bookId).findFirst();
    }

    @Override
    public long createNewBookId() {
        return Database.createNewBookId();
    }

    @Override
    public void addBook(Book book) {
        Database.addBook(book);
    }

    @Override
    public void editBookById(Book book) {
        Database.editBookById(book);
    }

    @Override
    public void deleteBookById(long bookId) {
        Database.deleteBookById(bookId);
    }

    @Override
    public List<Long> searchBookIdWithKeyword(String keyword) {
        return Database.searchBookIdWithKeyword(keyword);
    }
}
