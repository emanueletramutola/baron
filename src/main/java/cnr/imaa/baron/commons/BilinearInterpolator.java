package cnr.imaa.baron.commons;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

public class BilinearInterpolator implements BivariateGridInterpolator {

    @Override
    public BivariateFunction interpolate(final double[] xval, final double[] yval, final double[][] fval)
            throws NoDataException, DimensionMismatchException, NonMonotonicSequenceException,
            NumberIsTooSmallException {

        if (xval == null || yval == null || fval == null || fval[0] == null) {
            throw new IllegalArgumentException("Input arguments must all be non-null");
        }

        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }

        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);

        return new BilinearInterpolatingFunction(xval, yval, fval);
    }
}