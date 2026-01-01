import java.util.Scanner;

/**
 * Represents a human controlled player
 * <p>
 *     Extends an abstract class player and implements makeMove with user input
 *     Has its own logic and printed out statements, while inputting coordinates for moves.
 * </p>
 *
 * @author augward
 */

public class HumanPlayer extends Player {
    // Scanner for input on moves
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for human class, adds their mark and type
     *
     * @param newMark of X or O
     */
    public HumanPlayer(char newMark) {
        super(newMark, "Human");
    }


    /**
     * Asks user for row and column input 1-3
     * Validates the position called and only runs if moves are possible.
     *
     * @param board the gameboard that is being acted upon
     */
    @Override
    public void makeMove(Board board) {
        boolean moveMade = false;

        while (!moveMade && !board.checkFullBoard()) {
            System.out.printf("%s %s's turn.\n", getType(), getMark());
            System.out.print("Enter chosen row (1, 2, or 3): ");
            int row = scanner.nextInt() - 1;
            System.out.print("Enter chosen column (1, 2, or 3): ");
            int col = scanner.nextInt() - 1;

            if (board.checkValidPosition(row, col) && board.getCell(row, col) == ' ') {
                board.setCell(row, col, getMark());
                moveMade = true;
            } else {
                System.out.println("Invalid position, try again.");
            }
        }
    }
}
