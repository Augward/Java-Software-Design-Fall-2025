/**
 * Subclass of Shape and Three Dimensional Shape that represents a Cube
 * <p>
 *     Area method overrides abstract super class and finds area and volume of a cube
 * </p>
 *
 * @author augward
 */

public class Cube extends ThreeDimensionalShape {
    private final double size;

    /**
     * Creates a cube with given size after validating it
     *
     * @param size double of a cube edge
     */
    public Cube(double size) {
        super("Cube");

        validSize(size);
        this.size = size;
    }

    /**
     * Final definition of method get area for class Cube
     *
     * @return area = 6 * s^2
     */
    @Override
    public double getArea() {
        return 6.0 * (size * size);
    }

    /**
     * Final definition of method get volume for class Cube
     *
     * @return area = s^3
     */
    @Override
    public double getVolume() {
        return (size * size * size);
    }
}
