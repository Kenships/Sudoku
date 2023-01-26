import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SudokuController {
    /** TL;DR
    communicates between front end and back end
     */
    private ArrayList<GameBoard> steps = new ArrayList<>();
    public int currentStep = 0;
    private int row;
    private int col;
    public boolean createMode = false; // used when creating a sudoku

    public Logic logic;

    public GameBoard solvedSudoku; //stores the current solved sudoku for error detection

    public SudokuController(GameBoard gameBoard){
        steps.add(gameBoard.makeDeepCopy());
        logic = new Logic(gameBoard.makeDeepCopy(), this);
        solvedSudoku = logic.masterSolveSudoku(gameBoard.makeDeepCopy());
    }//constructor
    public int getNumberOfFilledIn(){
        int count = 0;
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(!Window.inputGameBoard[r][c].getValue().equals("")){
                    count++;
                }
            }
        }
        return count;
    }//counts how many squares have values
    public void clearNotes(){
        Window.inputGameBoard[row][col].clearNotes();
    }//used to clear notes in the selected square
    public void clearInputGameBoard(){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                Window.inputGameBoard[r][c].setValue("");
                Window.inputGameBoard[r][c].setValueColor(Color.BLACK);
                Window.inputGameBoard[r][c].setPlayerPressed(false);
                Window.inputGameBoard[r][c].setEditable(true);
            }
        }
    }//clears the visual game-board
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
                }
            }
        }
        return gameBoard;
    }//takes the values from the GUI game board and passes it into a gameBoard class
    public void toggleEditable(boolean isEditable){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(!Window.inputGameBoard[r][c].equals("")){
                    Window.inputGameBoard[r][c].setEditable(isEditable);
                }
            }
        }
    }//changes between editable cells and non-editable
    public void skipToStep(int step){
        currentStep++;
        addStep(logic.localSteps.get(step));
        updateAllSquares(logic.localSteps.get(step));
    }//used mainly for debugging and skipping through the "hints"
    public void addStep(GameBoard gameBoard){
        steps.add(gameBoard.makeDeepCopy());
    }//adds a gameboard to local steps
    public void nextStep(){
        removeAllErrors();
        highlightAll();
        currentStep++;
        addStep(logic.getNextStep(inputToGameBoard()));
        updateAllSquares(steps.get(currentStep));
        if(BoardValidity.isSolvedSudoku(inputToGameBoard())){
            youWin();
        }
    }//finds the logical next step

    private void removeAllErrors() {
        for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
            for(int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                if(Window.inputGameBoard[row][col].getBgColor().equals(Color.RED)){
                    Window.inputGameBoard[row][col].setValue("");
                    Window.inputGameBoard[row][col].resetBgColor();
                }
            }
        }
    }//remove all cells with the color red
    public GameBoard getCurrentStep(){
        return steps.get(currentStep);
    }//gets the game-board at the current step

    public void removeStep(int index){
        steps.remove(index);
    }//removes the step at the index
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
    public void lockValues (){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(!Window.inputGameBoard[r][c].getValue().equals("")){
                    Window.inputGameBoard[r][c].setEditable(false);
                }
            }

        }
    }
    public void highlightError(){
        if(!correctPosition(row,col) && !createMode)
            Window.inputGameBoard[row][col].setBgColor(Color.red);
    }// if entered number is not correct it highlights the square red

    private boolean correctPosition(int r, int c) {
        return Window.inputGameBoard[r][c].getValue().equals(String.valueOf(solvedSudoku.board[r][c].getValue()))
                || Window.inputGameBoard[r][c].getValue().equals("");
    }//checks if a square in in the correct position


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
    }//gives highlights to aid the player and point out mistakes
    public void resetHighlight(){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(correctPosition(r,c) || createMode)
                    Window.inputGameBoard[r][c].resetBgColor();
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    for(int note = 1; note <= SudokuMain.BOARD_SIZE; note++){
                        Window.inputGameBoard[r][c].unboldNote(String.valueOf(note));
                    }
                }
            }
        }
    }//resets the board to its original highlights; no highlight
    private void highlightValue() {
        String currentValue = getCurrentValue();
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(Window.inputGameBoard[r][c].getValue().equals(currentValue) && (correctPosition(r,c) || createMode)){
                    Window.inputGameBoard[r][c].setBgColor(new Color(187, 162, 255));
                }
                if(!Window.inputGameBoard[r][c].notesIsEmpty()){
                    Window.inputGameBoard[r][c].boldNote(currentValue);
                }
            }
        }
    }//highlights all the same value selected

    private void highlightBox() {
        int x = row/3 * 3;
        int y = col/3 * 3;
        for(int r = x; r < x + 3; r++){
            for(int c = y; c < y + 3; c++){
                if(correctPosition(r,c) || createMode)
                    Window.inputGameBoard[r][c].setBgColor(new Color(219, 205, 255));
            }
        }
    }//highlights the box of selected

    private void highlightCol() {
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            if(correctPosition(r,col) || createMode)
                Window.inputGameBoard[r][col].setBgColor(new Color(219, 205, 255));
        }
    }//highlights the col selected

    private void highlightRow() {
        for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
            if(correctPosition(row,c) || createMode)
                Window.inputGameBoard[row][c].setBgColor(new Color(219, 205, 255));
        }
    }//highlights the row selected

    public void setValue(String value){
        Window.inputGameBoard[row][col].setValue(value);
    }//sets the value in the input board
    public void addValue(String value, boolean noteMode){
        if(createMode){//during creating mode the value will be set and notes will not be allowed
            Window.inputGameBoard[row][col].setValue(value);
            highlightAll();
            return;
        }
        if(Window.inputGameBoard[row][col].getPlayerPressed())//if its placed by a player change the font to blue
            Window.inputGameBoard[row][col].setValueColor(Color.BLUE);
        if(!noteMode){
            Window.inputGameBoard[row][col].addValue(value);
            highlightAll();
            GameBoard currentBoard = inputToGameBoard();
            if(!value.equals("")){
                NoteHandler.removeConflictingNotes(currentBoard,getRow(),getCol(),Integer.parseInt(value));
                updateAllNotes(currentBoard);
            }//if the inputted value is not 0/empty string then remove all the conflicting notes
            addStep(currentBoard);
        }
        else{//if it is note mode then just set the note
            Window.inputGameBoard[row][col].setNote(value);
            addStep(inputToGameBoard());
        }
        currentStep++;
        if(BoardValidity.isSolvedSudoku(inputToGameBoard())){
            youWin();
        }//check for complete board
    }
    public void youWin(){
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        win.setPreferredSize(new Dimension(600, 200));
        win.setMaximumSize(new Dimension(600,200));
        win.setMinimumSize(new Dimension(600,200));
        win.requestFocus();
        win.setLocationRelativeTo(null);
        JLabel label = new JLabel("YOU WIN!");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font(Window.fontName, Font.BOLD, 60));
        win.add(label);
        win.setVisible(true);
    }//creates a win screen
    public String getCurrentValue(){
        return Window.inputGameBoard[row][col].getValue();
    }//gets the current value of selected square

    public void setRow(int row) {
        this.row = row;
    }//sets the row number of selected

    public int getRow() {
        return row;
    }//gets the row number of selected

    public void setCol(int col) {
        this.col = col;
    }//sets the column number of selected

    public int getCol() {
        return col;
    }//gets the column number of selected
}
