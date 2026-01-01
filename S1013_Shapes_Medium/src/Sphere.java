/**
 * Subclass of Shape and Three Dimensional Shape that represents a Sphere
 * <p>
 *     Area method overrides abstract super class and finds area and volume of a sphere
 * </p>
 *
 * @author augward
 */

public class Sphere extends ThreeDimensionalShape {
    public final double radius;

    /**
     * Creates a sphere with given radius after validating it
     *
     * @param radius double of a sphere radius
     */
    public Sphere(double radius) {
        super("Sphere");

        validSize(radius);
        this.radius = radius;
    }

    /**
     * Final definition of method get area for class Sphere
     *
     * @return area = 4 * pi * r^2
     */
    @Override
    public double getArea() {
        return 4.0 * (Math.PI * (radius * radius));
    }

    /**
     * Final definition of method get volume for class Sphere
     *
     * @return area = 4/3 * pi * r^3
     */
    @Override
    public double getVolume() {
        return (4.0 / 3.0) * (Math.PI * (radius * radius *radius));
    }
}
