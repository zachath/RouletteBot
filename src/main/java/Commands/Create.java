//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

public class Create implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member leader = event.getMember();
        TextChannel textChannel = event.getTextChannel();

        if (DataHandler.onGoingGames.containsKey(textChannel)) {
            textChannel.sendMessage("A game is already ongoing in this text channel").queue();
        }
        else {
            DataHandler.addMember(leader.getUser());
            Game game = new Game(leader, guild, textChannel);
            DataHandler.onGoingGames.put(textChannel, game);

            textChannel.sendMessage(String.format("A game has been created, type %s to join", getCommandShortHand())).queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sc", RouletteBot.PREFIX);
    }
}
