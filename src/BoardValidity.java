import java.util.HashSet;

public class BoardValidity {
    public static boolean valuesAreFilled(GameBoard gameBoard) {
        boolean valid = true;
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].value == 0){
                    valid = false;
                }
            }
        }
        return valid;
    }
    public static boolean notesAreFilled(GameBoard gameBoard) {
        boolean valid = true;
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() == 0 && gameBoard.board[r][c].notes.isEmpty()){
                    valid = false;
                }
            }
        }
        return valid;
    }

    public static boolean isValidSection(GameSquare[] section) {
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
    }
    public static boolean isValidSudoku(GameBoard gameBoard){
        boolean isValid = true;
        for (int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            GameSquare[] row = gameBoard.getRow(i);
            GameSquare[] col = gameBoard.getCol(i);
            int[] coordinates = GameBoard.getBoxCoordinates(i);
            GameSquare[] box = gameBoard.getBox(coordinates[0], coordinates[1]);
            if(!(isValidSection(row) && isValidSection(col) &&isValidSection(box))){
                isValid = false;
            }
        }
        return isValid;
    }
}
