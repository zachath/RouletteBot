//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

public class Join implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        TextChannel textChannel = event.getTextChannel();
        Member member = event.getMember();

        if (member == null) return;

        Game game = DataHandler.onGoingGames.get(textChannel);

        if (game == null) {
            textChannel.sendMessage("No game here mate").queue();
        }
        else if (game.getPlayers().contains(member)) {
            textChannel.sendMessage("You have already joined this game").queue();
        }
        else if (!(game.addPlayer(member))) {
            DataHandler.addMember(member.getUser());
            textChannel.sendMessage(String.format("The Game is full and waiting to start\n%s may use %s to start the game.", game.getLeader().getAsMention(), getCommandShortHand())).queue();
        }
        else {
            textChannel.sendMessage(member.getEffectiveName() + " has joined!").queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sj", RouletteBot.PREFIX);
    }
}