package View;

import Model.*;
import Model.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Viewer extends JFrame {
    public final static Viewer INSTANCE = new Viewer();

    private final Platform platform = Platform.INSTANCE;
    private final Menu mainMenu = Menu.INSTANCE;
    private final GameField gameField = GameField.INSTANCE;
    private final Ball ball = Ball.INSTANCE;
    private final BlockManager blockManager = BlockManager.INSTANCE;
    private final Font font = new Font("Fixedsys Regular", Font.PLAIN, 40);
    private final Font statisticFont = new Font("Fixedsys Regular", Font.BOLD, 40);

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

    private Image currentFrame;

    private Viewer() {
        setSize(BackField.WIDTH, BackField.HEIGHT);
        setLocation(400, 100);
        setResizable(false);
        setUndecorated(true);
        loadImages();
    }

    private void loadImages() {
        ballImage = new ImageIcon("src/main/java/Pictures/ball.png").getImage().getScaledInstance(2 * Ball.RADIUS, 2 * Ball.RADIUS,  Image.SCALE_DEFAULT);
        mainMenuImage = new ImageIcon("src/main/java/Pictures/Main1.jpg").getImage();
        aboutMenuImage = new ImageIcon("src/main/java/Pictures/About.jpg").getImage();
        platformImage = new ImageIcon("src/main/java/Pictures/platform.png").getImage().getScaledInstance(Platform.LEN_OF_PLATFORM, Platform.HEIGHT_OF_PLATFORM, Image.SCALE_DEFAULT);
        cursorImage = new ImageIcon("src/main/java/Pictures/cursor.jpg").getImage();
        winImage = new ImageIcon("src/main/java/Pictures/win.png").getImage();
        loseImage = new ImageIcon("src/main/java/Pictures/lose.png").getImage();
        gameFieldImage = new ImageIcon("src/main/java/Pictures/FieldBlack.jpg").getImage();
        redBlockImage = new ImageIcon("src/main/java/Pictures/redBlock.jpg").getImage();
        greenBlockImage = new ImageIcon("src/main/java/Pictures/greenBlock.jpg").getImage();
        blueBlockImage = new ImageIcon("src/main/java/Pictures/blueBlock.jpg").getImage();
        registratorImage = new ImageIcon("src/main/java/Pictures/Registrator.jpg").getImage();
        recordsImage = new ImageIcon("src/main/java/Pictures/Records.jpg").getImage();
        currentFrame = new BufferedImage(BackField.WIDTH, BackField.HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    private void drawBlocks(Graphics g) {
        Arrays.stream(blockManager.getBlocks())
                .filter(Block::isLife)
                .forEach(block -> g.drawImage(getBlockImage(block), block.getPositionX(), block.getPositionY(), this));
    }

    private Image getBlockImage(Block block) {
        return switch (block.getLives()) {
            case 1 -> redBlockImage;
            case 2 -> greenBlockImage;
            case 3 -> blueBlockImage;
            default -> null;
        };
    }

    @Override
    public void paint(Graphics gg) {
        Graphics g = currentFrame.getGraphics();
        if (mainMenu.isVisible()) {
            switch (mainMenu.getCondition()) {
                case Menu.IN_REGISTRATION -> drawRegistrator(g);
                case Menu.IN_HIGH_SCORES_MENU -> drawHighScoresMenu(g);
                case Menu.IN_MAIN_MENU -> drawMainMenu(g);
                case Menu.IN_ABOUT_MENU -> drawAboutMenu(g);
            }
        }

        else if(gameField.PlayerWin()) {
            g.drawImage(winImage, 0,0,this);
        }
        else if(gameField.ComputerWin()) {
            g.drawImage(loseImage, 0,0,this);
        }
        else {
            drawGameField(g);
        }
        gg.drawImage(currentFrame,0,0, this);
    }

    private void drawAboutMenu(Graphics g) {
        g.drawImage(aboutMenuImage, 0, 0, this);
    }

    private void drawRegistrator(Graphics g) {
        g.drawImage(registratorImage, 0, 0, this);
    }

    private void drawGameField(Graphics g) {
        g.setFont(statisticFont);
        g.setColor(Color.red);
        g.drawImage(gameFieldImage, 0, 0, this);
        drawBlocks(g);
        g.drawImage(ballImage, (int)ball.getPositionX(), (int)ball.getPositionY(), this);
        g.drawImage(platformImage, platform.getPositionX(), platform.getPositionY(), this);
        g.drawString(GameField.INSTANCE.getScore().toString(), 550, 270);
        g.drawString(((Integer)gameField.getRound()).toString(), 580, 530);
    }

    private void drawMainMenu(Graphics g) {
        g.drawImage(mainMenuImage, 0, 0, this);
        g.drawImage(cursorImage, 150, 180 + mainMenu.getCursorPosition() * 68, this);
    }

    private void drawHighScoresMenu(Graphics g) {
        g.drawImage(recordsImage, 0, 0, this);
        g.setFont(font);
        g.setColor(Color.black);
        drawHighScoresTable(g, (ArrayList<Registrator.Note>) Registrator.table);
    }

    // Draw less 8 top players
    private void drawHighScoresTable(Graphics g, ArrayList<Registrator.Note> table) {
        AtomicInteger i = new AtomicInteger(1);
        g.setColor(Color.white);

        table.stream()
                .sorted((n1, n2) -> n2.score().compareTo(n1.score()))
                .toList()
                .subList(0, Math.min(table.size(), 8))
                .forEach(note -> g.drawString(i.toString() + ") " + note.name() + " : " + note.score(), 100 , 100 + (i.getAndIncrement()) * getFontMetrics(font).getHeight()));
    }
}
