//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

import java.util.List;
import java.util.Map;

/**
 * Displays information about the caller's 5 latest games. Called by ?g
 */
public class GetGames implements Command {
    private final int gamesToDisplay;

    public GetGames(int gamesToDisplay) {
        this.gamesToDisplay = gamesToDisplay;
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();

        if (DataHandler.userExistsInDataBase(messageAuthor)) {
            List<Map<String, String>> rows = DataHandler.getGamesOfUser(messageAuthor);

            if (rows == null) {
                event.getTextChannel().sendMessage("No games found.").queue();
                return;
            }

            try {
                //Only last 5, not all in database.
                rows = rows.subList(0, gamesToDisplay + 1);
            } catch (IndexOutOfBoundsException ignored) {
                //If it fails then it is less than five games and nothing needs to be done.
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(RouletteBot.DISCORD_MULTIPLE_CODE_MARKDOWN);


            for (Map<String, String> row : rows) {
                for (String column : row.keySet()) {

                    if (column.equals("winner") && !row.get(column).equals("NONE")) {
                        stringBuilder.append(String.format("%s: %s\n", column, DataHandler.getUserName(User.fromId(row.get(column)))));
                    }
                    else {
                        stringBuilder.append(String.format("%s: %s\n", column, row.get(column)));
                    }
                }
                stringBuilder.append("\n");
            }

            stringBuilder.append(RouletteBot.DISCORD_MULTIPLE_CODE_MARKDOWN);

            event.getTextChannel().sendMessage(stringBuilder.toString()).queue();
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
