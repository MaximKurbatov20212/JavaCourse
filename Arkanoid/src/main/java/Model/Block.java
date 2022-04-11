package Model;

import javax.swing.*;
import java.awt.*;

// Red - 3 lives
// Green - 2 lives
// Blue - 1 live
public class Block extends JFrame {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 80;
    private final int positionX;
    private final int positionY;
    private Image blockImage;

    private int numberOfLives;

    public Block(int x, int y) {
        positionX = x;
        positionY = y;

        switch((int)(Math.random() * 3)) {
            case 0: {
                blockImage = new ImageIcon("src/main/java/Pictures/blueBlock.jpg").getImage();
                numberOfLives = 3;
                break;
            }
            case 1: {
                blockImage = new ImageIcon("src/main/java/Pictures/greenBlock.jpg").getImage();
                numberOfLives = 2;
                break;
            }
            case 2: {
                blockImage = new ImageIcon("src/main/java/Pictures/redBlock.jpg").getImage();
                numberOfLives = 1;
            }
        }
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

    public Image getImage() {
        return blockImage;
    }
}
