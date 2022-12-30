import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;

public class SudokuCell extends JPanel {
    private JButton valueField;
    private JButton[] noteFields;
    private JPanel notesPanel;
    private boolean showNotes;
    private boolean editable;
    private int row;
    private int col;
    private SudokuController sudokuController;

    public SudokuCell(int row, int col, SudokuController sudokuController) {
        editable = true;
        this.row = row;
        this.col = col;
        this.sudokuController = sudokuController;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK));
        valueField = new JButton();
        valueField.setHorizontalAlignment(JTextField.CENTER);
        valueField.setBackground(Color.WHITE);
        valueField.setBorderPainted(false);
        valueField.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(valueField, c);

        noteFields = new JButton[9];
        notesPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            noteFields[i] = new JButton();
            noteFields[i].setHorizontalAlignment(JTextField.CENTER);
            noteFields[i].setBorderPainted(false);
            noteFields[i].setBackground(Color.WHITE);
            noteFields[i].setFont(new Font("Times New Roman",Font.PLAIN, 20));
            noteFields[i].setMargin(new Insets(0,0,0,0));
            noteFields[i].addActionListener(e -> {
                sudokuController.setRow(row);
                sudokuController.setCol(col);
            });
            notesPanel.add(noteFields[i]);
        }
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(notesPanel, c);

        showNotes = false;
        valueField.setEnabled(true);
        notesPanel.setVisible(false);

        valueField.addActionListener(e -> {
            sudokuController.setRow(row);
            sudokuController.setCol(col);
        });
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
        if(!editable)
            return;
        clearNotes();
        notesPanel.setVisible(false);
        valueField.setVisible(true);
        if(!value.equals(valueField.getText())){
            valueField.setEnabled(editable);
            valueField.setText(value);
        }
        else
            valueField.setText("");
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

    public void clearNotes() {
        for (JButton noteField : noteFields) {
            noteField.setText("");
        }
    }
    public void setShowNotes() {
        showNotes ^= true;
        valueField.setVisible(!showNotes);
        notesPanel.setVisible(showNotes);
    }
}
