/**
 * Represents a mathematical matrix with double-precision elements.
 * <p>
 *     A class with no inheritance or composition that represents a 2D
 *     matrix and can preform basic operations on it.
 *     Two constructors; one that initializes a matrix with zeros given
 *     the row and column sizes, the other takes in an array of doubles.
 * </p>
 *
 * @author augward
 */

public class Matrix {
    // Rows and Columns of Matrix.
    private int rows;
    private int cols;

    // 2D Double Array of Matrix Data, not final due to being assigned to a different array in multiply in place.
    private double[][] data;


    /**
     * Constructs a matrix with given rows and columns, where all values are 0.
     *
     * @param rows the number of rows in the matrix.
     * @param cols the number of columns in the matrix.
     */
    public Matrix(int rows, int cols) {
        checkValidDimensions(rows, cols);

        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    /**
     * Constructs a matrix with given data array, creates row and column count.
     *
     * @param data the array of arrays, holding the doubles of each element.
     */
    public Matrix(double[][] data) {
        checkValidData(data);

        rows = data.length;
        cols = data[0].length;
        this.data = data;
    }


    /**
     * Returns the matrix's rows.
     *
     * @return rows
     */
    public int getRows() {
        return rows;
    }


    /**
     * Returns the matrix's columns.
     *
     * @return cols
     */
    public int getCols() {
        return cols;
    }


    /**
     * Returns the matrix's data, 2D array.
     *
     * @return data
     */
    public double[][] getData() {
        return data;
    }

    // No need to add setters for rows, cols, and data.  Due to it warping matrices established.


    /**
     * Returns an element from a certain row and column.
     *
     * @param rows the desired row to call from
     * @param cols the desired column to call from
     * @return double element from data
     */
    public double getElement(int rows, int cols) {
        checkValidPosition(rows, cols);
        return data[rows - 1][cols - 1];
    }

    /**
     * Sets an element at a desired row and column.
     *
     * @param rows the desired row to set at
     * @param cols the desired column to set at
     * @param newElement double element to insert be inserted
     */
    public void setElement(int rows, int cols, double newElement) {
        checkValidPosition(rows, cols);
        data[rows - 1][cols - 1] = newElement;
    }


    /**
     * Helper method to make sure data inside matrix is valid.
     *
     * @param data 2D Double array that contains matrix values
     */
    public void checkValidData(double[][] data) {
        if (data == null) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("data must not be null");
        }
        if ((data.length == 0) || (data[0].length == 0)) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("data length must not be 0");
        }

