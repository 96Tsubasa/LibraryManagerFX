package application.logic;

public class Book {
    private long bookId;
    private String isbn;
    private String title;
    private String[] authors;
    private String publisher;
    private int publicationYear;
    private String[] genres;
    private int copiesAvailable = 0;
    private String description;
    private byte[] coverImage;

    public Book() {

    }

    /**
     * Create a new Book with the given ISBN, using Google Books API to get more
     * information.
     * 
     * @param isbn: The given ISBN for the book.
     */
    public Book(String isbn) {
        this.isbn = isbn;
        // Use Google Books API to get more info
    }

    /**
     * Create a new Book with the given parameters:
     * 
     * @param bookId:          The ID of the book in this system.
     * @param title:           The title of the book.
     * @param authors:         The author(s) of the book.
     * @param publisher:       The publisher of the book.
     * @param publicationYear: The publication year of the book.
     * @param genres:          The genre(s) this book belongs to.
     * @param copiesAvailable: The amount of copies available in the Library.
     * @param description:     The description for the book.
     * @param coverImage:      The cover image of the book.
     * @param isbn:            The ISBN for the book.
     */
    public Book(long bookId, String title, String[] authors, String publisher, int publicationYear, String[] genres,
            int copiesAvailable, String description, byte[] coverImage, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genres = genres;
        this.copiesAvailable = copiesAvailable;
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

    /** Check if there are any copies available in the Library. */
    public boolean isAvailable() {
        return (copiesAvailable > 0);
    }

    /** Update copiesAvailable after a user borrow this book. */
    public void borrow() {
        if (isAvailable()) {
            copiesAvailable--;
        } else {
            throw new RuntimeException("This book is not available for borrowing at the moment.");
        }
    }

    /** Update copiesAvailable after returning the book. */
    public void returnBook() {
        copiesAvailable++;
        System.out.println("Returned the book.");
    }
}
