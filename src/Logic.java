import java.util.ArrayList;

public class Logic {
    private static GameBoard gameBoard;
    private static final int BOARD_SIZE = SudokuMain.BOARD_SIZE;
    public Logic(GameBoard gameBoard){

        Logic.gameBoard = gameBoard;
        gameBoard.output();
        solveSudoku();
    }

    private void solveSudoku() {
        NoteHandler.addAllNotes(gameBoard);
        int steps = 0;
        while(!BoardValidity.isFullyFilledIn(gameBoard) && steps < 69){
            findNakedSingles();
            findAllDominoes();
            steps++;
        }
        gameBoard.output();
        System.out.println("steps: " + steps);
        System.out.println(BoardValidity.isValidSudoku(gameBoard));
    }
    private void findAllDoubles(){
        findNakedDoubles();
    }

    private void findNakedDoubles() {
        for(int r = 0; r < BOARD_SIZE; r++){
            for(int c = 0; c < BOARD_SIZE; c++){
                GameSquare current = gameBoard.board[r][c];
                if(current.getNumberOfNotes() == 2){

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
            int count = 0;
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){
                if(section[i].notes[note - 1]){
                    hasNote.add(section[i]);
                    count++;
                    section[i].value = -1;
                }
            }
            if(count == 2 || count == 3){
                if(NoteHandler.notesInSameRow(hasNote))
                    NoteHandler.removeConflictingNote(gameBoard.getRow(hasNote.get(0).row),note);
                else if(NoteHandler.notesInSameCol(hasNote))
                    NoteHandler.removeConflictingNote(gameBoard.getCol(hasNote.get(0).col),note);
            }
            removeNegativesValues(section);
        }
    }
    private void findLineDomino(GameSquare[] section) {
        for(int note = 1; note <= BOARD_SIZE; note++){
            int count = 0;
            ArrayList<GameSquare> hasNote = new ArrayList<>();
            for(int i = 0; i < section.length; i++){
                if(section[i].notes[note - 1]){
                    section[i].value = -1;
                    hasNote.add(section[i]);
                    count++;
                }
            }
            if(count == 2 || count == 3){
                if(NoteHandler.notesInSameBox(hasNote)){
                    NoteHandler.removeConflictingNote(gameBoard.getBox(hasNote.get(0).row,hasNote.get(0).col),note);
                }
            }
            removeNegativesValues(section);
        }
    }
    private void removeNegativesValues(GameSquare[] section) {
        for (GameSquare gameSquare : section) {
            if (gameSquare.value == -1) {
                gameSquare.value = 0;
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
                int note = 0;
                if(current.value == 0){
                    for(int i = 0; i < BOARD_SIZE; i++){
                        if(current.notes[i]){
                            if(findFixedPosition(gameBoard,r,c,i + 1)){
                                current.addValue(i + 1);
                                System.out.println("value added: " + (i + 1));
                                NoteHandler.removeConflictingNotes(gameBoard,r,c,current.value);
                                gameBoard.output();
                                count = 0;
                                break;
                            }
                            else{
                                note = i + 1;
                                count++;
                            }
                        }
                    }
                    if(count == 1){
                        current.addValue(note);
                        NoteHandler.removeConflictingNotes(gameBoard,r,c,note);
                        gameBoard.output();
                    }
                }
            }
        }
    }
}
