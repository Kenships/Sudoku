import java.util.HashSet;

public class BoardValidity {
    /** TL;DR
    various forms of verification of the state of a given sudoku.
    */
    public static boolean valuesAreFilled(GameBoard gameBoard) {
        //loops through each cell of the game board
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].value == 0){
                    return false;
                }// if the value at a particular cell is 0 that means it is not filled in and will return false;
            }
        }
        return true;
    }//checks if there are empty squares
    public static boolean notesAreFilled(GameBoard gameBoard) {
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() == 0 && gameBoard.board[r][c].notes.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }//checks if there are empty squares with no notes.

    public static boolean isSolvedSection(GameSquare[] section) {
        boolean valid = true;
        HashSet<Integer> values = new HashSet<>();
        for(int i = 0; i < section.length; i++){
            int currentValue = section[i].getValue();

            if(!values.contains(currentValue) && currentValue != 0){
                values.add(currentValue);
            }
            else{
                valid = false;
            }
        }
        return valid;
    }//checks if a section ei. row, col, box has no repeats and is considered in a "solved" state
    public static boolean isSolvedSudoku(GameBoard gameBoard){
        for (int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            GameSquare[] row = gameBoard.getRow(i);
            GameSquare[] col = gameBoard.getCol(i);
            int[] coordinates = GameBoard.getBoxCoordinates(i);
            GameSquare[] box = gameBoard.getBox(coordinates[0], coordinates[1]);
            if(!(isSolvedSection(row) && isSolvedSection(col) && isSolvedSection(box))){
                return false;
            }
        }
        return true;
    }// loops through each row/col/box and checks if each section is fully solved.
    public static boolean isValidSudoku(GameBoard gameBoard){
        for (int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            GameSquare[] row = gameBoard.getRow(i);
            GameSquare[] col = gameBoard.getCol(i);
            int[] coordinates = GameBoard.getBoxCoordinates(i);
            GameSquare[] box = gameBoard.getBox(coordinates[0], coordinates[1]);
            if(!(isValidSection(row) && isValidSection(col) && isValidSection(box))){
                return false;
            }
        }
        return true;
    }//checks if a game board has overlapping numbers for example there are two 4s in one row

    private static boolean isValidSection(GameSquare[] section) {
        HashSet<Integer> values = new HashSet<>();
        for(int i = 0; i < section.length; i++){
            int currentValue = section[i].getValue();
            if(currentValue == 0){
                continue;
            }
            if(!values.contains(currentValue)){
                values.add(currentValue);
            }
            else{
                return false;
            }
        }
        return true;
    }// checks if a section as overlapping numbers for example there are two 4s in one row

}
