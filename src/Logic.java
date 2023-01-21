import org.omg.PortableInterceptor.INACTIVE;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Logic {
    private int changeDescription;
    /**
    0 = naked single
    1 = domino
    2 = double
    3 = XY wing
    4 = swordfish
    */

    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE;
    private SudokuController sudokuController;
    public ArrayList<GameBoard> localSteps = new ArrayList<>();
    private ArrayList<GameBoard> history = new ArrayList<>();
    private boolean guessing;
    private GameBoard beforeGuess;
    public Logic(GameBoard gameBoard, SudokuController sudokuController){
        beforeGuess = NoteHandler.addAllNotes(gameBoard.makeDeepCopy());
        solveSudoku(beforeGuess);
        this.sudokuController = sudokuController;
    }


    public Logic(SudokuController sudokuController){
        this.sudokuController = sudokuController;
    }
    public GameBoard getNextStep(GameBoard gameBoard){
        localSteps.clear();
        if(BoardValidity.isValidSudoku(gameBoard)){
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
            return gameBoard;
        }
        //checks if the input gameBoard is equal to the solved version of the board before guessing
        boolean temp = gameBoard.equals(solveSudoku(beforeGuess)) && !BoardValidity.valuesAreFilled(gameBoard);
        System.out.println(temp);
        if (temp) {
            history.clear();
            guess(gameBoard);
            gameBoard.output();
            beforeGuess = solveSudoku(gameBoard.makeDeepCopy());
        }
        else{
            findNakedSingles(gameBoard);
            findAllDominoes(gameBoard);
            findAllDoubles(gameBoard);
        }
        if(localSteps.isEmpty()){
            localSteps.add(NoteHandler.addAllNotes(gameBoard.makeDeepCopy()));
        }
        return localSteps.get(0);
    }

    private boolean historyContains(GameBoard gameBoard) {
        if(history.isEmpty()){
            return false;
        }//does not contain game Board
        for (GameBoard board : history){
            if(board.equals(gameBoard)){
                return true;
            }
        }
        return false;
    }
    public GameBoard solveSudoku(GameBoard gameBoard){
        int steps = 0;
        while(!BoardValidity.valuesAreFilled(gameBoard) && steps < 69){
            findNakedSingles(gameBoard);
            findAllDominoes(gameBoard);
            findAllDoubles(gameBoard);
            steps++;
        }
        return gameBoard;
    }

    public GameBoard masterSolveSudoku(GameBoard gameBoard) {
        localSteps.clear();
        NoteHandler.addAllNotes(gameBoard);
        localSteps.add(gameBoard.makeDeepCopy());
        int steps = 0;
        while(true){
            GameBoard solved = solveSudoku(gameBoard);
            if(BoardValidity.isValidSudoku(solved)){
                break;
            }
            else{
                guess(gameBoard);
            }
            steps++;
        }
        gameBoard.output();
        return gameBoard;
    }
    private void guess(GameBoard gameBoard){
        Stack<GameBoard> tree = new Stack<>();
        if(!historyContains(gameBoard)) {
            history.add(gameBoard.makeDeepCopy());
            guessing = true;
            tree.push(gameBoard.makeDeepCopy());
        }
        else{
            return;
        }
        ArrayList<GameBoard> possibleValues = new ArrayList<>();
        int initRow = 0;
        int initCol = 0;
        int min = 10, row = 0, col = 0;
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].notes.size() < min && !gameBoard.board[r][c].notes.isEmpty()){
                    min = gameBoard.board[r][c].notes.size();
                    initRow = r;
                    initCol = c;
                }
            }
        }
        while(!tree.isEmpty()){
            GameBoard current = solveSudoku(tree.pop());
            //find square with the lowest amount of notes
            if(!BoardValidity.notesAreFilled(current)){
                System.out.println("uh oh");
            }
            else if(!BoardValidity.valuesAreFilled(current)){
                min = 10;
                for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
                    for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                        if(current.board[r][c].notes.size() < min && current.board[r][c].getValue() == 0){
                            min = current.board[r][c].notes.size();
                            row = r;
                            col = c;
                        }
                    }
                }
                for(Integer note : current.board[row][col].notes){
                    GameBoard temp = current.makeDeepCopy();
                    temp.board[row][col].setValue(note);
                    NoteHandler.removeConflictingNotes(temp,row,col,note);
                    if(!historyContains(temp)){
                        history.add(temp.makeDeepCopy());
                        tree.push(temp);
                    }
                }
            }
            else{
                possibleValues.add(current.makeDeepCopy());
            }
        }
        guessing = false;
        if(possibleValues.size() > 1 || possibleValues.isEmpty()){
            System.out.println("impossible");
        }
        else{
            gameBoard.board[initRow][initCol].setValue(possibleValues.get(0).board[initRow][initCol].getValue());
            NoteHandler.removeConflictingNotes(gameBoard, initRow, initCol, possibleValues.get(0).board[initRow][initCol].getValue());
            System.out.println("Guessed");
            gameBoard.output();
            localSteps.add(gameBoard);
        }
    }
    private void findAllDoubles(GameBoard gameBoard){
        findNakedDoubles(gameBoard);
        findHiddenDoubles(gameBoard);
    }

    private void findHiddenDoubles(GameBoard gameBoard) {

    }

    private void findNakedDoubles(GameBoard gameBoard) {
        for(int rowNumber = 0; rowNumber < BOARD_SIZE; rowNumber++){
            GameSquare[] row = gameBoard.getRow(rowNumber);
            findSectionDouble(row, gameBoard);
        }
        for(int colNumber = 0; colNumber < BOARD_SIZE; colNumber++){
            GameSquare[] col = gameBoard.getCol(colNumber);
            findSectionDouble(col, gameBoard);
        }
        for(int boxNumber = 0; boxNumber < BOARD_SIZE; boxNumber++){
            int[] boxCoordinates = GameBoard.getBoxCoordinates(boxNumber);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findSectionDouble(box, gameBoard);
        }
    }

    private void findSectionDouble(GameSquare[] section, GameBoard gameBoard) {
        ArrayList<GameSquare> possibleDoubles = new ArrayList<>();
        for(int i = 0; i < section.length; i++){
            GameSquare current = section[i];
            if(current.notes.size() == 2){
                possibleDoubles.add(current);
            }
        }
        if(possibleDoubles.size() > 1) {
            for(int i = 0; i < possibleDoubles.size() - 1; i++){
                for(int j = i + 1; j < possibleDoubles.size(); j++){
                    GameSquare square1 = possibleDoubles.get(i);
                    GameSquare square2 = possibleDoubles.get(j);
                    if(NoteHandler.notesAreEqual(possibleDoubles.get(i),possibleDoubles.get(j))){
                        for(int note = 1; note <= BOARD_SIZE; note++){
                            if(square1.containsNote(note)){
                                square1.addValue(-1);
                                square2.addValue(-1);
                                if(NoteHandler.removeConflictingNote(section,note) && !guessing){
                                    localSteps.add(gameBoard.makeDeepCopy());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void findAllDominoes(GameBoard gameBoard){
        for(int rowNumber = 0; rowNumber < BOARD_SIZE; rowNumber++){
            GameSquare[] row = gameBoard.getRow(rowNumber);
            findLineDomino(row, gameBoard);
        }
        for(int colNumber = 0; colNumber < BOARD_SIZE; colNumber++){
            GameSquare[] col = gameBoard.getCol(colNumber);
            findLineDomino(col, gameBoard);
        }
        for(int boxNumber = 0; boxNumber < BOARD_SIZE; boxNumber++){
            int[] boxCoordinates = GameBoard.getBoxCoordinates(boxNumber);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findBoxDomino(box, gameBoard);
        }
    }

    private void findBoxDomino(GameSquare[] section, GameBoard gameBoard) {
        for(int note = 1; note <= BOARD_SIZE; note++){
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){
                if(section[i].containsNote(note)){
                    hasNote.add(section[i]);
                    section[i].addValue(-1);
                }
            }
            if(hasNote.size() == 2 || hasNote.size() == 3){
                if(NoteHandler.notesInSameRow(hasNote)){
                    if(NoteHandler.removeConflictingNote(gameBoard.getRow(hasNote.get(0).getRowNumber()),note) && !guessing){
                        localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
                else if(NoteHandler.notesInSameCol(hasNote)){
                    if(NoteHandler.removeConflictingNote(gameBoard.getCol(hasNote.get(0).getColNumber()),note) && !guessing){
                        localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
            }
            removeNegativesValues(section);
        }
    }
    private void findLineDomino(GameSquare[] section, GameBoard gameBoard) {
        for(int note = 1; note <= BOARD_SIZE; note++){
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){
                if(section[i].containsNote(note)){
                    section[i].addValue(-1);
                    hasNote.add(section[i]);
                }
            }
            if(hasNote.size() == 2 || hasNote.size() == 3){
                if(NoteHandler.notesInSameBox(hasNote)){
                    if(NoteHandler.removeConflictingNote(gameBoard.getBox(hasNote.get(0).getRowNumber(),hasNote.get(0).getColNumber()),note) && !guessing){
                        localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
            }
            removeNegativesValues(section);
        }
    }
    private void removeNegativesValues(GameSquare[] section) {
        for (GameSquare gameSquare : section) {
            if (gameSquare.getValue() == -1) {
                gameSquare.addValue(0);
            }
        }
    }
    private boolean findFixedPosition(GameBoard gameBoard, int r, int c, int note) {
        return NoteHandler.findNumberOfNotes(gameBoard.getRow(r), note) == 1
                || NoteHandler.findNumberOfNotes(gameBoard.getCol(c), note) == 1
                || NoteHandler.findNumberOfNotes(gameBoard.getBox(r, c), note) == 1;
    }

    private void findNakedSingles(GameBoard gameBoard) {
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                GameSquare current = gameBoard.board[r][c];
                int count = 0;
                int fixedNote = 0;
                if(current.getValue() == 0){
                    for(int note = 1; note <= BOARD_SIZE; note++){
                        if(current.containsNote(note)){
                            if(findFixedPosition(gameBoard,r,c,note)){
                                current.setValue(note);
//                                System.out.println("value added: " + (note));
                                NoteHandler.removeConflictingNotes(gameBoard,r,c,current.getValue());
                                if(!guessing) localSteps.add(gameBoard.makeDeepCopy());
                                count = 0;
                                break;
                            }
                            else{
                                fixedNote = note;
                                count++;
                            }
                        }
                    }
                    if(count == 1){
                        current.setValue(fixedNote);
//                        System.out.println("value added: " + (fixedNote));
                        NoteHandler.removeConflictingNotes(gameBoard,r,c,fixedNote);
                        if(!guessing) localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
            }
        }
    }
}
