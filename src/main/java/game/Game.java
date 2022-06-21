//Zacharias Thorell

package game;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a individual game.
 */
public class Game {
    private final static int PLAYER_LIMIT = 3;
    private final static int CYLINDER_SIZE = 6;
    private final List<Member> players = new ArrayList<>();

    private final TextChannel textChannel;
    private final Guild guild;
    private Member leader;
    private TypeOfGame type = TypeOfGame.UNDECLARED;

    //Used for timing out players?
    private final long cTime;

    private boolean status;
    private int current;
    private int clicksUntilFire;

    public Game(Member leader, Guild guild, TextChannel textChannel) {
        this.leader = leader;
        players.add(leader);
        this.textChannel = textChannel;
        this.guild = guild;
        current = 0;
        cTime = System.currentTimeMillis();
        clicksUntilFire = new Random().nextInt(CYLINDER_SIZE + 1);
    }

    public boolean addPlayer(Member player) {
        if (players.size() == PLAYER_LIMIT) {
            return false;
        }
        else {
            players.add(player);
            return true;
        }
    }

    public void dropPlayer(Member player) {
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

    public List<Member> getPlayers() {
        return players;
    }

    public boolean contains(Member member) {
        for (Member player : players) {
            if (player.equals(member)) {
                return true;
            }
        }
        return false;
    }

    public Member getLeader() {
        return leader;
    }

    public TypeOfGame getType() {
        return type;
    }

    public Member getCurrentPlayer() {
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

    public boolean isFinished() {
        return clicksUntilFire == 0;
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
