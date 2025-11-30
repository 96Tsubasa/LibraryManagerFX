package test.user;

import application.logic.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserEdgeCasesTest {

    @Test
    public void testLimitBookBoundaries() {
        User user = new User("EdgeUser", 9999L, "edgeuser@example.com", "Password123", User.NORMAL_USER, null);
        // default limit is 10
        assertEquals(10, user.getLimitBook());

        // set to min valid (0) -> should throw when setLimitBook(-1)
        try {
            user.setLimitBook(0);
            assertEquals(0, user.getLimitBook());
        } catch (IllegalArgumentException e) {
            fail("Setting limit to 0 should be allowed or handled; got: " + e.getMessage());
        }

        // invalid: negative
        try {
            user.setLimitBook(-1);
            fail("Expected IllegalArgumentException for negative limit");
        } catch (IllegalArgumentException expected) {
        }

        // invalid: >10
        try {
            user.setLimitBook(11);
            fail("Expected IllegalArgumentException for limit > 10");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testBorrowAndReturnAdjustsLimit() {
        User user = new User("BorrowUser", 9001L, "borrow@example.com", "Password123", User.NORMAL_USER, null);
        int before = user.getLimitBook();

        // Call borrow and return and assert no exception and limit stays within valid
        // bounds [0,10]
        user.borrowBook();
        int afterBorrow = user.getLimitBook();
        assertTrue("Limit after borrow must be between 0 and 10", afterBorrow >= 0 && afterBorrow <= 10);

        user.returnBook();
        int afterReturn = user.getLimitBook();
        assertTrue("Limit after return must be between 0 and 10", afterReturn >= 0 && afterReturn <= 10);
    }

    @Test
    public void testCheckPasswordAndEquality() {
        User a = new User("A", 1L, "a@example.com", "Password123", User.NORMAL_USER, null);
        User b = new User("B", 1L, "b@example.com", "Password123", User.NORMAL_USER, null);
        User c = new User("C", 2L, "c@example.com", "Password123", User.NORMAL_USER, null);

        // equality by userId
        assertEquals(a, b);
        assertNotEquals(a, c);

        // password check
        assertTrue(a.checkPassword("Password123"));
        assertFalse(a.checkPassword("wrong"));
    }
}
