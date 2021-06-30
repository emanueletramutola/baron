package cnr.imaa.baron.commons;


import cnr.imaa.baron.model.Measurement;
import cnr.imaa.baron.model.Station;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.decimal4j.util.DoubleRounder;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BaronCommons {
    public static int YEAR_START = 1978;
    public static final Float LATITUDE_FROM_NORTH_POLE = 70.0f;
    public static final Float LATITUDE_TO_NORTH_POLE = 90.0f;
    public static final Float LATITUDE_FROM_NH = 25.01f;
    public static final Float LATITUDE_TO_NH = 69.99f;
    public static final Float LATITUDE_FROM_EQUATOR = -25.00f;
    public static final Float LATITUDE_TO_EQUATOR = 25.00f;
    public static final Float LATITUDE_FROM_SH = -69.99f;
    public static final Float LATITUDE_TO_SH = -25.01f;
    public static final Float LATITUDE_FROM_SOUTH_POLE = -91.0f;
    public static final Float LATITUDE_TO_SOUTH_POLE = -70.0f;

    public static final Float LATITUDE_FROM_MEDITERRANEAN_SEA = 34.0f;
    public static final Float LATITUDE_TO_MEDITERRANEAN_SEA = 55.0f;
    public static final Float LONGITUDE_FROM_MEDITERRANEAN_SEA = -11.0f;
    public static final Float LONGITUDE_TO_MEDITERRANEAN_SEA = 35.0f;

    public static final int VALUE_CHECKED_VALID = 0;
    public static final int VALUE_CHECKED_DISCARDED = 1;
    public static final int VALUE_CHECKED_MISSING = 2;

    private final static double Rd = 287.04;
    private final static double γ45 = 9.80665;

    public enum NETWORKS {
        GUAN,
        RHARM,
        GRUAN
    }

    public enum CLIMATE_ZONE {
        NORTH_POLE, NH, EQUATOR, SH, SOUTH_POLE //,TROPICS_N, TROPICS_S
    }

    public enum GEOGRAPHIC_AREA {
        MEDITERRANEAN_SEA,
        LAT_34_55_PLANET
    }

    public enum ECV {
        TEMP,
        RH,
        U,
        V
    }

    public enum ZEN {
        DAY,
        NIGHT
    }

    public enum SIGNIFICANT_FIGURES {
        TEMP(2),
        RH(4),
        WIND(2),
        WVMR(2);

        public final Integer numSignificantFigures;

        SIGNIFICANT_FIGURES(Integer numSignificantFigures) {
            this.numSignificantFigures = numSignificantFigures;
        }
    }

    public static <T> Collection<List<T>> partitionByCores(List<T> inputList) {
        int size = inputList.size() < getNumberOfCores()
                ? 1 :
                Double.valueOf(Math.ceil(inputList.size() / (float) getNumberOfCores())).intValue();

        return partitionByQuantity(inputList, size);
    }

    public static <T> Collection<List<T>> partitionByQuantity(List<T> inputList, int quantity) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / quantity))
                .values();
    }

    public static void addVariable(NetcdfFileWriter writer, String shortName, DataType dataType, List<Dimension> dims, String units, String long_name,
                                   String standard_name, String comment, boolean useNaN, String[] flag_values, String[] flag_meanings) {

        Variable variable = writer.addVariable(null, shortName, dataType, dims);

        if (!standard_name.equals("")) variable.addAttribute(new Attribute("standard_name", standard_name));
        variable.addAttribute(new Attribute("long_name", long_name));
        variable.addAttribute(new Attribute("units", units));
        if (useNaN) variable.addAttribute(new Attribute("format_nan", "NaN"));
        variable.addAttribute(new Attribute("comment", comment));
        if (flag_values != null) variable.addAttribute(new Attribute("flag_values", Arrays.asList(flag_values)));
        if (flag_meanings != null) variable.addAttribute(new Attribute("flag_meanings", Arrays.asList(flag_meanings)));
    }

    public static Float calculateU(Float wspd, Float wdir) {
        //http://tornado.sfsu.edu/geosciences/classes/m430/Wind/WindDirection.html

        return -Math.abs(wspd) * new Float(Math.sin(Math.toRadians(wdir.doubleValue())));
    }

    public static Float calculateV(Float wspd, Float wdir) {
        //http://tornado.sfsu.edu/geosciences/classes/m430/Wind/WindDirection.html

        return -Math.abs(wspd) * new Float(Math.cos(Math.toRadians(wdir.doubleValue())));
    }

    public static Instant calculateDate(Integer year, Integer month, Integer day, Integer hours, Integer minutes, Integer seconds) {
        return Instant.parse(
                String.format("%04d", year) + "-" +
                        String.format("%02d", month) + "-" +
                        String.format("%02d", day) + "T" +
                        String.format("%02d", hours) + ":" +
                        String.format("%02d", minutes) + ":" +
                        String.format("%02d", seconds) + "Z");
    }

    public static List<Double> calculatePsat(List<Double> temperatureList) {
        List<Double> pSatList = new ArrayList<>();

        for (Double temp : temperatureList) {
            pSatList.add(calculatePsat(temp));
        }

        return pSatList;
    }

    public static double calculatePsat(Double temperature) {
        //Hyland and Wexler, 1983
        //https://www.eas.ualberta.ca/jdwilson/EAS372_13/Vomel_CIRES_satvpformulae.html

        return Math.exp((-0.58002206E4 / temperature)
                + 0.13914993E1
                - (0.48640239E-1 * temperature)
                + (0.41764768E-4 * Math.pow(temperature, 2))
                - (0.14452093E-7 * Math.pow(temperature, 3))
                + (0.65459673E1 * Math.log(temperature))) / 100;
    }

    public static Float calculateWvmrFromDewpoint(Float dewpoint_temperature, Float press) {
        Float Pactual = new Float(calculatePsat(dewpoint_temperature.doubleValue()));

        return calculateWvmr(Pactual, press);
    }

    public static Float calculateWvmr(Float Pactual, Float press) {
        //https://www.hatchability.com/Vaisala.pdf
        Float wvmr = (621.9907f * Pactual) / (press - Pactual);

        if (!wvmr.isNaN()) {
            wvmr = new Float(DoubleRounder.round(wvmr, 2));
        }

        return wvmr;
    }

    public static Float conversion_pa_to_hpa(Float value) {
        if (value != null) {
            return new Float(value / 100.0f);
        } else {
            return null;
        }
    }

    public static float calculateSza(int year, int month, int day, int hour, int minute, int second, double lat, double lon) {
        Double juliandate = new Double(AstronomicUtility.calculateJulianDay(year, month, day, hour, minute, second, 0));

        float sza = AstronomicUtility.zensun(juliandate.doubleValue(), hour, lat, lon);

        sza = (90.0f - sza);

        if (sza > 85.0f) {
            sza = 85.0f;
        }

        return sza;
    }

    public static float calculateSza_IDL(int year, int month, int day, int hour, int minute, int second, double lat, double lon) {
        Double juliandate = new Double(AstronomicUtility.calculateJulianDay(year, month, day, hour, minute, second, 0));

        return AstronomicUtility.zensun(juliandate.doubleValue(), hour, lat, lon);
    }

    public static float celsiusToKelvin(float tempCelsius) {
        return tempCelsius + 273.15f;
    }

    public static void exportCSV(ObjectWriter myObjectWriter, File tempFile, Object objToPrint) throws IOException {
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
        myObjectWriter.writeValue(writerOutputStream, objToPrint);
    }

    public static double calculateRh(Double temperature, Double dewpoint) {
        Double rh = null;

        if ((temperature != null) && (dewpoint != null)) {
            if (dewpoint >= 30.0) {
                rh = 0.0;
            } else {
                Double dewpoint_temperature = temperature - dewpoint;

                double e_actual = BaronCommons.calculatePsat(dewpoint_temperature.doubleValue());
                double e_saturation = BaronCommons.calculatePsat(temperature.doubleValue());

                rh = (100 * e_actual / e_saturation);
            }
        }

        return DoubleRounder.round(rh /= 100.0f, 4);
    }

    public static void fixGeopotentialHeight(Station station, List<Measurement> measurementList) {
        measurementList.sort(Comparator.comparing(Measurement::getPress).reversed());

        for (int i = 0; i < measurementList.size() - 1; i++) {
            Measurement measurementStart = measurementList.get(i);

            if (i == 0 && (measurementStart.getGeopot() == null || measurementStart.getGeopot() <= -8888.0f)) {
                measurementStart.setGeopot(station.getElevation());
                continue;
            }

            if (measurementStart.getGeopot() == null || measurementStart.getGeopot() <= -8888.0f) {
                for (int j = i - 1; j > -1; j--) {
                    if (measurementList.get(j).getGeopot() != null && measurementList.get(j).getGeopot() > -8888.0f) {
                        measurementStart = measurementList.get(j);
                        break;
                    }
                }
            }

            Measurement measurementEnd = measurementList.get(i + 1);

            if (measurementEnd.getGeopot() == null || measurementEnd.getGeopot() <= -8888.0f) {
                Double ΔH = calculateΔH(measurementStart, measurementEnd);

                if (ΔH != null) {
                    measurementEnd.setGeopot(measurementStart.getGeopot() + ΔH.floatValue());
                } else {
                    measurementEnd.setGeopot(null);
                }
            }
        }
    }

    private static Double calculateΔH(Measurement measurementStart, Measurement measurementEnd) {
        if (measurementStart.getDpdp() == null || measurementStart.getDpdp() <= -8888.0f || measurementEnd.getDpdp() == null || measurementEnd.getDpdp().doubleValue() <= -8888.0f)
            return null;

        double pStart = measurementStart.getPress().doubleValue();
        double pEnd = measurementEnd.getPress().doubleValue();
        double tempStart = measurementStart.getTemp().doubleValue();
        double tempEnd = measurementEnd.getTemp().doubleValue();

        double fattPress = Math.log10(pStart / pEnd);
        double tempMedia = (tempStart + tempEnd) / 2;
        double ps = BaronCommons.calculatePsat(tempMedia);
        double pMedia = Math.sqrt(pStart * pEnd);

        double dewpointStart = measurementStart.getDpdp().doubleValue();
        double dewpointEnd = measurementEnd.getDpdp().doubleValue();
        double dewpointMedia = (dewpointStart + dewpointEnd) / 2;

        double rh = BaronCommons.calculateRh(tempMedia, dewpointMedia);

        double tv = tempMedia / (1 - (0.01 * rh * (1 - 0.622) * ps / pMedia));

        return (Rd / γ45) * tv * fattPress;
    }

    public static int getNumberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static Integer getYear(Instant dateOfObservation) {
        Integer year = null;

        if (dateOfObservation != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(dateOfObservation, ZoneId.of("UTC"));

            year = localDateTime.getYear();
        }

        return year;
    }

    public static Integer getHour(Instant dateOfObservation) {
        Integer hour = null;

        if (dateOfObservation != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(dateOfObservation, ZoneId.of("UTC"));

            hour = localDateTime.getHour();
        }

        return hour;
    }
}
