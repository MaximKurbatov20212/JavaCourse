package model;

import controller.GameStateHandler;
import view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Registrator {
    private final Container c;
    private final JTextField registrationField = new JTextField(15);
    private static String name;
    private final boolean wasRegistrate = false;

    public void tryRegistrate() {
        if (!wasRegistrate) {
            name = registrationField.getText();
            if(!isFree(name) || !isValid(name)) return;

            Viewer.INSTANCE.remove(c);
            GameStateHandler.INSTANCE.initGame();
        }
    }

    private boolean isValid(String name) {
        if(name.length() > 15 || name.length() == 0) return false;
        for(int i = 0; i < name.length(); i++) {
            if(name.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }

    public Registrator() {
        c = Viewer.INSTANCE.getContentPane();
        c.setLayout(null);

        registrationField.setFont(new Font("Arial", Font.BOLD, 60));
        registrationField.setBackground(Color.black);
        registrationField.setForeground(Color.red);
        registrationField.setBounds(GameField.MAIN_AREA_WIDTH / 6, (int) (GameField.HEIGHT * 0.75),450,70);
        c.add(registrationField);
    }

    public void registrate() {
        if(wasRegistrate) return;
        registrationField.addActionListener(GameStateHandler.INSTANCE);
        Viewer.INSTANCE.setVisible(true);
    }

    private static boolean isFree(String name) {
        List<RecordManager.Note> notes = RecordManager.INSTANCE.getTable();
        for (RecordManager.Note note : notes) {
            if (Objects.equals(note.name(), name)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }
}
