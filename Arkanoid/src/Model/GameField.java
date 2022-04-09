package Model;

import javax.swing.*;
import java.awt.*;

public class GameField extends JPanel {
    public final static GameField INSTANCE = new GameField();
    private static final int WALL_WIDTH = 10;
    private static final int WIDTH = 700;
    private static final int HEIGHT = 550;
    private static final int BALL_AREA_WIDTH = 490;

    private Integer score = 0;
    private Image backGroundImage;
    private Image winImage;
    private Image loseImage;

    private boolean isWinVisible = false;
    private boolean isLoseVisible = false;

    public Image getWinImage() {
        return winImage;
    }

    public Image getLoseImage() {
        return loseImage;
    }

    public void setWinVisible(boolean winVisible) {
        isWinVisible = winVisible;
    }

    public void setLoseVisible(boolean loseVisible) {
        isLoseVisible = loseVisible;
    }

    public boolean getWinVisible() {
        return isWinVisible;
    }

    public boolean getLoseVisible() {
        return isLoseVisible;
    }

    private GameField() {
        loadImage();
    }

    public void increaseScore(int i) {
        score += i;
    }

    private void loadImage() {
        ImageIcon backGround = new ImageIcon("src/Pictures/Field.jpg");
        backGroundImage = backGround.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon iWin = new ImageIcon("src/Pictures/win.png");
        winImage = iWin.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon iLose = new ImageIcon("src/Pictures/lose.png");
        loseImage = iLose.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
    }

    public Image getImage() {
        return backGroundImage;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getAreaWidth() {
        return BALL_AREA_WIDTH;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int i) {
        score = i;
    }

    public int getWallWidth() {
        return WALL_WIDTH;
    }
}
