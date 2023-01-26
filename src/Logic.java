import java.util.ArrayList;
import java.util.Stack;

public class Logic {
    /** TL;DR
    oh boy...
    */
    private int changeDescription; // was going to implement but didn't have time, in the meantime you can print it...

    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE;
    private SudokuController sudokuController;
    public ArrayList<GameBoard> localSteps;
    private ArrayList<GameBoard> history;//stores history for guess
    private boolean guessing;
    private GameBoard beforeGuess;
    public Logic(GameBoard gameBoard, SudokuController sudokuController){
        this.sudokuController = sudokuController;
        localSteps = new ArrayList<>();
        history = new ArrayList<>();
        beforeGuess = NoteHandler.addAllNotes(gameBoard.makeDeepCopy());
        solveSudoku(beforeGuess);
    }//constructor


    public Logic(){
        localSteps  = new ArrayList<>();
        history = new ArrayList<>();
    }//generic constructor
    public GameBoard getNextStep(GameBoard gameBoard){
        localSteps.clear();
        if(BoardValidity.isSolvedSudoku(gameBoard)){
            return gameBoard;
        }
        //checks if the input gameBoard is equal to the solved version of the board before guessing
        boolean temp = gameBoard.equals(solveSudoku(beforeGuess)) && !BoardValidity.valuesAreFilled(gameBoard);
        System.out.println(temp);
        if (temp) {
            history.clear();
            guess(gameBoard);
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
    }//tries all the techniques and pushes out the easiest one

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
    }//checks if history contains this game board
    public GameBoard solveSudoku(GameBoard gameBoard){
        int steps = 0;
        while(!BoardValidity.valuesAreFilled(gameBoard) && steps < 69){
            findNakedSingles(gameBoard);
            findAllDominoes(gameBoard);
            findAllDoubles(gameBoard);
            steps++;
        }
        return gameBoard;
    }//solves the sudoku at the current state without guessing

