//Zacharias Thorell

package bot;

import Commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class RouletteBot extends ListenerAdapter {
    public static final String PREFIX = "?";

    private static String OWNER_ID;
    private static String TOKEN;

    private static final Create CREATE_COMMAND = new Create();
    private static final Begin BEGIN_COMMAND = new Begin();
    private static final End END_COMMAND = new End();
    private static final Info INFO_COMMAND = new Info();
    private static final Join JOIN_COMMAND = new Join();
    private static final Leave LEAVE_COMMAND = new Leave();
    private static final Pull PULL_COMMAND = new Pull();

    //args[0] -> Discord token, args[1] -> owner id
    public static void main (String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("The discord token and the id of the owner must be specified in the command line.");
            System.exit(1);
        }

        TOKEN = args[0];
        OWNER_ID = args[1];

        JDA jda = JDABuilder
                .createDefault(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .addEventListeners(new RouletteBot())
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                .build();

        jda.awaitReady();
        System.out.println("Build Finished!");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("Message Received!");
        String content = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot()) {
            return;
        }

        if (content.startsWith(CREATE_COMMAND.getCommandShortHand())) {
            CREATE_COMMAND.run(event);
        }
        else if (content.startsWith(BEGIN_COMMAND.getCommandShortHand())) {
            BEGIN_COMMAND.run(event);
        }
        else if (content.startsWith( END_COMMAND.getCommandShortHand())) {
            END_COMMAND.run(event);
        }
        else if (content.startsWith(INFO_COMMAND.getCommandShortHand())) {
            INFO_COMMAND.run(event);
        }
        else if (content.startsWith(JOIN_COMMAND.getCommandShortHand())) {
            JOIN_COMMAND.run(event);
        }
        else if (content.startsWith(LEAVE_COMMAND.getCommandShortHand())) {
            LEAVE_COMMAND.run(event);
        }
        else if (content.startsWith(PULL_COMMAND.getCommandShortHand())) {
            PULL_COMMAND.run(event);
        }
    }
}