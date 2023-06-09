import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {

    // Constant values for width and height in number of blocks
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;

    /*
        Adjust the PERIOD_INTERVAL constant
        to change the speed at which the blocks fall
    */
    private final int PERIOD_INTERVAL = 400;
    private Timer timer;

    private int numLinesCleared = 0;

    // Keeps track of the current (newest) piece
    private Tetromino curPiece;
    private int curX = 0;
    private int curY = 0;
    private boolean isAtBottom = false;


    // The board is essentially an array of Tetrominoes
    private Tetromino.Shape[] board;

    // The status bar at the bottom
    private JLabel statusbar;


    // Constructor which initializes the Board with a new game
    public Board(Tetris game) {
        initBoard(game);
    }

    public void initBoard(Tetris game) {
        setFocusable(true);
        setBackground(new Color(36, 36, 36));
        statusbar = game.getStatusbar(); // Get status bar from Tetris class
        initKeyBindings();
    }

    // Key Bindings
    private void initKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_LEFT,
                "move left",
                () -> tryMove(curPiece, curX -1, curY)
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_RIGHT,
                "move right",
                () -> tryMove(curPiece, curX + 1, curY)
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_UP,
                "rotate left",
                () -> tryMove(curPiece.rotateLeft(), curX, curY)
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_DOWN,
                "rotate right",
                () -> tryMove(curPiece.rotateRight(), curX, curY)
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_SPACE,
                "drop to bottom",
                this::dropDown
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_D,
                "drop one line down",
                this::oneLineDown
        );
        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_ENTER,
                "start new game",
                this::start
        );
    }

    private void setupKetBinding(InputMap inputMap, ActionMap actionMap, int keycode, String actionKey, Runnable action) {
        inputMap.put(KeyStroke.getKeyStroke(keycode, 0), actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public void removeKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.clear();
        actionMap.clear();
    }

    // Gets the height of each block (square)
    private int getSquareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    // Gets width of each block (square)
    private int getSquareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    // Return array to represent board positions
    private Tetromino.Shape shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    // Our paintComponent method
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawShape(g);
    }

    // use drawSquare method to assemble our Tetromino blocks
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

        Color colors[] = {
                new Color(252, 119, 119),
                new Color(232, 159, 95),
                new Color(250, 245, 115),
                new Color(112, 217, 74),
                new Color(70, 171, 176),
                new Color(54, 118, 191),
                new Color(122, 57, 191),
                new Color(242, 141, 219)
        };

        // Use the ordinal value of the Tetromino shape to pick a color from the array above
        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, getSquareWidth() - 2, getSquareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + getSquareHeight() - 1, x, y);
        g.drawLine(x, y, x + getSquareWidth() - 1, y);

        g.setColor(color.brighter());
        g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);
    }

    // Key command to drop piece to the bottom
    private void dropDown() {
        int newY = curY;

        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }

        pieceDropped();
    }

    private void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = Tetromino.Shape.NO_SHAPE;
        }
    }

    // Set curPiece to our new shape
    // Change to corresponding new shape on player move
    private void newPiece() {

        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setShape(Tetromino.Shape.NO_SHAPE);
            statusbar.setText("Score: " + numLinesCleared + " | Press `ENTER` to play again!");
        }
    }

    // return true if move is within window bounds
    private boolean tryMove(Tetromino newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {

            int x = newX + newPiece.getX(i);
            int y = newY - newPiece.getY(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

                return false;
            }

            if (shapeAt(x, y) != Tetromino.Shape.NO_SHAPE) {

                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    /*
        Method to perform each time a block hits the bottom.
        This will run the removeFullLines() method or us
        and triggers a new piece.
     */
    private void pieceDropped() {

        for (int i = 0; i < 4; i++) {

            int x = curX + curPiece.getX(i);
            int y = curY - curPiece.getY(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isAtBottom) {
            newPiece();
        }
    }

    // Key command to go do down one line
    private void oneLineDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    // Method to check and remove a line and add to score when a row is full
    // Add to score when we successfully remove a line
    private void removeFullLines() {

        int score = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == Tetromino.Shape.NO_SHAPE) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                score++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (score > 0) {
            numLinesCleared += score;

            statusbar.setText("Score: " + numLinesCleared);
            isAtBottom = true;
            curPiece.setShape(Tetromino.Shape.NO_SHAPE);
        }
    }

    // GameCycle timer arg to update and repaint UI
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private void update() {

        if (isAtBottom) {
            isAtBottom = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    // This resets our timer for each game
    // and obviously resets the score
    private void clearScore() {
        if (timer != null) {
            timer.stop();
        }
        numLinesCleared = 0;
    }


    // Start method.
    // Creates our board object.
    // The `newPiece()` method is triggered by the
    // `pieceDropped()` method for subsequent pieces.
    void start() {

        curPiece = new Tetromino();
        board = new Tetromino.Shape[BOARD_WIDTH * BOARD_HEIGHT];

        clearScore();
        clearBoard();
        newPiece();

        timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();
    }
}
