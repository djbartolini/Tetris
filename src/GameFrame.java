import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        this.add(new GamePanel());
        this.setBounds(0, 0, 600, 900);
    }

    public static void main(String args[]) {
        new GameFrame().setVisible(true);

    }
}
