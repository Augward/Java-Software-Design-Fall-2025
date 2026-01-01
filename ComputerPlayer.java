import java.util.Random;

/**
 * Represents a computer controlled player
 * <p>
 *     Extends an abstract class and implements makeMove with random inputs
 *     Surprisingly functional random, with logic to check valid moves.
 * </p>
 *
 * @author augward
 */

public class ComputerPlayer extends Player {

    /**
     * Constructor of Computer class, adds mark and type
     *
     * @param newMark of X or O
     */
    public ComputerPlayer(char newMark) {
        super(newMark,  "Computer");
    }


    /**
     * Utilizes random to randomly select cell in matrix.
     * If cell is not empty, tries again until empty found.
     *
     * @param board the gameboard that the bot acts on
     */
    @Override
    public void makeMove(Board board) {
        if (!board.checkFullBoard()) {
            boolean moveMade = false;
            Random rand = new Random();

            while (!moveMade) {
                int randomRow = rand.nextInt(3);
                int randomCol = rand.nextInt(3);

                if (board.getCell(randomRow, randomCol) == ' ') {
                    board.setCell(randomRow, randomCol, getMark());
                    moveMade = true;
                }
            }
        }
    }
}
