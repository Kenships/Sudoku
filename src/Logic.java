import java.util.ArrayList;

public class Logic {
    private static GameBoard gameBoard;
    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE;
    private SudokuController sudokuController;
    public Logic(GameBoard gameBoard, SudokuController sudokuController){
        this.gameBoard = gameBoard;
        this.sudokuController = sudokuController;
        gameBoard.output();
        solveSudoku();
    }

    private void solveSudoku() {
        NoteHandler.addAllNotes(gameBoard);
        sudokuController.updateAllSquares(gameBoard);
        int steps = 0;
        while(!BoardValidity.isFullyFilledIn(gameBoard) && steps < 69){
            findNakedSingles();
            findAllDominoes();
            findAllDoubles();
            steps++;
        }
        gameBoard.output();
        System.out.println("steps: " + steps);
        System.out.println(BoardValidity.isValidSudoku(gameBoard));
    }
    private void findAllDoubles(){
        findNakedDoubles();
        findHiddenDoubles();
    }

    private void findHiddenDoubles() {

    }

    private void findNakedDoubles() {
        for(int rowNumber = 0; rowNumber < BOARD_SIZE; rowNumber++){
            GameSquare[] row = gameBoard.getRow(rowNumber);
            findSectionDouble(row);
        }
        for(int colNumber = 0; colNumber < BOARD_SIZE; colNumber++){
            GameSquare[] col = gameBoard.getCol(colNumber);
            findSectionDouble(col);
        }
        for(int boxNumber = 0; boxNumber < BOARD_SIZE; boxNumber++){
            int[] boxCoordinates = GameBoard.getBoxCoordinates(boxNumber);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findSectionDouble(box);
        }
    }

    private void findSectionDouble(GameSquare[] section) {
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
                                NoteHandler.removeConflictingNote(section,note);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findAllDominoes(){
        for(int rowNumber = 0; rowNumber < BOARD_SIZE; rowNumber++){
            GameSquare[] row = gameBoard.getRow(rowNumber);
            findLineDomino(row);
        }
        for(int colNumber = 0; colNumber < BOARD_SIZE; colNumber++){
            GameSquare[] col = gameBoard.getCol(colNumber);
            findLineDomino(col);
        }
        for(int boxNumber = 0; boxNumber < BOARD_SIZE; boxNumber++){
            int[] boxCoordinates = GameBoard.getBoxCoordinates(boxNumber);
            GameSquare[] box = gameBoard.getBox(boxCoordinates[0],boxCoordinates[1]);
            findBoxDomino(box);
        }
    }

    private void findBoxDomino(GameSquare[] section) {
        for(int note = 1; note <= BOARD_SIZE; note++){
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){
                if(section[i].containsNote(note)){
                    hasNote.add(section[i]);
                    section[i].addValue(-1);
                }
            }
            if(hasNote.size() == 2 || hasNote.size() == 3){
                if(NoteHandler.notesInSameRow(hasNote))
                    NoteHandler.removeConflictingNote(gameBoard.getRow(hasNote.get(0).getRowNumber()),note);
                else if(NoteHandler.notesInSameCol(hasNote))
                    NoteHandler.removeConflictingNote(gameBoard.getCol(hasNote.get(0).getColNumber()),note);
            }
            removeNegativesValues(section);
        }
    }
    private void findLineDomino(GameSquare[] section) {
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
                    NoteHandler.removeConflictingNote(gameBoard.getBox(hasNote.get(0).getRowNumber(),hasNote.get(0).getColNumber()),note);
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

    private void findNakedSingles() {
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
                                System.out.println("value added: " + (note));
                                NoteHandler.removeConflictingNotes(gameBoard,r,c,current.getValue());
                                gameBoard.output();
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
                        System.out.println("value added: " + (fixedNote));
                        NoteHandler.removeConflictingNotes(gameBoard,r,c,fixedNote);
                        gameBoard.output();
                    }
                }
            }
        }
    }
}
