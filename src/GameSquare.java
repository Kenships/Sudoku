import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GameSquare {
    public HashSet<Integer> notes;
    public int value;

    public int row;
    public int col;

    public GameSquare(int row, int col){
        this.row = row;
        this.col = col;
        notes = new HashSet<>();
    }
    public GameSquare(int value, int row, int col){
        this.row = row;
        this.col = col;
        this.value = value;
        notes = new HashSet<>();
    }
    public void setValue(int note){
        clearNotes();
        value = note;
    }
    public boolean containsNote(int note){
        return notes.contains(note);
    }
    public void addNote(int note){
        notes.add(note);
    }
    public void addNotes(List<Integer> notes){
        this.notes.addAll(notes);
    }
    public void removeNote(int note){
        notes.remove(note);
    }
    public void clearNotes(){
        notes.clear();
    }
    public int getRowNumber(){
        return row;
    }
    public void setRowNumber(int row){
        this.row = row;
    }
    public int getColNumber(){
        return col;
    }
    public void setColNumber(int col){
        this.col = col;
    }
    public int getValue(){
        return value;
    }
    public void addValue(int value){
        this.value = value;
    }
}
