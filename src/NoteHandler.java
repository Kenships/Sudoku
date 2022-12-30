import java.util.ArrayList;

public class NoteHandler {
    public static int findNumberOfNotes(GameSquare[] section, int note) {
        int count = 0;
        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            if(section[i].containsNote(note)){
                count++;
            }
        }
        return count;
    }
    public static boolean notesAreEqual(GameSquare square1, GameSquare square2){
        return square1.notes.equals(square2.notes);
    }
    public static void removeConflictingNotes(GameBoard gameBoard, int r, int c, int note){
        removeConflictingNote(gameBoard.getRow(r), note);
        removeConflictingNote(gameBoard.getCol(c), note);
        removeConflictingNote(gameBoard.getBox(r,c), note);
    }
    public static void removeConflictingNote(GameSquare[] section, int note){
        for(int i = 0; i < section.length; i++){
            if(section[i].getValue() == -1){
                section[i].addValue(0);
            }
            else if(section[i].containsNote(note)){
                section[i].removeNote(note);
            }
        }
    }
    public static void addAllNotes(GameBoard gameBoard){
        for (int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() != 0){
                    gameBoard.board[r][c].clearNotes();
                    removeConflictingNotes(gameBoard,r,c,gameBoard.board[r][c].getValue());
                }
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
