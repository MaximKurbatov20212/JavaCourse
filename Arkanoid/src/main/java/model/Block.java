package Model;


public class Block {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 80;
    private final int positionX;
    private final int positionY;

    private int numberOfLives;

    // CR: remove
    public Block(int x, int y) {
        positionX = x;
        positionY = y;

        switch ((int) (Math.random() * 3)) {
            case 0 -> numberOfLives = 3;
            case 1 -> numberOfLives = 2;
            case 2 -> numberOfLives = 1;
        }
    }

    public Block(int x, int y, int lives) {
        positionX = x;
        positionY = y;
        this.numberOfLives = lives;
    }

//    HitType hit(double x, double y) {
//        return HitType.LEFT;
//    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public boolean isAlive() {
        return numberOfLives > 0;
    }

    public void decreaseLives() {
        numberOfLives--;
    }

    // CR: remove
    public int getLives() {
        return numberOfLives;
    }
}
