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
        frame.setVisible(true);
        JPanel panel = new JPanel();
        frame.add(panel);
        JTextField button = new JTextField();
        button.setBounds(100,100,32, 32);
        panel.add(button);

    }
}
