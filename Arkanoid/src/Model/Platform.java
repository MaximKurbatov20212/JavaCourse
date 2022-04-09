package Model;

import javax.swing.*;
import java.awt.*;

public class Platform extends JFrame  {
    public final static Platform INSTANCE = new Platform();
    private final int OFFSET = 10; // Moved OFFSET pixels after pressed key

    private final int RIGHT_CODE = 39;
    private final int LEFT_CODE = 37;

    private final int lenOfPlatform = 80;
    private final int heightOfPlatform = 13;

    private Image platformImage;
    private int positionX;
    private int positionY;

    private int direction; // 37 - left, 39 right, 0 - stand

    private void loadImage() {
        ImageIcon iplatform = new ImageIcon("src/Pictures/platform.png");
        platformImage = iplatform.getImage().getScaledInstance(lenOfPlatform, heightOfPlatform, Image.SCALE_DEFAULT);
    }

    private Platform() {
        loadImage();
        positionX = GameField.INSTANCE.getWidth() / 2;
        positionY = GameField.INSTANCE.getHeight() - HEIGHT - 50;
    }

    public void move(int direction) {
        if (direction == RIGHT_CODE && positionX + lenOfPlatform  < GameField.INSTANCE.getAreaWidth()) {
            positionX += OFFSET;
        }

        else if (direction == LEFT_CODE  && positionX > 10) {
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
        return lenOfPlatform;
    }

    public void setPosition(int i) {
        positionX = i;
    }
}
