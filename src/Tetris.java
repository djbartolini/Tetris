import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Member;

// Main class Tetris inherits from JFrame to paint the game board
public class Tetris extends JFrame {

    private Menu menu;
    private Board board;

    public Tetris() {
        initFrame();
        initMenu();
    }

    public void initFrame() {
        // Frame setup
        setTitle("Tetris");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void initMenu() {
        menu = new Menu();
        menu.setStartGameListener(this::startGame);
        add(menu);
        menu.requestFocusAndAddKeyListener();
    }

    public void startGame() {
        menu.setFocusable(false);
        menu.removeKeyListener();
        remove(menu);

        Board board = new Board(this);
        add(board);
        board.start();

        validate();
        repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}