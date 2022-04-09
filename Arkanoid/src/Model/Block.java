package Model;

import javax.swing.*;
import java.awt.*;

// Red - 3 lives
// Green - 2 lives
// Blue - 1 live
public class Block extends JFrame {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 80;
    private int positionX;
    private int positionY;
    private boolean isLife;
    private Image blockImage;

    private Color color;
    private int numberOfLives;

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Block(int X, int Y) {
        positionX = X;
        positionY = Y;
        isLife = true;

        switch((int)(Math.random() * 3)) {
            case 0: {
                blockImage = new ImageIcon("src/Pictures/blueBlock.jpg").getImage();
                numberOfLives = 3;
                break;
            }
            case 1: {
                blockImage = new ImageIcon("src/Pictures/greenBlock.jpg").getImage();
                numberOfLives = 2;
                break;
            }
            case 2: {
                blockImage = new ImageIcon("src/Pictures/redBlock.jpg").getImage();
                numberOfLives = 1;
            }
        }
    }

    public void setLife(boolean a) {
        isLife = a;
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

    public Color getColor() {
        return color;
    }

    public void decreaseLives() {
        numberOfLives--;
    }

    public Image getImage() {
        return blockImage;
    }
}
