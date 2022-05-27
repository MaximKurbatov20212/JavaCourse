package model;

public class Block {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 80;
    public static final double EPS = 5;

    private final GameField.Position position;

    public int numberOfLives;

    public Block(int x, int y, int lives) {
        position = new GameField.Position(x, y);
        this.numberOfLives = lives;
    }

    public GameField.Position getPosition() {
        return position;
    }

    public boolean isAlive() {
        return numberOfLives > 0;
    }

    public boolean decreaseLives() {
        numberOfLives--;
        return isAlive();
    }
}