    public GameBoard masterSolveSudoku(GameBoard gameBoard) {
        localSteps.clear();
        NoteHandler.addAllNotes(gameBoard);
        localSteps.add(gameBoard.makeDeepCopy());
        int steps = 0;
        boolean possible = true;
        while(true && possible){
            GameBoard solved = solveSudoku(gameBoard);
            if(BoardValidity.isSolvedSudoku(solved)){
                break;
            }
            else{
                possible = guess(gameBoard);
            }
            steps++;
        }
        return gameBoard;
    }// fully solves any board
    private boolean guess(GameBoard gameBoard){// DYNAMIC PROGRAMING TIME!!!
        history.clear();// resets history
        Stack<GameBoard> tree = new Stack<>();//stack for deep first search to find the solution
        history.add(gameBoard.makeDeepCopy());
        guessing = true;//when guessing is true nothing will be added to local steps
        tree.push(gameBoard.makeDeepCopy());// pushes the original game board into the stack
        ArrayList<GameBoard> possibleValues = new ArrayList<>();//possible boards
        int initRow = 0;//the initial row
        int initCol = 0;//the initial col
        //used at the end to create ony a singular step
        int min = 10, row = 0, col = 0;

        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                if(gameBoard.board[r][c].notes.size() < min && !gameBoard.board[r][c].notes.isEmpty()){
                    min = gameBoard.board[r][c].notes.size();
                    initRow = r;
                    initCol = c;
                }
            }
        }//finds the box the first guess will be based apon

        while(!tree.isEmpty() && possibleValues.size() <= 1){//when the tree is empty or there is more than one answer then the loop will break
            GameBoard current = solveSudoku(tree.pop());

            if(!BoardValidity.valuesAreFilled(current) && !BoardValidity.notesAreFilled(current)){//if the notes are fully filled in and there are empty spaces
                min = 10;
                for(int r = 0; r < SudokuMain.BOARD_SIZE; r++){
                    for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                        if(current.board[r][c].notes.size() < min && current.board[r][c].getValue() == 0){
                            min = current.board[r][c].notes.size();
                            row = r;
                            col = c;
                        }
                    }
                }//find square with the lowest amount of notes

                for(Integer note : current.board[row][col].notes){
                    GameBoard temp = current.makeDeepCopy();
                    temp.board[row][col].setValue(note);
                    NoteHandler.removeConflictingNotes(temp,row,col,note);
                    if(!historyContains(temp)){
                        history.add(temp.makeDeepCopy());
                        tree.push(temp);//adds a board with the guess into the stack for further processing
                    }
                }
            }
            else{
                possibleValues.add(current.makeDeepCopy());// the board is fully solved and is added to possible values
            }
        }
        guessing = false;
        if(possibleValues.size() > 1 || possibleValues.isEmpty()){// if there are no possible values or multiple values the puzzle is impossible
            return false;
        }
        else{// otherwise take the value from the possible values and put it in the original board and return that this sudoku is indeed solveable
            gameBoard.board[initRow][initCol].setValue(possibleValues.get(0).board[initRow][initCol].getValue());
            NoteHandler.removeConflictingNotes(gameBoard, initRow, initCol, possibleValues.get(0).board[initRow][initCol].getValue());
            System.out.println("Guessed");
            localSteps.add(gameBoard);
        }
        return true;
    }// guessing
    public void findAllDoubles(GameBoard gameBoard){
        findNakedDoubles(gameBoard);
        findHiddenDoubles(gameBoard);
    }// finds doubles

    public void findHiddenDoubles(GameBoard gameBoard) {

    }//didn't get to this yet...

    public void findNakedDoubles(GameBoard gameBoard) {
        for(int index = 0; index < BOARD_SIZE; index++){
            GameSquare[] row = gameBoard.getRow(index);
            GameSquare[] col = gameBoard.getCol(index);
            int[] boxCoordinates = GameBoard.getBoxCoordinates(index);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findSectionDouble(row, gameBoard);
            findSectionDouble(col, gameBoard);
            findSectionDouble(box, gameBoard);
        }//loops and finds doubles in all sections
    }

    public void findSectionDouble(GameSquare[] section, GameBoard gameBoard) {
        ArrayList<GameSquare> possibleDoubles = new ArrayList<>();
        for(int i = 0; i < section.length; i++){
            GameSquare current = section[i];
            if(current.notes.size() == 2){
                possibleDoubles.add(current);
            }
        }//adds all squares that have exactly two notes
        if(possibleDoubles.size() > 1) {
            //gets every combination of the possible doubles
            for(int i = 0; i < possibleDoubles.size() - 1; i++){
                for(int j = i + 1; j < possibleDoubles.size(); j++){
                    boolean changeOccured = false;
                    GameSquare square1 = possibleDoubles.get(i);
                    GameSquare square2 = possibleDoubles.get(j);
                    if(NoteHandler.notesAreEqual(possibleDoubles.get(i),possibleDoubles.get(j))){
                        for(int note : square1.notes){
                            //when values are set to -1 their notes will not be affected
                            square1.addValue(-1);
                            square2.addValue(-1);
                            if(NoteHandler.removeConflictingNote(section,note) && !guessing){
                                changeOccured = true;
                            }
                        }
                        if(changeOccured)
                            localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
            }
        }
    }

    public void findAllDominoes(GameBoard gameBoard){
        for(int index = 0; index < BOARD_SIZE; index++){
            GameSquare[] row = gameBoard.getRow(index);
            GameSquare[] col = gameBoard.getCol(index);
            int[] boxCoordinates = GameBoard.getBoxCoordinates(index);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findLineDomino(row, gameBoard);
            findLineDomino(col, gameBoard);
            findBoxDomino(box, gameBoard);
        }
    }

    public void findBoxDomino(GameSquare[] section, GameBoard gameBoard) {
        for(int note = 1; note <= BOARD_SIZE; note++){//loops through note values from 1 to 9
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){// loops through each cell of the section
                if(section[i].containsNote(note)){
                    hasNote.add(section[i]);
                    section[i].addValue(-1);
                }
            }
            if(hasNote.size() == 2 || hasNote.size() == 3){
                if(NoteHandler.notesInSameRow(hasNote)){//if there is exactly two or three of a note, and they are in the same row or col
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
    public void findLineDomino(GameSquare[] section, GameBoard gameBoard) {
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
                        changeDescription = 1;
                    }
                }
            }
            removeNegativesValues(section);
        }
    }//does almost the same thing except checks for lines example row and col
    public void removeNegativesValues(GameSquare[] section) {
        for (GameSquare gameSquare : section) {
            if (gameSquare.getValue() == -1) {
                gameSquare.addValue(0);
            }
        }
    }//removes all the negative values from the board
    public boolean findFixedPosition(GameBoard gameBoard, int r, int c, int note) {
        return NoteHandler.findNumberOfNotes(gameBoard.getRow(r), note) == 1
                || NoteHandler.findNumberOfNotes(gameBoard.getCol(c), note) == 1
                || NoteHandler.findNumberOfNotes(gameBoard.getBox(r, c), note) == 1;
    }//finds if a note can only go in one spot in a row, column or box

    public void findNakedSingles(GameBoard gameBoard) {// I jus wanna optimize this so bad but it looks so ugly T-T
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                GameSquare current = gameBoard.board[r][c];
                int fixedNote = 0;
                if(current.getValue() == 0){
                    for(int note : current.notes){//loops through all possible notes
                        if(findFixedPosition(gameBoard,r,c,note)){
                            current.setValue(note);
//                               System.out.println("value added: " + (note));
                            NoteHandler.removeConflictingNotes(gameBoard,r,c,current.getValue());
                            if(!guessing){
                                localSteps.add(gameBoard.makeDeepCopy());
                                changeDescription = 0;
                            }
                            //to show that this is a fixed value;
                            break;
                        }
                        fixedNote = note;
                    }
                    if(current.notes.size() == 1){//if there is only one
                        current.setValue(fixedNote);
                        changeDescription = 1;
                        NoteHandler.removeConflictingNotes(gameBoard,r,c,fixedNote);
                        if(!guessing) localSteps.add(gameBoard.makeDeepCopy());
                    }
                }
            }
        }
    }
}
