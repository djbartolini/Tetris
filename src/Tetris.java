import javax.swing.*;
import java.awt.*;

// Main class Tetris inherits from JFrame to paint the game board
public class Tetris extends JFrame {

    public Tetris() {
        initFrame();
    }

    // Constructor for our Tetris class
    public void initFrame() {
        // Instantiate our Board and add it to the Frame
        Board board = new Board(this);
        add(board);

        // Frame setup
        setTitle("Tetris");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        board.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }

}