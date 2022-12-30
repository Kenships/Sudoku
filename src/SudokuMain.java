import java.awt.*;
import java.util.Arrays;

public class SudokuMain
{
    public static final int WIDTH = 1920, HEIGHT = WIDTH / 16 * 9;
    public static final int BOARD_SIZE = 9;

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
            {{0,5,0,2,0,0,0,0,0},
            {0,0,0,0,9,0,0,0,4},
            {0,1,3,0,0,8,0,9,0},
            {0,0,0,7,0,0,3,0,0},
            {0,9,8,0,0,1,0,4,0},
            {6,0,0,0,0,0,0,0,0},
            {4,0,0,0,0,6,0,0,0},
            {0,3,6,2,0,0,0,0,7},
            {5,0,0,0,0,0,0,3,0}};
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
    private GameBoard gameBoard = new GameBoard(expert2);
    private SudokuController sudokuController = new SudokuController();

    public SudokuMain(){
        new Window(WIDTH,HEIGHT,"Sudoku.ca", sudokuController);
        new Logic(gameBoard, sudokuController);
        System.out.println("end");
    }
    public static void main(String[] args) {
        new SudokuMain();
    }
}
