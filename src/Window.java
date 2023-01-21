import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;


public class Window extends JFrame{
    public static boolean noteMode = false;
    public static SudokuCell[][] inputGameBoard = new SudokuCell[SudokuMain.BOARD_SIZE][SudokuMain.BOARD_SIZE];
    private static ArrayList<JPanel> subPanels = new ArrayList<>();
    private JPanel mainPanel = new JPanel();
    private JPanel inputNumbers = new JPanel();
    private JPanel stepControl = new JPanel();
    private JPanel edit = new JPanel();
    private JButton next = new JButton("Hint");
    private JButton previous = new JButton("Undo");
    private JButton erase = new JButton();
    private JToggleButton toggleButton = new JToggleButton();
    private JTextField stepSkip = new JTextField();
    private JLabel label = new JLabel("Skip to");
    /**
     * font list:
     * Constantia
     * Book Antiqua
     * Bahnschrift
     */
    public static String fontName = "Bahnschrift";
    public Window(int width, int height, String title, SudokuController sudokuController){

        this.setTitle(title);
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        this.setLayout(null);
        this.add(mainPanel);
        this.add(edit);
        this.add(stepControl);
        this.add(stepSkip);
        this.add(label);
        this.add(inputNumbers);

        GridLayout layout = new GridLayout(3,3);
        layout.setHgap(3);
        layout.setVgap(3);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
        mainPanel.setLayout(layout);
        mainPanel.setBounds(10,10,1000,1000);


        for(int i = 0; i < SudokuMain.BOARD_SIZE; i++){
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3,3));
            subPanels.add(panel);
            subPanels.get(i).setBorder(null);
            mainPanel.add(panel);
        }
        for(int r = 0; r < SudokuMain.BOARD_SIZE; r++ ){
            for(int c = 0; c < SudokuMain.BOARD_SIZE; c++){
                int boxIndex = GameBoard.getBoxNumber(r,c) - 1;
                inputGameBoard[r][c] = new SudokuCell(r,c,sudokuController, this);
                subPanels.get(boxIndex).add(inputGameBoard[r][c]);
            }
        }

        inputNumbers.setLayout(new GridLayout(3,3));
        inputNumbers.setBounds(3 * width/4 - 200, height/2 - 300, 500,500);
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                JButton button = new JButton();
                button.setFont(new Font(fontName, Font.PLAIN, 50));
                button.setText(String.valueOf(r * 3 + 1 + c%3));
                button.setBackground(Color.WHITE);
                button.setMargin(new Insets(0,0,0,0));
                button.setVerticalAlignment(JButton.CENTER);
                button.addActionListener(e -> {
                    inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].setPlayerPressed(true);
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
        next.setFont(new Font(fontName, Font.PLAIN, 40));
        next.setBackground(Color.WHITE);
        next.setMargin(new Insets(0,0,0,0));
        next.setVerticalAlignment(JButton.CENTER);
        next.addActionListener(e -> sudokuController.nextStep());
        next.addKeyListener(new KeyInput(sudokuController, this));
        previous.addActionListener(e -> sudokuController.previousStep());
        previous.setFont(new Font(fontName, Font.PLAIN, 40));
        previous.setMargin(new Insets(0,0,0,0));
        previous.setVerticalAlignment(JButton.CENTER);
        previous.setBackground(Color.WHITE);
        previous.addKeyListener(new KeyInput(sudokuController, this));
        edit.setLayout(new GridLayout(2,1));
        edit.setBounds(3 * width/4 - 200, height/2 - 450, 200,100);


        edit.add(toggleButton);
        edit.add(erase);
        erase.setFont(new Font(fontName, Font.PLAIN, 40));
        erase.setText("Erase");
        erase.setBackground(Color.WHITE);
        erase.setMargin(new Insets(0,0,0,0));
        erase.setVerticalAlignment(JButton.CENTER);
        erase.addActionListener(e -> {
            inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].setPlayerPressed(false);
            sudokuController.addValue("", false);
            sudokuController.addStep(sudokuController.inputToGameBoard());
            sudokuController.highlightAll();
            sudokuController.currentStep++;});
        erase.addKeyListener(new KeyInput(sudokuController, this));

        toggleButton.setFont(new Font(fontName, Font.PLAIN, 40));
        toggleButton.setText("Notes");
        toggleButton.setBackground(Color.WHITE);
        toggleButton.setMargin(new Insets(0,0,0,0));
        toggleButton.setVerticalAlignment(JToggleButton.CENTER);
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

        label.setBounds(3 * width/4 + 240, height/2 - 450, 100,50);
        label.setFont(new Font(fontName, Font.PLAIN, 30));
        stepSkip.setBounds(3 * width/4 + 240, height/2 - 400, 100,50);
        stepSkip.setFont(new Font(fontName, Font.PLAIN, 30));
        stepSkip.setMargin(new Insets(0,0,0,0));
        stepSkip.addActionListener(e -> {
            try{
                sudokuController.skipToStep(Integer.parseInt(stepSkip.getText()));
            }catch (NumberFormatException exception){}
        });
        this.setVisible(true);
    }
    public void toggleNotes(){
        toggleButton.doClick();
    }
}
