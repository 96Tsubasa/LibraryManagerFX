package application.logic;

import application.database.Database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RatingSystem {
    private List<Rating> ratings;

    /** Constructor for RatingSystem. */
    public RatingSystem() {
        ratings = Database.loadRatings();
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
    public List<Rating> getRatingForUserId(long userId) {
        List<Rating> returnRateForUser = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getUserId() == userId) {
                returnRateForUser.add(rating);
            }
        }
        returnRateForUser.sort((r1, r2) -> r2.getRatingDate().compareTo(r1.getRatingDate()));
        return returnRateForUser;
    }

    /** Return Rating for Book. */
    public List<Rating> getRatingForBookId(long bookId) {
        List<Rating> returnRateForBook = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getBookId() == bookId) {
                returnRateForBook.add(rating);
            }
        }
        returnRateForBook.sort((r1, r2) -> r2.getRatingDate().compareTo(r1.getRatingDate()));
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
    public Rating getRatingByRatingId(long ratingId) {
        for (Rating rating : ratings) {
            if (rating.getRatingId() == ratingId) {
                return rating;
            }
        }
        return null;
    }

    /** User changes rating. */
    public void editRatingByUserId(Rating rating, int star, String comment) {
        rating.setStar(star);
        rating.setRatingDate(LocalDate.now());
        rating.setComment(comment);
        Database.editRatingById(rating);
    }

    /** Add rating. */
   public void addRating(long userId, long bookId, int star, LocalDate ratingDate, String comment) {
       long ratingId = Database.createNewRatingId();
       Rating newRating = new Rating(ratingId, userId, bookId, star, ratingDate, comment);
       ratings.add(newRating);
       Database.addRating(newRating);
   }

   /** Delete rating. */
   public void deleteRatingById(long ratingId) {
       ratings.removeIf(rating -> rating.getRatingId() == ratingId);
       Database.deleteRatingById(ratingId);
   }
}
