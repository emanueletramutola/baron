package cnr.imaa.baron.commons;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathArrays;

import java.util.Arrays;

public class BilinearInterpolatingFunction
        implements BivariateFunction {

    /**
     * The minimum number of points that are needed to compute the
     * function.
     */
    private static final int MIN_NUM_POINTS = 2;

    /**
     * Samples x-coordinates.
     */
    private final double[] xval;

    /**
     * Samples y-coordinates.
     */
    private final double[] yval;

    /**
     * Set of cubic splines patching the whole data grid.
     */
    private final double[][] fval;

    /**
     * @param x Sample values of the x-coordinate, in increasing order.
     * @param y Sample values of the y-coordinate, in increasing order.
     * @param f Values of the function on every grid point. the expected
     *          number of elements.
     * @throws DimensionMismatchException    if the length of x and y don't
     *                                       match the row, column height of f
     * @throws IllegalArgumentException      if any of the arguments are null
     * @throws NoDataException               if any of the arrays has zero length.
     * @throws NonMonotonicSequenceException if {@code x} or {@code y}
     *                                       are not strictly increasing.
     */
    BilinearInterpolatingFunction(final double[] x, final double[] y, final double[][] f)
            throws DimensionMismatchException, IllegalArgumentException, NoDataException,
            NonMonotonicSequenceException {

        if (x == null || y == null || f == null || f[0] == null) {
            throw new IllegalArgumentException("All arguments must be non-null");
        }

        final int xLen = x.length;
        final int yLen = y.length;

        if (xLen == 0 || yLen == 0 || f.length == 0 || f[0].length == 0) {
            throw new NoDataException();
        }

        if (xLen < MIN_NUM_POINTS || yLen < MIN_NUM_POINTS || f.length < MIN_NUM_POINTS ||
                f[0].length < MIN_NUM_POINTS) {
            throw new InsufficientDataException();
        }

        if (xLen != f.length) {
            throw new DimensionMismatchException(xLen, f.length);
        }

        if (yLen != f[0].length) {
            throw new DimensionMismatchException(yLen, f[0].length);
        }

        MathArrays.checkOrder(x);
        MathArrays.checkOrder(y);

        xval = x.clone();
        yval = y.clone();
        fval = f.clone();
    }

    @Override
    public double value(final double x, final double y) {
        final int offset = 1;
        final int count = offset + 1;
        final int i = searchIndex(x, xval, offset, count);
        final int j = searchIndex(y, yval, offset, count);

        final double x1 = xval[i];
        final double x2 = xval[i + 1];
        final double y1 = yval[j];
        final double y2 = yval[j + 1];
        final double fQ11 = fval[i][j];
        final double fQ21 = fval[i + 1][j];
        final double fQ12 = fval[i][j + 1];
        final double fQ22 = fval[i + 1][j + 1];

        final double f = (fQ11 * (x2 - x) * (y2 - y) + fQ21 * (x - x1) * (y2 - y) + fQ12 * (x2 - x) * (y - y1) + fQ22 *
                (x - x1) *
                (y - y1)) /
                ((x2 - x1) * (y2 - y1));

        return f;
    }

    /**
     * @param c      Coordinate.
     * @param val    Coordinate samples.
     * @param offset how far back from found value to offset for
     *               querying
     * @param count  total number of elements forward from beginning that
     *               will be queried
     * @return the index in {@code val} corresponding to the interval
     * containing {@code c}.
     * @throws OutOfRangeException if {@code c} is out of the range
     *                             defined by the boundary values of {@code val}.
     */
    private int searchIndex(final double c, final double[] val, final int offset, final int count) {
        int r = Arrays.binarySearch(val, c);

        if (r == -1 || r == -val.length - 1) {
            throw new OutOfRangeException(c, val[0], val[val.length - 1]);
        }

        if (r < 0) {
            // "c" in within an interpolation sub-interval, which
            // returns
            // negative
            // need to remove the negative sign for consistency
            r = -r - offset - 1;
        } else {
            r -= offset;
        }

        if (r < 0) {
            r = 0;
        }

        if ((r + count) >= val.length) {
            // "c" is the last sample of the range: Return the index
            // of the sample at the lower end of the last sub-interval.
            r = val.length - count;
        }

        return r;
    }
}