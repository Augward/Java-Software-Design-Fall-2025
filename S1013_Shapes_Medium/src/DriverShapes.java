/**
 * Driver Class that tests all the shapes in the system
 * <p>
 *     Initializes all the different subclass shapes into an array.
 *     Prints out each shape's unique toString.
 * </p>
 *
 * @author augward
 */

public class DriverShapes {
    public static void main(String[] args) {

        // Array of Shapes of different types and names
        Shape[] shapes = new Shape[] {
                new Circle(5.0),
                new Square(5.0),
                new Triangle(5.0, 5.0),
                new Triangle(5.0),
                new Sphere(5.0),
                new Cube(5.0),
                new Tetrahedron(5.0)
        };

        // Loop that iterates through different shapes and prints them out, works but not how HW sets it up
        for (Shape s : shapes) {
            System.out.println(s);
        }

        /*
        Code to check what type of Shape s is and print it out.  HW choose this method.
        for (Shape s : shapes) {
            if (s instanceof TwoDimensionalShape) {
                System.out.printf("%s, area = %f%n", s.getName(), s.getArea());
            } else if (s instanceof ThreeDimensionalShape) {
                System.out.printf("%s, area = %f & volume = %f%n", s.getName(), s.getArea(), ((ThreeDimensionalShape) s).getVolume());
            } else {
                System.out.printf("%s", s.getName());
            }
         }
         */
    }
}