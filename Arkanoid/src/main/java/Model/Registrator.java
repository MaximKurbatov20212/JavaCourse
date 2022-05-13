package Model;

import Controller.GameStateHandler;
import View.Viewer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class Registrator {
    private final Container c;
    private final JTextField registrationField = new JTextField(15);
    private static FileWriter Writer;

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

    public record Note(String name, Integer score){
        @Override
        public String toString() {
            return name + " " + score;
        }
    }

    public static List<Note> table = new ArrayList<>();

    public Registrator() throws IOException {
        c = Viewer.INSTANCE.getContentPane();
        c.setLayout(null);

        registrationField.setFont(new Font("Arial", Font.BOLD, 60));
        registrationField.setBackground(Color.black);
        registrationField.setForeground(Color.red);
        registrationField.setBounds(BackField.WIDTH / 6, (int) (BackField.HEIGHT * 0.75),450,70);
        c.add(registrationField);
        loadScoresTable();
    }


    public void registrate() {
        if(wasRegistrate) return;
        registrationField.addActionListener(GameStateHandler.INSTANCE);
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

    public static void writeNewRecords() {
        try {
            Writer = new FileWriter("src/main/java/Pictures/HighScores.txt");
            Writer.write(""); // delete all in the file
            Writer.close();

            Writer = new FileWriter("src/main/java/Pictures/HighScores.txt", true);
            for(Note note : table) {
                Writer.append(note.toString()).append("\n");
            }
            Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToTable(Integer score) {
        boolean hasNote = false;

        for(int i = 0; i < table.size(); i++) {
            Note note = table.get(i);
            if(Objects.equals(note.name, name)) {
                hasNote = true;
                if(note.score >= score) break;

                table.remove(i);
                table.add(new Note(name, score));
                break;
            }
        }
        if(!hasNote) {
            table.add(new Note(name, score));
        }
    }
}
