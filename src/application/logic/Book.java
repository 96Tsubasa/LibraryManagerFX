package application.logic;

import application.database.Database;

import javax.xml.crypto.Data;
import java.util.Arrays;

public abstract class Book {
    protected long bookId;
    protected String isbn;
    protected String title;
    protected String[] authors;
    protected String publisher;
    protected int publicationYear;
    protected String[] genres;
    protected String description;
    protected byte[] coverImage;

    /**
     * Create a new Book with the given parameters:
     * @param bookId The ID of the book in this system.
     * @param title The title of the book.
     * @param authors The author(s) of the book.
     * @param publisher The publisher of the book.
     * @param publicationYear The publication year of the book.
     * @param genres The genre(s) this book belongs to.
     * @param description The description for the book.
     * @param coverImage The cover image of the book.
     * @param isbn The ISBN for the book.
     */
    public Book(long bookId, String title, String[] authors, String publisher, int publicationYear,
                String[] genres, String description, byte[] coverImage, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genres = genres;
        this.description = description;
        this.coverImage = coverImage;
        this.isbn = isbn;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthorsAsString() {
        return String.join(", ", authors);
    }

    public String getGenresAsString() {
        return String.join(", ", genres);
    }

    /** Get info of the book. */
    public String getInfo() {
        return String.format("ID: %d\n" +
                "Title: %s\n" +
                "Authors: %s\n" +
                "Publisher: %s\n" +
                "Publication Year: %d\n" +
                "Genres: %s\n" +
                "ISBN: %s",
                bookId, title, getAuthorsAsString(), publisher, publicationYear,
                getGenresAsString(), isbn);
    }
}
