import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GameSquare {
    /** TL;DR
     object that stores all necessary information in a single cell on the sudoku grid
     */
    public HashSet<Integer> notes;
    public int value;

    public int row;
    public int col;

    public GameSquare(int row, int col){
        this.row = row;
        this.col = col;
        notes = new HashSet<>();
    }//basic constructor
    public GameSquare(int value, int row, int col){
        this.row = row;
        this.col = col;
        this.value = value;
        notes = new HashSet<>();
    }//constructor with a value
    public void setValue(int note){
        clearNotes();
        value = note;
    }//remove notes and sets the value to the note
    public boolean containsNote(int note){
        return notes.contains(note);
    }//if a cell contains a note
    public void addNote(int note){
        notes.add(note);
    }//used to add a note
    public void addNotes(List<Integer> notes){
        this.notes.addAll(notes);
    }//adds a list of notes
    public void removeNote(int note){
        notes.remove(note);
    }//removes a note
    public void clearNotes(){
        notes.clear();
    }//clears the notes
    public int getRowNumber(){
        return row;
    }//gets row
    public void setRowNumber(int row){
        this.row = row;
    }//sets row
    public int getColNumber(){
        return col;
    }//gets column
    public void setColNumber(int col){
        this.col = col;
    }//sets column
    public int getValue(){
        return value;
    }//gets value
    public void addValue(int value){
        this.value = value;
    }//adds a value without affecting notes
}
