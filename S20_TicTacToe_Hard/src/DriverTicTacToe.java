import java.util.Scanner;

/**
 * Driver class that runs the main game loop.
 * Prints off the extra lines needed for the game to be constructed and look smoother,
 * and double-checks the flow of the game for ease of use.
 *
 * @author augward
 */

public class DriverTicTacToe {
    public static void main(String[] args) {
        // Scanner for player inputs
        final Scanner input = new Scanner(System.in);

        // Initializations of players
        Player pX = null;
        Player pO = null;

        // Game title
        System.out.print("- - - - Tic-Tac-Toe - - - -\nChoose player types:\n1 for Human, 0 for Computer\n");

        // While loop that asks for input on the construction of the players
        boolean validPlayers = false;
        while (!validPlayers) {
            System.out.print("Player X: ");
            int xChoice = input.nextInt();
            System.out.print("Player O: ");
            int oChoice = input.nextInt();

            // sets valid players to true and makes sure valid inputs were selected
            validPlayers = true;
            // player X
            if (xChoice == 1) {
                pX = new HumanPlayer('X');
            } else if (xChoice == 0) {
                pX = new ComputerPlayer('X');
            } else {
                System.out.println("Invalid choice, re-enter types");
                validPlayers = false;
            }
            // player O
            if (oChoice == 1) {
                pO = new HumanPlayer('O');
            } else if (oChoice == 0) {
                pO = new ComputerPlayer('O');
            } else {
                System.out.println("Invalid choice.");
                validPlayers = false;
            }
        }

        // Initialization of game loop variables
        final Board gameBoard = new Board();
        int turn = 1;
        boolean gameLive = true;

        // Main game loop that prints out live board and keeps track of turns
        while (gameLive) {
            // if board is reset, keeps track of which players turn it is
            if (turn == 1) {
                if (!gameBoard.checkFullBoard() && gameBoard.checkWinner() == 'R') {
                    System.out.printf("\n%s-X vs. %s-O\n", pX.getType(), pO.getType());
                    System.out.println(gameBoard);
                    System.out.println();
                    pX.makeMove(gameBoard);
                    turn = 2;
                }
            }
            if (turn == 2) {
                if (!gameBoard.checkFullBoard() && gameBoard.checkWinner() == 'R') {
                    System.out.printf("\n%s-X vs. %s-O\n", pX.getType(), pO.getType());
                    System.out.println(gameBoard);
                    System.out.println();
                    pO.makeMove(gameBoard);
                    turn = 1;
                }
            }

            // Checks to see if there is a winner and prints victory to screen
            if (gameBoard.checkWinner() != 'R') {
                gameLive = false;
                System.out.println("\nFinal Layout\n" + gameBoard + "\n");
                System.out.printf("Winner is: Player %s\n", gameBoard.checkWinner());
            }
            gameBoard.checkResetBoard();
        }
    }
}