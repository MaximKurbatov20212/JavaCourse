package model;

import java.util.List;

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