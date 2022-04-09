package View;

import Controller.GameController;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class Viewer extends JFrame {
    public final static Viewer INSTANCE = new Viewer();

    private final Platform platform = Platform.INSTANCE;
    private final GameController gf = GameController.INSTANCE;
    private final MainMenu mainMenu = MainMenu.getInstance();
    private final GameField gameField = GameField.INSTANCE;
    private final Ball ball = Ball.INSTANCE;
    private final BlockManager blockManager = BlockManager.INSTANCE;
    private final Font font = new Font("Fixedsys Regular", Font.BOLD, 40);

    private Viewer() {
        setSize(gameField.getWidth(), gameField.getHeight());
        setLocation(400, 100);
        setResizable(false);
        setUndecorated(true);
        addKeyListener(GameController.INSTANCE);
        setVisible(true);
    }

    private void drawBlocks(Graphics g) {
        for (int i = 0; i < BlockManager.COUNT; i++) {
            Block block = blockManager.getBlock(i);
            if(blockManager.getBlock(i).isLife()) {
                g.drawImage(block.getImage(), block.getPositionX(), block.getPositionY(), this);
            }
        }
    }

    public void paint(Graphics g) {
        if (mainMenu.isVisible()) {
            switch (mainMenu.getCondition()) {
                case MainMenu.IN_HIGH_SCORES_MENU: {
                    g.setFont(font);
                    g.setColor(Color.black);
                    g.fillRect(0,0,gameField.getWidth(), gameField.getHeight());
                    drawHighScoresTable(g, mainMenu.getHighScoresTable());
                    break;
                }
                case MainMenu.IN_MAIN_MENU: {
                    g.drawImage(mainMenu.getMainMenuImage(), 0, 0, this);
                    g.drawImage(mainMenu.getCursorImage(), 150,180 + mainMenu.getCursorPosition() * 68,this);
                    break;
                }
                case MainMenu.IN_ABOUT_MENU: {
                    g.drawImage(mainMenu.getAboutMenuImage(), 0, 0, this);
                }
            }
        }
        else if(gameField.getWinVisible()) {
            g.drawImage(gameField.getWinImage(), 0,0,this);
        }
        else if(gameField.getLoseVisible()) {
            g.drawImage(gameField.getLoseImage(), 0,0,this);
        }
        else {
            g.setFont(font);
            g.setColor(Color.red);
            g.drawImage(gameField.getImage(), 0, 0, this);
            drawBlocks(g);
            g.drawImage(ball.getImage(), ball.getPositionX(), ball.getPositionY(), this);
            g.drawImage(platform.getImage(), platform.getPositionX(), platform.getPositionY(), this);
            g.drawString(gameField.getScore().toString(), 550, 270);
        }
    }

    private void drawHighScoresTable(Graphics g, String table) {
        String[] a = table.split("\n");
        g.setColor(Color.white);
        for(int i = 0; i < a.length; i++) {
            g.drawString(a[i], 100, i * getFontMetrics(font).getHeight());
        }
    }
}
