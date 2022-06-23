//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import game.TypeOfGame;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * To pull the trigger, called by ?p
 */
public class Pull implements Command {
    private static final int DEFAULT_BET = 50;


    /**
     * Extracts the bet from the string, at exception default is used.
     * @param s String to parse
     * @return bet amount.
     */
    private int getBet(String s) {
        System.out.println(s);
        if (s.equals("")) {
            return DEFAULT_BET;
        }

        int b;
        try {
            b = Integer.parseInt(s);
        }
        catch (Exception e) {
            return DEFAULT_BET;
        }
        return b;
    }

    @Override
    public void run(MessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel textChannel = event.getTextChannel();
        Game game = DataHandler.onGoingGames.get(textChannel);

        if (game != null) {
            Member currentPlayer = game.getCurrentPlayer();

            if (!(currentPlayer.equals(member))) {
                textChannel.sendMessage(String.format("Current player is %s", currentPlayer.getAsMention())).queue();
            }
            else {
                int bet;

                try {
                    bet = getBet(event.getMessage().getContentRaw().substring(getCommandShortHand().length() + 1));
                }
                catch (Exception e) {
                    bet = DEFAULT_BET;
                }

                textChannel.sendMessage(String.format("You bet %d", bet)).queue();

                boolean dead = game.pullTrigger();

                if (dead) {
                    game.dropPlayer(currentPlayer);
                    textChannel.sendMessage("BOOM!, ya dead.").queue();
                    //TODO: (database)
                    //memberAccount.addValue(-bet);
                    textChannel.sendMessage(String.format("You lost %d", bet)).queue();

                    if (game.isLive()) {
                        textChannel.sendMessage(String.format("Next player: %s", game.getCurrentPlayer().getEffectiveName())).queue();
                    }
                    else {
                        if (game.getType() == TypeOfGame.MULTIPLE) {
                            currentPlayer = game.getCurrentPlayer();
                            textChannel.sendMessage(String.format("Game over %s is the winner, your account value has been doubled.", currentPlayer.getAsMention())).queue();
                            //TODO: (database)
                            //memberAccount = DataCollection.memberAccounts.get(currentPlayer.getUser());
                            //memberAccount.addValue(memberAccount.getValue());
                        }
                        else if (game.getType() == TypeOfGame.SINGLE) {
                            textChannel.sendMessage(String.format("What a sad end, %s is dead, game over.", currentPlayer.getEffectiveName())).queue();
                        }
                        DataHandler.onGoingGames.remove(textChannel);
                    }
                }
                else {
                    textChannel.sendMessage("\"Click\"").queue();
                    //TODO: (database)
                    //memberAccount.addValue(bet);

                    if (game.isFinished()) {
                        //TODO: (database)
                        //memberAccount.addValue(memberAccount.getValue());
                        textChannel.sendMessage("You win! Your account value has been doubled.").queue();
                        DataHandler.onGoingGames.remove(textChannel);
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
        return String.format("%sp", RouletteBot.PREFIX);
    }
}