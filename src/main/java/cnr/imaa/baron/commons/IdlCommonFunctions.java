package cnr.imaa.baron.commons;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IdlCommonFunctions {
    public static final String IDL_OPERATOR_EQ = "EQ";
    public static final String IDL_OPERATOR_NE = "NE";
    public static final String IDL_OPERATOR_GE = "GE";
    public static final String IDL_OPERATOR_GT = "GT";
    public static final String IDL_OPERATOR_LE = "LE";
    public static final String IDL_OPERATOR_LT = "LT";

    public static Double absdev;

    /**
     * Compute the median absolute deviation of a vector.
     *
     * @param values vector of double
     * @return the median absolute deviation of a vector.
     */
    public static double mad(double[] values) {
        double[] tempTable = new double[values.length];

        Median m = new Median();
        double medianValue = m.evaluate(values);

        for (int i = 0; i < values.length; i++) {
            tempTable[i] = Math.abs(values[i] - medianValue);
        }

        return m.evaluate(tempTable) / 0.6745;
    }

    public static double mad(Double[] input) {
        return mad(ArrayUtils.toPrimitive(input));
    }

    public static double[] lowess(double[] x, double[] y, double width, int order) {
        double[] ysmoo = new double[x.length];
        double tribase = 0.01d;

        TreeMap<Double, Integer> xTreeMap = new TreeMap<Double, Integer>();
        for (int i = 0; i < x.length; i++) {
            xTreeMap.put(x[i], i);
        }

        IntStream.range(0, x.length)
                .parallel()
                .forEach(idx -> {
                    Double idxLeftObj = xTreeMap.lowerKey(x[idx] - width);
                    Double idxRightObj = xTreeMap.higherKey(x[idx] + width);

                    double idxLeft = idxLeftObj == null ? xTreeMap.firstKey() : idxLeftObj.doubleValue();
                    double idxRight = idxRightObj == null ? xTreeMap.lastKey() : idxRightObj.doubleValue();

                    int min = xTreeMap.get(idxLeft);
                    int max = xTreeMap.get(idxRight);

                    int count = max - min;
                    if (count <= (order + 1)) {
                        ysmoo[idx] = y[idx];
                    } else {
                        boolean fromInclusive = idxLeft < (x[idx] - width) ? false : true;
                        boolean toInclusive = idxRight > (x[idx] + width) ? false : true;

                        Set<Double> xZ = xTreeMap.subMap(idxLeft, fromInclusive, idxRight, toInclusive).keySet();

                        double[] xZarr = new double[xZ.size()];
                        int i = 0;
                        for (double el : xZ) {
                            xZarr[i] = el - x[idx];

                            i++;
                        }

                        Set<Double> w_secondElement = xTreeMap.subMap(idxLeft, fromInclusive, idxRight, toInclusive).keySet();

                        double[] w = new double[w_secondElement.size()];
                        i = 0;
                        double a = ((1.0 - tribase) / width);
                        for (double el : w_secondElement) {
                            w[i] = 1.0 - ((Math.abs(el - x[idx])) * a);

                            i++;
                        }

                        double[] measure_errors = arrSqrt(arrDivArrayToScalar(w, 1.0));

                        double[] yZ = Arrays.copyOfRange(y, fromInclusive ? min : min + 1, toInclusive ? max + 1 : max);
                        double[] coeff = poly_fit(xZarr, yZ, order, measure_errors);

                        ysmoo[idx] = poly(0.0, coeff);
                    }
                });

        return ysmoo;
    }

    public static double[] poly_fit(double[] x, double[] y, int ndegree, double[] measure_errors) {
        int n = x.length;
        int m = ndegree + 1;
        double[] sdev = new double[measure_errors.length];

        System.arraycopy(measure_errors, 0, sdev, 0, measure_errors.length);

        double[] sdev2 = new double[sdev.length];
        for (int i = 0; i < sdev.length; i++) {
            sdev2[i] = Math.pow(sdev[i], 2);
        }

        double[][] covar = new double[m][m];

        double[][] b = new double[m][1];
        double z = 1.0d;
        double[] zArr = null;

        double[] wy = new double[y.length];
        System.arraycopy(y, 0, wy, 0, y.length);

        int wynewSize = wy.length <= sdev2.length ? wy.length : sdev2.length;
        double[] wynew = new double[y.length];
        b[0][0] = 0.0d;
        for (int i = 0; i < wynewSize; i++) {
            wynew[i] = (wy[i] / sdev2[i]);
            b[0][0] += wynew[i];
        }
        System.arraycopy(wynew, 0, wy, 0, wynew.length);

        double yfit = Double.NaN;
        double yband = Double.NaN;
        double yerror = Double.NaN;

        covar[0][0] = 0.0d;
        for (int i = 0; i < sdev2.length; i++) {
            covar[0][0] += (1.0d / sdev2[i]);
        }

        double sum;
        for (int p = 1; p < ((2 * ndegree) + 1); p++) {
            double[] zArrNew = null;

            if (p == 1) {
                zArrNew = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    zArrNew[i] = x[i] * z;
                }
            } else {
                int zArrSize = zArr.length <= x.length ? zArr.length : x.length;
                zArrNew = new double[zArrSize];

                for (int i = 0; i < zArrSize; i++) {
                    zArrNew[i] = zArr[i] * x[i];
                }
            }

            zArr = new double[zArrNew.length];
            System.arraycopy(zArrNew, 0, zArr, 0, zArrNew.length);

            if (p < m) {
                b[p][0] = 0.0d;

                int wyZarrSize = wy.length <= zArr.length ? wy.length : zArr.length;
                for (int i = 0; i < wyZarrSize; i++) {
                    b[p][0] += (wy[i] * zArr[i]);
                }
            }

            sum = 0.0d;
            int zArrSdev2Size = zArr.length <= sdev2.length ? zArr.length : sdev2.length;
            for (int i = 0; i < zArrSdev2Size; i++) {
                sum += (zArr[i] / sdev2[i]);
            }

            for (int j = Math.max(0, (p - ndegree)); j < (Math.min(ndegree, p)) + 1; j++) {
                covar[j][p - j] = sum;
            }
        }

        covar = invert(covar);

        double[][] result = matrixMultiplication(covar, b);

        double[] coeff = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            coeff[i] = result[i][0];
        }

        return coeff;
    }

    public static double poly(double x, double[] c) {
        double y;
        int n = c.length - 1;

        y = c[n];

        for (int i = n - 1; i >= 0; i--) {
            y = c[i] + (x * y);
        }

        return y;
    }

    public static Integer[] getValuesByIndices_Integer(Integer[] data, List<Integer> listIndices) {
        return getValuesByIndices_Integer(Arrays.asList(data), listIndices);
    }

    public static Integer[] getValuesByIndices_Integer(List<Integer> data, List<Integer> listIndices) {
        List<Integer> values = new ArrayList<>();

        for (Integer index : listIndices) {
            values.add(data.get(index));
        }

        return values.stream().mapToInt(x -> (x == null) ? 0 : x).boxed().toArray(Integer[]::new);
    }

    public static String[] getValuesByIndices_String(String[] data, List<Integer> listIndices) {
        return getValuesByIndices_String(Arrays.asList(data), listIndices);
    }

    public static String[] getValuesByIndices_String(List<String> data, List<Integer> listIndices) {
        List<String> values = new ArrayList<>();

        for (Integer index : listIndices) {
            values.add(data.get(index));
        }

        return values.stream().map(x -> (x == null) ? "" : x).toArray(String[]::new);
    }

    public static Double[] getValuesByIndices_Double(Double[] data, List<Integer> listIndices) {
        return getValuesByIndices_Double(Arrays.asList(data), listIndices);
    }

    public static Double[] getValuesByIndices_Double(List<Double> data, List<Integer> listIndices) {
        List<Double> values = new ArrayList<>();

        for (Integer index : listIndices) {
            values.add(data.get(index));
        }

        return values.stream().mapToDouble(x -> (x == null) ? 0.0 : x).boxed().toArray(Double[]::new);
    }

    private static List<Integer> getIndexes(Number[] data, String operator, Number valueToCheck) {
        IntStream intStream = IntStream.range(0, data.length);

        switch (operator) {
            case IDL_OPERATOR_EQ:
                intStream = intStream.filter(i -> data[i] != null && data[i].equals(valueToCheck));
                break;
            case IDL_OPERATOR_NE:
                intStream = intStream.filter(i -> data[i] != null && !data[i].equals(valueToCheck));
                break;
            case IDL_OPERATOR_GE:
                intStream = intStream.filter(i -> data[i] != null && data[i].doubleValue() >= valueToCheck.doubleValue());
                break;
            case IDL_OPERATOR_GT:
                intStream = intStream.filter(i -> data[i] != null && data[i].doubleValue() > valueToCheck.doubleValue());
                break;
            case IDL_OPERATOR_LE:
                intStream = intStream.filter(i -> data[i] != null && data[i].doubleValue() <= valueToCheck.doubleValue());
                break;
            case IDL_OPERATOR_LT:
                intStream = intStream.filter(i -> data[i] != null && data[i].doubleValue() < valueToCheck.doubleValue());
                break;
        }

        return intStream.boxed().collect(Collectors.toList());
    }

    public static double stdDev(List<Double> arr) {
        return stdDev(arr.toArray(new Double[arr.size()]));
    }

    public static double stdDev(Double[] arr) {
        return Math.sqrt(StatUtils.variance(ArrayUtils.toPrimitive(arr)));
    }

    public static Double median(Double[] arr) {
        return median(ArrayUtils.toPrimitive(arr));
    }

    public static Double median(double[] arr) {
        return StatUtils.percentile(arr, 50);
    }

    public static Double[] ladfit(Double[] a, Double[] b) {
        Double[] c = new Double[2];

        Double sx = total(a);
        Double sy = total(b);

        Double sxy = total(arrMult(a, b));
        Double sxx = total(arrMult(a, a));
        Double del = a.length * total(arrMult(a, a)) - Math.pow(sx, 2);

        if (del == 0.0) {
            c[0] = median(b);
            c[1] = 0.0;

            return c;
        }

        Double aa = (sxx * sy - sx * sxy) / del;
        Double bb = (a.length * sxy - sx * sy) / del;

        Double chisqr = total(arrPow(arrMinus(b, arrSumScalarToArray(arrMultScalarToArray(a, bb), aa)), 2));

        Double sigb = Math.sqrt(chisqr / del);

        Double b1 = bb;
        Double eps = 1d - 7;

        Double[] f1Arr = mdfunc(b1, a, b, aa, eps);
        Double f1 = f1Arr[0];
        aa = f1Arr[1];
        if (f1 == 0.0) {
            bb = b1;

            c[0] = aa;
            c[1] = bb;

            return c;
        }

        Double delb = ((f1 > 0.0) ? 3.0 : -3.0) * sigb;

        Double b2 = b1 + delb;
        Double[] f2Arr = mdfunc(b2, a, b, aa, eps);
        Double f2 = f2Arr[0];
        aa = f2Arr[1];

        while (f1 * f2 > 0.0) {
            b1 = b2;
            f1 = f2;
            b2 = b1 + delb;

            f2Arr = mdfunc(b2, a, b, aa, eps);
            f2 = f2Arr[0];
            aa = f2Arr[1];
        }

        bb = b2;
        Double f = f2;

        sigb = 0.01d * sigb;

        while (Math.abs(b2 - b1) > sigb && f != 0) {
            bb = 0.5 * (b1 + b2);

            if (bb.equals(b1) || bb.equals(b2)) break;

            Double[] fArr = mdfunc(bb, a, b, aa, eps);
            f = fArr[0];
            aa = fArr[1];

            if (f * f1 >= 0) {
                f1 = f;
                b1 = bb;
            } else {
                f2 = f;
                b2 = bb;
            }
        }

        c[0] = aa;
        c[1] = bb;

        absdev /= a.length;

        return c;
    }

    public static Double[] mdfunc(Double b1, Double[] x, Double[] y, Double a, Double eps) {
        Double[] mdfuncResult = new Double[2];

        a = median(arrMinus(y, arrMultScalarToArray(x, b1)));

        Double[] d = arrMinus(y, arrSumScalarToArray(arrMultScalarToArray(x, b1), a));

        absdev = total(arrAbs(d));

        List<Integer> nz = where(y, IDL_OPERATOR_NE, 0.0);

        if (nz.size() != 0) {
            Double[] result = arrDiv(arrValuesAtPositions(d, nz), arrAbs(arrValuesAtPositions(y, nz)));

            for (Integer nzvalue : nz) {
                d[nzvalue.intValue()] = result[nzvalue.intValue()];
            }
        }

        nz = where(arrAbs(d), IDL_OPERATOR_GT, eps);

        Double value;
        if (nz.size() != 0) {
            value = total(arrMult(arrValuesAtPositions(x, nz), arrMinus(arrCheckValue(arrValuesAtPositions(d, nz), IDL_OPERATOR_GT, 0.0)
                    , arrCheckValue(arrValuesAtPositions(d, nz), IDL_OPERATOR_LT, 0.0))));
        } else value = 0.0;

        mdfuncResult[0] = value;
        mdfuncResult[1] = a;

        return mdfuncResult;
    }

    public static Double[] arrAbs(Double[] a) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.abs(a[i]);
        }

        return b;
    }

    public static double[] arrSqrt(double[] a) {
        double[] b = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.sqrt(a[i]);
        }

        return b;
    }

    public static Double[] arrSqrt(Double[] a) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.sqrt(a[i]);
        }

        return b;
    }

    public static Double[] arrDiv(Double[] a, Double[] b) {
        Double[] c = new Double[getDimC(a, b)];

        for (int i = 0; i < c.length; i++) {
            c[i] = a[i] / b[i];
        }

        return c;
    }

    public static Double[] arrCheckValue(Double[] a, String operator, Double valueToCheck) {
        Double[] b = new Double[a.length];
        Arrays.fill(b, 0.0);

        List<Integer> listIndices = getIndexes(a, operator, valueToCheck);

        for (Integer index : listIndices) {
            b[index] = 1.0;
        }

        return b;
    }

    public static List<Integer> where(Number[] a, String operator, Number valueToCheck) {
        return getIndexes(a, operator, valueToCheck);
    }

    public static Double[] ts_smooth(Double[] arr, Integer nvalues) {
        Double[] ysmoo = null;

        List<Double> arrList = new ArrayList<>();
        arrList.addAll(Arrays.asList(arr));

        Integer nx = arrList.size();
        Integer order = Math.max(10, new Double(0.05 * nx).intValue());

        if (arrList.size() >= 11 && nvalues >= 3) {
            Double[] smx = new Double[nx];

            Integer smwidth = nvalues % 2 == 0 ? nvalues + 1 : nvalues;

            Double[] arr1_x = TS_FCAST(arrList, order, smwidth / 2, true);
            Double[] arr2_x = TS_FCAST(arrList, order, smwidth / 2, false);

            List<Double> x = new ArrayList<>();
            x.addAll(Arrays.asList(arr1_x));
            x.addAll(arrList);
            x.addAll(Arrays.asList(arr2_x));

            for (int k = 0; k < nx; k++) {
                smx[k] = total(x.subList(k, smwidth + k).toArray(new Double[smwidth]));
            }

            ysmoo = arrDivScalarToArray(smx, new Double(smwidth));
        }

        return ysmoo;
    }

    public static Double[] TS_FCAST(List<Double> x, Integer p, Integer nvalues, Boolean backcast) {
        if (nvalues > 0 && !(p < 2 || p > x.size() - 1)) {
            if (backcast) {
                Collections.reverse(x);
            }

            List<Double> y = new ArrayList<>();
            for (Double value : x) {
                y.add(value);
            }

            List<Double> data = y.subList(y.size() - p, y.size());
            Collections.reverse(data);

            Double[] fcast = new Double[nvalues];

            Double[] arcoef = TS_Coef(x, p);

            List<Double> data1 = new ArrayList<>();
            for (int j = 0; j < nvalues; j++) {
                if (j > 0) {
                    data.clear();
                    data = (ArrayList<Double>) ((ArrayList<Double>) data1).clone();
                    data1.clear();
                }

                data1.add(total(arrMult(data.toArray(new Double[data.size()]), arcoef)));
                data1.addAll(data.subList(0, p - 1));

                fcast[j] = data1.get(0);
            }

            if (backcast) {
                Collections.reverse(x);

                Collections.reverse(Arrays.asList(fcast));
            }

            return fcast;
        }

        return null;
    }

    public static Double[] TS_Coef(List<Double> x, Integer p) {
        Integer nx = x.size();

        Double mse = total(arrPow(x.toArray(new Double[x.size()]), 2)) / x.size();

        Double[] arcoef = new Double[p];

        Arrays.fill(arcoef, 0.0);

        Double[] str1 = new Double[nx + 1];
        str1[0] = 0.0;
        str1[nx] = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            str1[i + 1] = x.get(i);
        }

        Double[] str2 = new Double[nx + 1];
        str2[0] = 0.0;
        str2[nx] = 0.0;
        for (int i = 1; i < nx; i++) {
            str2[i] = x.get(i);
        }

        Double[] str3 = new Double[nx + 1];

        for (Integer k = 1; k < p + 1; k++) {
            arcoef[k - 1] = 2.0 *
                    total(arrMult(Arrays.copyOfRange(str1, 1, nx - k + 1), Arrays.copyOfRange(str2, 1, nx - k + 1)))
                    / total(arrSum(arrPow(Arrays.copyOfRange(str1, 1, nx - k + 1), 2), arrPow(Arrays.copyOfRange(str2, 1, nx - k + 1), 2)));

            mse *= (1.0 - Math.pow(arcoef[k - 1], 2));

            if (k > 1) {
                Double[] i = arrSumScalarToArray(lindgen(k - 1), 1.0);

                Double[] arr1 = Arrays.copyOfRange(str3, new Double(i[0]).intValue(), new Double(i[i.length - 1] + 1).intValue());

                Double[] str3SubArray = arrMinusArrayToScalar(i, k.doubleValue());

                Integer[] str3SubArrayInt = new Integer[str3SubArray.length];
                for (int ii = 0; ii < str3SubArray.length; ii++) {
                    str3SubArrayInt[ii] = str3SubArray[ii].intValue();
                }

                Double[] arr2 = arrMultScalarToArray(arrValuesAtPositions(str3, str3SubArrayInt), arcoef[k - 1]);

                Double[] valuesForArcoef = arrMinus(arr1, arr2);

                Double[] positionsIndexArcoef = arrMinusScalarToArray(i, new Integer(1).doubleValue());

                for (Double index : positionsIndexArcoef) {
                    arcoef[index.intValue()] = valuesForArcoef[index.intValue()];
                }
            }

            if (k == p) {
                break;
            }

            for (int ii = 1; ii < k + 1; ii++) {
                str3[ii] = arcoef[ii - 1];
            }

            for (int j = 1; j < nx - k; j++) {
                str1[j] = str1[j] - str3[k] * str2[j];
                str2[j] = str2[j + 1] - str3[k] * str1[j + 1];
            }
        }

        return arcoef;
    }

    public static Double[] lindgen(Integer value) {
        Double[] a = new Double[value];

        for (int i = 0; i < value; i++) {
            a[i] = new Integer(i).doubleValue();
        }

        return a;
    }

    public static Double total(Double[] a) {
        Double total = 0.0;
        for (Double value : a) {
            total += value;
        }

        return total;
    }

    public static Double[] arrValuesAtPositions(Double[] a, List<Integer> positions) {
        Double[] c = new Double[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            c[i] = a[positions.get(i)];
        }

        return c;
    }

    public static Double[] arrValuesAtPositions(Double[] a, Integer[] positions) {
        Double[] c = new Double[positions.length];
        for (int i = 0; i < positions.length; i++) {
            c[i] = a[positions[i]];
        }

        return c;
    }

    public static Double[] arrMinusScalarToArray(Double[] a, Double b) {
        Double[] c = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] - b;
        }

        return c;
    }

    public static Double[] arrMinusArrayToScalar(Double[] a, Double b) {
        Double[] c = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = b - a[i];
        }

        return c;
    }

    public static Double[] arrSumScalarToArray(Double[] a, Double b) {
        Double[] c = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b;
        }

        return c;
    }

    private static Integer getDimC(Number[] a, Number[] b) {
        Integer dimA = a.length;
        Integer dimB = b.length;

        return dimA <= dimB ? dimA : dimB;
    }

    public static Double[] arrMinus(Number[] a, Number[] b) {
        Double[] c = new Double[getDimC(a, b)];

        double firstElement = 0.0;
        double secondElement = 0.0;
        for (int i = 0; i < c.length; i++) {
            firstElement = a[i] != null ? a[i].doubleValue() : 0.0;
            secondElement = b[i] != null ? b[i].doubleValue() : 0.0;

            c[i] = firstElement - secondElement;
        }

        return c;
    }

    public static Double[] arrSum(Double[] a, Double[] b) {
        Double[] c = new Double[getDimC(a, b)];

        for (int i = 0; i < c.length; i++) {
            c[i] = a[i] + b[i];
        }

        return c;
    }

    public static Double[] arrExp(Double[] a) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.exp(a[i]);
        }

        return b;
    }

    public static Double[] arrLog(Double[] a) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.log(a[i]);
        }

        return b;
    }

    public static Double[] arrPow(Double[] a, Integer pow) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Math.pow(a[i], pow);
        }

        return b;
    }

    public static Double[] arrMult(Double[] a, Double[] b) {
        Double[] c = new Double[getDimC(a, b)];

        for (int i = 0; i < c.length; i++) {
            c[i] = a[i] * b[i];
        }

        return c;
    }

    public static Double[] arrMultScalarToArray(Double[] a, Double b) {
        Double[] c = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] * b;
        }

        return c;
    }

    public static Double[] arrDivScalarToArray(Double[] a, Double b) {
        Double[] c = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] / b;
        }

        return c;
    }

    public static double[] arrDivArrayToScalar(double[] a, double b) {
        double[] c = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = b / a[i];
        }

        return c;
    }

    public static double[][] invert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < n - 1; ++i)
            for (int j = i + 1; j < n; ++j)
                for (int k = 0; k < n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i] * b[index[i]][k];

        // Perform backward substitutions
        for (int i = 0; i < n; ++i) {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    public static void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i = 0; i < n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l = j + 1; l < n; ++l)
                    a[index[i]][l] -= pj * a[index[j]][l];
            }
        }
    }

    public static double[][] matrixMultiplication(double first[][], double second[][]) {
        double sum = 0;

        int m = first.length;
        int n = first[0].length;
        int p = second.length;
        int q = second[0].length;

        double multiply[][] = new double[m][q];

        for (int c = 0; c < m; c++) {
            for (int d = 0; d < q; d++) {
                for (int k = 0; k < p; k++) {
                    sum = sum + first[c][k] * second[k][d];
                }

                multiply[c][d] = sum;
                sum = 0;
            }
        }

        return multiply;
    }
}