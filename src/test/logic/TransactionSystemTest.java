package test.logic;

import application.database.Database;
import application.logic.Book;
import application.logic.BookSystem;
import application.logic.TransactionSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionSystemTest {
    private static TransactionSystem transactionSystem;
    private static List<Long> transactionIdAdded;

    @BeforeClass
    public static void setUpBeforeClass() {
        transactionSystem = new TransactionSystem();
        transactionIdAdded = new ArrayList<>();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        for (Long transactionId : transactionIdAdded) {
            try {
                Database.deleteTransactionById(transactionId);
            } catch (Exception e) {
                // Ignore this exception
            }
        }
    }


}
