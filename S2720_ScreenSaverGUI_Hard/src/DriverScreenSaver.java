import javax.swing.JFrame;

/**
 * Drive Class that customizes the screen savor of LinesPanel class
 *
 * @author augward
 */
public class DriverScreenSaver {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1500, 700);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new LinesPanel());
        frame.setVisible(true);
    }
}