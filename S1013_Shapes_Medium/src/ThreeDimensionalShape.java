/**
 * Abstract class for all three-dimensional shapes
 * <p>
 *     Inherits getArea from Shape to be defined as surface area in subclasses.
 *     Creates the base of getVolume method for 3D shapes only.
 *     Overrides the toString method so that it prints differently for 3D Shapes.
 * </p>
 *
 * @author augward
 */

public abstract class ThreeDimensionalShape extends Shape {

    /**
     * Constructs a 3D Shape with a name
     *
     * @param name of the shapes in three-dimensions
     */
    public ThreeDimensionalShape(String name) {
        super(name);
    }

    /**
     * Gets the volume of all 3D shapes
     * Inside Three Dimensions only due to 2D being flat and not needing it
     *
     * @return volume of a 3D shape
     */
    public abstract double getVolume();

    /**
     * Returns a personally formatted string that overwrites superclass
     * 3D Shapes inherit this method.
     *
     * @return string of name, volume, and surface area
     */
    @Override
    public String toString() {
        return String.format("%s, area = %f & volume = %f", getName(), getArea(), getVolume());
    }
}
