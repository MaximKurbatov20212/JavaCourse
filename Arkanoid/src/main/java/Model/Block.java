package Model;

import javax.swing.*;

public class Block extends JFrame {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 80;
    private final int positionX;
    private final int positionY;

    private int numberOfLives;

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

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public boolean isLife() {
        return numberOfLives > 0;
    }

    public void decreaseLives() {
        numberOfLives--;
    }

    public int getLives() {
        return numberOfLives;
    }
}
