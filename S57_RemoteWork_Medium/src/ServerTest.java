import javax.swing.JFrame;

/**
 * Driver class that starts the Server GUI
 * <p>
 *     Creates a server and sets up the actions when closed.
 *     Begins interactions with Clients when the server is run.
 * </p>
 *
 * @author augward
 */
public class ServerTest {
    public static void main(String[] args) {
        Server serverApp = new Server();
        serverApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        serverApp.runServer();
    }
}
