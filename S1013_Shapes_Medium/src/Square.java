/**
 * Subclass of Shape and Two Dimensional Shape that represents a Square
 * <p>
 *     Area method overrides abstract super class and finds area of a square
 * </p>
 *
 * @author augward
 */

public class Square extends TwoDimensionalShape {
    private final double size;

    /**
     * Creates a square with given size after validating it
     *
     * @param size double of a square edge
     */
    public Square(double size) {
        super("Square");

        validSize(size);
        this.size = size;
    }

    /**
     * Final definition of method get area for class Square
     *
     * @return area = s^2
     */
    @Override
    public double getArea() {
        return (size * size);
    }
}
