package application;

public class Book {
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private int publicationYear;
    private String[] genres;
    private int copiesAvailable = 0;
    private String description;

    public Book() {

    }

    /**
     * Create a new Book with the given ISBN, using Google Books API to get more information.
     * @param isbn: The given ISBN for the book.
     */
    public Book(String isbn) {
        this.isbn = isbn;
        // Use Google Books API to get more info
    }

    /**
     * Create a new Book with the given parameters:
     * @param title: The title of the book.
     * @param authors: The author(s) of the book.
     * @param publisher: The publisher of the book.
     * @param publicationYear: The publication year of the book.
     * @param genres: The genre(s) this book belongs to.
     * @param copiesAvailable: The amount of copies available in the Library.
     * @param description: The description for the book.
     */
    public Book(String title, String[] authors, String publisher, int publicationYear, String[] genres, int copiesAvailable, String description) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genres = genres;
        this.copiesAvailable = copiesAvailable;
        this.description = description;
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

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
