//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * To leave a game, called by ?l
 * Game ends if all players leave, if leader leaves a new leader is chosen.
 */
public class Leave implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        TextChannel textChannel = event.getTextChannel();
        Member member = event.getMember();

        Game game = DataHandler.onGoingGames.get(textChannel);

        if (game == null) {
            textChannel.sendMessage("No game here.").queue();
        }
        else if (!(game.contains(member))) {
            textChannel.sendMessage(String.format("You are not in a game %s.", member.getAsMention())).queue();
        }
        else {
            textChannel.sendMessage(String.format("%s has left the game.", member.getEffectiveName())).queue();

            game.dropPlayer(member);

            if (game.getPlayers().size() == 0) {
                textChannel.sendMessage("All players have left the game, game cancelled.").queue();
                DataHandler.onGoingGames.remove(textChannel);
            }
            else if (member.equals(game.getLeader())) {
                textChannel.sendMessage(String.format("Leader has left, new leader: %s", game.newLeader())).queue();
            }
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sl", RouletteBot.PREFIX);
    }
}