package Model;

import javax.swing.*;

public class Platform extends JFrame  {
    public final static Platform INSTANCE = new Platform();
    private static final int OFFSET = 5; // Moved OFFSET pixels after pressed key

    private static final int RIGHT_CODE = 39;
    private static final int LEFT_CODE = 37;

    public static final int LEN_OF_PLATFORM = 80;
    public static final int HEIGHT_OF_PLATFORM = 13;

    private int positionX;
    private final int positionY;

    private final Direction direction = Direction.STAND;

    private Platform() {
        positionX = BackField.INSTANCE.getWidth() / 2;
        positionY = BackField.INSTANCE.getHeight() - HEIGHT - 50;
    }

    enum Direction {
        LEFT,
        RIGHT,
        STAND
    }

    public void move(int keyCode) {
        Direction direction = interpretDirection(keyCode);
        if(direction == null) return;

        if (direction == Direction.RIGHT && positionX + LEN_OF_PLATFORM < BackField.INSTANCE.getAreaWidth()) {
            positionX += OFFSET;
        }

        else if (direction == Direction.LEFT  && positionX > BackField.INSTANCE.getWallWidth()) {
            positionX -= OFFSET;
        }
    }


    private Direction interpretDirection(int keyCode) {
        switch (keyCode) {
            case RIGHT_CODE -> {
                return Direction.RIGHT;
            }
            case LEFT_CODE -> {
                return Direction.LEFT;
            }
            case 0 -> {
                return Direction.STAND;
            }
            default -> {
                return null;
            }
        }
    }

    public void setPosition(int i) {
        positionX = i;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
