/**
 * Abstract class that is the base of all shapes
 * <p>
 *     Each shape has a name of its type and this is the superclass for many shapes
 *     2D Shapes and 3D shapes are made from it, each with different area and string methods.
 *     They polymorphically change what the base of this class gives for each shape.
 * </p>
 *
 * @author augward
 */

public abstract class Shape {
    private final String name;

    /**
     * Constructor that creates a shape with given name
     *
     * @param name name of shape type
     */
    public Shape(String name) {
        this.name = name;
    }

    /**
     * Returns the name
     *
     * @return String of shape name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the area of a 2D shape, or surface area of a 3D
     *
     * @return double of area from shape
     */
    public abstract double getArea();

    /**
     * Helper method that makes sure size in shape is valid
     *
     * @param size of some base, radius, or edge of a shape
     */
    public void validSize(double size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }
    }

    /**
     * Default of string, with just name printed
     * Overwritten by extended classes
     *
     * @return the shape name
     */
    @Override
    public String toString() {
        return name;
    }
}
