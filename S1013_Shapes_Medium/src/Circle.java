/**
 * Subclass of Shape and Two Dimensional Shape that represents a Circle
 * <p>
 *     Area method overrides abstract super class and finds area of a circle
 * </p>
 *
 * @author augward
 */

public class Circle extends TwoDimensionalShape {
    private final double radius;

    /**
     * Creates a circle with given radius after validating it
     *
     * @param radius double of a circle radius
     */
    public Circle(double radius) {
        super("Circle");

        validSize(radius);
        this.radius = radius;
    }

    /**
     * Final definition of method get area for class Circle
     *
     * @return area = pi * r^2
     */
    @Override
    public double getArea() {
        return Math.PI * (radius * radius);
    }

    // No toString since no need to override class, same for all
}
