import javax.swing.*;
import java.awt.*;

public class Window {

    public Window(int width, int height, String title, SudokuMain game){
        JFrame frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        panel1.add(panel);
        frame.add(panel1);
        panel1.setLayout(new GridLayout(1,2));
        panel.setLayout(new GridLayout(9,9));
        for(int i = 0; i < 81; i++){
            Font font = new Font("font", 17, 30);
            JTextField text = new JTextField();
            text.setColumns(1);
            text.setFont(font);
            panel.add(text);
        }
        panel1.add(new JButton());
        frame.setVisible(true);

    }
}
