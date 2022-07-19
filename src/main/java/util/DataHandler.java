//Zacharias Thorell

package util;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the connection to the local MySQL database.
 */
public class DataHandler {
    //Tracks currently onGoingGames the bot is handling.
    public static HashMap<TextChannel, Game> onGoingGames = new HashMap<>();

    public static final String DATABASE_CONNECTION_URL = "jdbc:mysql://localhost:3306/roulettebot_db";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "root";

    public static final String TEST_ROW_NAME = "test";

    private static final String NOT_FOUND_STRING = "NOT_FOUND";

    public static List<Map<String, String>> getGamesOfUser(User user) {
        return executeQueryRows(
                String.format("select datetime, pulls, bets, total_bets_value, players, winner from roulettebot_db.games inner join roulettebot_db.game_player_bridge_table ON roulettebot_db.games.id = roulettebot_db.game_player_bridge_table.gameID and roulettebot_db.game_player_bridge_table.playerID = %s;", formatStringValue(user.getId())),
                List.of("datetime", "pulls", "bets", "total_bets_value", "players", "winner")
        );
    }

    /**
     * Ends the game by removing it from the active pool, sets winner and saves it to the database.
     * @param textChannel of game to end.
     */
    public static void endGame(TextChannel textChannel) {
        Game game = onGoingGames.remove(textChannel);
        try {
            game.setWinner(game.getActivePlayers().get(0));
        }
        catch (Exception ignored) {}
        saveGameToDatabase(game);
    }

    /**
     * Saves finished game to database.
     * @param game game to save.
     */
    public static void saveGameToDatabase(Game game) {
        executeQueryWithoutResult(String.format(
                "INSERT INTO GAMES VALUES (%s, %s, %d, %d, %d, %d, %s, %s);",
                formatStringValue(game.getId()),
                formatStringValue(game.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                game.getPulls(),
                game.getBets(),
                game.getTotalBetsValue(),
                game.getAllPlayers().size(),
                formatStringValue(game.getType().toString()),
                formatStringValue(game.getWinner() != null ? game.getWinner().getId() : "NONE")
        ));

        for (User user : game.getAllPlayers()) {
            executeQueryWithoutResult(String.format(
                    "INSERT INTO GAME_PLAYER_BRIDGE_TABLE VALUES (%s, %s);",
                    formatStringValue(game.getId()),
                    formatStringValue(user.getId())
            ));
        }
    }

    /**
     * Add user to the database.
     * @param user user to add.
     * @return if the user has been added or not.
     */
    public static boolean addUser(User user) {
        if (!userExistsInDataBase(user)) {
            executeQueryWithoutResult(String.format("INSERT INTO USERS VALUES (%s, %d, 0, 0, %s);", formatStringValue(user.getId()), RouletteBot.START_ACCOUNT_VALUE, formatStringValue(user.getName())));
            return true;
        }

        updateUserName(user);
        return false;
    }

    /**
     * Remove user from the database.
     * @param user to remove.
     */
    public static void removeUser(User user) {
        executeQueryWithoutResult(String.format("DELETE FROM USERS WHERE id = %s", formatStringValue(user.getId())));
    }

    /**
     * @param user to modify.
     * @param value to modify by (negative to decrease value).
     */
    public static void modifyAccountValue(User user, int value) {
        executeQueryWithoutResult(String.format("UPDATE USERS SET AMOUNT = AMOUNT + %d WHERE ID = %s;", value, formatStringValue(user.getId())));
    }

    /**
     * Increments the amount of wins of the user.
     * @param user to modify.
     */
    public static void incrementWins(User user) {
        executeQueryWithoutResult(String.format("UPDATE USERS SET WINS = WINS + 1 WHERE ID = %s;", formatStringValue(user.getId())));
    }

    /**
     * Increments the amount of bets of the user.
     * @param user to modify.
     */
    public static void incrementBets(User user) {
        executeQueryWithoutResult(String.format("UPDATE USERS SET BETS = BETS + 1 WHERE ID = %s;", formatStringValue(user.getId())));
    }

    /**
     * @param user user to get wins of.
     * @return wins of user.
     */
    public static int getUserWins(User user) {
        return getIntegerValue(user.getId(), "wins");
    }

    /**
     * @param user user to get bets of.
     * @return bets of user.
     */
    public static int getUserBets(User user) {
        return getIntegerValue(user.getId(), "bets");
    }

    /**
     * @param user user to get amount of.
     * @return amount of user.
     */
    public static int getUserAmount(User user) {
        return getIntegerValue(user.getId(), "amount");
    }

    /**
     * @param user user to get name of in databae.
     * @return name of user in database.
     */
    public static String getUserName(User user) {
        return getUserColumnValue(user.getId(), "name");
    }

    /**
     * @param user user to check whether they exist.
     * @return if the user exists in the database or not.
     */
    public static boolean userExistsInDataBase(User user) {
        return !getDatabaseId(user).equals(NOT_FOUND_STRING);
    }

    /** Updates the name of the user in the database if it is found to not be the same as current Discord name.
     * @param user User to check.
     */
    private static void updateUserName(User user) {
        if (!user.getName().equals(getUserName(user))) {
            executeQueryWithoutResult(String.format("UPDATE USERS SET name = %s WHERE id = %s", formatStringValue(user.getName()), formatStringValue(user.getId())));
        }
    }

    /**
     * Gets the Integer value by parsing, at failure 0 is returned.
     * @param userId id of user.
     * @param column column to get.
     * @return value of column or 0 if not int.
     */
    private static int getIntegerValue(String userId, String column) {
        try {
            return Integer.parseInt(getUserColumnValue(userId, column));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param user user in the database to get the id of.
     * @return the id of the user if they exist, otherwise NOT_FOUND.
     */
    private static String getDatabaseId(User user) {
        return getUserColumnValue(user.getId(), "id");
    }

    /**
     * Executes the query and returns the result from the database.
     * @param userId id of user.
     * @param column column to get from database.
     * @return value if present, otherwise NOT_FOUND.
     */
    private static String getUserColumnValue(String userId, String column) {
        String query = String.format("SELECT %s FROM USERS WHERE ID = %s;", column, formatStringValue(userId));
        try (Connection conn = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return NOT_FOUND_STRING;
    }

    private static List<Map<String, String>> executeQueryRows(String sqlQuery, List<String> rowColumns) {
        List<Map<String, String>> listOfRows = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                listOfRows.add(row);
                for (String column : rowColumns) {
                    row.put(column, resultSet.getString(column));
                }
            }

            return listOfRows;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Executes the query without any result.
     * @param sqlQuery Query to execute.
     */
    private static void executeQueryWithoutResult(String sqlQuery) {
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
}
