package model;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

// CR: move to MenuPanel
public class Menu {
    private final Cursor cursor = new Cursor();

    public void cursorUp() {
        cursor.dec();
    }

    public void cursorDown() {
        cursor.inc();
    }


    public int getCursorPosition() {
        return cursor.getPosition();
    }

    public List<GameObject> getGameObjects() {
        return List.of(new GameObject(150, 180 + 68 * cursor.position, ObjectType.CURSOR, ""));
    }

    public static class Cursor {
        public static final int NEW_GAME = 0;
        public static final int ABOUT = 1;
        public static final int HIGH_SCORES = 2;
        public static final int EXIT = 3;

        int position = NEW_GAME;

        public void inc() {
            if (position == EXIT) return;
            position++;
        }

        public void dec() {
            if(position == NEW_GAME) return;
            position--;
        }

        public int getPosition() {
            return position;
        }
    }
}
//
//interface MenuListener {
//    void optionSelected(Menu.MenuOption option);
//}
//
//class MenuPanel extends JPanel {
//
//    MenuPanel(MenuListener listener) {
//        this.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    listener.optionSelected(menuOption);
//                    return;
//                }
//                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                    select(true);
//                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
//                    select(false);
//                }
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                super.keyPressed(e);
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                super.keyReleased(e);
//            }
//        });
//
//    }
//
//
//    public enum MenuOption {
//        HIGH_SCORES,
//        ABOUT;
//    }
//
//    private MenuOption menuOption = MenuOption.ABOUT;
//
//    void select(boolean isNext) {
//        int next = isNext ? 1 : -1;
//        MenuOption[] values = MenuOption.values();
//        menuOption = values[(menuOption.ordinal() + next) % values.length];
//    }
//}