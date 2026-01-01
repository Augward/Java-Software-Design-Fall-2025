/**
 * Abstract class that represents any type of player
 * <p>
 *     Defines variables like type of player and their mark.
 *     Uses polymorphic behavior to change methods across subclasses.
 * </p>
 *
 * @author augward
 */

public abstract class Player {
    // Players mark of X or O and type of Computer or Human
    private final char mark;
    private final String type;

    /**
     * Constructs a new player, whether human or computer
     *
     * @param newMark X or O of player representation
     * @param newType Human or Computer type of player
     */
    public Player(char newMark, String newType) {
        if(newMark != 'X' && newMark != 'O') {
            throw new IllegalArgumentException("Invalid mark, must be X or O");
        }
        mark = newMark;
        type = newType;
    }


    /**
     * Returns the mark/char of a player
     *
     * @return char of player class
     */
    public char getMark() {
        return mark;
    }

    /**
     * Returns type of player, as a string
     *
     * @return string of player name
     */
    public String getType() {
        return type;
    }


    /**
     * Abstract method forcing all subclasses to define
     * Changes how each make their moves.
     *
     * @param board the current game board for manipulation
     */
    public abstract void makeMove(Board board);
}
