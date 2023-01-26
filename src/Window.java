import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;


public class Window extends JFrame{
    /** TL;DR
    Creates the main game window to play the game as well as sets up all the action listeners.
     */
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
    private JMenuBar menuBar = new JMenuBar();
    private JMenu newGame = new JMenu("New Game");
    private JMenuItem easy = new JMenuItem("Easy");
    private JMenuItem medium = new JMenuItem("Medium");
    private JMenuItem hard = new JMenuItem("Hard");
    private JMenuItem expert = new JMenuItem("Expert");
    private JMenuItem custom = new JMenuItem("Custom");
    private JButton confirm = new JButton("Confirm Board");
    private JButton clearBoard = new JButton("Clear");
    /**
     * font list:
     * Constantia
     * Book Antiqua
     * Bahnschrift
     */
    public static String fontName = "Bahnschrift";
    public Window(int width, int height, String title, SudokuController sudokuController){

        this.setTitle(title);
        this.setSize(width,height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        //480p
        this.setMinimumSize(new Dimension(854, 480));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                resize();
            }
        });

        this.setLayout(null);
        this.add(mainPanel);
        this.add(edit);
        this.add(stepControl);
        //this.add(stepSkip);
        //this.add(label);
        this.add(inputNumbers);
        this.add(confirm);
        this.add(clearBoard);
        this.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,50, SudokuMain.WIDTH);
        menuBar.add(newGame);
        newGame.add(easy);
        newGame.add(medium);
        newGame.add(hard);
        newGame.add(expert);
        newGame.add(custom);

        easy.addActionListener(e -> {
            createNewBoard(SudokuGenerator.EASY, sudokuController);
        }); medium.addActionListener(e -> {
            createNewBoard(SudokuGenerator.MEDIUM, sudokuController);
        }); hard.addActionListener(e -> {
            createNewBoard(SudokuGenerator.HARD, sudokuController);
        }); expert.addActionListener(e -> {
            createNewBoard(SudokuGenerator.EXPERT, sudokuController);
        });custom.addActionListener(e -> {
            sudokuController.clearSteps();
            sudokuController.clearInputGameBoard();
            sudokuController.currentStep = -1;
            sudokuController.updateAllSquares(new GameBoard());
            sudokuController.solvedSudoku = new GameBoard();
            sudokuController.createMode = true;
            sudokuController.highlightAll();
            confirm.setVisible(true);
            clearBoard.setVisible(true);
        });

        confirm.setVisible(false);
        confirm.setBackground(Color.WHITE);
        confirm.setBounds(3 * width/4 - 250, height/2 + 220, 500,100);
        confirm.setMargin(new Insets(0,0,0,0));
        confirm.setFont(new Font(fontName, Font.BOLD, 50));
        confirm.addActionListener(e -> {
            if(sudokuController.getNumberOfFilledIn() < 23){
                confirm.setText("Not Filled Enough");
                return;
            }
            if(!BoardValidity.isValidSudoku(sudokuController.inputToGameBoard())){
                confirm.setText("Invalid Input");
                return;
            }
            GameBoard solved = sudokuController.logic.masterSolveSudoku(sudokuController.inputToGameBoard());
            if(BoardValidity.isSolvedSudoku(solved)){
                sudokuController.solvedSudoku = solved;
                sudokuController.lockValues();
            }
            else{
                confirm.setText("impossible try again");
                return;
            }
            confirm.setVisible(false);
            clearBoard.setVisible(false);
        });
        clearBoard.setVisible(false);
        clearBoard.setBackground(Color.WHITE);
        clearBoard.setBounds(3 * width/4 + 240, height/2 - 450, 200,100);
        clearBoard.setMargin(new Insets(0,0,0,0));
        clearBoard.setFont(new Font(fontName, Font.PLAIN, 30));
        clearBoard.addActionListener( e -> sudokuController.clearInputGameBoard());

        GridLayout layout = new GridLayout(3,3);
        layout.setHgap(3);
        layout.setVgap(3);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));
        mainPanel.setLayout(layout);
        mainPanel.setBounds(10,10,1000, 1000);


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
                    if(Window.inputGameBoard[sudokuController.getRow()][sudokuController.getCol()].getValue().equals(""))
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
            noteMode = !noteMode; //determines if values should be filled or notes
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
    public void resize() {
        mainPanel.setBounds(10,10,this.getHeight() - 100, this.getHeight() - 100);
        inputNumbers.setBounds(3 * this.getWidth()/4 - this.getWidth()/7, this.getHeight()/2 - this.getHeight()*3/10, this.getHeight()/2,this.getHeight()/2);
        edit.setBounds(inputNumbers.getX() , inputNumbers.getY() - inputNumbers.getHeight()/5 - 20, inputNumbers.getWidth()/2,inputNumbers.getHeight()/5);
        stepControl.setBounds(edit.getX() + edit.getWidth(), inputNumbers.getY() - inputNumbers.getHeight()/5 -20, inputNumbers.getWidth()/2,inputNumbers.getHeight()/5);
        confirm.setBounds(inputNumbers.getX(), inputNumbers.getY() + inputNumbers.getHeight() + 20, inputNumbers.getWidth(),inputNumbers.getHeight()/3);
        clearBoard.setBounds(stepControl.getX() + stepControl.getWidth() + 20, stepControl.getY(), stepControl.getWidth()/2, stepControl.getHeight());
        this.repaint();
        this.revalidate();
    }
    public void createNewBoard(int difficulty, SudokuController sudokuController){
        sudokuController.createMode = false;
        sudokuController.clearSteps();
        sudokuController.clearInputGameBoard();
        sudokuController.currentStep = -1;
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        sudokuController.solvedSudoku = sudokuGenerator.generateSolvedSudoku();
        sudokuController.updateAllSquares(sudokuGenerator.generateSudoku(difficulty, sudokuController.solvedSudoku));
        sudokuController.highlightAll();
        confirm.setVisible(false);
        clearBoard.setVisible(false);
    }
}
