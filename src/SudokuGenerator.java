import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    /** TL;DR
    generates a sudoku LOL
    */
    //constants to represent difficulties
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    public static final int EXPERT = 3;
    private Logic sudokuLogic;
    private Random random;
    public SudokuGenerator(){
        sudokuLogic = new Logic();
        random = new Random();
    }//basic constructor

    public GameBoard generateSudoku(int difficulty, GameBoard solvedSudoku){
        GameBoard generatedBoard = new GameBoard();

        switch(difficulty){
            case EASY:
                generatedBoard = generateEasyBoard(solvedSudoku);
                break;
            case MEDIUM:
                generatedBoard = generateMediumBoard(solvedSudoku);
                break;
            case HARD:
                generatedBoard = generateHardBoard(solvedSudoku);
                break;
            case EXPERT:
                generatedBoard = generateExpertBoard(solvedSudoku);
                break;
        }
        return generatedBoard;
    }//gets the difficulty and generates a corresponding board

    private GameBoard generateEasyBoard(GameBoard solvedSudoku) {
        GameBoard possibleBoard = new GameBoard();
        boolean possible = false;
        while(!possible){// if the iteration is not possible keep on trying
            possibleBoard = solvedSudoku.makeDeepCopy(); // makes a copy of the solved board
            removeSquares(random.nextInt(3) + 43, possibleBoard); //remove values from the solved sudoku
            GameBoard solve = possibleBoard.makeDeepCopy();
            NoteHandler.addAllNotes(solve);// adds all possible notes to the game board
            while(true){//attempts to solve the sudoku with only finding singled out digits
                if(solve.equals(solvedSudoku)){
                    possible = true;
                    break;
                }//if the sudoku is solved then break out of the loop
                sudokuLogic.localSteps.clear();// clears the local list of gameBoards
                sudokuLogic.findNakedSingles(solve);// find singled out digits
                if(sudokuLogic.localSteps.isEmpty()){// if there is no steps in the local list the board is impossible and thus breaks
                    break;
                }

            }
        }
        return possibleBoard;
    }// generates easy board

    private GameBoard generateMediumBoard(GameBoard solvedSudoku) {
        GameBoard possibleBoard = new GameBoard();
        boolean possible = false;
        while(!possible){
            possibleBoard = solvedSudoku.makeDeepCopy();
            removeSquares(random.nextInt(4) + 51, possibleBoard);
            GameBoard solve = possibleBoard.makeDeepCopy();
            NoteHandler.addAllNotes(solve);
            while(true){
                if(solve.equals(solvedSudoku)){
                    possible = true;
                    break;
                }
                sudokuLogic.localSteps.clear();
                sudokuLogic.findNakedSingles(solve);
                if(sudokuLogic.localSteps.isEmpty()){
                    break;
                }

            }
        }
        return possibleBoard;
    }//does the same thing as easy board

    private GameBoard generateHardBoard(GameBoard solvedSudoku) {
        GameBoard possibleBoard = new GameBoard();
        boolean possible = false;

        //used to check if a technique has been used at least once
        while(!possible){
            possibleBoard = solvedSudoku.makeDeepCopy();
            removeSquares(random.nextInt(4) + 51, possibleBoard);
            GameBoard solve = possibleBoard.makeDeepCopy();
            NoteHandler.addAllNotes(solve);
            boolean domino = false;
            while(true){
                if(solve.equals(solvedSudoku)){
                    if(domino) possible = true;
                    break;
                }
                sudokuLogic.localSteps.clear();
                sudokuLogic.findNakedSingles(solve);
                if(sudokuLogic.localSteps.isEmpty()){// when local steps is empty then we try finding dominoes
                    domino = true;
                    sudokuLogic.localSteps.clear();
                    sudokuLogic.findAllDominoes(solve);
                    if(sudokuLogic.localSteps.isEmpty()){// when no dominoes can be found this board is unsolvable with only these logics
                        break;
                    }
                }
            }
        }
        return possibleBoard;
    }// does the same thing as the other ones except when it can not be solved it checks the next technique

    private GameBoard generateExpertBoard(GameBoard solvedSudoku) {
        GameBoard possibleBoard = new GameBoard();
        boolean possible = false;
        //used to check if a technique has been used at least once
        while(!possible) {
            boolean domino = false;
            boolean doub = false;
            possibleBoard = solvedSudoku.makeDeepCopy();
            removeSquares(random.nextInt(4) + 51, possibleBoard);
            GameBoard solve = possibleBoard.makeDeepCopy();
            NoteHandler.addAllNotes(solve);
            while (true) {
                if (solve.equals(solvedSudoku)) {
                    if(domino && doub) possible = true;
                    break;
                }
                sudokuLogic.localSteps.clear();
                sudokuLogic.findNakedSingles(solve);
                if (sudokuLogic.localSteps.isEmpty()) {
                    sudokuLogic.localSteps.clear();
                    sudokuLogic.findAllDominoes(solve);
                    domino = true;
                    if (sudokuLogic.localSteps.isEmpty()) {
                        sudokuLogic.localSteps.clear();
                        sudokuLogic.findAllDoubles(solve);
                        doub = true;
                        if(sudokuLogic.localSteps.isEmpty()){
                            break;
                        }
                    }
                }
            }
        }
        return possibleBoard;
    }// same as hard except one more technique

    private void removeSquares(int numberToRemove, GameBoard gameBoard) {
        for(int i = 0; i < numberToRemove; i++){
            while(true){
                int row = random.nextInt(SudokuMain.BOARD_SIZE);
                int col = random.nextInt(SudokuMain.BOARD_SIZE);
                if(gameBoard.board[row][col].getValue() != 0){
                    gameBoard.board[row][col].setValue(0);
                    break;
                }
            }
        }
    }// removes random values from the game board

    public GameBoard generateSolvedSudoku() {
        GameBoard gameBoard = new GameBoard();
        NoteHandler.fillNotes(gameBoard);// fills the notes
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                if(gameBoard.board[row][col].notes.isEmpty()){// if a box has no notes the state of the board is impossible
                    gameBoard.resetBoard();
                    NoteHandler.addAllNotes(gameBoard);
                    row = -1;// offsets the row so when row++ happens it does not skip row = 0
                    break;
                }
                ArrayList<Integer> possibleNotes = new ArrayList<>();
                possibleNotes.addAll(gameBoard.board[row][col].notes);//retrieves the possible notes at any particular square
                int index = random.nextInt(possibleNotes.size());//generates a random index
                int value = possibleNotes.get(index);//get the value at generated index
                gameBoard.board[row][col].setValue(value);//sets the value
                NoteHandler.removeConflictingNotes(gameBoard,row,col,value);//remove all the conflicting notes
            }
        }
        return gameBoard;
    }
}
