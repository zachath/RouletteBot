//Zacharias Thorell

package game;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a individual game.
 */
public class Game {
    private final static int PLAYER_LIMIT = 3;
    private final static int CYLINDER_SIZE = 6;
    private final List<User> activePlayers = new ArrayList<>(); //The active players of the game, when a player dies or leave they are removed.
    private List<User> allPlayers; //All players, is not modified after game start.

    private User leader;
    private TypeOfGame type = TypeOfGame.UNDECLARED;

    private boolean status;
    private int current;
    private int clicksUntilFire;

    private final String id;
    private User winner;
    private int bets;
    private int pulls;
    private int totalBetsValue;
    private final LocalDateTime dateTime;

    public Game(User leader, TextChannel textChannel) {
        this.leader = leader;
        activePlayers.add(leader);
        current = 0;
        clicksUntilFire = new Random().nextInt(CYLINDER_SIZE + 1);
        //clicksUntilFire = 6; //Use for debug.
        this.dateTime = LocalDateTime.now();
        id = textChannel.getId() + dateTime;
    }

    /**
     * Adds a player to the game. Returns false if game is already full, otherwise true.
     * @param player User to add to game.
     * @return if user was added.
     */
    public boolean addPlayer(User player) {
        if (activePlayers.size() == PLAYER_LIMIT) {
            return false;
        }
        else {
            activePlayers.add(player);
            return true;
        }
    }

    /**
     * Removes a player from the game, game is ended if all have left.
     * @param player User to remove.
     */
    public void dropPlayer(User player) {
        activePlayers.remove(player);

        if (type == TypeOfGame.SINGLE) {
            status = false;
        }
        else if (type == TypeOfGame.MULTIPLE && activePlayers.size() == 1) {
            status = false;
        }
    }

    /**
     * Main game mechanic, clickUntilFire is decremented and compared to 0, if true a bullet is said to be fired.
     * @return if "bullet is fire or not."
     */
    public boolean pullTrigger() {
        pulls++;
        current++;

        if (current > activePlayers.size()) {
            current = 0;
        }

        return --clicksUntilFire == 0;
    }

    /**
     * @return the active players of the game.
     */
    public List<User> getActivePlayers() {
        return activePlayers;
    }

    /**
     * @return all players that was in the game at start.
     */
    public List<User> getAllPlayers() {
        return allPlayers;
    }

    /**
     * Checks if the specified user is in the game or not.
     * @param user User to check.
     * @return If the user is in the game or not.
     */
    public boolean contains(User user) {
        for (User player : activePlayers) {
            if (player.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public User getLeader() {
        return leader;
    }

    public TypeOfGame getType() {
        return type;
    }

    /**
     * Returns the current player, if current is out of bounds it is reset.
     * @return the current player.
     */
    public User getCurrentPlayer() {
        if (current >= activePlayers.size()) {
            current = 0;
        }

        return activePlayers.get(current);
    }

    /**
     * Sets new leader for the game, will be the one who joined after last leader.
     * @return the discord mention of the new leader.
     */
    public String newLeader() {
        leader = activePlayers.get(0);
        return leader.getAsMention();
    }

    /**
     * @return status of game.
     */
    public boolean isLive() {
        return status;
    }

    /**
     * If only 1 chamber is left to fire, then one "wins".
     * @return if all but one cylinder has been fired.
     */
    public boolean onlyOneChamberLeft() {
        return pulls == CYLINDER_SIZE - 1;
    }

    /**
     * Starts the game.
     */
    public void goLive() {
        status = true;
        if (activePlayers.size() > 1) {
            type = TypeOfGame.MULTIPLE;
        }
        else {
            type = TypeOfGame.SINGLE;
        }
        allPlayers = new ArrayList<>(activePlayers);
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public void addBet(int bet) {
        totalBetsValue += bet;
        bets++;
    }

    //Various getters.

    public User getWinner() {
        return winner;
    }

    public String getId() {
        return id;
    }

    public int getBets() {
        return bets;
    }

    public int getTotalBetsValue() {
        return totalBetsValue;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getPulls() {
        return pulls;
    }
}
