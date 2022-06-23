//Zacharias Thorell

package util;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the connection to the local MySQL database.
 */
public class DataHandler {
    //Tracks currently onGoingGames the bot is handling.
    public static HashMap<TextChannel, Game> onGoingGames = new HashMap<>();

    public static final String DATABASE_CONNECTION_URL = "jdbc:mysql://localhost:3306/roulettebot_db";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "root";

    public static final String GET_USER_STATEMENT = "SELECT * FROM USERS WHERE NAME = %s;";
    public static final String TEST_ROW_NAME = "test";

    /**
     * Add user to the database.
     * @param username of the user.
     * @return if the user has been added or not.
     */
    public static boolean addUser(String username) {
        if (getDataBaseUser(username) == null) {
            executeNonResultQuery(String.format("INSERT INTO USERS VALUES (NULL, %s, %d, 0, 0);", formatStringValue(username), RouletteBot.START_ACCOUNT_VALUE));
            return true;
        }

        return false;
    }

    /**
     * Remove user from the database.
     * @param username of the user.
     * @return if the user has been removed or not.
     */
    public static boolean removeUser(String username) {
        executeNonResultQuery(String.format("DELETE FROM USERS WHERE NAME = %s", formatStringValue(username)));
        return true;
    }

    /**
     * @param username of the user.
     * @param value to modify by (negative to decrease value).
     */
    public static void modifyAccountValue(String username, int value) {
        executeNonResultQuery(String.format("UPDATE USERS SET AMOUNT = AMOUNT + %d WHERE NAME = %s;", value, formatStringValue(username)));
    }

    /**
     * Increments the amount of wins of the user.
     * @param username of the user.
     */
    public static void incrementWins(String username) {
        executeNonResultQuery(String.format("UPDATE USERS SET WINS = WINS + 1 WHERE NAME = %s;", formatStringValue(username)));
    }

    /**
     * Increments the amount of bets of the user.
     * @param username of the user.
     */
    public static void incrementBets(String username) {
        executeNonResultQuery(String.format("UPDATE USERS SET BETS = BETS + 1 WHERE NAME = %s;", formatStringValue(username)));
    }

    /**
     * @param username of the user.
     * @return total account value of user.
     */
    public static int getUserAccountValue(String username) {
        return getDataBaseUser(username).amount;
    }

    /**
     * @param username of the user.
     * @return the total amount of wins of user.
     */
    public static int getUserWins(String username) {
        return getDataBaseUser(username).wins;
    }

    /**
     * @param username of the user.
     * @return the total amount of bets of the user.
     */
    public static int getUserBets(String username) {
        return getDataBaseUser(username).bets;
    }

    /**
     * @return a List of all the users in the database.
     */
    public static List<DataBaseUser> getAllUsers() {
        List<DataBaseUser> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM USERS");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new DataBaseUser(rs.getInt("id"), rs.getString("name"), rs.getInt("amount"), rs.getInt("bets"), rs.getInt("wins")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Reset the AUTO_INCREMENT of the database.
     * @param value to reset to.
     */
    public static void resetDatabaseAutoIncrement(int value) {
        if (getAllUsers().size() == 0) {
            executeNonResultQuery(String.format("ALTER TABLE USERS AUTO_INCREMENT = %d", value));
        }
    }

    /**
     * @param username of the user.
     * @return DataBaseUser representation of the specified username.
     */
    private static DataBaseUser getDataBaseUser(String username) {
        return executeResultQuery(String.format(GET_USER_STATEMENT, formatStringValue(username)));
    }

    /**
     * Executes a query and returns the result in the form of a DataBaseUser.
     * @param sqlQuery Query to execute.
     * @return Resulting DataBaseUser.
     */
    private static DataBaseUser executeResultQuery(String sqlQuery) {
        try (Connection conn = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sqlQuery);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new DataBaseUser(rs.getInt("id"), rs.getString("name"), rs.getInt("amount"), rs.getInt("bets"), rs.getInt("wins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Executes the query without any result.
     * @param sqlQuery Query to execute.
     */
    private static void executeNonResultQuery(String sqlQuery) {
        try (Connection conn = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

             ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats the string so that is seen as a value and not a column.
     * @param s String to format.
     * @return "s"
     */
    private static String formatStringValue(String s) {
        return "\"" + s + "\"";
    }

    /**
     * Represents a user in the database, an in between the Discord JDA api User and the database.
     */
    private static class DataBaseUser {
        final int id;
        final String name;
        final int amount;
        final int bets;
        final int wins;

        public DataBaseUser(int id, String name, int amount, int bets, int wins) {
            this.id = id;
            this.name = name;
            this.amount = amount;
            this.bets = bets;
            this.wins = wins;
        }
    }
}
