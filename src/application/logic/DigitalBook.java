package application.logic;

public class DigitalBook extends Book {
    private String bookUrl;

    public DigitalBook(long bookId, String title, String[] authors, String publisher,
                       int publicationYear, String[] genres, String description,
                       byte[] coverImage, String isbn, String bookUrl) {
        super(bookId, title, authors, publisher, publicationYear, genres, description, coverImage, isbn);
        this.bookUrl = bookUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    /** Get info of the book. */
    public String getInfo() {
        return String.format("%s\n" +
                        "Book URL: %s\n" +
                        "Description: %s",
                super.getInfo(), bookUrl, super.getDescription());
    }
}
