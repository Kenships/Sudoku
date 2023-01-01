import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Window extends JFrame{
    public static boolean noteMode = false;
    public static SudokuCell[][] inputGameBoard = new SudokuCell[SudokuMain.BOARD_SIZE][SudokuMain.BOARD_SIZE];
    private JPanel panel = new JPanel();
    private JPanel inputNumbers = new JPanel();
    private JPanel stepControl = new JPanel();
    private JPanel edit = new JPanel();
    private JButton next = new JButton("Next");
    private JButton previous = new JButton("Prev");
    private JButton erase = new JButton();
    private JToggleButton toggleButton = new JToggleButton();
    private JTextField stepSkip = new JTextField();
    private JLabel label = new JLabel("Skip to");
    public Window(int width, int height, String title, SudokuController sudokuController){

        this.setTitle(title);
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        this.setLayout(null);
        this.add(panel);
        this.add(edit);
        this.add(stepControl);
        this.add(stepSkip);
        this.add(label);

        panel.setLayout(new GridLayout(9,9));
        panel.setBounds(10,10,1000,1000);
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++ ){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                inputGameBoard[r][c] = new SudokuCell(r,c,sudokuController, this);
                panel.add(inputGameBoard[r][c]);
            }
        }

        inputNumbers.setLayout(new GridLayout(3,3));
        inputNumbers.setBounds(3 * width/4 - 200, height/2 - 300, 500,500);
        this.add(inputNumbers);
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                JButton button = new JButton();
                button.setFont(new Font("Times New Roman", Font.PLAIN, 50));
                button.setText(String.valueOf(r * 3 + 1 + c%3));
                button.setBackground(Color.WHITE);
                button.addActionListener(e -> {
                    sudokuController.addValue(button.getText(),noteMode);
                });
                button.addKeyListener(new KeyInput(sudokuController, this));
                inputNumbers.add(button);
            }
        }

        stepControl.setLayout(new GridLayout(2,1));
        stepControl.setBounds(3 * width/4 + 20, height/2 - 450, 200,100);
        stepControl.add(next);
        stepControl.add(previous);
        next.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        next.setBackground(Color.WHITE);
        next.addActionListener(e -> sudokuController.nextStep());
        previous.addActionListener(e -> sudokuController.previousStep());
        previous.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        previous.setBackground(Color.WHITE);

        edit.setLayout(new GridLayout(2,1));
        edit.setBounds(3 * width/4 - 200, height/2 - 450, 200,100);


        edit.add(toggleButton);
        edit.add(erase);
        erase.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        erase.setText("Erase");
        erase.setBackground(Color.WHITE);
        erase.addActionListener(e -> {sudokuController.addValue("", false); sudokuController.highlightAll();});
        erase.addKeyListener(new KeyInput(sudokuController, this));

        toggleButton.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        toggleButton.setText("Notes");
        toggleButton.setBackground(Color.WHITE);
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
            inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].clickNote();
        });
        toggleButton.addKeyListener(new KeyInput(sudokuController, this));
        this.setVisible(true);

        label.setBounds(3 * width/4 + 240, height/2 - 450, 100,50);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        stepSkip.setBounds(3 * width/4 + 240, height/2 - 400, 100,50);
        stepSkip.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        stepSkip.addActionListener(e -> {
            try{

                sudokuController.skipToStep(Integer.parseInt(stepSkip.getText()));
            }catch (NumberFormatException exception){}
        });
    }
    public void toggleNotes(){
        toggleButton.doClick();
    }
}
