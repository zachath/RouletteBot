//Zacharias Thorell

package game;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: Comment this file.

/**
 * Represents a individual game.
 */
public class Game {
    private final static int PLAYER_LIMIT = 3;
    private final static int CYLINDER_SIZE = 6;
    private final List<User> activePlayers = new ArrayList<>();
    private List<User> allPlayers;

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

    public boolean addPlayer(User player) {
        if (activePlayers.size() == PLAYER_LIMIT) {
            return false;
        }
        else {
            activePlayers.add(player);
            return true;
        }
    }

    public void dropPlayer(User player) {
        activePlayers.remove(player);

        if (type == TypeOfGame.SINGLE) {
            status = false;
        }
        else if (type == TypeOfGame.MULTIPLE && activePlayers.size() == 1) {
            status = false;
        }
    }

    public boolean pullTrigger() {
        pulls++;
        current++;

        if (current > activePlayers.size()) {
            current = 0;
        }

        return --clicksUntilFire == 0;
    }

    public List<User> getActivePlayers() {
        return activePlayers;
    }

    public List<User> getAllPlayers() {
        return allPlayers;
    }

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

    public User getCurrentPlayer() {
        if (current >= activePlayers.size()) {
            current = 0;
        }

        return activePlayers.get(current);
    }

    public String newLeader() {
        leader = activePlayers.get(0);
        return leader.getAsMention();
    }

    public boolean isLive() {
        return status;
    }

    public boolean onlyOneChamberLeft() {
        return pulls == CYLINDER_SIZE - 1;
    }

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
