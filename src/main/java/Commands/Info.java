//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
        TextChannel textChannel = event.getTextChannel();
        Member member = event.getMember();

        DataHandler.addMember(member.getUser());

        //Account account = DataHandler.memberAccounts.get(member.getUser());
        //textChannel.sendMessage("Current Account value: " + account.getValue()).queue();
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%si", RouletteBot.PREFIX);
    }
}
