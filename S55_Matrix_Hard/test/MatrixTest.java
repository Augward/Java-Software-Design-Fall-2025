import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Testing for Matrix Class
 *
 * @author augward
 */

class MatrixTest {

    /**
     * Method that tests the getters and setters of the class.
     */
    @org.junit.jupiter.api.Test
    void gettersAndSetters() {
        Matrix m1 = new Matrix(3,3);
        assertEquals(3, m1.getCols());
        assertEquals(3, m1.getRows());

        m1.setElement(1,1,1);
        assertEquals(1, m1.getElement(1,1));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m1.setElement(1,4,1));
    }

    /**
     * Method that tests the checks of the constructor and getters.
     */
    @org.junit.jupiter.api.Test
    void initialChecks(){
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-1,-1));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(null));

        Matrix m2 = new Matrix(3,3);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m2.setElement(1,4,1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m2.getElement(1,4));
    }

    /**
     * Method that tests the equal method and its exceptions
     */
    @org.junit.jupiter.api.Test
    void testEquals() {
        Matrix m1 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m2 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m3 = new Matrix(new double[][]{{1,2},{3,4},{5,6}});
        Matrix m4 = new Matrix(new double[][]{{0,0},{0,0}});

        assertEquals(m1, m2);
        assertEquals(m1, m1);

        assertNotEquals(m1, m3);
        assertNotEquals(m1, m4);

        // This last one works, but yellow line appears due to redundancy.
        // assertThrows(ClassCastException.class, () -> m1.equals("not a matrix"));
    }

    /**
     * Method that tests the identity method and its exceptions
     */
    @org.junit.jupiter.api.Test
    void identity() {
        Matrix m1 = Matrix.identity(3);
        assertEquals(3, m1.getRows());
        assertEquals(3, m1.getCols());
        assertEquals(1, m1.getElement(2,2));

        assertThrows(IllegalArgumentException.class, () -> Matrix.identity(0));
    }

    /**
     * Method that tests the subMatrix method and its exceptions
     */
    @org.junit.jupiter.api.Test
    void subMatrix() {
        Matrix m1 = new Matrix(new double[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}});
        Matrix m2 = new Matrix(new double[][]{{6,7},{10,11}});
        Matrix m3 = new Matrix(new double[][]{{6,7},{10,10}});

        assertEquals(2, m1.subMatrix(2,2,3,3).getRows());

        assertEquals(m2, m1.subMatrix(2,2,3,3));
        assertNotEquals(m3, m1.subMatrix(2,2,3,3));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m1.subMatrix(2,2,5,5));
    }

    /**
     * Method that tests the toString method and its exceptions
     */
    @org.junit.jupiter.api.Test
    void testToString() {
        Matrix m1 = new Matrix(new double[][]{{1.1,2.2},{3.3,4.4}});
        String s = m1.toString();
        assertEquals("[1.1, 2.2]\n[3.3, 4.4]", s);
    }

    /**
     * Method that tests the 3 different add methods and exceptions
     */
    @org.junit.jupiter.api.Test
    void add() {
        Matrix m1 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m2 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m3 = new Matrix(new double[][]{{2,4},{6,8}});

        assertEquals(m3, m1.add(m2));
        assertEquals(m3, Matrix.add(m1, m2));
        m1.addInPlace(m2);
        assertEquals(m3, m1);

        Matrix m4 = new Matrix(new double[][]{{1,2},{3,4},{5,6}});
        assertThrows(IllegalArgumentException.class, () -> m1.add(m4));
    }

    /**
     * Method that tests the 3 different subtract methods and exceptions
     */
    @org.junit.jupiter.api.Test
    void subtract() {
        Matrix m1 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m2 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m3 = new Matrix(2,2);

        assertEquals(m3, m1.subtract(m2));
        assertEquals(m3, Matrix.subtract(m1, m2));
        m1.subtractInPlace(m2);
        assertEquals(m3, m1);

        Matrix m4 = new Matrix(new double[][]{{1,2},{3,4},{5,6}});
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m4));
    }

    /**
     * Method that tests the 3 different scalar methods
     */
    @org.junit.jupiter.api.Test
    void scalarMultiply() {
        Matrix m1 = new Matrix(new double[][]{{1,2},{3,4}});
        double scalar = -3.3;
        Matrix m2 = new Matrix(new double[][]{{-3.3,-6.6},{-9.9,-13.2}});

        assertEquals(m2, m1.scalarMultiply(scalar));
        assertEquals(m2, Matrix.scalarMultiply(m1, scalar));
        m1.scalarMultiplyInPlace(scalar);
        assertEquals(m2, m1);
    }

    /**
     * Method that tests the 3 different multiply methods and exceptions
     */
    @org.junit.jupiter.api.Test
    void multiply() {
        Matrix m1 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m2 = new Matrix(new double[][]{{1,2,3},{4.4,5,6}});
        Matrix m3 = new Matrix(new double[][]{{9.8,12,15},{20.6,26,33}});

        assertEquals(m3, m1.multiply(m2));
        assertEquals(m3, Matrix.multiply(m1, m2));
        m1.multiplyInPlace(m2);
        assertEquals(m3, m1);

        Matrix m4 = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix m5 = new Matrix(new double[][]{{1,2},{3,4},{5,6}});
        assertThrows(IllegalArgumentException.class, () -> m4.multiply(m5));
    }
}