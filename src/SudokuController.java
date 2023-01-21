import java.awt.*;
import java.util.ArrayList;

public class SudokuController {
    private ArrayList<GameBoard> steps = new ArrayList<>();
    public int currentStep = 0;
    private int row;
    private int col;

    public Logic logic;

    private GameBoard solvedSudoku;

    public SudokuController(GameBoard gameBoard){
        steps.add(gameBoard.makeDeepCopy());
        logic = new Logic(gameBoard.makeDeepCopy(), this);
        solvedSudoku = logic.masterSolveSudoku(gameBoard.makeDeepCopy());
    }

    public void clearNotes(){
        Window.inputGameBoard[row][col].clearNotes();
    }
    public void clearInputGameBoard(){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                Window.inputGameBoard[r][c].setValue("");
                Window.inputGameBoard[r][c].setEditable(true);
            }
        }
    }
    public GameBoard inputToGameBoard(){
        GameBoard gameBoard = new GameBoard();
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(!Window.inputGameBoard[r][c].getValue().equals("")){
                    gameBoard.board[r][c] = new GameSquare(Integer.parseInt(Window.inputGameBoard[r][c].getValue()),r,c);
                    gameBoard.board[r][c].setValue(Integer.parseInt(Window.inputGameBoard[r][c].getValue()));
                }
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    gameBoard.board[r][c] = new GameSquare(r,c);
                    gameBoard.board[r][c].notes.addAll(Window.inputGameBoard[r][c].getNotes());
//                    System.out.println(gameBoard.board[r][c].notes.toString());
                }
            }
        }
        //gameBoard.output();
        return gameBoard;
    }
    public void toggleEditable(boolean isEditable){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(!Window.inputGameBoard[r][c].equals("")){
                    Window.inputGameBoard[r][c].setEditable(isEditable);
                }
            }
        }
    }
    public void skipToStep(int step){
        System.out.println(step);
        currentStep++;
        addStep(logic.localSteps.get(step));
        updateAllSquares(logic.localSteps.get(step));


    }
    public void addStep(GameBoard gameBoard){
        steps.add(gameBoard.makeDeepCopy());
    }
    public void nextStep(){
        removeAllErrors();
        highlightAll();
        currentStep++;
        System.out.println("current step: " + currentStep);
        addStep(logic.getNextStep(inputToGameBoard()));
        updateAllSquares(steps.get(currentStep));
    }

    private void removeAllErrors() {
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                if(Window.inputGameBoard[row][col].getBgColor().equals(Color.RED)){
                    Window.inputGameBoard[row][col].setValue("");
                    Window.inputGameBoard[row][col].resetBgColor();
                }
            }
        }
    }
    public GameBoard getCurrentStep(){
        return steps.get(currentStep);
    }

    public void removeStep(int index){
        steps.remove(index);
    }
    public void clearSteps(){
        steps.clear();
    }
    public void previousStep(){
        if(currentStep > 0) {
            removeStep(currentStep);
            currentStep--;
            updateAllSquares(steps.get(currentStep));
            highlightAll();
        }
    }
    public void updateAllSquares(GameBoard gameBoard){
        updateAllValues(gameBoard);
        updateAllNotes(gameBoard);
    }
    public void updateAllNotes(GameBoard gameBoard){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() == 0 && Window.inputGameBoard[r][c].getValue().equals("")){
                    Window.inputGameBoard[r][c].clearNotes();
                    for(Integer note : gameBoard.board[r][c].notes){
                        Window.inputGameBoard[r][c].setNote(String.valueOf(note));
                    }
                }
            }
        }
    }
    public void updateAllValues(GameBoard gameBoard){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() != 0){
                    Window.inputGameBoard[r][c].setValue(String.valueOf(gameBoard.board[r][c].getValue()));
                    Window.inputGameBoard[r][c].setEditable(false);
                    if(Window.inputGameBoard[r][c].getPlayerPressed())
                        Window.inputGameBoard[r][c].setValueColor(Color.BLUE);
                    else
                        Window.inputGameBoard[r][c].setValueColor(Color.BLACK);
                } else if (!Window.inputGameBoard[r][c].getValue().equals("")) {
                    Window.inputGameBoard[r][c].setValue("");
                    Window.inputGameBoard[r][c].setEditable(true);
                }//if value from internal board is not 0 it checks if the window displays a number and if it does removes it
            }
        }
    }
    public void highlightError(){
        if(!correctPosition(row,col))
            Window.inputGameBoard[row][col].setBgColor(Color.red);
    }// if entered number is not correct it highlights the square red

    private boolean correctPosition(int r, int c) {
        return Window.inputGameBoard[r][c].getValue().equals(String.valueOf(solvedSudoku.board[r][c].getValue()))
                || Window.inputGameBoard[r][c].getValue().equals("");
    }

    public void highlightAll(){
        resetHighlight();
        highlightRow();
        highlightCol();
        highlightBox();
        if(!getCurrentValue().equals("")){
            highlightValue();
        }
        Window.inputGameBoard[row][col].setBgColor(new Color(163, 128, 255));
        highlightError();
    }
    public void resetHighlight(){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(correctPosition(r,c))
                    Window.inputGameBoard[r][c].resetBgColor();
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    for(int note = 1; note <= SudokuMain.BOARD_SIZE; note++){
                        Window.inputGameBoard[r][c].unboldNote(String.valueOf(note));
                    }
                }
            }
        }
    }
    private void highlightValue() {
        String currentValue = getCurrentValue();
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(Window.inputGameBoard[r][c].getValue().equals(currentValue) && correctPosition(r,c)){
                    Window.inputGameBoard[r][c].setBgColor(new Color(187, 162, 255));
                }
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    Window.inputGameBoard[r][c].boldNote(currentValue);
                }
            }
        }
    }

    private void highlightBox() {
        int x = row/3 * 3;
        int y = col/3 * 3;
        for(int r = x; r < x + 3; r++){
            for(int c = y; c < y + 3; c++){
                if(correctPosition(r,c))
                    Window.inputGameBoard[r][c].setBgColor(new Color(219, 205, 255));
            }
        }
    }

    private void highlightCol() {
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            if(correctPosition(r,col))
                Window.inputGameBoard[r][col].setBgColor(new Color(219, 205, 255));
        }
    }

    private void highlightRow() {
        for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
            if(correctPosition(row,c))
                Window.inputGameBoard[row][c].setBgColor(new Color(219, 205, 255));
        }
    }

    public void setValue(String value){
        Window.inputGameBoard[row][col].setValue(value);

    }
    public void addValue(String value, boolean noteMode){
        if(Window.inputGameBoard[row][col].getPlayerPressed())
            Window.inputGameBoard[row][col].setValueColor(Color.BLUE);
        if(!noteMode){
            Window.inputGameBoard[row][col].addValue(value);
            highlightAll();
            GameBoard currentBoard = inputToGameBoard();
            if(!value.equals("")){
                NoteHandler.removeConflictingNotes(currentBoard,getRow(),getCol(),Integer.parseInt(value));
                updateAllNotes(currentBoard);
            }
            addStep(currentBoard);
        }
        else{
            Window.inputGameBoard[row][col].setNote(value);
            addStep(inputToGameBoard());
        }
        currentStep++;
    }
    public String getCurrentValue(){
        return Window.inputGameBoard[row][col].getValue();
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }
}
