import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel {

    private boolean gameStarted;

    private Font largeFont = new Font("Century", Font.BOLD, 16);
    private Font smallFont = new Font("Century", Font.PLAIN, 12);

    private JTextField textField;
    private StartGameListener startGameListener;

    public Menu() {
        initMenu();
    }

    public void initMenu() {
        setFocusable(true);
    }

    public void setStartGameListener(StartGameListener listener) {
        this.startGameListener = listener;
    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        BufferedImage background;
        try {
            background = ImageIO.read(new File("resources/background-menu.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(background, 0, 0, null);

        g2.setFont(largeFont);
        g2.setColor(Color.WHITE);
        String largeText = "Brought to you by Daniel Bartolini";
        FontMetrics largeFontMetrics = g2.getFontMetrics(largeFont);
        int largeTextWidth = largeFontMetrics.stringWidth(largeText);
        int largeTextX = (getWidth() - largeTextWidth) / 2;
        int largeTextY = 376;
        g2.drawString(largeText, largeTextX, largeTextY);

        g2.setFont(smallFont);
        g2.setColor(Color.WHITE);
        String smallText = "Press `ENTER` to start a new game";
        FontMetrics smallFontMetrics = g2.getFontMetrics(smallFont);
        int smallTextWidth = smallFontMetrics.stringWidth(smallText);
        int smallTextX = (getWidth() - smallTextWidth) / 2;
        int smallTextY = 424;
        g2.drawString(smallText, smallTextX, smallTextY);
    }


    public void requestFocusAndAddKeyListener() {
        requestFocusInWindow();  // Request keyboard focus for the panel
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    public void removeKeyListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
    }

    private final KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (startGameListener != null) {
                        startGameListener.onStartGame();
                        return true;  // Consume the ENTER key event
                    }
                }
            }
            return false;  // Let other components handle the key event
        }
    };

    public interface StartGameListener {
        void onStartGame();
    }
}
