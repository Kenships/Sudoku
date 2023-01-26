import java.util.HashSet;

public class GameBoard {
    /** TL;DR
    An object designed to store all necessary information on a sudoku game board
    */
    public GameSquare[][] board; // the main board
    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE; //a copy of the constant from SudokuMain

    public GameBoard(){
        board = new GameSquare[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
    }// a general constructor with an empty board
    public GameBoard(int[][] board){
        this.board = new GameSquare[BOARD_SIZE][BOARD_SIZE];
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                this.board[r][c] = new GameSquare(board[r][c],r,c);
            }
        }
    }// a constructor to initialize a board using a 2d array of integers
    public void resetBoard(){
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                board[r][c] = new GameSquare(r,c);
            }
        }
    }// loops through each square in the game board and reinitialized it

    public boolean equals(GameBoard board1){
        boolean result = true;
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                if(board1.board[row][col].getValue() != board[row][col].getValue()){
                    result = false;
                }// if the values are different return false
                if(!((board1.board[row][col].notes.isEmpty() && board[row][col].notes.isEmpty()) ||
                        board[row][col].notes.equals(board1.board[row][col].notes))){
                    result = false;
                }// if both board and board 1 are both not empty or board1 does not have all the same notes from board return false

            }
        }
        return result;
    }// checks if this object is equal, in terms of values, to another gameboard

    public static int getBoxNumber(int r, int c) {
        return r/3 * 3 + 1 + c/3;
    }// returns the index of a box using the coordinates; index goes from left to right up to down
    public static int[] getBoxCoordinates(int boxNumber){
        int row = boxNumber/3 * 3;
        int col = boxNumber%3 * 3;
        return new int[]{row, col};
    }//returns the x and y coordinates of the top right corner of the box at the given index
    public GameSquare[] getRow(int row){
        return board[row];
    }//returns a row of gameSquares in an array
    public GameSquare[] getCol(int col){
        GameSquare[] thisCol = new GameSquare[9];
        for(int i = 0; i < 9; i++){
            thisCol[i] = board[i][col];
        }
        return thisCol;
    }//returns a column of gameSquares in an array
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
    }//returns the box in which the x and y coordinates can be found
    public void output(){
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                System.out.print(board[r][c].getValue()+ " ");
            }
            System.out.println();
        }
        System.out.println("---------------------");
    }//outputs the board for debugging
    public GameBoard makeDeepCopy(){
        GameBoard copy = new GameBoard();
        GameSquare[][] boardCopy = copy.board;
        copy.resetBoard();
        for(int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                GameSquare current = boardCopy[r][c];
                current.addValue(board[r][c].getValue());
                current.setRowNumber(r);
                current.setColNumber(c);
                current.notes.addAll(board[r][c].notes);
            }
        }
        return copy;
    }//makes a deep copy of a game board using values of this board

}
