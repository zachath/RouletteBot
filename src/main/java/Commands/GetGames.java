//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

import java.util.List;

/**
 * Displays information about the caller's 5 latest games. Called by ?g
 */
public class GetGames implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();

        if (DataHandler.userExistsInDataBase(messageAuthor)) {
            List<List<String>> rows = DataHandler.getGamesOfUser(messageAuthor);

            if (rows == null) {
                event.getTextChannel().sendMessage("No games found.").queue();
                return;
            }

            for (List<String> row : rows) {
                for (String column : row) {
                    System.out.print(column);
                }
                System.out.println();
            }
        }
        else {
            event.getTextChannel().sendMessage("No games found.").queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sg", RouletteBot.PREFIX);
    }
}
