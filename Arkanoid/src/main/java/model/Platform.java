package Model;


public class Platform {
    public final static Platform INSTANCE = new Platform();
    public static final int LEN_OF_PLATFORM = 80;
    public static final int HEIGHT_OF_PLATFORM = 13;
    private static final int OFFSET = 5; // Moved OFFSET pixels after pressed key
    private static final int RIGHT_CODE = 39;
    private static final int LEFT_CODE = 37;

    private final Direction direction = Direction.STAND;

    private final int positionY;
    private int positionX;

    private Platform() {
        positionX = BackField.INSTANCE.getWidth() / 2;
        positionY = BackField.INSTANCE.getHeight() - GameField.HEIGHT - 50;
    }

//    HitType hit(double x, double y) {
//        return null;
////        return HitType.UP;
//    }

    // CR: pass enum
    public void move(int keyCode) {
        Direction direction = interpretDirection(keyCode);
        if (direction == null) return;

        if (direction == Direction.RIGHT && positionX + LEN_OF_PLATFORM < BackField.INSTANCE.getAreaWidth()) {
            positionX += OFFSET;
        } else if (direction == Direction.LEFT && positionX > BackField.INSTANCE.getWallWidth()) {
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

    // CR: pass in ctor
    public void setPosition(int i) {
        positionX = i;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    enum Direction {
        LEFT,
        RIGHT,
        STAND
    }
}
