package Controller;

import Model.*;
import View.Viewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameStateHandler implements ActionListener {
    public static final GameStateHandler INSTANCE = new GameStateHandler();

    // Conditions of Controller.GameStateHandler
    private final int GAME_IS_NOT_INIT = -1;
    private final int IN_GAME = 1;
    private final int IN_ABOUT_MENU = 3;
    private final int IN_HIGH_SCORES_MENU = 2;
    private final int IN_MAIN_MENU = 0;
    private final int STOP_THE_WORLD = 4;
    private boolean areYouWin = false;
    private boolean areYouLose = false;

    // Codes of > < symbols
    private final int RIGHT = 39;
    private final int LEFT = 37;
    private final int UP = 38;
    private final int DOWN = 40;
    private final int ENTER = 10;
    private final int ESC = 27;
    private final int EXIT = 4;

    private int condition;
    private Timer timer;

    private final MainMenu mainMenu = MainMenu.getInstance();

    private GameStateHandler() {
        condition = GAME_IS_NOT_INIT;
    }

    public void handleAction(int keyCode) {
        switch(condition) {
            case IN_MAIN_MENU: {
                switch (keyCode) {
                    case ENTER -> checkCursorPosition();
                    case UP -> cursorUp();
                    case DOWN -> cursorDown();
                }
                break;
            }
            case(IN_ABOUT_MENU):
            case(IN_HIGH_SCORES_MENU): {
                if(keyCode == ESC) goToMainMenu();
            }

            case(IN_GAME): {
                switch(keyCode) {
                    case ESC -> goToMainMenu();
                    case EXIT -> System.exit(0);
                    case RIGHT -> Platform.INSTANCE.move(RIGHT);
                    case LEFT -> Platform.INSTANCE.move(LEFT);
                }
                break;
            }
            case(STOP_THE_WORLD): {
                if((areYouWin || areYouLose) && (keyCode == ESC || keyCode == ENTER)) {
                    GameField.INSTANCE.setWinVisible(false);
                    GameField.INSTANCE.setLoseVisible(false);
                    areYouWin = false;
                    areYouLose = false;
                    goToMainMenu();
                }
            }
        }
    }

    private void checkCursorPosition() {
        int pos = MainMenu.getInstance().getCursorPosition();
        switch (pos) {
            case MainMenu.Cursor.NEW_GAME -> startGame();
            case MainMenu.Cursor.HIGH_SCORES -> goToHighScoresMenu();
            case MainMenu.Cursor.ABOUT -> goToAboutMenu();
            case MainMenu.Cursor.EXIT -> System.exit(0);
        }
    }

    private void cursorUp() {
        mainMenu.cursorUp();
    }

    private void cursorDown() {
        mainMenu.cursorDown();
    }

    public void initGame() {
        timer = new Timer(30, this);
        timer.start();
        condition = IN_MAIN_MENU;
        mainMenu.setCondition(MainMenu.IN_MAIN_MENU);
        mainMenu.setVisible(true);
    }

    public void startGame() {
        condition = IN_GAME;
        setStartPositions();
        Viewer.INSTANCE.repaint();
    }

    private void setStartPositions() {
        Ball.INSTANCE.setPosition(250, 300);
        Ball.INSTANCE.setDirectingVector(0,-1);

        Platform.INSTANCE.setPosition(210);

        GameField.INSTANCE.setScore(0);

        mainMenu.setCondition(MainMenu.IN_GAME);
        mainMenu.setVisible(false);

        BlockManager.INSTANCE.fillField();
    }

    public void goToAboutMenu() {
        condition = IN_ABOUT_MENU;
        mainMenu.setCondition(MainMenu.IN_ABOUT_MENU);
    }

    public void goToMainMenu() {
        condition = IN_MAIN_MENU;
        mainMenu.setCondition(MainMenu.IN_MAIN_MENU);
        mainMenu.setVisible(true);
    }

    public void goToHighScoresMenu() {
        condition = IN_HIGH_SCORES_MENU;
        mainMenu.setCondition(MainMenu.IN_HIGH_SCORES_MENU);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        field.makeMove();
//        view.repaint();
        Viewer.INSTANCE.repaint();
        if(condition != IN_GAME) return;
        if(BlockManager.INSTANCE.isAllBlocksDied()) {
            GameField.INSTANCE.setWinVisible(true);
            areYouWin = true;
            condition = STOP_THE_WORLD;
        }
        if(GameField.INSTANCE.getLoseVisible()) {
            GameField.INSTANCE.setLoseVisible(true);
            areYouLose = true;
            condition = STOP_THE_WORLD;
        }
        Ball.INSTANCE.move();
        changeDirectionIfBallHitsBlock();
    }

    private boolean changeDirectionIfBallHitsBlock() {
        Ball ball = Ball.INSTANCE;
        BlockManager blockManager = BlockManager.INSTANCE;
        for(int i = 0; i < BlockManager.COUNT; i++) {
            Block block = blockManager.getBlock(i);
            if(!block.isLife()) {
                continue;
            }
            if (ballHitsBlockBelow(ball, block)) {
                ball.setDirectingVector(ball.getDirectingVectorCordX(), -ball.getDirectingVectorCordY());
            } else if (ballHitsBlockAbove(ball, block)) {
                ball.setDirectingVector(ball.getDirectingVectorCordX(), -ball.getDirectingVectorCordY());
            } else if (ballHitsBlockRight(ball, block)) {
                ball.setDirectingVector(-ball.getDirectingVectorCordX(), ball.getDirectingVectorCordY());
            } else if (ballHitsBlockLeft(ball, block)) {
                ball.setDirectingVector(-ball.getDirectingVectorCordX(), ball.getDirectingVectorCordY());
            }
            else {
                continue;
            }
            blockManager.decreaseLives(block);
            if(!block.isLife()) {
                GameField.INSTANCE.increaseScore(100);
            }
            return true;
        }
        return false;
    }

    private boolean ballHitsBlockAbove(Ball ball, Block block) {
        if(     block.getPositionX() < ball.getPositionX() + Block.HEIGHT &&
                block.getPositionX() + Block.WIDTH > ball.getPositionX() &&
                block.getPositionY() - Ball.EPS < ball.getPositionY() + 2 * ball.getRadius() &&
                block.getPositionY() + Ball.EPS > ball.getPositionY() + 2 * ball.getRadius()) {
            ball.setPosition(ball.getPositionX(), ball.getPositionY() - 3);
            return true;
        }
        return false;
    }

    private boolean ballHitsBlockBelow(Ball ball, Block block) {
        if(block.getPositionX() < ball.getPositionX() + block.getHeight() &&
                block.getPositionX() + Block.WIDTH > ball.getPositionX() &&
                block.getPositionY() + Block.HEIGHT - Ball.EPS < ball.getPositionY() &&
                block.getPositionY() + Block.HEIGHT + Ball.EPS >= ball.getPositionY()) {
            ball.setPosition(ball.getPositionX(), ball.getPositionY() + 3);
            return true;
        }
        return false;
    }

    private boolean ballHitsBlockLeft(Ball ball, Block block) {
        if(     block.getPositionX() + Ball.EPS > ball.getPositionX() + ball.getRadius() &&
                block.getPositionX() <= ball.getPositionX() + ball.getRadius() &&
                block.getPositionY() < ball.getPositionY() + ball.getRadius() &&
                block.getPositionY() + Block.HEIGHT > ball.getPositionY()) {

            ball.setPosition(ball.getPositionX() - 3, ball.getPositionY());
            return true;
        }
        return false;
    }


    private boolean ballHitsBlockRight(Ball ball, Block block) {
        if(     block.getPositionX() + Block.WIDTH - Ball.EPS < ball.getPositionX() &&
                block.getPositionX() + Block.WIDTH  >= ball.getPositionX() &&
                block.getPositionY() < ball.getPositionY() + ball.getRadius() &&
                block.getPositionY() + Block.HEIGHT > ball.getPositionY()) {

            ball.setPosition(ball.getPositionX() + 3, ball.getPositionY());
            return true;
        }
        return false;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
