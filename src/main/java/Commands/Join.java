//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 *  To join a game, called by ?j
 */
public class Join implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        TextChannel textChannel = event.getTextChannel();
        User messageAuthor = event.getAuthor();

        Game game = DataHandler.onGoingGames.get(textChannel);
        DataHandler.addUser(messageAuthor);

        if (game == null) {
            textChannel.sendMessage("No game here.").queue();
        }
        else if (game.getPlayers().contains(messageAuthor)) {
            textChannel.sendMessage(String.format("You have already joined %s", messageAuthor.getAsMention())).queue();
        }
        else if (!(game.addPlayer(messageAuthor))) {
            textChannel.sendMessage(String.format("The Game is full and waiting to start\n%s may use %s to start the game.", game.getLeader().getAsMention(), getCommandShortHand())).queue();
        }
        else {
            textChannel.sendMessage(String.format("%s has joined!", messageAuthor.getName())).queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sj", RouletteBot.PREFIX);
    }
}
