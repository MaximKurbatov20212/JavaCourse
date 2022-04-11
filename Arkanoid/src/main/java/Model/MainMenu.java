package Model;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainMenu {
    private static MainMenu INSTANCE;

    static {
        try {
            INSTANCE = new MainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image mainMenuImage;
    private Image aboutMenuImage;

    public static final int IN_ABOUT_MENU = 3;
    public static final int IN_HIGH_SCORES_MENU = 2;
    public static final int IN_MAIN_MENU = 0;
    public static final int IN_GAME = 1;
    public static final int IN_REGISTRATION = 4;

    private Cursor cursor = new Cursor();
    private boolean isVisible = false;

    private String data = "";
    private int condition;

    public MainMenu() throws IOException {
        loadImage();
        FileReader file = new FileReader("src/main/java/Pictures/HighScores.txt");
        BufferedReader bf = new BufferedReader(file);
        while(true) {
            String a = bf.readLine();
            if (a == null) break;
            data += a;
            data += "\n";
        }
    }

    public static MainMenu getInstance() {
        return INSTANCE;
    }

    public String getHighScoresTable() {
        return data;
    }

    private void loadImage() {
        ImageIcon iMainMenu = new ImageIcon("src/main/java/Pictures/Main1.jpg");
        mainMenuImage = iMainMenu.getImage();
        ImageIcon iAboutMenu = new ImageIcon("src/main/java/Pictures/About.jpg");
        aboutMenuImage = iAboutMenu.getImage();
    }

    public Image getMainMenuImage() {
        return mainMenuImage;
    }

    public Image getAboutMenuImage() {
        return aboutMenuImage;
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

    public Image getCursorImage() {
        return cursor.getImage();
    }

    public int getCursorPosition() {
        return cursor.getPosition();
    }

    public class Cursor {
        private final Image cursorImage;
        private int position;

        public static final int NEW_GAME = 0;
        public static final int ABOUT = 1;
        public static final int HIGH_SCORES = 2;
        public static final int EXIT = 3;

        Cursor() {
            ImageIcon iCursor = new ImageIcon("src/main/java/Pictures/cursor.jpg");
            cursorImage = iCursor.getImage();
        }

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

        public Image getImage() {
            return cursorImage;
        }

        public int getPosition() {
            return position;
        }
    }
}