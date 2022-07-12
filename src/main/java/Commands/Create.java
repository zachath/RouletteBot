//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * Creates a game, called by ?c
 */
public class Create implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        User leader = event.getAuthor();
        TextChannel textChannel = event.getTextChannel();

        if (DataHandler.onGoingGames.containsKey(textChannel)) {
            textChannel.sendMessage("A game is already ongoing in this text channel.").queue();
        }
        else {
            DataHandler.addUser(leader);
            Game game = new Game(leader, textChannel);
            DataHandler.onGoingGames.put(textChannel, game);

            textChannel.sendMessage(String.format("A game has been created, type %s to join", RouletteBot.JOIN_COMMAND.getCommandShortHand())).queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sc", RouletteBot.PREFIX);
    }
}
