import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class NoteHandler {
    public static int findNumberOfNotes(GameSquare[] section, int note) {
        int count = 0;
        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            if(section[i].notes[note-1]){
                count++;
            }
        }
        return count;
    }
    public static boolean notesAreTheSame(GameSquare square1, GameSquare square2){
        boolean isTheSame = true;
        for(int note = 0; note < SudokuMain.BOARD_SIZE; note++){
            if(square1.notes[note] != square2.notes[note]){
                isTheSame = false;
            }
        }
        return isTheSame;
    }
    public static void removeConflictingNotes(GameBoard gameBoard, int r, int c, int note){
        removeConflictingNote(gameBoard.getRow(r), note);
        removeConflictingNote(gameBoard.getCol(c), note);
        removeConflictingNote(gameBoard.getBox(r,c), note);
    }
    public static void removeConflictingNote(GameSquare[] section, int note){
        for(int i = 0; i < section.length; i++){
            if(section[i].value == -1){
                section[i].value = 0;
            }
            else if(section[i].notes[note - 1]){
                section[i].removeNote(note - 1);
            }
        }
    }
    public static void addAllNotes(GameBoard gameBoard){
        for (int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].value == 0)
                    addNotes(r,c,gameBoard);
            }
        }
    }
    public static void addNotes(int r, int c, GameBoard gameBoard) {
        HashSet<Integer> possibleNotes = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        GameBoard.getSectionValues(gameBoard.getRow(r), possibleNotes);
        GameBoard.getSectionValues(gameBoard.getCol(c), possibleNotes);
        GameBoard.getSectionValues(gameBoard.getBox(r,c), possibleNotes);
        for(int note : possibleNotes){
            gameBoard.board[r][c].addNote(note - 1);
        }
    }
    public static boolean notesInSameCol(ArrayList<GameSquare> hasNote) {
        boolean isSameRow = true;
        int previous = hasNote.get(0).col;
        for(int index = 1; index < hasNote.size(); index++){
            if(hasNote.get(index).col != previous){
                isSameRow = false;
            }
        }
        return isSameRow;
    }
    public static boolean notesInSameBox(ArrayList<GameSquare> squares) {
        boolean isSameBox = true;
        int previousBoxNumber = GameBoard.getBoxNumber(squares.get(0).row,squares.get(0).col);
        for(int index = 1; index < squares.size(); index++){
            if (previousBoxNumber != GameBoard.getBoxNumber(squares.get(index).row,squares.get(index).col)) {
                isSameBox = false;
            }
        }
        return isSameBox;
    }
    public static boolean notesInSameRow(ArrayList<GameSquare> hasNote) {
        boolean isSameRow = true;
        int previous = hasNote.get(0).row;
        for(int index = 1; index < hasNote.size(); index++){
            if(hasNote.get(index).row != previous){
                isSameRow = false;
            }
        }
        return isSameRow;
    }
}
