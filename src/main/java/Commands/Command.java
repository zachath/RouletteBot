//Zacharias Thorell

package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void run(MessageReceivedEvent event);
    String getCommandShortHand();
}
