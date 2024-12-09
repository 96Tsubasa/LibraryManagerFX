package test.logic;

import application.database.Database;
import application.logic.Rating;
import application.logic.RatingSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RatingSystemTest {
    private static RatingSystem ratingSystem;
    private static List<Long> ratingIdAdded;

    @BeforeClass
    public static void setUpBeforeClass() {
        ratingSystem = new RatingSystem();
        ratingIdAdded = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        for (Long ratingId : ratingIdAdded) {
            try {
                Database.deleteRatingById(ratingId);
            } catch (Exception e) {
                // Ignore this exception
            }
        }
    }

    private Rating addRatingForTest(long userId, long bookId, int star,
                                  LocalDate ratingDay, String comment) {
        Rating rating = ratingSystem.addRating(userId, bookId, star, ratingDay, comment);
        ratingIdAdded.add(rating.getRatingId());
        return rating;
    }

    @Test
    public void testGetAvgBookRating1() {
        addRatingForTest(900000001, 900000001, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");

        assertEquals(5, ratingSystem.getAvgBookRating(900000001), 0.001);
    }

    @Test
    public void testGetAvgBookRating2() {
        addRatingForTest(900000001, 900000002, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");
        addRatingForTest(900000002, 900000002, 4, LocalDate.now().minusDays(11),
                "Great plot, interesting story.");
        addRatingForTest(900000003, 900000002, 3, LocalDate.now().minusDays(5),
                "This book is alright, not the best.");
        addRatingForTest(900000004, 900000002, 3, LocalDate.now().minusDays(2),
                "I think it's good but it's not my cup of tea.");
        addRatingForTest(900000005, 900000002, 4, LocalDate.now(),
                null);

        assertEquals(3.8, ratingSystem.getAvgBookRating(900000002), 0.001);
    }

    @Test
    public void testGetAvgBookRating3() {
        addRatingForTest(900000001, 900000003, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");
        addRatingForTest(900000002, 900000003, 4, LocalDate.now().minusDays(13),
                "Great plot, interesting story.");
        addRatingForTest(900000003, 900000004, 3, LocalDate.now().minusDays(13),
                "This book is alright, not the best.");
        addRatingForTest(900000004, 900000003, 2, LocalDate.now().minusDays(11),
                "I think it's good but it's not my cup of tea.");
        addRatingForTest(900000005, 900000004, 4, LocalDate.now().minusDays(9),
                null);
        addRatingForTest(900000006, 900000003, 5, LocalDate.now().minusDays(7),
                "I have read this book multiple times and I enjoy it every single time.");
        addRatingForTest(900000007, 900000003, 1, LocalDate.now().minusDays(4),
                null);
        addRatingForTest(900000008, 900000004, 1, LocalDate.now().minusDays(3),
                "I expected too much from this book.");
        addRatingForTest(900000009, 900000003, 3, LocalDate.now().minusDays(2),
                null);
        addRatingForTest(900000010, 900000003, 4, LocalDate.now(),
                null);

        assertEquals(3.4, ratingSystem.getAvgBookRating(900000003), 0.001);
        assertEquals(2.7, ratingSystem.getAvgBookRating(900000004), 0.001);
    }

    @Test
    public void testGetRatingForUserId() {
        addRatingForTest(900000011, 900000001, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");
        addRatingForTest(900000011, 900000002, 3, LocalDate.now().minusDays(11),
                "The story is alright, but the pace is too fast imo.");
        addRatingForTest(900000011, 900000003, 2, LocalDate.now().minusDays(7),
                "I expected something better than this tbh.");
        addRatingForTest(900000011, 900000004, 1, LocalDate.now().minusDays(2),
                "What did I just read? The plot is very weird and I can't understand what " +
                        "are the characters thinking about with their actions at all...");

        List<Rating> result = ratingSystem.getRatingForUserId(900000011);

        boolean book1 = false;
        boolean book2 = false;
        boolean book3 = false;
        boolean book4 = false;
        for (Rating rating : result) {
            assertEquals(900000011, rating.getUserId());
            if (!book1 && rating.getBookId() == 900000001) {
                assertEquals(5, rating.getStar());
                assertEquals(LocalDate.now().minusDays(14), rating.getRatingDate());
                assertEquals("This book is very amazing, recommend for romance lovers.",
                        rating.getComment());
                book1 = true;
            } else if (!book2 && rating.getBookId() == 900000002) {
                assertEquals(3, rating.getStar());
                assertEquals(LocalDate.now().minusDays(11), rating.getRatingDate());
                assertEquals("The story is alright, but the pace is too fast imo.",
                        rating.getComment());
                book2 = true;
            } else if (!book3 && rating.getBookId() == 900000003) {
                assertEquals(2, rating.getStar());
                assertEquals(LocalDate.now().minusDays(7), rating.getRatingDate());
                assertEquals("I expected something better than this tbh.",
                        rating.getComment());
                book3 = true;
            } else if (!book4 && rating.getBookId() == 900000004) {
                assertEquals(1, rating.getStar());
                assertEquals(LocalDate.now().minusDays(2), rating.getRatingDate());
                assertEquals("What did I just read? The plot is very weird and I can't " +
                                "understand what are the characters thinking about with their " +
                                "actions at all...",
                        rating.getComment());
                book4 = true;
            }
        }

        if (!book1 || !book2 || !book3 || !book4) {
            fail("testGetRatingForUserId() failed.");
        }
    }

    @Test
    public void testGetRatingForBookId() {
        addRatingForTest(900000001, 900000005, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");
        addRatingForTest(900000002, 900000005, 3, LocalDate.now().minusDays(11),
                "The story is alright, but the pace is too fast imo.");
        addRatingForTest(900000003, 900000005, 4, LocalDate.now().minusDays(7),
                "I expected something better than this tbh.");
        addRatingForTest(900000004, 900000005, 3, LocalDate.now().minusDays(2),
                null);

        List<Rating> result = ratingSystem.getRatingForBookId(900000005);

        boolean user1 = false;
        boolean user2 = false;
        boolean user3 = false;
        boolean user4 = false;
        for (Rating rating : result) {
            assertEquals(900000005, rating.getBookId());
            if (!user1 && rating.getUserId() == 900000001) {
                assertEquals(5, rating.getStar());
                assertEquals(LocalDate.now().minusDays(14), rating.getRatingDate());
                assertEquals("This book is very amazing, recommend for romance lovers.",
                        rating.getComment());
                user1 = true;
            } else if (!user2 && rating.getUserId() == 900000002) {
                assertEquals(3, rating.getStar());
                assertEquals(LocalDate.now().minusDays(11), rating.getRatingDate());
                assertEquals("The story is alright, but the pace is too fast imo.",
                        rating.getComment());
                user2 = true;
            } else if (!user3 && rating.getUserId() == 900000003) {
                assertEquals(4, rating.getStar());
                assertEquals(LocalDate.now().minusDays(7), rating.getRatingDate());
                assertEquals("I expected something better than this tbh.",
                        rating.getComment());
                user3 = true;
            } else if (!user4 && rating.getUserId() == 900000004) {
                assertEquals(3, rating.getStar());
                assertEquals(LocalDate.now().minusDays(2), rating.getRatingDate());
                assertNull(rating.getComment());
                user4 = true;
            }
        }

        if (!user1 || !user2 || !user3 || !user4) {
            fail("testGetRatingForBookId() failed.");
        }
    }

    @Test
    public void testGetRatingByRatingId() {
        Rating rating = addRatingForTest(900000012, 900000006, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");

        assertEquals(rating, ratingSystem.getRatingByRatingId(rating.getRatingId()));
    }

    @Test
    public void testEditRatingByUserId() {
        Rating rating = addRatingForTest(900000013, 900000007, 5, LocalDate.now().minusDays(14),
                "This book is very amazing, recommend for romance lovers.");

        ratingSystem.editRatingByUserId(rating, 4, "New comment");

        Rating newRating = ratingSystem.getRatingByRatingId(rating.getRatingId());
        assertEquals(4, newRating.getStar());
        assertEquals("New comment", newRating.getComment());
    }

    @Test
    public void testAddRating() {

    }

    @Test
    public void testDeleteRatingById() {

    }
}
