package application.repository;

import application.logic.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> loadBooks();

    Optional<Book> findById(long bookId);

    long createNewBookId();

    void addBook(Book book);

    void editBookById(Book book);

    void deleteBookById(long bookId);

    List<Long> searchBookIdWithKeyword(String keyword);
}
