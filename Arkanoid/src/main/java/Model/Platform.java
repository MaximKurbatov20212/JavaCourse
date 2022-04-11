package Model;

import javax.swing.*;
import java.awt.*;

public class Platform extends JFrame  {
    public final static Platform INSTANCE = new Platform();
    private static final int OFFSET = 10; // Moved OFFSET pixels after pressed key

    private static final int RIGHT_CODE = 39;
    private static final int LEFT_CODE = 37;

    private static final int LEN_OF_PLATFORM = 80;
    private static final int HEIGHT_OF_PLATFORM = 13;

    private Image platformImage;
    private int positionX;
    private final int positionY;

    private int direction; // 37 - left, 39 right, 0 - stand

    private void loadImage() {
        ImageIcon iplatform = new ImageIcon("src/main/java/Pictures/platform.png");
        platformImage = iplatform.getImage().getScaledInstance(LEN_OF_PLATFORM, HEIGHT_OF_PLATFORM, Image.SCALE_DEFAULT);
    }

    // CR: create in GameField
    private Platform() {
        loadImage();
        positionX = GameField.INSTANCE.getWidth() / 2;
        positionY = GameField.INSTANCE.getHeight() - HEIGHT - 50;
    }

//    CR:
//    enum Direction {
//        LEFT,
//        RIGHT
//    }
    public void move(int direction) {
        if (direction == RIGHT_CODE && positionX + LEN_OF_PLATFORM < GameField.INSTANCE.getAreaWidth()) {
            positionX += OFFSET;
        }

        else if (direction == LEFT_CODE  && positionX > GameField.INSTANCE.getWallWidth()) {
            positionX -= OFFSET;
        }
    }

    public Image getImage() {
        return platformImage;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getDirection() {
        return direction;
    }

    public int getLen() {
        return LEN_OF_PLATFORM;
    }

    public void setPosition(int i) {
        positionX = i;
    }
}
