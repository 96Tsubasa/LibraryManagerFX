package test.rating;

import application.logic.Rating;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class RatingTest {

    @Test
    public void testRatingConstructor() {
        LocalDate ratingDate = LocalDate.now();
        Rating rating = new Rating(1L, 100L, 200L, 5, ratingDate, "Great book!");

        assertEquals(1L, rating.getRatingId());
        assertEquals(100L, rating.getUserId());
        assertEquals(200L, rating.getBookId());
        assertEquals(5, rating.getStar());
        assertEquals(ratingDate, rating.getRatingDate());
        assertEquals("Great book!", rating.getComment());
    }

    @Test
    public void testSettersGetters() {
        Rating rating = new Rating(1L, 100L, 200L, 5, LocalDate.now(), "Good");

        rating.setRatingId(2L);
        assertEquals(2L, rating.getRatingId());

        rating.setUserId(300L);
        assertEquals(300L, rating.getUserId());

        rating.setBookId(400L);
        assertEquals(400L, rating.getBookId());

        rating.setStar(4);
        assertEquals(4, rating.getStar());

        LocalDate newDate = LocalDate.now().minusDays(1);
        rating.setRatingDate(newDate);
        assertEquals(newDate, rating.getRatingDate());

        rating.setComment("Excellent!");
        assertEquals("Excellent!", rating.getComment());
    }

    @Test
    public void testRatingWithDifferentStars() {
        for (int star = 1; star <= 5; star++) {
            Rating rating = new Rating(1L, 100L, 200L, star, LocalDate.now(), "Comment");
            assertEquals(star, rating.getStar());
        }
    }

    @Test
    public void testRatingWithNullComment() {
        Rating rating = new Rating(1L, 100L, 200L, 5, LocalDate.now(), null);
        assertNull(rating.getComment());
    }

    @Test
    public void testRatingWithEmptyComment() {
        Rating rating = new Rating(1L, 100L, 200L, 3, LocalDate.now(), "");
        assertEquals("", rating.getComment());
    }
}
