import java.awt.*;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.metal.MetalButtonUI;

public class SudokuCell extends JPanel {
    /** TL;DR
    Class to store all the information for the front end of the sudoku cell
     */
    private JButton valueField;
    private JButton[] noteFields;
    private JPanel notesPanel;
    private boolean showNotes;
    private boolean editable;

    private boolean playerPlacedNumber;
    private int row;
    private int col;
    private SudokuController sudokuController;

    public SudokuCell(int row, int col, SudokuController sudokuController, Window window) {
        playerPlacedNumber = false;
        editable = true;
        this.row = row;
        this.col = col;
        this.sudokuController = sudokuController;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); //dynamic jpanel that allows adding components
        setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK)); // nice black border
        valueField = new JButton();
        valueField.setHorizontalAlignment(JTextField.CENTER);
        valueField.setBackground(Color.WHITE);
        valueField.setForeground(Color.BLUE);
        valueField.setBorderPainted(false);
        valueField.setFont(new Font("Book Antiqua", Font.PLAIN, 50));
        valueField.setFocusPainted(false);
        valueField.setUI (new MetalButtonUI() {
            protected void paintButtonPressed (Graphics g, AbstractButton b) { }
        });//makes it so the button does not have a click animation
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        //sets the constraints for the size of the component in relation to others
        c.fill = GridBagConstraints.BOTH;
        add(valueField, c);
        valueField.addActionListener(e -> {
            sudokuController.setRow(row);
            sudokuController.setCol(col);
            sudokuController.highlightAll();
        });
        valueField.addKeyListener(new KeyInput(sudokuController, window));

        noteFields = new JButton[9];
        notesPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            noteFields[i] = new JButton();
            noteFields[i].setHorizontalAlignment(JTextField.CENTER);
            noteFields[i].setBorderPainted(false);
            noteFields[i].setBackground(Color.WHITE);
            noteFields[i].setForeground(new Color(71, 72, 72));
            noteFields[i].setFont(new Font(Window.fontName, Font.PLAIN, 20));
            noteFields[i].setFocusPainted(false);
            noteFields[i].setMargin(new Insets(0,0,0,0));
            noteFields[i].setUI (new MetalButtonUI() {
                protected void paintButtonPressed (Graphics g, AbstractButton b) { }
            });
            noteFields[i].addActionListener(e -> {
//                System.out.println("clicked");
                sudokuController.setRow(row);
                sudokuController.setCol(col);
                sudokuController.highlightAll();
            });
            noteFields[i].addKeyListener(new KeyInput(sudokuController, window));
            notesPanel.add(noteFields[i]);
        }
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(notesPanel, c);

        showNotes = false;
        notesPanel.setVisible(false);

    }
    public HashSet<Integer> getNotes(){
        HashSet<Integer> allNotes = new HashSet<>();
        for(int note = 0; note < SudokuMain.BOARD_SIZE; note++){
            if(!noteFields[note].getText().equals("")){
//                System.out.println("added" + (note + 1));
                allNotes.add(note + 1);
            }
        }
        return allNotes;
    }
    public void setPlayerPressed(boolean playerPressed) {
        playerPlacedNumber = playerPressed;
    }
    public boolean getPlayerPressed(){
        return playerPlacedNumber;
    }
    public void clickNote(){
        noteFields[0].doClick();
    }
    public void resetValueColor(){
        setValueColor(Color.BLUE);
    }
    public void setValueColor(Color color){
        valueField.setForeground(color);
    }
    public void resetBgColor(){
        setBgColor(Color.WHITE);
    }
    public Color getBgColor(){
        return valueField.getBackground();
    }
    public void setBgColor(Color color){
        valueField.setBackground(color);
        for(JButton b : noteFields){
            b.setBackground(color);
        }
    }
    public boolean notesIsEmpty(){
        boolean result = true;
        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            if(!noteFields[i].getText().equals("")){
                result = false;
            }
        }
        return result;
    }
    public void setEditable(boolean editable){
        this.editable = editable;
    }
    public boolean isEditable(){
        return editable;
    }
    public void setValue(String value) {
        clearNotes();
        notesPanel.setVisible(false);
        valueField.setVisible(true);
        valueField.setText(value);
    }
    public void addValue(String value) {
        if(!editable && !playerPlacedNumber)//if uneditable then you can't change this value
            return;
        clearNotes();
        notesPanel.setVisible(false);
        valueField.setVisible(true);
        if(!value.equals(valueField.getText())){
            valueField.setText(value);
        }
        else{
            valueField.setText("");
        }
        sudokuController.highlightAll();
    }

    public String getValue() {
        return valueField.getText();
    }
    public void setNote(String value){
        if(!editable)
            return;
        notesPanel.setVisible(true);
        valueField.setVisible(false);
        if(!value.equals(noteFields[Integer.parseInt(value) - 1].getText()))
            noteFields[Integer.parseInt(value) - 1].setText(value);
        else
            noteFields[Integer.parseInt(value) - 1].setText("");
    }

    public void setNotes(int[] notes) {
        clearNotes();
        for (int i = 0; i < notes.length; i++) {
            noteFields[notes[i] - 1].setText("" + notes[i]);
        }
    }
    public void boldNote(String note){
        noteFields[Integer.parseInt(note) - 1].setFont(new Font(Window.fontName, Font.BOLD, 20));
        noteFields[Integer.parseInt(note) - 1].setForeground(Color.BLACK);
    }
    public void unboldNote(String note){
        noteFields[Integer.parseInt(note) - 1].setFont(new Font(Window.fontName, Font.PLAIN, 20));
        noteFields[Integer.parseInt(note) - 1].setForeground(new Color(71, 72, 72));
    }
    public void clearNotes() {
        for (JButton noteField : noteFields) {
            noteField.setText("");
        }
    }
    public void setShowNotes() {
        showNotes ^= true; //flips between true and false;
        valueField.setVisible(!showNotes);
        notesPanel.setVisible(showNotes);
    }
}
