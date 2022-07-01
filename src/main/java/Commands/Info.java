//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * Displays information about the user stored in the database.
 */
public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();

        StringBuilder builder = new StringBuilder();

        if (DataHandler.userExistsInDataBase(messageAuthor)) {
            builder.append(String.format("%s, your stats:\n", messageAuthor.getAsMention()));
            builder.append(String.format("Account value: %d\n", DataHandler.getUserAmount(messageAuthor)));
            builder.append(String.format("Bets: %d\n", DataHandler.getUserBets(messageAuthor)));
            builder.append(String.format("Wins: %d\n", DataHandler.getUserWins(messageAuthor)));
        }
        else {
            builder.append("You are not present in the database.");
        }

        event.getTextChannel().sendMessage(builder.toString()).queue();
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%si", RouletteBot.PREFIX);
    }
}
