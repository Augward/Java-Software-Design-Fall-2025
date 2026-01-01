/**
 * Class represents a 3 x 3 tic-tac-toe board with cells and arrays
 * <p>
 *     Class creates a 3 x 3 character array for Xs and Os
 *     Checks the validity of moves made and positions taken to keep a real game.
 *     Also checks for any winners and prints off the matrix as a custom board.
 * </p>
 *
 * @author augward
 */

public class Board {
    // Key variables in the creation of a game board matrix
    private static final int BOARD_SIZE = 3;
    private final char[][] board;

    /**
     * Constructs a new board with no given input, due to always creating a new game of size Board_Size
     */
    public Board() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        setEmptyBoard();
    }


    /**
     * Returns the data at a certain cell of the game board
     *
     * @param row of the gameboard array
     * @param col of the gameboard array
     * @return element char at position row,column
     */
    public char getCell(int row, int col) {
        checkValidPosition(row, col);
        return board[row][col];
    }

    /**
     * Sets element data at certain position of the board.
     *
     * @param row of the gameboard array
     * @param col of the gameboard array
     * @param newValue new char at validation position, usually from Player.java
     */
    public void setCell(int row, int col, char newValue) {
        checkValidPosition(row, col);
        board[row][col] = newValue;
    }

    /**
     * Sets every cell in board to empty space ' '
     */
    public void setEmptyBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }


    /**
     * Helper method to make sure every call is at a valid cell
     *
     * @param row row to check if valid
     * @param col column to check if valid
     * @return true if valid called cell, false if not
     */
    public boolean checkValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Helper method to check if game board is full, often used with reset
     *
     * @return true if board is at full capacity, false if not
     */
    public boolean checkFullBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helper method that runs after every input to see if someone has won the game.
     * Checks all configurations of 3 in a row
     *
     * @return char of winning player, X if X wins, or O if O wins
     */
    public char checkWinner() {
        // Checks if 3 in a row
        for (int row = 0; row < BOARD_SIZE; row++) {
            if ((board[row][0] != ' ') && ((board[row][0] == board[row][1]) && (board[row][0] == board[row][2]))) {
                return board[row][0];
            }
        }
        // Checks if 3 in a column
        for (int col = 0; col < BOARD_SIZE; col++) {
            if ((board[0][col] != ' ') && ((board[0][col] ==  board[1][col]) && (board[0][col] == board[2][col]))) {
                return board[0][col];
            }
        }
        // Checks if 3 in a diagonal
        if ((board[0][0] != ' ') && ((board[0][0] == board[1][1]) && (board[0][0] == board[2][2]))) {
            return board[0][0];
        } else if ((board[0][2] != ' ') && ((board[0][2] == board[1][1]) && (board[0][2] == board[2][0]))) {
            return board[0][2];
        } else {
            // R for returned with no winner
            return 'R';
        }
    }

    /**
     * Helper method to reset the board if full and there is no winner
     */
    public void checkResetBoard() {
        if (checkFullBoard() && (checkWinner() == 'R')) {
            setEmptyBoard();
            System.out.println("\nBoard has been reset.");
        }
    }


    /**
     * Prints off a string that shows a game board of tic-tac-toe, with live positions.
     *
     * @return String of all cells for current game status
     */
    @Override
    public String toString() {
        return String.format(" %c │ %c │ %c \n───┼───┼───\n %c │ %c │ %c \n───┼───┼───\n %c │ %c │ %c ",
                board[0][0], board[0][1], board[0][2],
                board[1][0], board[1][1], board[1][2],
                board[2][0], board[2][1], board[2][2]);
    }
}
