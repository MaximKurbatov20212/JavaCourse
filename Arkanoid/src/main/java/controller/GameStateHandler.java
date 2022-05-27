package Controller;

import Model.*;
import Model.Menu;
import View.Viewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static Model.GameField.Winner.NOBODY;

public class GameStateHandler implements ActionListener {
    public static final GameStateHandler INSTANCE = new GameStateHandler();

    // Conditions of Controller.GameStateHandler
    public static final int IN_REGISTRATION = -1;
    public static final int IN_GAME = 1;
    public static final int IN_ABOUT_MENU = 3;
    public static final int IN_HIGH_SCORES_MENU = 2;
    public static final int IN_MAIN_MENU = 0;
    public static final int STOP_THE_WORLD = 4;

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

    public int condition;
    private Timer timer;

    private final Menu mainMenu = Menu.getInstance();
    private Registrator registrator;

    private GameStateHandler() {
        try {
            registrator = new Registrator();
        }
        catch (IOException e) {
            System.err.println("Pls, don't delete HighScores.txt");
        }
        condition = IN_REGISTRATION;
    }

    public void handleAction(int keyCode) {
        switch(condition) {
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
            case(IN_ABOUT_MENU):
            case(IN_HIGH_SCORES_MENU): {
                if(keyCode == ESC) goToMainMenu();
            }

            case(IN_GAME): {
                switch(keyCode) {
                    case ESC -> goToMainMenu();
                    case EXIT -> exitGame();
                    case RIGHT -> Platform.INSTANCE.move(RIGHT);
                    case LEFT -> Platform.INSTANCE.move(LEFT);
                }
                break;
            }
            case(STOP_THE_WORLD): {
                if((keyCode == ESC || keyCode == ENTER)) {
                    GameField.INSTANCE.setWinner(GameField.Winner.NOBODY);
                    Registrator.addToTable(GameField.INSTANCE.getScore());
                    goToMainMenu();
                }
            }
        }
    }

    private void checkCursorPosition() {
        int cursorPosition = Menu.getInstance().getCursorPosition();
        switch (cursorPosition) {
            case Menu.Cursor.NEW_GAME -> startGame();
            case Menu.Cursor.HIGH_SCORES -> goToHighScoresMenu();
            case Menu.Cursor.ABOUT -> goToAboutMenu();
            case Menu.Cursor.EXIT -> exitGame();
        }
    }

    private void exitGame() {
        Registrator.writeNewRecords();
        System.exit(0);
    }

    private void cursorUp() {
        mainMenu.cursorUp();
    }

    private void cursorDown() {
        mainMenu.cursorDown();
    }

    public void initGame() {
        timer = new Timer((int) gameDelay, this);
        timer.start();

        Viewer.INSTANCE.addKeyListener(GameController.INSTANCE);
        Viewer.INSTANCE.requestFocus();

        condition = IN_MAIN_MENU;
        mainMenu.setCondition(Menu.IN_MAIN_MENU);
    }

    public void registrate() {
        condition = IN_REGISTRATION;
        mainMenu.setCondition(Menu.IN_REGISTRATION);
        mainMenu.setVisible(true);
        registrator.registrate();
    }

    public void startGame() {
        GameController.INSTANCE.setDelay(gameDelay);
        condition = IN_GAME;
        setStartPositions();
        Viewer.INSTANCE.repaint();
    }

    private void setStartPositions() {
        Ball.INSTANCE.setPosition(250, 300);
        Ball.INSTANCE.setDirectingVector(0,-1);
        Platform.INSTANCE.setPosition(210);
        GameField.INSTANCE.setScore(0);

        mainMenu.setCondition(Menu.IN_GAME);
        mainMenu.setVisible(false);

        GameField.INSTANCE.setWinner(NOBODY);
        GameField.INSTANCE.fillField();
    }

    public void goToAboutMenu() {
        condition = IN_ABOUT_MENU;
        mainMenu.setCondition(Menu.IN_ABOUT_MENU);
    }

    public void goToMainMenu() {
        GameController.INSTANCE.setDelay(menuDelay);
        condition = IN_MAIN_MENU;
        mainMenu.setCondition(Menu.IN_MAIN_MENU);
        mainMenu.setVisible(true);
    }

    public void goToHighScoresMenu() {
        condition = IN_HIGH_SCORES_MENU;
        mainMenu.setCondition(Menu.IN_HIGH_SCORES_MENU);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(condition == IN_REGISTRATION) {
            registrator.tryRegistrate();
        }
        else if(condition == IN_GAME) {
            GameField.INSTANCE.makeMove();
        }
        Viewer.INSTANCE.repaint();
    }
}
