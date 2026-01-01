/**
 * Abstract class for all two-dimensional shapes
 * <p>
 *     Inherits getArea from Shape to be defined in a subclasses
 *     Structures the string for all 2D Shapes when printed.
 * </p>
 *
 * @author augward
 */

public abstract class TwoDimensionalShape extends Shape {

    /**
     * Constructs a 2D Shape with a name
     *
     * @param name of the shapes in two-dimensional
     */
    public TwoDimensionalShape(String name) {
        super(name);
    }

    /**
     * Returns a personally formatted string that overwrites superclass
     * 2D Shapes inherit this method.
     *
     * @return string of name and area
     */
    @Override
    public String toString() {
        return String.format("%s, area = %f", getName(), getArea());
    }
}
