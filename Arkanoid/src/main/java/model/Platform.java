package model;

public class Platform {
    public static final int LEN_OF_PLATFORM = 80;
    public static final int HEIGHT_OF_PLATFORM = 13;
    private static final int SPEED = 5;

    private final int positionY = 500;
    private int positionX;

    public Platform() {
        positionX = GameField.WIDTH / 2;
    }

    public void move(Direction direction) {
        if (direction == null) return;
        if (direction == Direction.RIGHT && positionX + LEN_OF_PLATFORM < GameField.WIDTH) {
            positionX += SPEED;
        } else if (direction == Direction.LEFT && positionX > 10) {
            positionX -= SPEED;
        }
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public enum Direction {
        LEFT,
        RIGHT,
    }
}
