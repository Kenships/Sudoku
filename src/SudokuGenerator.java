import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    public static final int EXPERT = 3;
    public static void generateSudoku(int difficulty){
        GameBoard gameBoard = generateSolvedSudoku();
        switch(difficulty){
            case EASY:
            case MEDIUM:
            case HARD:
            case EXPERT:
        }
    }

    private static GameBoard generateSolvedSudoku() {
        Random random = new Random();
        GameBoard gameBoard = new GameBoard();
        NoteHandler.fillNotes(gameBoard);
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                ArrayList<Integer> possibleNotes = new ArrayList<>();
                possibleNotes.addAll(gameBoard.board[row][col].notes);
                int index = random.nextInt(possibleNotes.size());
                gameBoard.board[row][col].setValue(possibleNotes.get(index));
            }
        }
        return gameBoard;
    }
}
