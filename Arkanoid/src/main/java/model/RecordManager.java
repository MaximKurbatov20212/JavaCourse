package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RecordManager {
    public final static RecordManager INSTANCE = new RecordManager();

    private final List<Note> table = new ArrayList<>();

    private RecordManager() {}

    public ArrayList<Note> getTable() {
        return (ArrayList<Note>) table;
    }

    public void addToTable(String name, Integer score) {
        boolean hasNote = false;

        for(int i = 0; i < table.size(); i++) {
            Note note = table.get(i);
            if(Objects.equals(note.name(), name)) {
                hasNote = true;
                if(note.score() >= score) break;

                table.remove(i);
                table.add(new Note(name, score));
                break;
            }
        }
        if(!hasNote) {
            table.add(new Note(name, score));
        }
    }

    public List<GameObject> getGameObjects() {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < Math.min(15, table.size()); i++) {
            result.append(i + 1).append(") ").append(table.get(i).toString()).append("\n");
        }
        return List.of(new GameObject(100, 150, ObjectType.SCORE, result.toString()));
    }

    public record Note(String name, Integer score) {
        @Override
        public String toString() {
            return name + " " + score;
        }
    }
}
