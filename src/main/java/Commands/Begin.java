package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * Begins a game, called by ?b
 */
public class Begin implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();
        Game game = DataHandler.onGoingGames.get(event.getTextChannel());
        TextChannel textChannel = event.getTextChannel();

        if (game != null) {
            if (game.isLive()) {
                textChannel.sendMessage("Game already in process.").queue();
            }
            else {
                DataHandler.addUser(messageAuthor);

                User leader = game.getLeader();

                if (!(leader.equals(messageAuthor))) {
                    textChannel.sendMessage(String.format("Only available to leader, which is %s.", leader.getAsMention())).queue();
                }
                else {
                    game.goLive();
                    textChannel.sendMessage(String.format("Game has begun\nCurrent player is %s.", game.getCurrentPlayer().getAsMention())).queue();

                    if (game.getActivePlayers().size() == 1) {
                        textChannel.sendMessage("Single-player russian roulette, the saddest game known to man.").queue();
                    }
                }
            }
        }
        else {
            textChannel.sendMessage("No game here.").queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sb", RouletteBot.PREFIX);
    }
}
