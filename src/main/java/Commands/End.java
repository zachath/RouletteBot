//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * Ends a game, called by ?e
 * Only the leader may end a game.
 */
public class End implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        TextChannel textChannel = event.getTextChannel();
        User messageAuthor = event.getAuthor();

        Game game = DataHandler.onGoingGames.get(textChannel);

        if (game == null) {
            textChannel.sendMessage("No game here.").queue();
        }
        else if (!(game.getLeader().equals(messageAuthor))) {
            textChannel.sendMessage(String.format("You may not end this game, current leader is: %s.", game.getLeader().getAsMention())).queue();
        }
        else {
            DataHandler.onGoingGames.remove(textChannel);
            textChannel.sendMessage(String.format("The game has been cancelled by %s.", messageAuthor.getAsMention())).queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%se", RouletteBot.PREFIX);
    }
}