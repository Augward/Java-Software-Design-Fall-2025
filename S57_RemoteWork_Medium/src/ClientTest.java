import javax.swing.JFrame;

/**
 * Driver class that starts the Client GUI
 * <p>
 *     Creates a client window and sets up defaults.
 *     Launches the client connection to network/server
 * </p>
 *
 * @author augward
 */
public class ClientTest {
    public static void main(String[] args) {
        Client clientApp = new Client();
        clientApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clientApp.runClient();
    }
}
