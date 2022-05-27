package controller;

import model.Menu;
import model.Platform;
import model.Winner;
import view.Viewer;

import model.ArkanoidFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.Condition.*;

public class GameStateHandler implements ActionListener {
    public static final GameStateHandler INSTANCE = new GameStateHandler();

    // Codes of > < symbols
    private final int RIGHT = 39;
    private final int LEFT = 37;
    private final int UP = 38;
    private final int DOWN = 40;
    private final int ENTER = 10;
    private final int ESC = 27;
    private final int EXIT = 4;

    public static final double gameDelay = 15;
    public static final double menuDelay = 120;

    public ArkanoidFrame arkanoidFrame = new ArkanoidFrame(IN_REGISTRATION);

    private Timer timer;

    public void handleAction(int keyCode) {
        switch(arkanoidFrame.getCondition()) {
            case IN_REGISTRATION : {
                registrate();
            }
            case IN_MAIN_MENU: {
                switch (keyCode) {
                    case ENTER -> checkCursorPosition();
                    case UP -> cursorUp();
                    case DOWN -> cursorDown();
                }
                break;
            }
            case IN_ABOUT_MENU:
            case IN_HIGH_SCORES: {
                if(keyCode == ESC) goToMainMenu();
            }

            case IN_GAME: {
                switch(keyCode) {
                    case ESC -> goToMainMenu();
                    case EXIT -> exitGame();
                    case RIGHT -> ArkanoidFrame.gameField.platform.move(Platform.Direction.RIGHT);
                    case LEFT -> ArkanoidFrame.gameField.platform.move(Platform.Direction.LEFT);
                }
                break;
            }
            case END_GAME: {
                if((keyCode == ESC || keyCode == ENTER)) {
                    ArkanoidFrame.gameField.setWinner(Winner.NOBODY);
                    ArkanoidFrame.recordManager.addToTable(arkanoidFrame.registrator.getName(), ArkanoidFrame.gameField.getScore());
                    goToMainMenu();
                }
            }
        }
    }

    private void checkCursorPosition() {
        int cursorPosition = ArkanoidFrame.menu.getCursorPosition();
        switch (cursorPosition) {
            case Menu.Cursor.NEW_GAME -> startGame();
            case Menu.Cursor.HIGH_SCORES -> goToHighScoresMenu();
            case Menu.Cursor.ABOUT -> goToAboutMenu();
            case Menu.Cursor.EXIT -> exitGame();
        }
    }

    private void exitGame() {
        System.exit(0);
    }

    private void cursorUp() {
        ArkanoidFrame.menu.cursorUp();
        ArkanoidFrame.update();
    }

    private void cursorDown() {
        ArkanoidFrame.menu.cursorDown();
        ArkanoidFrame.update();
    }

    public void initGame() {
        timer = new Timer((int) gameDelay, this);
        timer.start();
        ArkanoidFrame.condition = IN_MAIN_MENU;

        Viewer.INSTANCE.addKeyListener(GameController.INSTANCE);
        Viewer.INSTANCE.requestFocus();

        ArkanoidFrame.update();
    }

    public void registrate() {
        ArkanoidFrame.update();
        arkanoidFrame.registrator.registrate();
    }

    public void startGame() {
        GameController.INSTANCE.setDelay(gameDelay);
        arkanoidFrame = new ArkanoidFrame(IN_GAME);
        ArkanoidFrame.update();
    }

    public void goToAboutMenu() {
        ArkanoidFrame.condition = IN_ABOUT_MENU;
        ArkanoidFrame.update();
    }

    public void goToMainMenu() {
        GameController.INSTANCE.setDelay(menuDelay);
        ArkanoidFrame.condition = IN_MAIN_MENU;
        ArkanoidFrame.update();
    }

    public void goToHighScoresMenu() {
        ArkanoidFrame.condition = IN_HIGH_SCORES;
        ArkanoidFrame.update();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(ArkanoidFrame.condition == IN_REGISTRATION) {
            arkanoidFrame.registrator.tryRegistrate();
        }
        else if(ArkanoidFrame.condition == IN_GAME) {
            ArkanoidFrame.gameField.makeMove();
        }
        Viewer.INSTANCE.repaint();
    }
}
