package application.logic;

import application.database.Database;

public class PhysicalBook extends Book {
    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_GOOD = "GOOD";
    public static final String STATUS_OLD = "OLD";
    private int copiesAvailable;
    private int shelfNumber;
    private double weight;
    private String status;

    /**
     * Constructor for PhysicalBook.
     * @param bookId The ID of the book in this system.
     * @param title The title of the book.
     * @param authors The author(s) of the book.
     * @param publisher The publisher of the book.
     * @param publicationYear The publication year of the book.
     * @param genres The genre(s) this book belongs to.
     * @param description The description for the book.
     * @param coverImage The cover image of the book.
     * @param isbn The ISBN for the book.
     * @param status The current status of the book (New/Good/Old).
     * @param weight The weight of the book.
     * @param shelfNumber The shelf number that contains this book in the library.
     * @param copiesAvailable The amount of this book copies available in the library.
     */
    public PhysicalBook(long bookId, String title, String[] authors, String publisher,
                        int publicationYear, String[] genres, String description,
                        byte[] coverImage, String isbn, String status,
                        double weight, int shelfNumber, int copiesAvailable) {
        super(bookId, title, authors, publisher, publicationYear, genres, description, coverImage, isbn);

        if (copiesAvailable < 0) {
            throw new IllegalArgumentException("Number of book copies cannot be a negative number.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("The weight of the book must be a positive value.");
        }
        if (!status.equals(STATUS_NEW)
                && !status.equals(STATUS_GOOD)
                && !status.equals(STATUS_OLD)) {
            throw new IllegalArgumentException("Invalid book status.");
        }

        this.status = status;
        this.weight = weight;
        this.shelfNumber = shelfNumber;
        this.copiesAvailable = copiesAvailable;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /** Check if there are any copies available in the Library. */
    public boolean isAvailable() {
        return (copiesAvailable > 0);
    }

    /** Update copiesAvailable after a user borrow this book. */
    public void borrow() {
        if (isAvailable()) {
            copiesAvailable--;
            Database.editBookById(this);
        } else {
            throw new RuntimeException("This book is not available for borrowing at the moment.");
        }
    }

    /** Update copiesAvailable after returning the book. */
    public void returnBook() {
        copiesAvailable++;
        Database.editBookById(this);
        System.out.println("Returned the book.");
    }
}
