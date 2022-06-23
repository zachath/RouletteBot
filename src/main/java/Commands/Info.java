//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;


/**
 *
 */
//TODO: Implement (includes database)
public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event) {
    }

    @Override
    public String getCommandShortHand() {
        return String.format("%si", RouletteBot.PREFIX);
    }
}
