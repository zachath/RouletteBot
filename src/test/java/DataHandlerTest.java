//Zacharias Thorell

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import util.DataHandler;

public class DataHandlerTest {
    private static final String TEST_ROW_NAME = DataHandler.TEST_ROW_NAME;

    /**
     * Since we need a row to operate on, ensure that the specified is always present.
     */
    @BeforeEach
    public void ensureTestRowPresent() {
        DataHandler.removeUser(TEST_ROW_NAME);
        DataHandler.addUser(TEST_ROW_NAME);
    }

    /**
     * Remove the test user and reset the AUTO_INCREMENT of the database.
     */
    @AfterAll
    public static void resetDatabase() {
        DataHandler.removeUser(TEST_ROW_NAME);
        DataHandler.resetDatabaseAutoIncrement(DataHandler.getAllUsers().size() + 1);
    }

    @Test
    public void incrementWinsTest() {
        int expected = DataHandler.getUserWins(TEST_ROW_NAME) + 1;
        DataHandler.incrementWins(TEST_ROW_NAME);
        assertEquals(expected, DataHandler.getUserWins(TEST_ROW_NAME));
    }

    @Test
    public void incrementBetsTest() {
        int expected = DataHandler.getUserBets(TEST_ROW_NAME) + 1;
        DataHandler.incrementBets(TEST_ROW_NAME);
        assertEquals(expected, DataHandler.getUserBets(TEST_ROW_NAME));
    }

    @Test
    public void modifyAccountValueTest() {
        int value = 50;
        int expected = DataHandler.getUserAccountValue(TEST_ROW_NAME) + value;
        DataHandler.modifyAccountValue(TEST_ROW_NAME, value);
        assertEquals(expected, DataHandler.getUserAccountValue(TEST_ROW_NAME));
    }

    @Test
    public void dropUserTest() {
        DataHandler.removeUser(TEST_ROW_NAME);
        assertTrue(DataHandler.addUser(TEST_ROW_NAME));
    }
}
