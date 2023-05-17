import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    // Constant values for width and height in number of blocks
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;

    // Keeps track of the current 'new' piece
    private Tetromino curPiece;
    private int curX = 0;
    private int curY = 0;
    private Tetromino.Shape[] board;

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

    // Return 2D array to represent board positions
    private Tetromino.Shape shapeAt(int x, int y) {
        System.out.println("x: " + x + ", y: " + y);
        return board[(y * BOARD_WIDTH) + x];
    }

    // Our paintComponent method
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawShape(g);
    }

    // use drawSquare method to assemble our Tetris blocks
    private void drawShape(Graphics g) {
        int boardTop = (int) getSize().getHeight() - BOARD_HEIGHT * getSquareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetromino.Shape shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Tetromino.Shape.NO_SHAPE) {
                    drawSquare(g, j * getSquareWidth(), boardTop + (i * getSquareHeight()), shape);
                }
            }
        }

        if (curPiece.getShape() != Tetromino.Shape.NO_SHAPE) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.getX(i);
                int y = curY - curPiece.getY(i);

                drawSquare(g, x * getSquareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * getSquareHeight(), curPiece.getShape());
            }
        }
    }

    // use Graphics.fillRect() method to paint blocks
    private void drawSquare(Graphics g, int x, int y, Tetromino.Shape shape) {

        g.setColor(Color.RED);
        g.fillRect(x + 1, y + 1, getSquareWidth() - 2, getSquareHeight() - 2);

        g.setColor(Color.PINK);
        g.drawLine(x, y + getSquareHeight() - 1, x, y);
        g.drawLine(x, y, x + getSquareWidth() - 1, y);

        // TODO: Add more colors
        g.setColor(Color.BLACK);

        // Draw our grid lines for the board
        g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);


    }

    private void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = Tetromino.Shape.NO_SHAPE;
        }
    }

    private void newPiece() {

        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setShape(Tetromino.Shape.NO_SHAPE);

            var msg = String.format("Game over. Score: %d", 0);
        }
    }

    private boolean tryMove(Tetromino newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.getX(i);
            int y = newY - newPiece.getY(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

                return false;
            }

//            if (shapeAt(x, y) != Tetromino.Shape.NO_SHAPE) {
//
//                return false;
//            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    void start() {

        curPiece = new Tetromino();
        board = new Tetromino.Shape[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
        newPiece();
    }
}
