import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        initKeyBindings();
    }

    public void setStartGameListener(StartGameListener listener) {
        this.startGameListener = listener;
    }

    // Key bindings
    private void initKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        setupKetBinding(
                inputMap,
                actionMap,
                KeyEvent.VK_ENTER,
                "start game",
                () -> startGameListener.onStartGame()
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

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        BufferedImage background;
        try {
            background = ImageIO.read(new File("resources/background-menu.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2.setBackground(new Color(36, 36, 36));
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

    public interface StartGameListener {
        void onStartGame();
    }
}
