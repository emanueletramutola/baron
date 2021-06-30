package cnr.imaa.baron.commons;

import jodd.datetime.JDateTime;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.decimal4j.util.DoubleRounder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public class AstronomicUtility {
    /**
     * day = Julian day (positive scalar or vector)
     * time = Universal Time in hours (scalar or vector)
     * lat = geographic latitude of point on earth's surface (degrees)
     * lon = geographic longitude of point on earth's surface (degrees)
     */
    public static float zensun(double day, double time, double lat, double lon) {
        double[] nday = {1.0, 6.0, 11.0, 16.0, 21.0, 26.0, 31.0, 36.0, 41.0, 46.0,
                51.0, 56.0, 61.0, 66.0, 71.0, 76.0, 81.0, 86.0, 91.0, 96.0,
                101.0, 106.0, 111.0, 116.0, 121.0, 126.0, 131.0, 136.0, 141.0, 146.0,
                151.0, 156.0, 161.0, 166.0, 171.0, 176.0, 181.0, 186.0, 191.0, 196.0,
                201.0, 206.0, 211.0, 216.0, 221.0, 226.0, 231.0, 236.0, 241.0, 246.0,
                251.0, 256.0, 261.0, 266.0, 271.0, 276.0, 281.0, 286.0, 291.0, 296.0,
                301.0, 306.0, 311.0, 316.0, 321.0, 326.0, 331.0, 336.0, 341.0, 346.0,
                351.0, 356.0, 361.0, 366.0};

        double[] eqt = {-3.23, -5.49, -7.60, -9.48, -11.09, -12.39, -13.34, -13.95, -14.23, -14.19,
                -13.85, -13.22, -12.35, -11.26, -10.01, -8.64, -7.18, -5.67, -4.16, -2.69,
                -1.29, -0.02, 1.10, 2.05, 2.80, 3.33, 3.63, 3.68, 3.49, 3.09,
                2.48, 1.71, 0.79, -0.24, -1.33, -2.41, -3.45, -4.39, -5.20, -5.84,
                -6.28, -6.49, -6.44, -6.15, -5.60, -4.82, -3.81, -2.60, -1.19, 0.36,
                2.03, 3.76, 5.54, 7.31, 9.04, 10.69, 12.20, 13.53, 14.65, 15.52,
                16.12, 16.41, 16.36, 15.95, 15.19, 14.09, 12.67, 10.93, 8.93, 6.70,
                4.32, 1.86, -0.62, -3.23};

        double[] dec = {-23.06, -22.57, -21.91, -21.06, -20.05, -18.88, -17.57, -16.13, -14.57, -12.91,
                -11.16, -9.34, -7.46, -5.54, -3.59, -1.62, 0.36, 2.33, 4.28, 6.19,
                8.06, 9.88, 11.62, 13.29, 14.87, 16.34, 17.70, 18.94, 20.04, 21.00,
                21.81, 22.47, 22.95, 23.28, 23.43, 23.40, 23.21, 22.85, 22.32, 21.63,
                20.79, 19.80, 18.67, 17.42, 16.05, 14.57, 13.00, 11.33, 9.60, 7.80,
                5.95, 4.06, 2.13, 0.19, -1.75, -3.69, -5.62, -7.51, -9.36, -11.16,
                -12.88, -14.53, -16.07, -17.50, -18.81, -19.98, -20.99, -21.85, -22.52, -23.02,
                -23.33, -23.44, -23.35, -23.06};

        //compute the subsolar coordinates
        double dd = (((int) day + DoubleRounder.round(time / 24.0, 1) - 1.0) % 365.25) + 1;

        UnivariateInterpolator interpolator = new LinearInterpolator();

        UnivariateFunction interpFunc_1 = interpolator.interpolate(nday, eqt);
        UnivariateFunction interpFunc_2 = interpolator.interpolate(nday, dec);
        double eqtime = interpFunc_1.value(dd) / 60;
        double decang = interpFunc_2.value(dd);

        double latsun = decang;
        double lonsun = -15 * (time - 12 + eqtime);

        //compute the solar zenith, azimuth and flux multiplier
        double t0 = Math.toRadians(90 - lat); //colatitude of point
        double t1 = Math.toRadians(90 - latsun); //colatitude of sun
        double p0 = Math.toRadians(lon); //longitude of point
        double p1 = Math.toRadians(lonsun); //longitude of sun
        double zz = Math.cos(t0) * Math.cos(t1) + Math.sin(t0) * Math.sin(t1) * Math.cos(p1 - p0); //normalized solar
        //double yy = Math.sin(t1) * Math.sin(p1 - p0); //coodinates in rotated
        //double xx = Math.cos(t0) * Math.sin(t1) * Math.cos(p1 - p0) - Math.sin(t0) * Math.cos(t1); //coordinate system
        //double azimuth = -Math.atan2(yy, xx) * 180.0 * Math.PI; //solar azimuth
        double zenith = Math.acos(zz) * 180.0 / Math.PI; //solar zenith

        return new Float(zenith);
    }

    public static double[] calculateJulianDay(Integer[] years, Integer[] months, Integer[] days, Integer[] hours) {
        Integer[] minutes = new Integer[hours.length];
        Arrays.fill(minutes, 0);

        Integer[] seconds = minutes.clone();
        Integer[] milliseconds = minutes.clone();

        return calculateJulianDay(years, months, days, hours, minutes, seconds, milliseconds);
    }

    public static double[] calculateJulianDay(Integer[] years, Integer[] months, Integer[] days, Integer[] hours, Integer[] minutes, Integer[] seconds, Integer[] milliseconds) {
        double[] julianDates = new double[years.length];

        for (int i = 0; i < years.length; i++) {
            julianDates[i] = calculateJulianDay(years[i], months[i], days[i], hours[i], minutes[i], seconds[i], milliseconds[i]);
        }

        return julianDates;
    }

    public static double calculateJulianDay(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        JDateTime jdt = new JDateTime(year, month, day, hour, minute, second, millisecond);

        return jdt.getJulianDateDouble();
    }

    public static double calculateJulianDay(Instant date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date, ZoneId.of("UTC"));

        return calculateJulianDay(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), 0);
    }

    public static int[] calculateDateFromJulianDay(double julianDay) {
        int[] dateReturn = new int[4];

        final int YEAR = 0;
        final int MONTH = 1;
        final int DAY = 2;
        final int HOUR = 3;

        JDateTime jdt = new JDateTime(julianDay);

        dateReturn[YEAR] = jdt.getYear();
        dateReturn[MONTH] = jdt.getMonth();
        dateReturn[DAY] = jdt.getDay();
        dateReturn[HOUR] = jdt.getHour();

        return dateReturn;
    }
}
