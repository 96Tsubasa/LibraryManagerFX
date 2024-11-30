package application.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rating {
    private long rateId;
    private long userId;
    private long bookId;
    private int star;
    private LocalDateTime rateDate;
    private String comment;

    /**
     * Create a new rating.
     * @param rateId Unique ID for this rating.
     * @param userId The user involved in this rating.
     * @param bookId The book involved in this rating.
     * @param rateDate The date and time this user rated the book.
     * @param star The point.
     */
    public Rating(long rateId, long userId, long bookId, int star, LocalDateTime rateDate, String comment) {
        this.rateId = rateId;
        this.userId = userId;
        this.bookId = bookId;
        this.star = star;
        this.comment = comment;
    }

    public long getRateId() {
        return rateId;
    }

    public void setRateId(long rateId) {
        this.rateId = rateId;
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

    public LocalDateTime getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDateTime rateDate) {
        this.rateDate = rateDate;
    }
}
