package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.model.GuanDataHeader;
import cnr.imaa.baron.model.GuanDataValue;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class IgraConverter implements Converter {
    private final Logger log = LoggerFactory.getLogger(IgraConverter.class);

    @Override
    public Object convert(Object fileRows) {
        //convert the lines of the file
        List<GuanDataValue> guanDataValueList = getGuanDataValueList((List<String>) fileRows);

        Optional<GuanDataValue> gdv_testAtLeastOneValueValid = guanDataValueList.parallelStream()
                .filter(x -> x.getTemp() != null && x.getTemp() > -8888)
                .findAny();

        //check that there is at least one valid data
        if (!gdv_testAtLeastOneValueValid.isPresent()) {
            return null;
        }

        //set the date on all observations
        for (GuanDataValue guanDataValue : guanDataValueList) {
            guanDataValue.setDateOfObservation(guanDataValue.getHeader().getDateOfObservation());
        }

        //filter the launches that have no data
        List<GuanDataValue> guanDataValuesToSave = new ArrayList<>();
        Map<Instant, List<GuanDataValue>> guanDataValuesPerDate = guanDataValueList.stream().collect(groupingBy(GuanDataValue::getDateOfObservation));
        for (Map.Entry me : guanDataValuesPerDate.entrySet()) {
            List<GuanDataValue> guanDataValuesToTest = (List<GuanDataValue>) me.getValue();

            Optional<GuanDataValue> gdv_test = guanDataValuesToTest.parallelStream()
                    .filter(x -> x.getTemp() != null && x.getTemp() > -8888)
                    .findAny();

            if (gdv_test.isPresent()) {
                guanDataValuesToSave.addAll(guanDataValuesToTest);
            }
        }

        return guanDataValuesToSave;
    }

    private List<GuanDataValue> getGuanDataValueList(List<String> fileRows) {
        Collection<GuanDataValue> guanDataValues = Collections.synchronizedCollection(new ArrayList<>());

        Optional<String> firstValidHeader = fileRows
                .stream()
                .filter(x -> x.startsWith("#"))
                .filter(y -> Integer.parseInt(y.split(" ")[1]) >= 1978)
                .findFirst();

        if (firstValidHeader.isPresent()) {
            List<String> fileRowsFrom1978 = fileRows.subList(fileRows.indexOf(firstValidHeader.get()), fileRows.size());

            List<Integer> indicesOfHeaders = IntStream.range(0, fileRowsFrom1978.size())
                    .filter(i -> fileRowsFrom1978.get(i).startsWith("#"))
                    .mapToObj(Integer::valueOf)
                    .collect(Collectors.toList());

            String[] fileRowsArray = fileRowsFrom1978.toArray(new String[0]);

            IntStream.range(0, indicesOfHeaders.size())
                    .parallel()
                    .forEach(i -> {
                        GuanDataHeader guanDataHeader = buildGuanDataHeader(fileRowsArray[indicesOfHeaders.get(i)]);

                        IntStream.range(indicesOfHeaders.get(i) + 1, (i < (indicesOfHeaders.size() - 1) ? indicesOfHeaders.get(i + 1) : fileRowsArray.length))
                                .parallel()
                                .forEach(j -> {
                                    guanDataValues.add(buildGuanDataValues(fileRowsArray[j], guanDataHeader));
                                });
                    });

            guanDataValues
                    .parallelStream()
                    .forEach(gdv -> {
                        if (((GuanDataHeader) gdv.getHeader()).getHour() != null && ((GuanDataHeader) gdv.getHeader()).getHour().equals(99))
                            ((GuanDataHeader) gdv.getHeader()).setHour(null);

                        if (gdv.getEtime() <= -8888.0f) gdv.setEtime(null);
                        if (gdv.getPress() <= -8888.0f) gdv.setPress(null);
                        if (gdv.getGph() <= -8888.0f) gdv.setGph(null);
                        if (gdv.getWdir() <= -8888.0f) gdv.setWdir(null);

                        if (gdv.getTemp() <= -8888.0f) gdv.setTemp(null);
                        else gdv.setTemp(BaronCommons.celsiusToKelvin(gdv.getTemp() / 10.0f));

                        if (gdv.getDpdp() <= -8888.0f) gdv.setDpdp(null);
                        else gdv.setDpdp(BaronCommons.celsiusToKelvin(gdv.getDpdp() / 10.0f));

                        if (gdv.getRh() <= -8888.0f) gdv.setRh(null);
                        else gdv.setRh(gdv.getRh() / 10.0f);

                        if (gdv.getWspd() <= -8888.0f) gdv.setWspd(null);
                        else gdv.setWspd(gdv.getWspd() / 10.0f);
                    });
        }

        return new ArrayList<>(guanDataValues);
    }

    private GuanDataHeader buildGuanDataHeader(String row) {
        GuanDataHeader guanDataHeader = new GuanDataHeader();

        Station station = new Station();
        station.setIdStation((String) getFieldFromRaw(row, GuanDataHeader.Field.idStation));

        guanDataHeader.setStation(station);
        guanDataHeader.setYear((Integer) getFieldFromRaw(row, GuanDataHeader.Field.year));
        guanDataHeader.setMonth((Integer) getFieldFromRaw(row, GuanDataHeader.Field.month));
        guanDataHeader.setDay((Integer) getFieldFromRaw(row, GuanDataHeader.Field.day));
        guanDataHeader.setHour((Integer) getFieldFromRaw(row, GuanDataHeader.Field.hour));
        guanDataHeader.setReltime((Integer) getFieldFromRaw(row, GuanDataHeader.Field.reltime));
        guanDataHeader.setNumlev((Integer) getFieldFromRaw(row, GuanDataHeader.Field.numlev));
        guanDataHeader.setP_src((String) getFieldFromRaw(row, GuanDataHeader.Field.p_src));
        guanDataHeader.setNp_src((String) getFieldFromRaw(row, GuanDataHeader.Field.np_src));
        guanDataHeader.setLat((Float) getFieldFromRaw(row, GuanDataHeader.Field.lat) / 10000.0f);
        guanDataHeader.setLon((Float) getFieldFromRaw(row, GuanDataHeader.Field.lon) / 10000.0f);

        guanDataHeader.setVersion(2);
        guanDataHeader.calculateDateOfObservation();

        return guanDataHeader;
    }

    private Object getFieldFromRaw(String row, GuanDataHeader.Field field) {
        Object obj = null;

        switch (field) {
            case idStation:
                obj = row.substring(GuanDataHeader.ID_BEGIN - 1, GuanDataHeader.ID_END).trim();
                break;
            case year:
                obj = Integer.parseInt(row.substring(GuanDataHeader.YEAR_BEGIN - 1, GuanDataHeader.YEAR_END).trim());
                break;
            case month:
                obj = Integer.parseInt(row.substring(GuanDataHeader.MONTH_BEGIN - 1, GuanDataHeader.MONTH_END).trim());
                break;
            case day:
                obj = Integer.parseInt(row.substring(GuanDataHeader.DAY_BEGIN - 1, GuanDataHeader.DAY_END).trim());
                break;
            case hour:
                obj = Integer.parseInt(row.substring(GuanDataHeader.HOUR_BEGIN - 1, GuanDataHeader.HOUR_END).trim());
                break;
            case reltime:
                obj = Integer.parseInt(row.substring(GuanDataHeader.RELTIME_BEGIN - 1, GuanDataHeader.RELTIME_END).trim());
                break;
            case numlev:
                obj = Integer.parseInt(row.substring(GuanDataHeader.NUMLEV_BEGIN - 1, GuanDataHeader.NUMLEV_END).trim());
                break;
            case p_src:
                obj = row.substring(GuanDataHeader.P_SRC_BEGIN - 1, GuanDataHeader.P_SRC_END).trim();
                break;
            case np_src:
                obj = row.substring(GuanDataHeader.NP_SRC_BEGIN - 1, GuanDataHeader.NP_SRC_END).trim();
                break;
            case lat:
                obj = Float.parseFloat(row.substring(GuanDataHeader.LAT_BEGIN - 1, GuanDataHeader.LAT_END).trim());
                break;
            case lon:
                obj = Float.parseFloat(row.substring(GuanDataHeader.LON_BEGIN - 1, GuanDataHeader.LON_END).trim());
                break;
        }

        return obj;
    }

    private GuanDataValue buildGuanDataValues(String row, GuanDataHeader guanDataHeader) {
        GuanDataValue guanDataValue = new GuanDataValue();

        guanDataValue.setLvltyp1(Integer.parseInt(row.substring(GuanDataValue.LVLTYP1_BEGIN - 1, GuanDataValue.LVLTYP1_END).trim()));
        guanDataValue.setLvltyp2(Integer.parseInt(row.substring(GuanDataValue.LVLTYP2_BEGIN - 1, GuanDataValue.LVLTYP2_END).trim()));
        guanDataValue.setEtime(Integer.parseInt(row.substring(GuanDataValue.ETIME_BEGIN - 1, GuanDataValue.ETIME_END).trim()));
        guanDataValue.setPress(Integer.parseInt(row.substring(GuanDataValue.PRESS_BEGIN - 1, GuanDataValue.PRESS_END).trim()));
        guanDataValue.setPflag(row.substring(GuanDataValue.PFLAG_BEGIN - 1, GuanDataValue.PFLAG_END).trim());
        guanDataValue.setGph(Integer.parseInt(row.substring(GuanDataValue.GPH_BEGIN - 1, GuanDataValue.GPH_END).trim()));
        guanDataValue.setZflag(row.substring(GuanDataValue.ZFLAG_BEGIN - 1, GuanDataValue.ZFLAG_END).trim());
        guanDataValue.setTflag(row.substring(GuanDataValue.TFLAG_BEGIN - 1, GuanDataValue.TFLAG_END).trim());
        guanDataValue.setWdir(Integer.parseInt(row.substring(GuanDataValue.WDIR_BEGIN - 1, GuanDataValue.WDIR_END).trim()));

        guanDataValue.setTemp(Float.parseFloat(row.substring(GuanDataValue.TEMP_BEGIN - 1, GuanDataValue.TEMP_END).trim()));
        guanDataValue.setDpdp(Float.parseFloat(row.substring(GuanDataValue.DPDP_BEGIN - 1, GuanDataValue.DPDP_END).trim()));
        guanDataValue.setRh(Float.parseFloat(row.substring(GuanDataValue.RH_BEGIN - 1, GuanDataValue.RH_END).trim()));
        guanDataValue.setWspd(Float.parseFloat(row.substring(GuanDataValue.WSPD_BEGIN - 1, GuanDataValue.WSPD_END).trim()));

        guanDataValue.setVersion(2);

        guanDataValue.setStation(guanDataHeader.getStation());

        guanDataValue.setHeader(guanDataHeader);

        return guanDataValue;
    }
}
