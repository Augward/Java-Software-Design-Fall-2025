import java.util.Arrays;

/**
 * Driver Class that shows examples of Matrix Class's functionality
 *
 * @author augward
 */

public class DriverMatrix {
    public static void main(String[] args) {
        // Test Matrix Constructors
        Matrix m1 = new Matrix(new double[][] {{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][] {{7, 8, 9}, {10, 11, 12}});

        System.out.println("Matrix 1:\n" + m1);
        System.out.println("Matrix 2:\n" + m2);
        System.out.println();

        // Test Matrix Addition
        Matrix result1 = m1.add(m2);
        System.out.println("Result 1; m1 + m2:\n" + result1);

        Matrix result2 = Matrix.add(m1, m2);
        System.out.println("Result 2; m1 + m2:\n" + result2);
        System.out.println();

        m1.addInPlace(m2);
        System.out.println("Matrix 1 changed:\n" + m1);
        System.out.println("Matrix 2 normal:\n" + m2);
        System.out.println();


        // Test Matrix Methods: Identity, Setters, Getters, Equal
        Matrix m3 = new Matrix(3, 4);
        Matrix m4 = Matrix.identity(3);

        System.out.println("Matrix 3:\n" + m3);
        System.out.println("Matrix 4:\n" + m4);
        System.out.println();

        m3.setElement(1, 2, 3);
        m3.setElement(1, 1, 5);
        m3.setElement(2, 1, 6);
        m3.setElement(3, 4, 7);

        System.out.println("Matrix 3 updated:\n" + m3);
        System.out.println("Matrix 3 Rows, Cols, Data: " + m3.getRows() + ", " + m3.getCols() + ", " + Arrays.deepToString(m3.getData()));
        System.out.println("Matrix 3 Element (3,4): " + m3.getElement(3, 4));

        System.out.println("Are Matrix 3 and 4 equal? → " + (m3 == m4));
        System.out.println();

        // Test Matrix SubMatrix
        Matrix m5 = m3.subMatrix(1,1,2,3);
        System.out.println("Matrix 5; sub matrix:\n" + m5);
        System.out.println("Matrix 5 Rows & Cols: " + m5.getRows() + ", " + m5.getCols());
        System.out.println();

        // Test Matrix Subtraction
        Matrix result3 = m1.subtract(m2);
        System.out.println("Result 3; m1 - m2:\n" + result3);

        Matrix result4 = Matrix.subtract(m1, m2);
        System.out.println("Result 4; m1 - m2:\n" + result4);
        System.out.println();

        m1.subtractInPlace(m2);
        System.out.println("Matrix 1 changed:\n" + m1);
        System.out.println("Matrix 2 normal:\n" + m2);
        System.out.println();

        // Test Matrix Scalar Multiplication
        Matrix result5a = m1.scalarMultiply(2);
        Matrix result5b = Matrix.scalarMultiply(result5a,2);
        System.out.println("Result 5; m1 * 2 * 2:\n" + result5b);

        m1.scalarMultiplyInPlace(3);
        System.out.println("Matrix 1; m1 * 3:\n" + m1);
        System.out.println();

        // Test Matrix Multiplication
        Matrix result6 = m1.multiply(m4);
        System.out.println("Result 6; m1 * m4:\n" + result6);

        m4.setElement(1, 2, 3);
        Matrix result7 = Matrix.multiply(m1, m4);
        System.out.println("Result 7; m1 * m4:\n" + result7);

        m4.setElement(1, 2, 4);
        m1.multiplyInPlace(m4);
        System.out.println("Matrix 1; m1 * m4:\n" + m1);
    }
}
