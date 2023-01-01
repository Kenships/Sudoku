import java.awt.*;
import java.util.ArrayList;

public class SudokuController {
    private ArrayList<GameBoard> steps = new ArrayList<>();
    private int currentStep = 0;
    private int row;
    private int col;
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
        if(step > 0 && step < steps.size()){
            currentStep = step;
            updateAllSquares(steps.get(currentStep));
        }
        else if(step == 0){
            currentStep = step;
            clearInputGameBoard();
            updateAllValues(steps.get(currentStep));
        }
    }
    public void addStep(GameBoard gameBoard){
        steps.add(gameBoard);
    }
    public void nextStep(){
        if(currentStep < steps.size() - 1){
            currentStep++;
            updateAllSquares(steps.get(currentStep));
        }
    }
    public void previousStep(){
        if(currentStep > 0) {
            currentStep--;
            updateAllSquares(steps.get(currentStep));
        }
        if(currentStep == 0){
            clearInputGameBoard();
            updateAllValues(steps.get(currentStep));
        }
    }
    public void removeStep(GameBoard gameBoard){
        steps.remove(gameBoard);
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
                        System.out.print(note + " ");
                        Window.inputGameBoard[r][c].setNote(String.valueOf(note));
                    }
                    System.out.println();
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
        else highlightAll();
    }

    private boolean correctPosition(int r, int c) {
        return Window.inputGameBoard[r][c].getValue().equals(String.valueOf(Logic.solvedSudoku.board[r][c].getValue()))
                || Window.inputGameBoard[r][c].getValue().equals("");
    }

    public void highlightAll(){
        highlightRow();
        highlightCol();
        highlightBox();
        if(!getCurrentValue().equals("")){
            highlightValue();
        }
        Window.inputGameBoard[row][col].setBgColor(new Color(160, 160, 160));
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
                    Window.inputGameBoard[r][c].setBgColor(new Color(170, 170, 170));
                }
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    Window.inputGameBoard[r][c].boldNote(currentValue);
                }
            }
        }
    }

    private void highlightBox() {
        int x= row/3 * 3;
        int y = col/3 * 3;
        for(int r = x; r < x + 3; r++){
            for(int c = y; c < y + 3; c++){
                if(correctPosition(r,c))
                    Window.inputGameBoard[r][c].setBgColor(new Color(214, 214, 214));
            }
        }
    }

    private void highlightCol() {
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            if(correctPosition(r,col))
                Window.inputGameBoard[r][col].setBgColor(new Color(214, 214, 214));
        }
    }

    private void highlightRow() {
        for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
            if(correctPosition(row,c))
                Window.inputGameBoard[row][c].setBgColor(new Color(214, 214, 214));
        }
    }

    public void setValue(String value){
        Window.inputGameBoard[row][col].setValue(value);

    }
    public void addValue(String value, boolean noteMode){
        if(!noteMode){
            Window.inputGameBoard[row][col].addValue(value);
            highlightError();
        }
        else{
            Window.inputGameBoard[row][col].setNote(value);
        }
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
