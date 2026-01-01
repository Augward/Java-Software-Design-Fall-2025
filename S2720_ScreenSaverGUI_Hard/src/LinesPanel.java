import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

/**
 * LinesPanel class for Screensaver
 * <p>
 *     A JPanel that simulates a screensaver by drawing an inputted
 *     number of lines on a background.  It refreshes everytime the swing Timer
 *     completes and the number of lines drawn is controlled by a JTextField.
 *     It only takes in integers into a textbox and prints next loop.
 *     Implements ActionListener and Extends JPanel
 * </p>
 *
 * @author augward
 */
public class LinesPanel extends JPanel implements ActionListener {
    // Random variable that pulls a new random value for lines, final since reference doesn't change.
    private final Random randomPosition = new Random();

    // Text field for input of lineCount
    private int lineCount = 100;
    private final JTextField textField;

    // Previously initialized timer out here, but it is only used in one location
    // So variable for it could become local, yellow underline.

    /**
     * Constructor that runs a timer that refreshes every 1 second.
     * Builds a simple interface of a JLabel and JTextField.
     * TextField adds an action listener, waiting for string prompts.
     */
    public LinesPanel() {
        Timer refreshTimer = new Timer(1000, this);
        refreshTimer.start();

        // Text prompt box, set to match input
        JLabel textPrompt = new JLabel("Enter Number of Random Lines: ");
        textPrompt.setOpaque(true);
        textPrompt.setBackground(Color.WHITE);
        textPrompt.setForeground(Color.BLACK);
        this.add(textPrompt);

        // Input field with action listener attached.
        textField = new JTextField(4);
        textField.addActionListener(new TextFieldListener()); // Maybe a-nom inner class here
        this.add(textField);
    }


    /**
     * Paints the components of the LinesPanel class
     * Creates the background, random strings, and their colors.
     *
     * @param g the Graphics provided by Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Allows for color changes down the line, just a personal code, defaults to these values.
        this.setBackground(Color.WHITE);
        g.setColor(Color.BLACK);

        // Draws # of lines on screen with random (x,y),(x,y) values.
        for (int i = 1; i <= lineCount; i++) {
            g.drawLine(
                    randomPosition.nextInt(getWidth()),
                    randomPosition.nextInt(getHeight()),
                    randomPosition.nextInt(getWidth()),
                    randomPosition.nextInt(getHeight())
            );
        }
    }


    /**
     * Timer calls this method every 1 second
     * In turn, this method repaints over again with painComponent
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }


    /**
     * Inner class that handles when Enter is pressed in TextField
     * If empty, negative, decimal, or a character are found, prints a message to screen
     * If an integer, then lineCount is updated for next screen
     */
    public class TextFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String input = textField.getText().trim();

            // Checks to make sure of valid conditions for Listener input
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(LinesPanel.this, "Enter a valid number in the blank.");
            } else {

                // checks every character in string for validity
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);

                    if (c == '.') {
                        JOptionPane.showMessageDialog(LinesPanel.this, "No decimal points, only valid integers.");
                        return;
                    }

                    if (c == '-') {
                        JOptionPane.showMessageDialog(LinesPanel.this, "No negative numbers, only valid integers.");
                        return;
                    }

                    if (!Character.isDigit(c)) {
                        JOptionPane.showMessageDialog(LinesPanel.this, "No characters, Only valid integers.");
                        return;
                    }
                }

                // updates lineCount with new input
                lineCount = Integer.parseInt(input);
            }
        }
    }
}
