//Zacharias Thorell

package game;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a individual game.
 */
public class Game {
    private final static int PLAYER_LIMIT = 3;
    private final static int CYLINDER_SIZE = 6;
    private final List<User> players = new ArrayList<>();

    private User leader;
    private TypeOfGame type = TypeOfGame.UNDECLARED;

    private boolean status;
    private int current;
    private int clicksUntilFire;

    public Game(User leader) {
        this.leader = leader;
        players.add(leader);
        current = 0;
        clicksUntilFire = new Random().nextInt(CYLINDER_SIZE + 1);
    }

    public boolean addPlayer(User player) {
        if (players.size() == PLAYER_LIMIT) {
            return false;
        }
        else {
            players.add(player);
            return true;
        }
    }

    public void dropPlayer(User player) {
        players.remove(player);

        if (type == TypeOfGame.SINGLE) {
            status = false;
        }
        else if (type == TypeOfGame.MULTIPLE && players.size() == 1) {
            status = false;
        }
    }

    public boolean pullTrigger() {
        current++;

        if (current > players.size()) {
            current = 0;
        }

        return --clicksUntilFire == 0;
    }

    public List<User> getPlayers() {
        return players;
    }

    public boolean contains(User user) {
        for (User player : players) {
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
        if (current >= players.size()) {
            current = 0;
        }

        return players.get(current);
    }

    public String newLeader() {
        leader = players.get(0);
        return leader.getAsMention();
    }

    public boolean isLive() {
        return status;
    }

    public boolean onlyOneChamberLeft() {
        return clicksUntilFire == 1;
    }

    public void goLive() {
        status = true;
        if (players.size() > 1) {
            type = TypeOfGame.MULTIPLE;
        }
        else {
            type = TypeOfGame.SINGLE;
        }
    }
}
