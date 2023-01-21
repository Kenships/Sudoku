import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashSet;

public class KeyInput implements KeyListener {

    private HashSet<String> possibleInput = new HashSet<>(Arrays.asList("1","2","3","4","5","6","7","8","9"));
    private SudokuController sudokuController;
    private Window window;

    public KeyInput(SudokuController sudokuController, Window window){
        this.sudokuController = sudokuController;
        this.window = window;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(possibleInput.contains(String.valueOf(e.getKeyChar()))){
            if(Window.inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].getValue().equals(""))
                Window.inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].setPlayerPressed(true);
            sudokuController.addValue(String.valueOf(e.getKeyChar()),Window.noteMode);
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_E){
            Window.inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].setPlayerPressed(false);
            sudokuController.addValue("", false);
            sudokuController.addStep(sudokuController.inputToGameBoard());
            sudokuController.currentStep++;
            sudokuController.highlightAll();
        }
        if (e.getKeyCode() == KeyEvent.VK_N){
            window.toggleNotes();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            if(sudokuController.getRow() > 0)
                sudokuController.setRow(sudokuController.getRow() - 1);
            sudokuController.highlightAll();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            if(sudokuController.getRow() < SudokuMain.BOARD_SIZE - 1)
                sudokuController.setRow(sudokuController.getRow() + 1);
            sudokuController.highlightAll();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if(sudokuController.getCol() > 0)
                sudokuController.setCol(sudokuController.getCol() - 1);
            sudokuController.highlightAll();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(sudokuController.getCol() < SudokuMain.BOARD_SIZE - 1)
                sudokuController.setCol(sudokuController.getCol() + 1);
            sudokuController.highlightAll();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

