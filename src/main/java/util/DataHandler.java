//Zacharias Thorell

package util;

import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
public class DataHandler {
    public static HashMap<TextChannel, Game> onGoingGames = new HashMap<>();

    public static void addMember(User user) {
    }
}
