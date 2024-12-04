package application.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rating {
    private long ratingId;
    private long userId;
    private long bookId;
    private int star;
    private LocalDate ratingDate;
    private String comment;

    /**
     * Create a new rating.
     * @param ratingId Unique ID for this rating.
     * @param userId The user involved in this rating.
     * @param bookId The book involved in this rating.
     * @param ratingDate The date and time this user rated the book.
     * @param star The point.
     */
    public Rating(long ratingId, long userId, long bookId, int star, LocalDate ratingDate, String comment) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.bookId = bookId;
        this.star = star;
        this.comment = comment;
    }

    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(LocalDate ratingDate) {
        this.ratingDate = ratingDate;
    }
}
