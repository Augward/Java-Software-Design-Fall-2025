import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Single Client that connects to a server and manipulates files
 * <p>
 *     The client can generate content and names for files to be manipulated
 *     and can call for files to be uploaded to server or retrieved from them
 *     for further editing/creation
 * </p>
 *
 * @author augward
 */
public class Client extends JFrame {
    // Input field for file names / content
    private final JTextField fileNameField;

    private final JTextArea fileContentArea;
    private final JTextArea displayArea;

    // Networking tools
    private Socket client;

    private ObjectOutputStream output;
    private ObjectInputStream input;


    /**
     * Constructs a Client GUI and initializes all components
     */
    public Client() {
        super("S57_RemoteWork_Medium: Client");

        setLayout(new BorderLayout());

        // Top panel with file names field
        JPanel topPanel = new JPanel();
        fileNameField = new JTextField("example.txt");
        fileNameField.setColumns(20);
        topPanel.add(fileNameField);
        add(topPanel, BorderLayout.NORTH);


        // Middle pane split between logs and content
        fileContentArea = new JTextArea();
        JScrollPane leftPane = new JScrollPane(fileContentArea);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane rightPane = new JScrollPane(displayArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);


        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel();

        JButton retrieveButton = new JButton("Retrieve");
        JButton uploadButton = new JButton("Upload");
        retrieveButton.setPreferredSize(new Dimension(100, 30));
        uploadButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(retrieveButton);
        buttonPanel.add(uploadButton);
        add(buttonPanel, BorderLayout.SOUTH);


        setSize(500, 400);
        setLocation(850,200);
        setVisible(true);


        // Retrieve action, file from server
        retrieveButton.addActionListener(e -> {
            String fileName = fileNameField.getText().trim();

            if (fileName.isEmpty()) {
                displayArea.append("\nNo file name entered");
                return;
            }

            sendData("RETRIEVE " + fileName);
        });

        // Upload action from contents to server
        uploadButton.addActionListener(e -> {
            String fileName = fileNameField.getText().trim();
            String fileContent = fileContentArea.getText().trim();

            if (fileName.isEmpty()) {
                displayArea.append("\nNo file name entered");
                return;
            }

            sendData("UPLOAD " + fileName + ":::" + fileContent);
        });
    }


    /**
     * Main client actions stored in here and done once, as it only needs to connect to server
     */
    public void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();
        } catch (EOFException e) {
            displayArea.append("\nEnd Of File");
        } catch (IOException e) {
            displayArea.append("\nServer Stopped");
        } finally {
            closeConnection();
        }
    }


    /**
     * Opens a socket to connect to the server of given IP
     *
     * @throws IOException if I/O error occurs when creating socket
     */
    private void connectToServer() throws IOException {
        displayArea.append("Attempting connection");
        int PORT = 23820;
        String IP = "127.0.0.1";
        client = new Socket(InetAddress.getByName(IP), PORT);
        displayArea.append("\nConnected to " + client.getInetAddress().getHostName());
    }

    /**
     * Establishes I/O streams for communicating with the client
     *
     * @throws IOException is thrown is an I/O error occurs while creating streams
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        displayArea.append("\nGot I/O streams\n");
    }

    /**
     * Processes incoming server messages until termination
     *
     * @throws IOException is thrown is an I/O error occurs while reading/writing streams
     */
    private void processConnection() throws IOException {
        String message;

        do {
            try {
                message = (String) input.readObject();
                if (message == null) break;
                displayArea.append("\n" + message);

                // When the server finds a file
                if (message.startsWith("SERVER>>> FOUND")) {
                    String rest = message.substring("SERVER>>> FOUND:::".length()).trim();

                    int idx = rest.indexOf(":::");
                    String content = rest;
                    if (idx >= 0) {
                        content = rest.substring(idx + 3);
                    }
                    fileContentArea.setText(content);
                    displayArea.append("File retrieved and placed in editor");

                // When the server doesn't find the file
                } else if (message.startsWith("SERVER>>> NOT_FOUND")) {
                    fileContentArea.setText("");
                    displayArea.append("\nServer: file not found");
                // When the server successfully uploads a file
                } else if (message.startsWith("SERVER>>> UPLOAD_SUCCESS")) {
                    displayArea.append("\nServer: upload succeeded");
                // When the server fails to upload a file
                } else if (message.startsWith("SERVER>>> UPLOAD_FAILED")) {
                    displayArea.append("\nServer: upload failed");
                } else if (message.equals("SERVER>>> TERMINATE")) {
                    displayArea.append("\nServer Terminated");
                    break;
                }
            } catch (ClassNotFoundException classNotFound) {
                displayArea.append("\nUnknown Object");
                break;
            }
        } while (true);
    }

    /**
     * Closes current server connection and all associated factors
     */
    private void closeConnection() {
        displayArea.append("\nClosing connection");

        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (client != null) client.close();
        } catch (IOException e) {
            displayArea.append("\nError Closing Client");
        } finally {
            output = null;
            input = null;
            client = null;
        }
    }

    /**
     * Sends a string message to the connected server and display area
     *
     * @param s the message to server/display
     */
    private void sendData(String s) {
        try {
            output.writeObject("CLIENT>>> " + s);
            output.flush();
            displayArea.append("\nCLIENT>>> " + s);
        } catch (IOException e) {
            displayArea.append("\nError writing object");
            closeConnection();
        }
    }
}
