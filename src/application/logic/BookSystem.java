package application.logic;

import application.repository.BookRepository;
import application.repository.DatabaseBookRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookSystem {
    private final BookRepository repo;
    private final List<Book> books;

    /** Default constructor (production) uses Database-backed repository. */
    public BookSystem() {
        this(new DatabaseBookRepository());
    }

    /** Constructor for DI/testing. */
    public BookSystem(BookRepository repo) {
        this.repo = repo;
        this.books = repo.loadBooks();
    }

    public List<Book> getBooks() {
        return books;
    }

    /** Create a new book, add to books list and repository. */
    public Book addBook(String title, String[] authors, String publisher, int publicationYear, String[] genres,
            int copiesAvailable, String description, byte[] coverImage, String isbn) {
        long bookId = repo.createNewBookId();
        Book book = new Book(bookId, title, authors, publisher, publicationYear, genres, copiesAvailable, description,
                coverImage, isbn);
        books.add(book);
        repo.addBook(book);
        return book;
    }

    /** Search book by ISBN. */
    public Book searchBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    /** Search books by keywords. */
    public List<Book> searchBooks(String keywords) {
        List<Long> checkId = repo.searchBookIdWithKeyword(keywords);
        List<Book> printBook = new ArrayList<>();
        for (Long check : checkId) {
            for (Book next : books) {
                if (next.getBookId() == check) {
                    printBook.add(next);
                }
            }
        }
        printBook.sort(Comparator.comparingLong(Book::getBookId));
        return printBook;
    }

    /** Return a reference to a book in the system with bookId. */
    public Book getBookById(long bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    /** Edit a book by bookId. */
    public void editBookById(Book book, String newTitle, String[] newAuthors, String newPublisher,
            int newPublicationYear, String[] newGenres, int newCopiesAvailable, String newDescription,
            byte[] newCoverImage, String newIsbn) {
        book.setTitle(newTitle);
        book.setAuthors(newAuthors);
        book.setPublisher(newPublisher);
        book.setPublicationYear(newPublicationYear);
        book.setGenres(newGenres);
        book.setCopiesAvailable(newCopiesAvailable);
        book.setDescription(newDescription);
        book.setCoverImage(newCoverImage);
        book.setIsbn(newIsbn);
        repo.editBookById(book);
    }

    /** Delete a book in the system with bookId. */
    public void deleteBookById(long bookId) {
        books.removeIf(book -> book.getBookId() == bookId);
        repo.deleteBookById(bookId);
    }

    /** Get the recently added books in the library. */
    public List<Book> getRecentBooks() {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>(); // return
        }
        int startIndex = Math.max(books.size() - 4, 0);
        return books.subList(startIndex, books.size());
    }
}
