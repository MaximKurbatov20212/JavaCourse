package Model;

import Controller.GameStateHandler;
import View.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Registrator {

    JTextField smallField;
    boolean wasRegistrate = false;

    private static String name;

    public record Note(String name, Integer score){};

    public static List<Note> table = new ArrayList<>();

    public Registrator() throws IOException {
        smallField = new JTextField(10);
        smallField.setFont(new Font("Arial", Font.BOLD, 72));
        smallField.setBackground(Color.black);
        smallField.setForeground(Color.red);
        Viewer.INSTANCE.add(smallField);
        loadScoresTable();
    }

    public void registrate() {
        if(wasRegistrate) return;

        smallField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                smallField.setBounds(0,0,100,100);

                if (!wasRegistrate) {
                    name = smallField.getText();
                    if(!isFree(name)) return;

                    Viewer.INSTANCE.remove(smallField);
                    GameStateHandler.INSTANCE.initGame();
                }
            }
        });

        Viewer.INSTANCE.setVisible(true);
    }

    private void loadScoresTable() throws IOException {
        FileReader file = new FileReader("src/main/java/Pictures/HighScores.txt");
        BufferedReader bf = new BufferedReader(file);

        while(true) {
            String a = bf.readLine();
            if (a == null) break;
            String[] b = a.split(" ");
            table.add(new Note(b[0], Integer.parseInt(b[1])));
        }
        file.close();
    }

    private static boolean isFree(String name) {
        return table.stream().noneMatch(note -> Objects.equals(note.name, name));
    }

    public static void addToTable(Integer score) {
        try {
            table.add(new Note(name, score));
            FileWriter file = new FileWriter("src/main/java/Pictures/HighScores.txt", true);
            file.append("\n").append(name).append(" ").append(((Integer) score).toString());
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Pls, don't delete HighScores.txt");
        }
    }
}
