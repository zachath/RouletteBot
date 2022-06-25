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

public class DataHandlerTest {
    private static final String TEST_ROW_NAME = DataHandler.TEST_ROW_NAME;
    private static final TestUser TEST_USER = new TestUser(TEST_ROW_NAME, "NAME:" + TEST_ROW_NAME);

    /**
     * Since we need a row to operate on, ensure that the specified is always present.
     */
    @BeforeEach
    public void ensureTestRowPresent() {
        DataHandler.removeUser(TEST_USER);
        DataHandler.addUser(TEST_USER);
    }

    /**
     * Remove the test user and reset the AUTO_INCREMENT of the database.
     */
    @AfterAll
    public static void resetDatabase() {
        DataHandler.removeUser(TEST_USER);
    }

    @Test
    public void incrementWinsTest() {
        int expected = DataHandler.getUserWins(TEST_USER) + 1;
        DataHandler.incrementWins(TEST_USER);
        assertEquals(expected, DataHandler.getUserWins(TEST_USER));
    }

    @Test
    public void incrementBetsTest() {
        int expected = DataHandler.getUserBets(TEST_USER) + 1;
        DataHandler.incrementBets(TEST_USER);
        assertEquals(expected, DataHandler.getUserBets(TEST_USER));
    }

    @Test
    public void modifyAccountValueTest() {
        int value = 50;
        int expected = DataHandler.getUserAccountValue(TEST_USER) + value;
        DataHandler.modifyAccountValue(TEST_USER, value);
        assertEquals(expected, DataHandler.getUserAccountValue(TEST_USER));
    }

    @Test
    public void dropUserTest() {
        DataHandler.removeUser(TEST_USER);
        assertTrue(DataHandler.addUser(TEST_USER));
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
