package Controller;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;

import static Controller.GameStateHandler.menuDelay;

public class GameController extends KeyAdapter {
    public static final GameController INSTANCE = new GameController();
    private final GameStateHandler gameHandler = GameStateHandler.INSTANCE;

    Timer timer;

    HashSet<Integer> pressedKeys = new HashSet<>();

    private GameController() {
        timer = new Timer((int) menuDelay, action -> {
            if(!pressedKeys.isEmpty()){
                for (Integer pressedKey : pressedKeys) {
                    gameHandler.handleAction(pressedKey);
                }
            }
        });
        timer.start();
    }

    public void initGame() {
        gameHandler.registrate();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // gameHandler.handleAction(pressedKey);  <-  delayed registration  https://stackoverflow.com/questions/12102619/java-keylistener-is-delayed-registration
        pressedKeys.add(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e){
        pressedKeys.remove(e.getKeyCode());
    }

    public void setDelay(double delay) {
        timer.setDelay((int) delay);
    }
}
