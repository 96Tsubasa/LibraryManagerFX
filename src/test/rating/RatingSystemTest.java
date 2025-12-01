package test.rating;

import application.logic.Rating;
import application.logic.RatingSystem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;

public class RatingSystemTest {
    private RatingSystem ratingSystem;

    private void setRatings(List<Rating> newRatings) {
        try {
            java.lang.reflect.Field field = RatingSystem.class.getDeclaredField("ratings");
            field.setAccessible(true);
            field.set(ratingSystem, newRatings != null ? newRatings : new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() {
        ratingSystem = new RatingSystem();
        ratingSystem = new RatingSystem() {
            {
                // Initialize ratings as empty list for testing
                java.lang.reflect.Field ratingsField;
                try {
                    ratingsField = RatingSystem.class.getDeclaredField("ratings");
                    ratingsField.setAccessible(true);
                    ratingsField.set(this, new ArrayList<Rating>());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Test
    public void testAddRating_SuccessfullyAddsAndPersists() {
        int sizeBefore = ratingSystem.getRatings().size();
        LocalDate date = LocalDate.of(2025, 4, 1);

        Rating newRating = ratingSystem.addRating(100L, 200L, 5, date, "Great book!");

        assertEquals(sizeBefore + 1, ratingSystem.getRatings().size());
        assertEquals(100L, newRating.getUserId());
        assertEquals(200L, newRating.getBookId());
        assertEquals(5, newRating.getStar());
        assertEquals(date, newRating.getRatingDate());
        assertEquals("Great book!", newRating.getComment());
    }

    @Test
    public void testGetAvgBookRating() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(1L, 100L, 200L, 5, LocalDate.now(), "Great"));
        ratings.add(new Rating(2L, 101L, 200L, 4, LocalDate.now(), "Good"));
        ratings.add(new Rating(3L, 102L, 200L, 3, LocalDate.now(), "OK"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double avg = ratingSystem.getAvgBookRating(200L);
        assertEquals(4.0, avg, 0.1);
    }

    @Test
    public void testGetAvgBookRating_NoRatings_ReturnsZero() {
        double avg = ratingSystem.getAvgBookRating(999999L);
        assertEquals(0.0, avg, 0.1);
    }

    @Test
    public void testGetAvgBookRatingSingleRating() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(1L, 100L, 300L, 5, LocalDate.now(), "Perfect"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double avg = ratingSystem.getAvgBookRating(300L);
        assertEquals(5.0, avg, 0.1);
    }

    @Test
    public void testGetRatingForUserId() {
        List<Rating> ratings = new ArrayList<>();
        LocalDate date1 = LocalDate.now().minusDays(2);
        LocalDate date2 = LocalDate.now().minusDays(1);
        LocalDate date3 = LocalDate.now();

        ratings.add(new Rating(1L, 100L, 200L, 5, date1, "First"));
        ratings.add(new Rating(2L, 100L, 201L, 4, date2, "Second"));
        ratings.add(new Rating(3L, 100L, 202L, 3, date3, "Third"));
        ratings.add(new Rating(4L, 101L, 200L, 5, date1, "Other user"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Rating> userRatings = ratingSystem.getRatingForUserId(100L);
        assertEquals(3, userRatings.size());
        // Should be sorted by date descending (most recent first)
        assertEquals(3, userRatings.get(0).getStar());
        assertEquals(4, userRatings.get(1).getStar());
        assertEquals(5, userRatings.get(2).getStar());
    }

    @Test
    public void testGetRatingForBookId() {
        List<Rating> ratings = new ArrayList<>();
        LocalDate date1 = LocalDate.now().minusDays(2);
        LocalDate date2 = LocalDate.now().minusDays(1);
        LocalDate date3 = LocalDate.now();

        ratings.add(new Rating(1L, 100L, 200L, 5, date1, "First"));
        ratings.add(new Rating(2L, 101L, 200L, 4, date2, "Second"));
        ratings.add(new Rating(3L, 102L, 200L, 3, date3, "Third"));
        ratings.add(new Rating(4L, 103L, 201L, 5, date1, "Other book"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Rating> bookRatings = ratingSystem.getRatingForBookId(200L);
        assertEquals(3, bookRatings.size());
        // Should be sorted by date descending (most recent first)
        assertEquals(3, bookRatings.get(0).getStar());
        assertEquals(4, bookRatings.get(1).getStar());
        assertEquals(5, bookRatings.get(2).getStar());
    }

    @Test
    public void testGetRatingForUserId_NoRatings_ReturnsEmptyList() {
        setRatings(new ArrayList<>());
        List<Rating> result = ratingSystem.getRatingForUserId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRatingForBookId_NoRatingsForBook_ReturnsEmptyList() {
        Rating r = new Rating(1L, 100L, 999L, 5, LocalDate.now(), "Test");
        setRatings(List.of(r));

        List<Rating> result = ratingSystem.getRatingForBookId(888L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRecentRating() {
        List<Rating> ratings = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ratings.add(new Rating(i, 100L + i, 200L + i, i % 5 + 1, LocalDate.now().minusDays(i), "Comment " + i));
        }

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Rating> recentRatings = ratingSystem.getRecentRating();
        assertEquals(5, recentRatings.size());
    }

    @Test
    public void testGetRecentRatingLessThan5() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(1L, 100L, 200L, 5, LocalDate.now(), "Comment"));
        ratings.add(new Rating(2L, 101L, 201L, 4, LocalDate.now().minusDays(1), "Comment"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Rating> recentRatings = ratingSystem.getRecentRating();
        assertEquals(2, recentRatings.size());
    }

    @Test
    public void testGetRecentRating_EmptyList_ReturnsEmpty() {
        setRatings(new ArrayList<>());
        List<Rating> recent = ratingSystem.getRecentRating();
        assertTrue(recent.isEmpty());
    }

    @Test
    public void testGetRecentRating_NullList_ReturnsEmpty() {
        setRatings(null);
        List<Rating> recent = ratingSystem.getRecentRating();
        assertTrue(recent.isEmpty());
    }

    @Test
    public void testGetRatingbyRatingId() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(1L, 100L, 200L, 5, LocalDate.now(), "First"));
        ratings.add(new Rating(2L, 101L, 201L, 4, LocalDate.now(), "Second"));
        ratings.add(new Rating(3L, 102L, 202L, 3, LocalDate.now(), "Third"));

        try {
            java.lang.reflect.Field ratingsField = RatingSystem.class.getDeclaredField("ratings");
            ratingsField.setAccessible(true);
            ratingsField.set(ratingSystem, ratings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Rating found = ratingSystem.getRatingByRatingId(2L);
        assertNotNull(found);
        assertEquals(2L, found.getRatingId());
        assertEquals(4, found.getStar());

        Rating notFound = ratingSystem.getRatingByRatingId(999L);
        assertNull(notFound);
    }

    @Test
    public void testEditRatingByUserId() {
        Rating rating = new Rating(1L, 100L, 200L, 3, LocalDate.now().minusDays(1), "Original");

        ratingSystem.editRatingByUserId(rating, 5, "Updated comment");

        assertEquals(5, rating.getStar());
        assertEquals("Updated comment", rating.getComment());
        assertEquals(LocalDate.now(), rating.getRatingDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditRating_InvalidStar_TooLow() {
        Rating r = new Rating(1L, 1L, 1L, 3, LocalDate.now(), "Old");
        ratingSystem.editRatingByUserId(r, 0, "Invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditRating_InvalidStar_TooHigh() {
        Rating r = new Rating(1L, 1L, 1L, 3, LocalDate.now(), "Old");
        ratingSystem.editRatingByUserId(r, 6, "Invalid");
    }

    @Test
    public void testDeleteRatingById_RemovesRating() {
        Rating r = new Rating(999L, 1L, 1L, 5, LocalDate.now(), "Test");
        setRatings(new ArrayList<>(List.of(r)));

        ratingSystem.deleteRatingById(999L);

        assertEquals(0, ratingSystem.getRatings().size());
        assertNull(ratingSystem.getRatingByRatingId(999L));
    }

    @Test
    public void testDeleteRatingById_NonExistent_DoesNothing() {
        setRatings(new ArrayList<>());
        int sizeBefore = ratingSystem.getRatings().size();

        ratingSystem.deleteRatingById(888L);

        assertEquals(sizeBefore, ratingSystem.getRatings().size());
    }
}