        for (int i = 0; i < rows; i++) {
            if (data[i] == null) {
                // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
                throw new IllegalArgumentException("data[i] must not be null");
            }
            if (data[i].length == 0) {
                // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
                throw new IllegalArgumentException("data[i] length must not be 0");
            }
        }
    }


    /**
     * Helper method to make sure new dimensions are valid.
     *
     * @param rows rows that need to be checked
     * @param cols columns that need to be checked
     */
    public void checkValidDimensions(int rows, int cols) {
        if (rows <= 0) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("rows cannot be negative or 0");
        } else if (cols <= 0) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("cols cannot be negative or 0");
        }
    }


    /**
     * Helper method to make sure that row and column are valid callable positions.
     *
     * @param rows row that needs to be checked
     * @param cols column that needs to be checked
     */
    public void checkValidPosition(int rows, int cols) {
        if (rows <= 0 || rows > this.rows) {
            // Thrown to indicate that an array has been accessed with an illegal index. The index is either negative or greater than or equal to the size of the array.
            throw new ArrayIndexOutOfBoundsException("rows is outside bounds");
        } else if (cols <= 0 || cols > this.cols) {
            // Thrown to indicate that an array has been accessed with an illegal index. The index is either negative or greater than or equal to the size of the array.
            throw new ArrayIndexOutOfBoundsException("cols is outside bounds");
        }
    }


    /**
     * Helper method to make sure that two matrices have the same dimensions.
     *
     * @param rows rows of second matrix for comparison
     * @param cols columns of second matrix for comparison
     */
    public void checkSameDimensions(int rows, int cols) {
        if ((this.rows != rows) || (this.cols != cols)) {
            // Throw to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("dimensions are not equal");
        }
    }



    /**
     * Compares the matrix with another object.
     * Compares against object to allow use of Override and ==, but only works with matrices.
     *
     * @param obj the object of the comparison
     * @return boolean if they are equal or not
     */
    @Override
    public boolean equals(Object obj) {
        // Turns into Matrix class now instead of Object.  Cast due to pattern.
        if (obj instanceof Matrix matrix2) {
            // Checks if sizes are equal first, doesn't use checkSameDimensions due to looking for false.
            if ((rows != matrix2.getRows()) || (cols != matrix2.getCols())) {
                return false;
            }

            // Checks if each element is equivalent.
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (data[i][j] != matrix2.data[i][j]) {
                        return false;
                    }
                }
            }
            return true;

        } else {
            // Thrown to indicate that the code has attempted to cast an object to a subclass of which it is not an instance.
            throw new ClassCastException("Object is not a Matrix");
        }
    }

    /**
     * Returns an identity matrix of given size.
     *
     * @param size the number of rows and columns of the matrix
     * @return Matrix full of zeros, and 1s along the center diagonal axis
     */
    public static Matrix identity(int size) {
        // Can't use checkValidDimensions due to this being a static class.
        if (size <= 0) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("size is invalid");
        }

        Matrix identityMatrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            identityMatrix.data[i][i] = 1;
        }

        return identityMatrix;
    }

    /**
     * Creates a sub matrix from current matrix.
     * Takes in 4 parameters, which guide the bounds of the smaller matrix.
     *
     * @param upperRow the upper bound of the sub matrix
     * @param lowerRow the lower bound of the sub matrix
     * @param leftCol the left bound of the sub matrix
     * @param rightCol the right bound of the sub matrix
     * @return Matrix which is a portion of the original matrix
     */
    public Matrix subMatrix(int upperRow, int leftCol, int lowerRow, int rightCol) {
        checkValidPosition(upperRow, leftCol);
        checkValidPosition(lowerRow, rightCol);

        // adds 1 to bounds because if bounds are 1 - 3, then it's not 3-1=2, but 3 rows.
        int newRows = lowerRow - upperRow + 1;
        int newCols = rightCol - leftCol + 1;
        checkValidDimensions(newRows, newCols);

        Matrix newMatrix = new Matrix(newRows, newCols);

        for (int i = 0; i < newRows; i++) {
            // Originally for loop, yellow line unless auto changed.  Due to more efficient copying of arrays.
            System.arraycopy(data[upperRow + i - 1], leftCol - 1, newMatrix.data[i], 0, newCols);
        }
        return newMatrix;
    }

    /**
     * Prints out the matrix representation of the class, each row has its own line.
     *
     * @return string representation of the matrix
     */
    @Override
    public String toString() {
        // Allows for quick construction of strings and appends.
        StringBuilder printedMatrix = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            printedMatrix.append("[");
            for (int j = 0; j < cols; j++) {
                // adds single element of matrix and decides whether to add a comma or bracket.
                printedMatrix.append(data[i][j]);
                if (j != cols - 1) {
                    printedMatrix.append(", ");
                } else {
                    printedMatrix.append("]");
                }
            }

            // Next row of matrix
            if (i != rows - 1) {
                printedMatrix.append("\n");
            }
        }
        return printedMatrix.toString();
    }


    /**
     * Returns a new matrix of this matrix and second matrix added.
     *
     * @param matrix2 the matrix to add to current
     * @return a new matrix of both previous added
     */
    public Matrix add(Matrix matrix2) {
        checkSameDimensions(matrix2.getRows(), matrix2.getCols());
        Matrix output = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                output.data[i][j] = data[i][j] + matrix2.data[i][j];
            }
        }
        return output;
    }

    /**
     * Allows two matrices to be added in static overall method.
     *
     * @param matrix1 first matrix
     * @param matrix2 second matrix
     * @return a new matrix of both previous added
     */
    public static Matrix add(Matrix matrix1, Matrix matrix2) {
        return matrix1.add(matrix2);
    }

    /**
     * Changes the current matrix by adding a second one.
     *
     * @param matrix2 second matrix to be added
     */
    public void addInPlace(Matrix matrix2) {
        checkSameDimensions(matrix2.getRows(), matrix2.getCols());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += matrix2.data[i][j];
            }
        }
    }


    /**
     * Returns a new matrix of this matrix and second matrix subtracted.
     *
     * @param matrix2 the matrix to subtract from current
     * @return a new matrix of both previous subtracted
     */
    public Matrix subtract(Matrix matrix2) {
        checkSameDimensions(matrix2.getRows(), matrix2.getCols());
        Matrix output = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                output.data[i][j] = data[i][j] - matrix2.data[i][j];
            }
        }
        return output;
    }

    /**
     * Allows to matrices to be subtracted in static overall method.
     *
     * @param matrix1 first matrix
     * @param matrix2 second matrix
     * @return a new matrix of both previous subtracted
     */
    public static Matrix subtract(Matrix matrix1, Matrix matrix2) {
        return matrix1.subtract(matrix2);
    }

    /**
     * Changes the current matrix by subtracting a second one.
     *
     * @param matrix2 second matrix to be subtracted
     */
    public void subtractInPlace(Matrix matrix2) {
        checkSameDimensions(matrix2.getRows(), matrix2.getCols());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] -= matrix2.data[i][j];
            }
        }
    }


    /**
     * Returns a new matrix of this matrix and a scalar multiplied.
     *
     * @param scalar the double to multiply against matrix
     * @return a new matrix of both components multiplied
     */
    public Matrix scalarMultiply(double scalar) {
        Matrix output = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Multiplying and dividing it by 1,000,000 gets rid of any floating point errors while still allowing accuracy.
                output.data[i][j] = (double) Math.round((data[i][j] * scalar) * 1000000) / 1000000;
            }
        }
        return output;
    }

    /**
     * Allows a matrix and scalar to be multiplied in static overall method.
     *
     * @param matrix1 first matrix to be multiplied
     * @param scalar factor to be multiplied by
     * @return a new matrix of both parameters multiplied
     */
    public static Matrix scalarMultiply(Matrix matrix1, double scalar) {
        return matrix1.scalarMultiply(scalar);
    }

    /**
     * Changes the current matrix by multiplying it against a scalar.
     *
     * @param scalar the number each element is multiplied by
     */
    public void scalarMultiplyInPlace(double scalar) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = (double) Math.round((data[i][j] * scalar) * 1000000) / 1000000; // Stops floating point error for e-6
            }
        }
    }


    /**
     * Returns a new matrix of this matrix and second matrix multiplied.
     *
     * @param matrix2 the matrix to multiply to current
     * @return a new matrix of both previous multiplied
     */
    public Matrix multiply(Matrix matrix2) {
        if (cols != matrix2.getRows()) {
            // Thrown to indicate that a method has been passed an illegal or inappropriate argument.
            throw new IllegalArgumentException("rows and cols are not equal for multiplication");
        }

        Matrix output = new Matrix(rows, matrix2.getCols());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix2.getCols(); j++) {
                for (int k = 0; k < cols; k++) {
                    // Casts it as a double, even if it ends as an int
                    output.data[i][j] += (double) Math.round((data[i][k] * matrix2.data[k][j]) * 1000000) / 1000000; // Stops floating point error for e-6
                }
            }
        }
        return output;
    }

    /**
     * Allows two matrices to be multiplied in a static method.
     *
     * @param matrix1 first matrix
     * @param matrix2 second matrix
     * @return a new matrix of both previous multiplied
     */
    public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        return matrix1.multiply(matrix2);
    }

    /**
     * Changes the current matrix by multiplying a second one.
     * Can change rows and columns, so they can't be final
     *
     * @param matrix2 second matrix to be multiplied
     */
    public void multiplyInPlace(Matrix matrix2) {
        Matrix product = multiply(matrix2);
        rows = product.getRows();
        cols = product.getCols();
        data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            // Originally for loop, yellow line unless auto changed.
            System.arraycopy(product.data[i], 0, data[i], 0, cols);
        }
    }
}
