import java.util.Arrays;
import java.util.HashSet;

public class GameSquare {
    public HashSet<Integer> notes;
    public int value;

    public int row;
    public int col;

    public GameSquare(int row, int col){
        this.row = row;
        this.col = col;
        notes = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    }
    public GameSquare(int value, int row, int col){
        this.row = row;
        this.col = col;
        notes = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        this.value = value;
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
