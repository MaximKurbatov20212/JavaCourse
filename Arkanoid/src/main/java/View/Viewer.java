package View;

import Model.*;
import Model.Menu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Viewer extends JFrame {
    public final static Viewer INSTANCE = new Viewer();

    private final Platform platform = Platform.INSTANCE;
    private final Menu mainMenu = Menu.INSTANCE;
    private final GameField gameField = GameField.INSTANCE;
    private final Ball ball = Ball.INSTANCE;
    private final BlockManager blockManager = BlockManager.INSTANCE;
    private final Font font = new Font("Fixedsys Regular", Font.BOLD, 40);

    private Image ballImage;
    private Image mainMenuImage;
    private Image aboutMenuImage;
    private Image platformImage;
    private Image cursorImage;
    private Image winImage;
    private Image loseImage;
    private Image gameFieldImage;

    private Viewer() {
        setSize(BackField.WIDTH, BackField.HEIGHT);
        setLocation(400, 100);
        setResizable(false);
        setUndecorated(true);
        loadImages();
    }

    private void loadImages() {
        ballImage = new ImageIcon("src/main/java/Pictures/ball.png").getImage().getScaledInstance(20,20,  Image.SCALE_DEFAULT);
        mainMenuImage = new ImageIcon("src/main/java/Pictures/Main1.jpg").getImage();
        aboutMenuImage = new ImageIcon("src/main/java/Pictures/About.jpg").getImage();
        platformImage = new ImageIcon("src/main/java/Pictures/platform.png").getImage().getScaledInstance(Platform.LEN_OF_PLATFORM, Platform.HEIGHT_OF_PLATFORM, Image.SCALE_DEFAULT);
        cursorImage = new ImageIcon("src/main/java/Pictures/cursor.jpg").getImage();
        winImage = new ImageIcon("src/main/java/Pictures/win.png").getImage();
        loseImage = new ImageIcon("src/main/java/Pictures/lose.png").getImage();
        gameFieldImage = new ImageIcon("src/main/java/Pictures/FieldBlack.jpg").getImage();
    }

    private void drawBlocks(Graphics g) {
        for (int i = 0; i < BlockManager.COUNT; i++) {
            Block block = blockManager.getBlock(i);
            if(blockManager.getBlock(i).isLife()) {
                g.drawImage(block.getImage(), block.getPositionX(), block.getPositionY(), this);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (mainMenu.isVisible()) {
            switch (mainMenu.getCondition()) {
                case Menu.IN_REGISTRATION -> {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, BackField.WIDTH, BackField.HEIGHT);
                }

                case Menu.IN_HIGH_SCORES_MENU -> {
                    g.setFont(font);
                    g.setColor(Color.black);
                    g.fillRect(0, 0, BackField.INSTANCE.getWidth(), gameField.getHeight());
                    drawHighScoresTable(g, (ArrayList<Registrator.Note>) Registrator.table);
                }

                case Menu.IN_MAIN_MENU -> {
                    g.drawImage(mainMenuImage, 0, 0, this);
                    g.drawImage(cursorImage, 150, 180 + mainMenu.getCursorPosition() * 68, this);
                }
                case Menu.IN_ABOUT_MENU -> g.drawImage(aboutMenuImage, 0, 0, this);
            }
        }

        else if(gameField.PlayerWin()) {
            g.drawImage(winImage, 0,0,this);
        }
        else if(gameField.ComputerWin()) {
            g.drawImage(loseImage, 0,0,this);
        }
        else {
            g.setFont(font);
            g.setColor(Color.red);


            g.drawImage(gameFieldImage, 0, 0, this);
            g.drawImage(ballImage, (int)ball.getPositionX(), (int)ball.getPositionY(), this);
            g.drawImage(platformImage, platform.getPositionX(), platform.getPositionY(), this);
            g.drawString(BackField.INSTANCE.getScore().toString(), 550, 270);
            drawBlocks(g);

            g.setColor(Color.white);
            g.fillRect(400, 450, 100, 100);
        }
    }

    private void drawHighScoresTable(Graphics g, ArrayList<Registrator.Note> table) {
        AtomicInteger i = new AtomicInteger(1);
        g.setColor(Color.red);

        table.stream()
                .sorted((n1, n2) -> n2.score().compareTo(n1.score()))
                .forEach(note -> g.drawString(note.name() + " : " + note.score(), 100 , (i.getAndIncrement()) * getFontMetrics(font).getHeight()));
    }
}
