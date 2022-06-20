//Zacharias Thorell

package game.currency;

import java.time.LocalDateTime;

public class Transaction {
    private final int value;
    private final LocalDateTime dateTime;

    public Transaction(int value, LocalDateTime dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }

    public int getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }
}
