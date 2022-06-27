//Zacharias Thorell

import static org.junit.jupiter.api.Assertions.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import util.DataHandler;

import java.util.EnumSet;
import java.util.List;

/**
 * Tests the DataHandler and its connection to the database,
 * current coverage: Method 16/16; Line 58/60
 */
public class DataHandlerTest {
    private static final String TEST_ROW_NAME = DataHandler.TEST_ROW_NAME;
    private static final TestUser TEST_USER = new TestUser(TEST_ROW_NAME, "NAME:" + TEST_ROW_NAME);

    /**
     * Since we need a row to operate on for testing, ensure that the specified is always present.
     */
    @BeforeEach
    public void ensureTestRowPresent() {
        DataHandler.removeUser(TEST_USER);
        DataHandler.addUser(TEST_USER);
    }

    /**
     * Remove the test user.
     */
    @AfterAll
    public static void resetDatabase() {
        DataHandler.removeUser(TEST_USER);
    }

    /*
    * Rest of the tests should be self-explanatory with the by praxis explicit and long names.
    */

    @Test
    public void incrementWinsTest() {
        DataHandler.DataBaseUser testUser = DataHandler.getDataBaseUser(TEST_USER);
        int expected = testUser.wins + 1;
        DataHandler.incrementWins(TEST_USER);
        assertEquals(expected, DataHandler.getDataBaseUser(TEST_USER).wins);
    }

    @Test
    public void incrementBetsTest() {
        DataHandler.DataBaseUser testUser = DataHandler.getDataBaseUser(TEST_USER);
        int expected = testUser.bets + 1;
        DataHandler.incrementBets(TEST_USER);
        assertEquals(expected, DataHandler.getDataBaseUser(TEST_USER).bets);
    }

    @Test
    public void modifyAccountValueTest() {
        DataHandler.DataBaseUser testUser = DataHandler.getDataBaseUser(TEST_USER);
        int value = 50;
        int expected = testUser.amount + value;
        DataHandler.modifyAccountValue(TEST_USER, value);
        assertEquals(expected, DataHandler.getDataBaseUser(TEST_USER).amount);
    }

    @Test
    public void dropUserTest() {
        DataHandler.removeUser(TEST_USER);
        assertTrue(DataHandler.addUser(TEST_USER));
    }

    @Test
    public void addingAlreadyExistingUserFails() {
        assertFalse(DataHandler.addUser(TEST_USER));
    }

    @Test
    public void usernameIsUpdatedInDatabaseIfUpdatedInDiscord() {
        String newName = "NEWNAME:" + TEST_ROW_NAME;
        TestUser newUser = new TestUser(TEST_ROW_NAME, newName);
        DataHandler.getDataBaseUser(newUser); //Any operation that queries the database will do.
        assertEquals(newName, DataHandler.getDataBaseUser(newUser).name);
    }

    /**
     * Class solely for testing purposes so that it can be passed to the methods as it implements the User interface.
     * Any method except getName() and getId() throws and exception.
     */
    private static class TestUser implements User {

        private final String id;
        private final String name;

        public TestUser(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @NotNull
        @Override
        public String getId() {
            return id;
        }

        @NotNull
        @Override
        public String getDiscriminator() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Nullable
        @Override
        public String getAvatarId() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public String getDefaultAvatarId() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public String getAsTag() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public boolean hasPrivateChannel() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public RestAction<PrivateChannel> openPrivateChannel() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public List<Guild> getMutualGuilds() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public boolean isBot() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public boolean isSystem() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public JDA getJDA() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public EnumSet<UserFlag> getFlags() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public int getFlagsRaw() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public boolean isFake() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @NotNull
        @Override
        public String getAsMention() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }

        @Override
        public long getIdLong() {
            throw new IllegalStateException("Not implemented, class only meant for testing purposes.");
        }
    }
}
