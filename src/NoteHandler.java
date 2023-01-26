import java.util.ArrayList;
import java.util.Arrays;

public class NoteHandler {
    /** TL;DR
    handles and makes operations with notes
     */
    public static int findNumberOfNotes(GameSquare[] section, int note) {
        int count = 0;
        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            if(section[i].containsNote(note)){
                count++;
            }
        }//counts the number of notes in a section
        return count;
    }
    public static boolean notesAreEqual(GameSquare square1, GameSquare square2){
        return square1.notes.equals(square2.notes);//check if the notes are equal
    }
    public static boolean removeConflictingNotes(GameBoard gameBoard, int r, int c, int note){
        boolean row = removeConflictingNote(gameBoard.getRow(r), note);
        boolean col = removeConflictingNote(gameBoard.getCol(c), note);
        boolean box = removeConflictingNote(gameBoard.getBox(r,c), note);
        if(row || col || box){
            return true;
        }
        return false;
    }
    public static boolean removeConflictingNote(GameSquare[] section, int note){
        boolean changed = false;
        for(int i = 0; i < section.length; i++){
            if(section[i].getValue() == -1){
                section[i].addValue(0);
            }
            else if(section[i].containsNote(note)){
                changed = true;
                section[i].removeNote(note);
            }
        }
        return changed;
    }
    public static GameBoard addAllNotes(GameBoard gameBoard){
        fillNotes(gameBoard);
        for (int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() != 0){
                    gameBoard.board[r][c].clearNotes();
                    removeConflictingNotes(gameBoard,r,c,gameBoard.board[r][c].getValue());
                }
            }
        }
        return gameBoard;
    }

    public static void fillNotes(GameBoard gameBoard) {
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                if(gameBoard.board[row][col].getValue() == 0)
                    gameBoard.board[row][col].addNotes(Arrays.asList(1,2,3,4,5,6,7,8,9));
            }
        }
    }

    public static boolean notesInSameCol(ArrayList<GameSquare> hasNote) {
        boolean isSameRow = true;
        int previous = hasNote.get(0).getColNumber();
        for(int index = 1; index < hasNote.size(); index++){
            if(hasNote.get(index).getColNumber() != previous){
                isSameRow = false;
            }
        }
        return isSameRow;
    }
    public static boolean notesInSameBox(ArrayList<GameSquare> squares) {
        boolean isSameBox = true;
        int previousBoxNumber = GameBoard.getBoxNumber(squares.get(0).getRowNumber(),squares.get(0).getColNumber());
        for(int index = 1; index < squares.size(); index++){
            if (previousBoxNumber != GameBoard.getBoxNumber(squares.get(index).getRowNumber(),squares.get(index).getColNumber())) {
                isSameBox = false;
            }
        }
        return isSameBox;
    }
    public static boolean notesInSameRow(ArrayList<GameSquare> hasNote) {
        boolean isSameRow = true;
        int previous = hasNote.get(0).getRowNumber();
        for(int index = 1; index < hasNote.size(); index++){
            if(hasNote.get(index).getRowNumber() != previous){
                isSameRow = false;
            }
        }
        return isSameRow;
    }
}
