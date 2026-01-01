/**
 * Subclass of Shape and Three Dimensional Shape that represents a Tetrahedron
 * <p>
 *     Area method overrides abstract super class and finds area and volume of a tetrahedron
 * </p>
 *
 * @author augward
 */

public class Tetrahedron extends ThreeDimensionalShape {
    private final double edge;

    /**
     * Creates a tetrahedron with given edge after validating it
     *
     * @param edge double of a tetrahedron's edges
     */
    public Tetrahedron(double edge){
        super("Tetrahedron");

        validSize(edge);
        this.edge = edge;
    }

    /**
     * Final definition of method get area for class Tetrahedron
     *
     * @return area = sqrt(3) * e^2
     */
    @Override
    public double getArea() {
        return Math.sqrt(3.0) * (edge * edge);
    }

    /**
     * Final definition of method get volume for class Sphere
     *
     * @return area = e^3 / (6 * sqrt(2))
     */
    @Override
    public double getVolume() {
        return (edge * edge * edge) / (6.0 * Math.sqrt(2.0));
    }
}
