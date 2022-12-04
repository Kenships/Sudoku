import java.util.HashSet;

public class GameBoard {
    public GameSquare[][] board;
    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE;

    public GameBoard(){
        board = new GameSquare[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
    }
    public GameBoard(int[][] board){
        this.board = new GameSquare[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                this.board[r][c].value = board[r][c];
            }
        }
    }
    public void resetBoard(){
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                board[r][c] = new GameSquare(r,c);
            }
        }
    }
    public static void getSectionValues(GameSquare[] section, HashSet<Integer> possibleNotes) {
        for(int i = 0; i < BOARD_SIZE; i++){
            int value = section[i].value;
            if(value != 0){
                possibleNotes.remove(value);
            }
        }
    }
    public static int getBoxNumber(int r, int c) {
        return r/3 * 3 + 1 + c/3;
    }
    public static int[] getBoxCoordinates(int boxNumber){
        int row = boxNumber/3 * 3;
        int col = boxNumber%3 * 3;
        return new int[]{row, col};
    }
    public GameSquare[] getRow(int row){
        return board[row];
    }
    public GameSquare[] getCol(int col){
        GameSquare[] thisCol = new GameSquare[9];
        for(int i = 0; i < 9; i++){
            thisCol[i] = board[i][col];
        }
        return thisCol;
    }
    public GameSquare[] getBox(int x, int y){
        GameSquare[] thisBox = new GameSquare[BOARD_SIZE];
        int row = x/3 * 3;
        int col = y/3 * 3;
        int linearIndex = 0;
        for(int r = row; r < row + 3; r++){
            for(int c = col; c < col + 3; c++){
                thisBox[linearIndex] = board[r][c];
                linearIndex++;
            }
        }
        return thisBox;
    }
    public void output(){
        System.out.println("---------------------");
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                System.out.print(board[r][c].value + " ");
            }
            System.out.println();
        }
    }
    public static GameBoard makeDeepCopy(GameBoard gameBoard){
        GameBoard copy = new GameBoard();
        copy.resetBoard();
        for(int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                copy.board[r][c].value = gameBoard.board[r][c].value;
                for(int note = 0; note < BOARD_SIZE; note++){
                    copy.board[r][c].notes[note] = gameBoard.board[r][c].notes[note];
                }
            }
        }
        return copy;
    }

}
