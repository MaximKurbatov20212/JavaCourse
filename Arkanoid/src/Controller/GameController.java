package Controller;

import java.awt.event.*;

public class GameController extends KeyAdapter {
    public final static GameController INSTANCE = new GameController();
    private final GameStateHandler gameHandler = GameStateHandler.INSTANCE;

    public void initGame() {
        gameHandler.initGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameHandler.handleAction(e.getKeyCode());
    }
}
