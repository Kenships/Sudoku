import java.awt.*;
import java.util.Arrays;

public class SudokuMain
{
    //constants
    public static final int WIDTH = 1920, HEIGHT = WIDTH / 16 * 9;
    public static final int BOARD_SIZE = 9;
    //test cases for debugging with varying difficulties
    public static int[][] expert =
            {{0,6,0,0,0,7,0,0,0},
            {0,1,0,0,0,0,7,0,0},
            {4,0,0,0,6,5,0,0,9},
            {0,0,0,0,0,0,0,4,0},
            {3,0,4,9,0,0,0,0,0},
            {5,0,0,0,7,2,0,0,0},
            {0,0,0,1,0,3,0,9,0},
            {0,0,0,0,0,0,0,0,6},
            {2,9,0,0,0,0,5,0,0}};
    public static int[][] evil =
            {{0,0,8,0,4,0,0,0,0},
            {9,0,3,0,0,1,2,0,0},
            {0,0,0,5,0,0,0,1,0},
            {0,8,0,0,0,9,0,0,0},
            {4,0,9,7,0,0,0,0,3},
            {0,5,0,0,0,0,4,0,0},
            {2,0,1,0,0,5,3,0,0},
            {0,0,0,0,6,0,0,0,7},
            {0,4,0,0,0,0,0,0,0}};
    /**
     * techniques
     * x wing
     * hidden quadruple
     * hidden triple
     */
    public static int[][] evil1 =
            {{0,0,4,0,6,0,0,0,0},
            {2,0,5,0,0,1,8,0,0},
            {0,0,0,8,0,0,0,0,3},
            {0,9,0,0,0,0,0,0,0},
            {0,0,0,0,7,0,0,6,0},
            {1,0,8,0,0,5,3,0,0},
            {0,3,0,0,0,9,0,0,0},
            {0,4,0,0,0,0,2,0,0},
            {9,0,2,0,5,0,0,0,7}};
    /**
     * techniques:
     * x wing
     * xy wing
     * hidden triple
     */
    public static int[][] evil2 =
            {{0,4,7,0,1,3,0,2,0},
            {0,0,0,0,6,0,0,0,5},
            {1,0,0,0,0,0,0,0,0},
            {2,0,0,0,0,1,0,0,0},
            {8,0,0,0,0,0,0,9,0},
            {0,9,1,0,3,0,6,0,0},
            {0,8,0,0,0,0,0,0,0},
            {0,0,0,4,0,0,2,0,0},
            {0,3,9,0,0,7,0,4,0}};
    /**
     * techniques:
     * hidden double
     * doesn't even deserve evil difficulty
     */
    public static int[][] evil3 =
            {{0,0,6,0,2,0,0,0,0},
            {9,2,0,0,0,3,4,0,0},
            {0,0,0,0,8,0,0,0,1},
            {5,6,0,0,3,0,0,8,0},
            {0,0,7,0,0,0,5,0,0},
            {0,0,4,0,0,6,0,0,0},
            {7,0,0,0,0,0,0,0,0},
            {0,0,0,9,0,0,0,4,0},
            {3,5,0,0,0,2,9,0,0}};
    public static int[][] expert2 =
            {{0,0,0,0,0,2,0,0,0},
            {7,0,0,0,0,0,1,0,0},
            {2,0,0,0,0,4,0,3,0},
            {0,6,8,0,0,0,9,0,0},
            {3,0,0,0,1,0,6,8,0},
            {0,0,0,0,0,3,0,5,0},
            {0,5,0,0,0,9,7,0,6},
            {0,2,0,0,7,0,0,0,0},
            {0,0,3,0,0,0,0,9,0}};
    public static int[][] expert3 =
            {{0,8,0,0,0,0,0,9,0},
            {0,7,0,0,6,0,2,1,8},
            {0,0,6,0,4,8,7,5,0},
            {8,0,0,0,0,0,5,3,0},
            {0,2,0,0,0,0,0,0,0},
            {1,6,3,0,0,0,0,0,0},
            {0,0,0,4,0,1,9,0,0},
            {0,0,0,0,0,0,0,7,0},
            {2,0,9,7,0,0,0,0,5}};
    public static int[][] expert4 =
            {{4,0,0,0,0,5,3,0,2},
            {0,0,6,0,3,0,9,0,0},
            {0,8,0,0,0,9,0,7,0},
            {0,0,0,1,0,0,8,0,0},
            {0,7,0,0,0,0,0,9,5},
            {0,0,1,0,6,0,2,0,0},
            {0,5,4,0,0,0,0,0,0},
            {0,0,2,0,0,0,0,0,0},
            {8,1,0,0,0,0,0,0,6}};
    public static int[][] hard2 =
            {{0,0,0,0,0,0,0,8,0},
                    {7,0,0,0,0,0,0,0,0},
                    {0,1,9,7,0,3,0,2,0},
                    {0,8,0,1,0,0,0,0,4},
                    {0,0,6,0,0,4,0,0,0},
                    {4,0,0,0,0,5,3,0,0},
                    {2,4,0,9,0,0,8,0,0},
                    {6,0,0,4,0,0,1,0,5},
                    {9,0,1,6,0,0,2,0,7}};
    public static int[][] hard =
            {{6,0,0,4,7,0,0,3,0},
            {2,0,0,5,3,8,0,0,0},
            {0,4,0,0,0,2,5,0,0},
            {0,1,0,0,0,0,0,0,0},
            {0,0,0,0,4,5,0,7,0},
            {0,0,7,0,0,1,0,0,6},
            {5,0,0,9,0,0,4,2,0},
            {3,0,0,0,0,7,0,1,0},
            {0,0,0,2,5,0,0,8,0}};
    public static int[][] medium =
                    {{0,0,4,0,0,0,0,2,0},
                     {0,0,5,0,0,0,0,0,3},
                     {0,6,0,3,0,8,4,0,5},
                     {5,0,0,9,0,0,0,7,2},
                     {0,4,0,2,0,0,0,0,0},
                     {0,2,0,6,1,3,0,0,9},
                     {0,5,0,0,9,6,0,0,0},
                     {2,1,8,0,0,0,0,9,0},
                     {4,9,0,0,0,0,0,0,7}};
    public static int[][] easy =
                    {{5,0,0,0,2,0,0,3,9},
                     {0,4,2,0,1,0,0,0,0},
                     {1,0,7,3,0,6,5,0,0},
                     {7,0,0,8,9,5,0,2,1},
                     {6,0,0,2,0,0,7,9,4},
                     {2,1,0,4,6,0,0,0,5},
                     {0,0,0,1,0,8,0,0,3},
                     {0,0,1,0,4,0,2,6,0},
                     {0,0,3,0,0,0,9,0,8}};
    public static int[][] newsPaper =
            {{3,6,0,0,0,0,0,1,5},
            {0,0,0,7,0,5,0,0,0},
            {1,7,0,0,0,0,0,8,2},
            {0,1,0,0,7,0,0,2,0},
            {7,0,2,4,8,6,5,0,1},
            {0,4,0,0,3,0,0,7,0},
            {9,2,0,0,0,0,0,5,4},
            {0,0,0,3,0,2,0,0,0},
            {6,3,0,0,0,0,0,9,8}};
    private GameBoard gameBoard;
    private SudokuController sudokuController;

    public SudokuMain(){
        //create an instance of the SudokuGenerator class and use it to generate an easy sudoku during the first boot up
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
//        gameBoard = sudokuGenerator.generateSudoku(SudokuGenerator.EASY, sudokuGenerator.generateSolvedSudoku());
        gameBoard = new GameBoard(expert4);
        sudokuController = new SudokuController(gameBoard);
        //creates a window to run the game
        new Window(WIDTH,HEIGHT,"Sudoku.ca", sudokuController);
        //updates the squares in the gui to match the default generated easy sudoku
        sudokuController.updateAllSquares(gameBoard);
    }
    public static void main(String[] args) {
        //calls the constructor of this class
        new SudokuMain();
    }
}


