//Zacharias Thorell

package Commands;

import bot.RouletteBot;
import game.Game;
import game.TypeOfGame;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.DataHandler;

/**
 * To pull the trigger, called by ?p
 */
public class Pull implements Command {
    private static final int DEFAULT_BET = 50;

    @Override
    public void run(MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();
        TextChannel textChannel = event.getTextChannel();
        Game game = DataHandler.onGoingGames.get(textChannel);

        if (game != null) {
            User currentPlayer = game.getCurrentPlayer();

            if (!(currentPlayer.equals(messageAuthor))) {
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

                if (bet > DataHandler.getUserAmount(currentPlayer)) {
                    textChannel.sendMessage(String.format("You cannot cover your bet, try betting lower. Or alternatively cry to %s about it.", User.fromId(RouletteBot.OWNER_ID).getAsMention())).queue();
                    return;
                }

                textChannel.sendMessage(String.format("You bet %d", bet)).queue();
                DataHandler.incrementBets(currentPlayer);

                boolean dead = game.pullTrigger();
                game.addBet(bet);

                if (dead) {
                    game.dropPlayer(currentPlayer);
                    textChannel.sendMessage("BOOM!, ya dead.").queue();
                    DataHandler.modifyAccountValue(currentPlayer, -bet);
                    textChannel.sendMessage(String.format("You lost %d", bet)).queue();

                    if (game.isLive()) {
                        textChannel.sendMessage(String.format("Next player: %s", game.getCurrentPlayer().getName())).queue();
                    }
                    else {
                        if (game.getType() == TypeOfGame.MULTIPLE) {
                            currentPlayer = game.getCurrentPlayer();
                            textChannel.sendMessage(String.format("Game over %s is the winner, your account value has been doubled.", currentPlayer.getAsMention())).queue();
                            DataHandler.modifyAccountValue(currentPlayer, DataHandler.getUserAmount(currentPlayer) * 2);
                            DataHandler.incrementWins(currentPlayer);
                        }
                        else if (game.getType() == TypeOfGame.SINGLE) {
                            textChannel.sendMessage(String.format("What a sad end, %s is dead, game over.", currentPlayer.getName())).queue();
                        }
                        DataHandler.endGame(textChannel);
                    }
                }
                else {
                    textChannel.sendMessage("\"Click\"").queue();
                    DataHandler.modifyAccountValue(currentPlayer, bet);

                    if (game.onlyOneChamberLeft()) {
                        DataHandler.modifyAccountValue(currentPlayer, DataHandler.getUserAmount(currentPlayer) * 2);
                        textChannel.sendMessage("You win! Your account value has been doubled.").queue();
                        DataHandler.endGame(textChannel);
                        DataHandler.incrementWins(currentPlayer);
                    }
                }

            }
        }
        else {
            textChannel.sendMessage("No game here.").queue();
        }
    }

    /**
     * Extracts the bet from the string, at exception default is used.
     * @param s String to parse
     * @return bet amount.
     */
    private int getBet(String s) {
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
    public String getCommandShortHand() {
        return String.format("%sp", RouletteBot.PREFIX);
    }
}