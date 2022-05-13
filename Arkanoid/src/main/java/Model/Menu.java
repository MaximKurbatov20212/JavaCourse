package Model;

public class Menu {
    public static Menu INSTANCE = new Menu();

    public static final int IN_ABOUT_MENU = 3;
    public static final int IN_HIGH_SCORES_MENU = 2;
    public static final int IN_MAIN_MENU = 0;
    public static final int IN_GAME = 1;
    public static final int IN_REGISTRATION = 4;

    private final Cursor cursor = new Cursor();
    private boolean isVisible = false;

    private int condition;

    public static Menu getInstance() {
        return INSTANCE;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCondition() {
        return condition;
    }

    public boolean isInAboutMenu() {
        return condition == IN_ABOUT_MENU;
    }

    public boolean isInMainMenu() {
        return condition == IN_MAIN_MENU;
    }

    public boolean isInHighScoresMenu() {
        return condition == IN_HIGH_SCORES_MENU;
    }

    public void setVisible(boolean b) {
        isVisible = b;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int cursorUp() {
        return cursor.dec();
    }

    public int cursorDown() {
        return cursor.inc();
    }


    public int getCursorPosition() {
        return cursor.getPosition();
    }

    public static class Cursor {
        private int position;

        public static final int NEW_GAME = 0;
        public static final int ABOUT = 1;
        public static final int HIGH_SCORES = 2;
        public static final int EXIT = 3;

        public int inc() {
            if(position == 3) return position;
            position++;
            return position;
        }

        public int dec() {
            if(position == 0) return position;
            position--;
            return position;
        }

        public int getPosition() {
            return position;
        }
    }
}