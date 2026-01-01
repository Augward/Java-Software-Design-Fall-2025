/**
 * Subclass of Shape and Two Dimensional Shape that represents a Triangle
 * <p>
 *     Area method overrides abstract super class and finds area of a triangle
 * </p>
 *
 * @author augward
 */

public class Triangle extends TwoDimensionalShape {
    private final double base;
    private final double height;

    /**
     * Creates a triangle with given base and height after validating it
     *
     * @param base double of a triangle's base edge
     * @param height double of a triangle's center height
     */
    public Triangle(double base, double height) {
        super("Triangle");

        validSize(base);
        validSize(height);
        this.base = base;
        this.height = height;
    }

    /**
     * Creates a triangle with given base of equilateral triangle after validating it
     *
     * @param base double of any triangle's edge
     */
    public Triangle(double base) {
        super("Triangle");

        validSize(base);
        this.base = base;
        this.height = Math.sqrt((base * base) - ((base / 2.0) * (base / 2.0)));
    }

    /**
     * Final definition of method get area for class Triangle
     *
     * @return area = b * h / 2
     */
    @Override
    public double getArea() {
        return (base * height) / 2;
    }
}
