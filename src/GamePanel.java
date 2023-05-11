import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel() {
        this.setBounds(0, 0, 100, 100);
        this.setBackground(Color.red);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(0, 0, 50, 50);
    }
}
