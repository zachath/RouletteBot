package Commands;

import bot.RouletteBot;
import game.Game;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

public class Begin implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        Member member = event.getMember();
        Game game = DataHandler.onGoingGames.get(event.getTextChannel());
        DataHandler.addMember(member.getUser());
        TextChannel textChannel = event.getTextChannel();

        if (game != null) {
            if (game.isLive()) {
                textChannel.sendMessage("Game already in process").queue();
            }
            else {
                Member leader = game.getLeader();

                if (!(leader.equals(member))) {
                    textChannel.sendMessage("Only available to leader, which is " + leader.getAsMention()).queue();
                }
                else {
                    game.goLive();
                    textChannel.sendMessage(String.format("Game has begun\nCurrent player is %S", game.getCurrentPlayer().getAsMention())).queue();

                    if (game.getPlayers().size() == 1) {
                        textChannel.sendMessage("Singleplayer russian roulette, the saddest game known to man").queue();
                    }
                }
            }
        }
        else {
            textChannel.sendMessage("No game here mate").queue();
        }
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%sb", RouletteBot.PREFIX);
    }
}
