import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Single client server that manages a directory to files.
 * <p>
 *     The server listens for a single client connection while open
 *     and supports two major commands from the client.
 *     Those being the Retrieve and Upload commands, manipulating files
 * </p>
 *
 * @author augward
 */
public class Server extends JFrame {
    // Area that displays log of server activity
    private final JTextArea displayArea;

    // Networking tools
    private ServerSocket serverSocket;
    private Socket connection;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    // File directory
    private final File filesDir = new File("oral_exam2/S57_RemoteWork_Medium/resources");


    /**
     * Constructs the Server GUI and initializes components like a Scroll Pane
     */
    public Server() {
        super("S57_RemoteWork_Medium: Server");

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setSize(500, 400);
        setLocation(150, 200);
        setVisible(true);

        displayArea.append("Server files directory: " + filesDir.getAbsolutePath());
    }


    /**
     * Starts the server listening loop, waiting for a client to connect to the server port
     */
    public void runServer() {
        int PORT = 23820;
        String IP = "127.0.0.1";

        try {
            serverSocket = new ServerSocket(PORT, 100, InetAddress.getByName(IP));
        } catch (IOException e) {
            displayArea.append("\nConnection Failed");
        }
        displayArea.append("\nListening for " + IP + " : " + PORT);

        // Accepts and processes a single client at a time
        boolean temp = true;
        while (temp) {
            try {
                waitForConnection();
                getStreams();
                processConnection();
            } catch (EOFException e) {
                displayArea.append("\nEnd Of File");
                temp = false;
            }  catch (IOException e) {
                displayArea.append("\nClient Stopped");
            } finally {
                closeConnection();
            }
        }

    }

    /**
     * Blocks until a client connect to the server and stores connection
     *
     * @throws IOException is thrown is an I/O error occurs during wait
     */
    private void waitForConnection() throws IOException {
        displayArea.append("\nAttempting connection");
        connection = serverSocket.accept();
        displayArea.append("\nConnecting to: " + connection.getInetAddress().getHostName());
    }

    /**
     * Establishes I/O streams for communicating with the client
     *
     * @throws IOException is thrown is an I/O error occurs while creating streams
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        displayArea.append("\nGot I/O streams");
    }

    /**
     * Processes incoming client messages until termination
     *
     * @throws IOException is thrown is an I/O error occurs while reading/writing streams
     */
    private void processConnection() throws IOException {
        sendData("Connection successful");
        String message;

        do {
            try {
                message = (String) input.readObject();
                if (message == null) break;
                displayArea.append("\n" + message);

                // When a retrieve is called from the client
                if (message.startsWith("CLIENT>>> RETRIEVE ")) {
                    String filename = message.substring("CLIENT>>> RETRIEVE ".length()).trim();
                    handleRetrieve(filename);
                // When an upload is called from the client
                } else if (message.startsWith("CLIENT>>> UPLOAD ")) {
                    String rest = message.substring("CLIENT>>> UPLOAD ".length()).trim();
                    String[] parts = rest.split(":::", 2); // filename:::content
                    String filename = parts[0].trim();
                    String content = parts.length > 1 ? parts[1] : "";
                    handleUpload(filename, content);
                }else if (message.equals("CLIENT>>> TERMINATE")) {
                    displayArea.append("\nClient Terminated");
                    break;
                }
            } catch (ClassNotFoundException classNotFound) {
                displayArea.append("\nUnknown Object");
                break;
            }
        } while (true);
    }

    /**
     * Closes current client connection and all associated factors
     */
    private void closeConnection() {
        displayArea.append("\nClosing connection");

        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (connection != null) connection.close();
        } catch (IOException e) {
            displayArea.append("\nError Closing Server");
        } finally {
            output = null;
            input = null;
            connection = null;
        }
    }

    /**
     * Handles client retrieve requests for an expected file
     *
     * @param filename the name of the file intended to be retrieved
     * @throws IOException if an I/O error occurs while reading file
     */
    private void handleRetrieve(String filename) throws IOException {
        displayArea.append("\nRETRIEVE:::" + filename);
        File file = new File(filesDir, filename);

        if (!file.exists()) {
            sendData("NOT_FOUND:::" + filename);
        } else {
            String content = readFileToString(file);
            sendData("FOUND:::" + filename + ":::" + content);
        }
    }

    /**
     * Handles client upload requests by writing content to new/existing files
     *
     * @param filename the filename intended to be created or manipulated
     * @param content the text/internals to add to chosen file
     */
    private void handleUpload(String filename, String content) {
        displayArea.append("\nUPLOAD:::" + filename);
        File file = new File(filesDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            sendData("UPLOAD_FAILED:::" + filename);
            return;
        }

        sendData("UPLOAD_SUCCESS:::" + filename);
        displayArea.append("\nFile uploaded/updated successfully on server");
    }

    /**
     * Reads text file fully into a single string
     *
     * @param file the file to read
     * @return the files contents
     * @throws IOException if an I/O error occurs during reading
     */
    private String readFileToString(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Sends a string message to the connected client and display area
     *
     * @param s the message to client/display
     */
    private void sendData(String s) {
        try {
            output.writeObject("SERVER>>> " + s);
            output.flush();
            displayArea.append("\nSERVER>>> " + s);
        } catch (IOException e) {
            displayArea.append("\nError writing object");
        }
    }
}
