package test.logic;

import application.logic.Book;
import application.logic.LibrarySystem;
import application.logic.Transaction;
import application.logic.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibrarySystemTest {
    private static LibrarySystem librarySystem;
//    private static User admin;
//    private static User normalUser;
//    private static Book book;
//    private static Transaction transaction;

    @BeforeClass
    public static void setUpBeforeClass() {
        librarySystem = LibrarySystem.getInstance();
        User root = librarySystem.getUsers().get(0);
        librarySystem.handleLogin(root.getUsername(), root.getPassword());
//        admin = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
//                "TEST_password1", User.ADMIN, new byte[] {1, 2, 3});
//        normalUser = librarySystem.addUser("TEST_username2", "TEST_email2@gmail.com",
//                "TEST_password2", User.NORMAL_USER, new byte[] {3, 2, 1});
//        book = librarySystem.addBook("TEST_bookTitle",
//                new String[] {"Author A", "Author B"},
//                "TEST_publisher",
//                2000,
//                new String[] {"Genre A", "Genre B", "Genre C"},
//                10,
//                "TEST_description",
//                new byte[] {1, 2, 3, 4, 5},
//                "9000000000001");
    }

    @AfterClass
    public static void tearDownAfterClass() {
//        librarySystem.deleteUserById(admin.getUserId());
//        librarySystem.deleteUserById(normalUser.getUserId());
//        librarySystem.deleteBookById(book.getBookId());
        System.out.println("Clean up completed.");
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testAddUser_SuccessNormalUser() {
        int normalUserCount = librarySystem.getCountUser();
        int userCount = librarySystem.getUsers().size();

        User newUser = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
                "TEST_password1", User.NORMAL_USER, new byte[] {2, 4, 5});

        assertEquals("TEST_username1", newUser.getUsername());
        assertEquals("TEST_email1@gmail.com", newUser.getEmail());
        assertEquals("TEST_password1", newUser.getPassword());
        assertEquals(User.NORMAL_USER, newUser.getRole());
        assertArrayEquals(new byte[] {2, 4, 5}, newUser.getImageUser());

        assertEquals(normalUserCount + 1, librarySystem.getCountUser());
        assertEquals(userCount + 1, librarySystem.getUsers().size());

        try {
            librarySystem.deleteUserById(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("Ignore this message.");
        }
    }

    @Test
    public void testAddUser_SuccessAdmin() {
        int adminCount = librarySystem.getCountAdmin();
        int userCount = librarySystem.getUsers().size();

        User newUser = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
                "TEST_password1", User.ADMIN, new byte[] {2, 4, 5});

        assertEquals("TEST_username1", newUser.getUsername());
        assertEquals("TEST_email1@gmail.com", newUser.getEmail());
        assertEquals("TEST_password1", newUser.getPassword());
        assertEquals(User.ADMIN, newUser.getRole());
        assertArrayEquals(new byte[] {2, 4, 5}, newUser.getImageUser());

        assertEquals(adminCount + 1, librarySystem.getCountAdmin());
        assertEquals(userCount + 1, librarySystem.getUsers().size());

        try {
            librarySystem.deleteUserById(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("Ignore this message.");
        }
    }

    @Test
    public void testAddUser_UsernameExisted() {
        User existedUser = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
                "TEST_password1", User.NORMAL_USER, new byte[] {1, 2, 3});

        boolean failed = true;
        User newUser = null;

        try {
            newUser = librarySystem.addUser(existedUser.getUsername(), "TEST_email2@gmail.com",
                    "TEST_password2", User.NORMAL_USER, new byte[] {2, 4, 5});
        } catch (IllegalArgumentException e) {
            assertEquals("Username is already registered.", e.getMessage());
            failed = false;
        }

        try {
            librarySystem.deleteUserById(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("Ignore this message.");
        }

        librarySystem.deleteUserById(existedUser.getUserId());

        if (failed) {
            fail("New account with existed username shouldn't be created.");
        }
    }

    @Test
    public void testAddUser_EmailExisted() {
        User existedUser = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
                "TEST_password1", User.NORMAL_USER, new byte[] {1, 2, 3});

        boolean failed = true;
        User newUser = null;

        try {
            newUser = librarySystem.addUser("TEST_username2", existedUser.getEmail(),
                    "TEST_password2", User.NORMAL_USER, new byte[] {2, 4, 5});
        } catch (IllegalArgumentException e) {
            assertEquals("Email is already registered.", e.getMessage());
            failed = false;
        }

        try {
            librarySystem.deleteUserById(newUser.getUserId());
        } catch (Exception e) {
            System.out.println("Ignore this message.");
        }

        librarySystem.deleteUserById(existedUser.getUserId());

        if (failed) {
            fail("New account with existed email shouldn't be created.");
        }
    }

    @Test
    public void testHandleLogin_Success() {
        User admin = librarySystem.getUsers().get(0);

        User newUser = librarySystem.handleLogin(admin.getUsername(), admin.getPassword());

        assertNotNull(newUser);
    }

    @Test
    public void testHandleLogin_UsernameNotExisted() {
        boolean failed = true;
        User newUser = null;

        try {
            newUser = librarySystem.handleLogin("TEST_username142857", "TEST_password2");
            User admin = librarySystem.getUsers().get(0);
            librarySystem.handleLogin(admin.getUsername(), admin.getPassword());
        } catch (IllegalArgumentException e) {
            assertEquals("Username does not exist.", e.getMessage());
            failed = false;
        }

        if (failed) {
            fail("Expect an IllegalArgumentException when no such username found during login.");
        }
    }

    @Test
    public void testHandleLogin_IncorrectPassword() {
        User existedUser = librarySystem.addUser("TEST_username1", "TEST_email1@gmail.com",
                "TEST_password1", User.NORMAL_USER, new byte[] {1, 2, 3});

        boolean failed = true;
        User newUser = null;

        try {
            newUser = librarySystem.handleLogin(existedUser.getUsername(), "TEST_password2");
            User admin = librarySystem.getUsers().get(0);
            librarySystem.handleLogin(admin.getUsername(), admin.getPassword());
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect password.", e.getMessage());
            failed = false;
        }

        librarySystem.deleteUserById(existedUser.getUserId());

        if (failed) {
            fail("Expect an IllegalArgumentException when the password is incorrect.");
        }
    }

    @Test
    public void testAddBook_Success() {
        int bookCount = librarySystem.getBooks().size();

        Book newBook = librarySystem.addBook("TEST_bookTitle",
                new String[] {"Author A", "Author B"},
                "TEST_publisher",
                2000,
                new String[] {"Genre A", "Genre B", "Genre C"},
                10,
                "TEST_description",
                new byte[] {1, 2, 3, 4, 5},
                "9000000000001");

        assertNotNull(newBook);
        assertEquals("TEST_bookTitle", newBook.getTitle());
        assertArrayEquals(new String[] {"Author A", "Author B"}, newBook.getAuthors());
        assertEquals("TEST_publisher", newBook.getPublisher());
        assertEquals(2000, newBook.getPublicationYear());
        assertArrayEquals(new String[] {"Genre A", "Genre B", "Genre C"}, newBook.getGenres());
        assertEquals(10, newBook.getCopiesAvailable());
        assertEquals("TEST_description", newBook.getDescription());
        assertArrayEquals(new byte[] {1, 2, 3, 4, 5}, newBook.getCoverImage());
        assertEquals("9000000000001", newBook.getIsbn());

        assertEquals(bookCount + 1, librarySystem.getBooks().size());

        librarySystem.deleteBookById(newBook.getBookId());
    }

    @Test
    public void testGetCountAdmin() {
        List<User> users = librarySystem.getUsers();
        List<User> admins = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(User.ADMIN)) {
                admins.add(user);
            }
        }

        assertEquals(admins.size(), librarySystem.getCountAdmin());
    }

    @Test
    public void testGetCountUser() {
        List<User> users = librarySystem.getUsers();
        List<User> normalUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(User.NORMAL_USER)) {
                normalUsers.add(user);
            }
        }

        assertEquals(normalUsers.size(), librarySystem.getCountUser());
    }

    // Need more tests later
}
