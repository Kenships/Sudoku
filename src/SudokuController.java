public class SudokuController {
    private int row;
    private int col;
    public  void updateAllSquares(GameBoard gameBoard){
        updateAllNotes(gameBoard);
        updateAllValues(gameBoard);
    }
    public void updateAllNotes(GameBoard gameBoard){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                Window.inputGameBoard[r][c].clearNotes();
                for(int note : gameBoard.board[r][c].notes){
                    Window.inputGameBoard[r][c].setNote(Integer.toString(note));
                }
            }
        }
    }
    public void updateAllValues(GameBoard gameBoard){
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for (int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].getValue() != 0){
                    Window.inputGameBoard[r][c].setValue(String.valueOf(gameBoard.board[r][c].getValue()));
                }
            }
        }
    }
    public void setValue(String value){
        Window.inputGameBoard[row][col].setValue(value);
        Window.inputGameBoard[row][col].setEditable(false);
    }
    public void addValue(String value, boolean noteMode){
        if(!noteMode){
            Window.inputGameBoard[row][col].setValue(value);
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
