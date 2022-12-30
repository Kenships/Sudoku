import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Window {
    private boolean noteMode = false;
    public static SudokuCell[][] inputGameBoard = new SudokuCell[SudokuMain.BOARD_SIZE][SudokuMain.BOARD_SIZE];
    public static final Font font = new Font("Times New Roman", 17, 30);
    public Window(int width, int height, String title, SudokuController sudokuController){
        JFrame frame = new JFrame(title);
        JToggleButton toggleButton = new JToggleButton("Edit Notes");
        JPanel panel = new JPanel();
        JPanel inputNumbers = new JPanel();

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        frame.setLayout(null);
        frame.add(panel);
        frame.add(toggleButton);

        panel.setLayout(new GridLayout(9,9));
        panel.setBounds(10,10,1000,1000);
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++ ){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                inputGameBoard[r][c] = new SudokuCell(r,c,sudokuController);
                panel.add(inputGameBoard[r][c]);
            }
        }

        inputNumbers.setLayout(new GridLayout(3,3));
        inputNumbers.setBounds(3 * width/4 - 400, height/2 - 300, 500,500);
        frame.add(inputNumbers);
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                JButton button = new JButton();
                button.setText(String.valueOf(r * 3 + 1 + c%3));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sudokuController.addValue(button.getText(),noteMode);
                    }
                });
                inputNumbers.add(button);
            }
        }

        toggleButton.setBounds(3 * width/4 - 400, height/2 - 450, 200,100);
        toggleButton.addActionListener(e -> {
            noteMode = !noteMode;
            for(int row = 0; row < SudokuMain.BOARD_SIZE; row++){
                for (int col = 0; col < SudokuMain.BOARD_SIZE; col++){
                    SudokuCell current = inputGameBoard[row][col];
                    if(current.getValue().equals("") && current.notesIsEmpty()){
                        current.setShowNotes();
                    }
                }
            }
        });
        frame.setVisible(true);

    }
}
