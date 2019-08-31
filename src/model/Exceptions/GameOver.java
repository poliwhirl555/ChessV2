package model.Exceptions;

public class GameOver extends InvalidMoveException {
    public GameOver(String winner) {
        super(winner);
    }

    public String getWinner() {
        return this.getMessage();
    }
}
