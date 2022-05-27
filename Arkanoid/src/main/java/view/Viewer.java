package view;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.*;

public class Viewer extends JFrame {
    public final static Viewer INSTANCE = new Viewer();
    private final Font statisticFont = new Font("Fixedsys Regular", Font.BOLD, 40);

    static GameCondition gameCondition;

    private Image ballImage;
    private Image mainMenuImage;
    private Image aboutMenuImage;
    private Image platformImage;
    private Image cursorImage;
    private Image winImage;
    private Image loseImage;
    private Image gameFieldImage;
    private Image redBlockImage;
    private Image blueBlockImage;
    private Image greenBlockImage;
    private Image registratorImage;
    private Image recordsImage;

    private BufferedImage currentFrame;

    private Viewer() {
        setSize(GameField.MAIN_AREA_WIDTH, GameField.HEIGHT);
        setLocation(400, 100);
        setResizable(false);
        setUndecorated(true);
        loadImages();
    }

    private void loadImages() {
        try {
            ballImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("ball.png"))).getScaledInstance(2 * Ball.RADIUS, 2 * Ball.RADIUS, Image.SCALE_DEFAULT);
            mainMenuImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Main1.jpg")));
            aboutMenuImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("About.jpg")));
            platformImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("platform.png"))).getScaledInstance(Platform.LEN_OF_PLATFORM, Platform.HEIGHT_OF_PLATFORM, Image.SCALE_DEFAULT);;
            cursorImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("cursor.jpg")));
            winImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("win.png")));
            loseImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("lose.png")));
            gameFieldImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("FieldBlack.jpg")));
            redBlockImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("redBlock.jpg")));
            greenBlockImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("greenBlock.jpg")));
            blueBlockImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("blueBlock.jpg")));
            registratorImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Registrator.jpg")));
            recordsImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Records.jpg")));
            currentFrame = new BufferedImage(GameField.MAIN_AREA_WIDTH, GameField.HEIGHT, BufferedImage.TYPE_INT_RGB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addGameCondition(GameCondition gameCondition) {
        Viewer.gameCondition = gameCondition;
    }

    @Override
    public void paint(Graphics gg) {
        Graphics g = currentFrame.getGraphics();

        Condition condition = gameCondition.condition();

        switch (condition) {
            case IN_GAME, END_GAME -> {
                drawGameField(g);
                for(GameObject gameObject : gameCondition.gameObjects()) {
                    switch (gameObject.objectType()) {
                        case PLATFORM -> drawPlatform(g, gameObject);
                        case BALL -> drawBall(g, gameObject);
                        case BLOCK -> drawBlock(g, gameObject);
                        case ROUND -> drawRound(g, gameObject);
                        case SCORE -> drawScore(g, gameObject);
                        case YOU_LOSE -> drawLose(g);
                        case YOU_WIN -> drawWin(g);
                        default -> throw new RuntimeException("Invalid GameObject" + gameObject.getClass());
                    }
                }
            }
            case IN_MAIN_MENU -> {
                drawMainMenu(g);
                for(GameObject gameObject : gameCondition.gameObjects()) {
                    if (gameObject.objectType() == ObjectType.CURSOR) {
                        drawCursor(g, gameObject);
                    } else {
                        throw new RuntimeException("Invalid GameObject" + gameObject.getClass());
                    }
                }
            }
            case IN_ABOUT_MENU -> drawAboutMenu(g);
            case IN_HIGH_SCORES -> drawHighScoresMenu(g, gameCondition.gameObjects().get(0));
            case IN_REGISTRATION -> drawRegistrator(g);
        }
        gg.drawImage(currentFrame,0,0, this);
    }
    private void drawGameField(Graphics g) {
        g.drawImage(gameFieldImage, 0, 0, this);
    }

    private void drawBall(Graphics g, GameObject gameObject) {
        g.drawImage(ballImage, gameObject.x(), gameObject.y(), this);
    }

    private void drawMainMenu(Graphics g) {
        g.drawImage(mainMenuImage, 0, 0, this);
    }

    private void drawPlatform(Graphics g, GameObject gameObject) {
        g.drawImage(platformImage, gameObject.x(), gameObject.y(), this);
    }

    private void drawCursor(Graphics g, GameObject gameObject) {
        g.drawImage(cursorImage, gameObject.x(), gameObject.y(), this);
    }

    private void drawRound(Graphics g, GameObject gameObject) {
        g.setColor(Color.red);
        g.setFont(statisticFont);
        g.drawString(gameObject.value(), gameObject.x(), gameObject.y());
    }

    private void drawScore(Graphics g, GameObject gameObject) {
        g.setColor(Color.red);
        g.setFont(statisticFont);
        g.drawString(gameObject.value(), gameObject.x(), gameObject.y());
    }

    private void drawAboutMenu(Graphics g) {
        g.drawImage(aboutMenuImage, 0, 0, this);
    }

    private void drawRegistrator(Graphics g) {
        g.drawImage(registratorImage, 0, 0, this);
    }

    private void drawLose(Graphics g) {
        g.drawImage(loseImage, 0, 0, this);
    }

    private void drawWin(Graphics g) {
        g.drawImage(winImage, 0, 0, this);
    }

    private void drawHighScoresMenu(Graphics g, GameObject gameObject) {
        g.drawImage(recordsImage, 0, 0, this);
        g.setFont(statisticFont);
        g.setColor(Color.red);
        g.drawString(gameObject.value(), gameObject.x(), gameObject.y());
    }

    private void drawBlock(Graphics g, GameObject gameObject) {
        switch (gameObject.value()) {
            case "1" -> g.drawImage(redBlockImage, gameObject.x(), gameObject.y(), this);
            case "2" -> g.drawImage(greenBlockImage, gameObject.x(), gameObject.y(), this);
            case "3" -> g.drawImage(blueBlockImage, gameObject.x(), gameObject.y(), this);
            default -> throw new RuntimeException("Too many lives");
        }
    }
}
