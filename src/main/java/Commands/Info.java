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
        User user = event.getAuthor();

        StringBuilder builder = new StringBuilder();

        try {
            builder.append(String.format("%s, your stats:\n", user.getAsMention()));
            DataHandler.DataBaseUser dataBaseUser = DataHandler.getDataBaseUser(user);
            builder.append(String.format("Account value: %d\n", dataBaseUser.amount));
            builder.append(String.format("Bets: %d\n", dataBaseUser.bets));
            builder.append(String.format("Wins: %d\n", dataBaseUser.wins));
        } catch (NullPointerException e) {
            builder.append("You have never played a game and therefore are not present in the database");
        }

        event.getTextChannel().sendMessage(builder.toString()).queue();
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%si", RouletteBot.PREFIX);
    }
}
