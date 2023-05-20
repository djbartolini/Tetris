import javax.swing.*;
import java.awt.*;

// Main class Tetris inherits from JFrame to paint the game board
public class Tetris extends JFrame {

    private Menu menu;
    private Board board;
    private JLabel statusbar;

    public Tetris() {
        initFrame();
        initMenu();
    }

    public JLabel getStatusbar() {
        return statusbar;
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
    }

    public void initStatusbar() {
        String statusbarString = "Score: 0";
        Font statusbarFont = new Font("Century", Font.BOLD, 18);
        Color statusbarColor = new Color(36, 36, 36);

        statusbar = new JLabel(statusbarString);
        statusbar.setBackground(statusbarColor);
        statusbar.setForeground(Color.WHITE);
        statusbar.setOpaque(true);
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        statusbar.setFont(statusbarFont);
    }

    public void startGame() {
        menu.removeKeyBindings();
        remove(menu);

        initStatusbar();
        add(statusbar, BorderLayout.SOUTH);

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