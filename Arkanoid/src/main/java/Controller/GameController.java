package Controller;

import View.Viewer;

import java.awt.event.*;

public class GameController extends KeyAdapter {
    public static final GameController INSTANCE = new GameController();
    private final GameStateHandler gameHandler = GameStateHandler.INSTANCE;

    private GameController() {}

    public void initGame() {
        gameHandler.registr();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameHandler.handleAction(e.getKeyCode());
    }
}
