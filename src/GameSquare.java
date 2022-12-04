public class GameSquare {
    public boolean[] notes;
    public int value;

    public int row;
    public int col;

    public GameSquare(int row, int col){
        this.row = row;
        this.col = col;
        notes = new boolean[SudokuMain.BOARD_SIZE];
    }
    public GameSquare(int value){
        notes = new boolean[SudokuMain.BOARD_SIZE];
        this.value = value;
    }
    public void addNote(int note){
        notes[note] = true;
    }
    public void addValue(int note){
        removeAllNotes();
        value = note;
    }
    public void removeNote(int note){
        notes[note] = false;
    }
    public int getNumberOfNotes(){
        int count = 0;
        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            if(notes[i]){
                count++;
            }
        }
        return count;
    }
    public void removeAllNotes() {
        for(int note = 0; note < SudokuMain.BOARD_SIZE; note++){
            removeNote(note);
        }
    }

}
