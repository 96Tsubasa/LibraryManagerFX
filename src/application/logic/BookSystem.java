package application.logic;

import application.database.Database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookSystem {
    private final List<Book> books;

    /** Constructor for BookSystem. */
    public BookSystem() {
        books = Database.loadBooks();
    }

    public List<Book> getBooks() {
        return books;
    }

    /** Create a new physical book, add to books list and database. */
    public Book addBook(String title, String[] authors, String publisher,
                        int publicationYear, String[] genres, String description,
                        byte[] coverImage, String isbn, String status,
                        int shelfNumber, int copiesAvailable) {
        long bookId = Database.createNewBookId();
        Book book = new PhysicalBook(bookId, title, authors, publisher,
                publicationYear, genres, description, coverImage,
                isbn, status, shelfNumber, copiesAvailable);
        books.add(book);
        Database.addBook(book);
        return book;
    }

    /** Create a new digital book, add to books list and database. */
    public Book addBook(String title, String[] authors, String publisher,
                        int publicationYear, String[] genres, String description,
                        byte[] coverImage, String isbn, String bookUrl) {
        long bookId = Database.createNewBookId();
        Book book = new DigitalBook(bookId, title, authors, publisher,
                publicationYear, genres, description, coverImage,
                isbn, bookUrl);
        books.add(book);
        Database.addBook(book);
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
        List<Long> checkId = Database.searchBookIdWithKeyword(keywords);
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

    /** Edit a physical book by bookId. */
    public void editBookById(PhysicalBook book, String newTitle, String[] newAuthors, String newPublisher,
                             int newPublicationYear, String[] newGenres, String newDescription,
                             byte[] newCoverImage, String newIsbn, String newStatus,
                             int newShelfNumber, int newCopiesAvailable) {
        book.setTitle(newTitle);
        book.setAuthors(newAuthors);
        book.setPublisher(newPublisher);
        book.setPublicationYear(newPublicationYear);
        book.setGenres(newGenres);
        book.setCopiesAvailable(newCopiesAvailable);
        book.setDescription(newDescription);
        book.setCoverImage(newCoverImage);
        book.setIsbn(newIsbn);
        book.setStatus(newStatus);
        book.setShelfNumber(newShelfNumber);
        Database.editBookById(book);
    }

    /** Edit a physical book by bookId. */
    public void editBookById(DigitalBook book, String newTitle, String[] newAuthors, String newPublisher,
                             int newPublicationYear, String[] newGenres, String newDescription,
                             byte[] newCoverImage, String newIsbn, String newBookUrl) {
        book.setTitle(newTitle);
        book.setAuthors(newAuthors);
        book.setPublisher(newPublisher);
        book.setPublicationYear(newPublicationYear);
        book.setGenres(newGenres);
        book.setDescription(newDescription);
        book.setCoverImage(newCoverImage);
        book.setIsbn(newIsbn);
        book.setBookUrl(newBookUrl);
        Database.editBookById(book);
    }

    /** Delete a book in the system with bookId. */
    public void deleteBookById(long bookId) {
        books.removeIf(book -> book.getBookId() == bookId);
        Database.deleteBookById(bookId);
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
