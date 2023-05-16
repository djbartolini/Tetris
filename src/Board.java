import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    // Constant values for width and height in number of blocks
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;

    // Constructor which initializes the Board with a new game
    public Board(Tetris game) {
        initBoard(game);
    }

    // Initializes the board
    public void initBoard(Tetris game) {
        setFocusable(true);
    }

    // Gets the height of each block (square)
    private int getSquareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    // Gets width of each block (square)
    private int getSquareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    // Our custom paintComponent
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawShape(g);
    }

    private void drawShape(Graphics g) {
        int boardTop = (int) getSize().getHeight() - BOARD_HEIGHT * getSquareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                drawSquare(g, j * getSquareWidth(), boardTop + (i * getSquareHeight()));
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y) {
        // TODO: Add more colors
        g.setColor(Color.BLACK);

        // Draw our grid lines for the board
        g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);


    }
}
