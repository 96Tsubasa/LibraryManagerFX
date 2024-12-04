package application.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RatingSystem {
    private List<Rating> ratings;

    /** Constructor for RatingSystem. */
    public RatingSystem() {
        // implement this later.
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    /** Return average rating of a book. */
    public double getAvgBookRating(long bookId) {
        int sum = 0;
        int count = 0;
        for (Rating rating : ratings) {
            if (rating.getBookId() == bookId) {
                sum += rating.getStar();
                count ++;
            }
        }
        double avg = (sum * 1.0) / count;
        double rounded = Math.round(avg * 10.0) / 10.0;
        return rounded;
    }

    /** Return Rating for User. */
    public List<Rating> getRatingforUserId(long userId) {
        List<Rating> returnRateForUser = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getUserId() == userId) {
                returnRateForUser.add(rating);
            }
        }
        returnRateForUser.sort((r1, r2) -> r2.getRateDate().compareTo(r1.getRateDate()));
        return returnRateForUser;
    }

    /** Return Rating for Book. */
    public List<Rating> getRatingforBookId(long bookId) {
        List<Rating> returnRateForBook = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getBookId() == bookId) {
                returnRateForBook.add(rating);
            }
        }
        returnRateForBook.sort((r1, r2) -> r2.getRateDate().compareTo(r1.getRateDate()));
        return returnRateForBook;
    }

    /** Return Rating for news. */
    public List<Rating> getRecentRating() {
        if (ratings == null || ratings.isEmpty()) {
            return new ArrayList<>(); // return
        }
        int startIndex = Math.max(ratings.size() - 5, 0);
        List<Rating> recent = ratings.subList(startIndex, ratings.size());
        return recent;
    }

    /** Get rating for search. */
    public Rating getRatingbyRatingId(long ratingId) {
        for (Rating rating : ratings) {
            if (rating.getRateId() == ratingId) {
                return rating;
            }
        }
        return null;
    }

    /** User changes rating. */
    public void editRatingByUserId(Rating rating, int star, String comment) {
        rating.setStar(star);
        rating.setRateDate(LocalDateTime.now());
        rating.setComment(comment);
    }

    /** Add rating. */
//    public void addRating(long userId, long bookId, int star, LocalDateTime rateDate, String comment) {
//        long ratingId = Database.createNewRatingId();
//        Rating newrating = new Rating(ratingId, userId, bookId, rateDate, comment);
//        Database.addRating(newrating);
//    }
}
