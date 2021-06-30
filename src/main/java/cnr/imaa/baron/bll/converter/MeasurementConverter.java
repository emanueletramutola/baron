package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.model.GuanDataValue;
import cnr.imaa.baron.model.Measurement;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class MeasurementConverter implements Converter{
    @Override
    public Object convert(Object input) {
        List<GuanDataValue> guanDataValues = (List<GuanDataValue>) input;

        List<Measurement> measurementList = Collections.synchronizedList(new ArrayList<>());

        guanDataValues
                .parallelStream()
                .forEach(guanDataValue -> {
                    Measurement measurement = new Measurement();

                    measurement.setTime(guanDataValue.getTime_seconds());
                    measurement.setPress(guanDataValue.getPress_hPa());
                    measurement.setTemp(guanDataValue.getTemp());
                    measurement.setWdir(guanDataValue.getWdir() == null ? null : guanDataValue.getWdir().floatValue());
                    measurement.setWspeed(guanDataValue.getWspd());
                    measurement.setGeopot(guanDataValue.getGph() == null ? null : guanDataValue.getGph().floatValue());
                    measurement.setLvltyp1(new Float(guanDataValue.getLvltyp1()));
                    measurement.setLvltyp2(new Float(guanDataValue.getLvltyp2()));
                    measurement.setPflag(guanDataValue.getPFLAG_value());
                    measurement.setZflag(guanDataValue.getZFLAG_value());
                    measurement.setTflag(guanDataValue.getTFLAG_value());
                    measurement.setDpdp(guanDataValue.getDpdp() == null ? null : guanDataValue.getDpdp() - 273.15f);
                    measurement.setDateOfObservation(guanDataValue.getDateOfObservation());
                    measurement.setStation(guanDataValue.getStation());
                    measurement.setRh(guanDataValue.getRh_calculated_CDMFormat(measurement.getTemp(), guanDataValue.getRh(), measurement.getDpdp()));
                    measurement.setU(guanDataValue.getZonalWind_CDMFormat());
                    measurement.setV(guanDataValue.getMeridionalWind_CDMFormat());
                    measurement.setFp(guanDataValue.getFrostpoint_CDMFormat());
                    measurement.setWvmr(guanDataValue.getWvmr_CDMFormat());

                    measurementList.add(measurement);
                });

        Map<Instant, List<Measurement>> measurementPerDate = measurementList.stream().collect(groupingBy(Measurement::getDateOfObservation));
        measurementPerDate.entrySet()
                .parallelStream()
                .forEach(x -> {
                    Float ascentSpeed = calculateAscentSpeed(x.getValue());

                    x.getValue()
                            .parallelStream()
                            .forEach(measurement -> {
                                measurement.setAsc(ascentSpeed);
                            });
                });

        return measurementList;
    }

    private Float calculateAscentSpeed(List<Measurement> measurementList) {
        Float ascentSpeed = null;

        List<Float> gphList = measurementList
                .parallelStream()
                .filter(x -> x.getGeopot() != null && x.getTime() != null && x.getGeopot() > -8888 && x.getTime() > -8888)
                .map(x -> x.getGeopot())
                .collect(Collectors.toList());

        if (gphList.size() > 0) {
            Float maxHeight = Collections.max(gphList);
            Float minHeight = Collections.min(gphList);

            Optional<Measurement> measurementMaxHeight = measurementList
                    .parallelStream()
                    .filter(x -> x.getTime() != null && x.getTime() > -8888 && x.getGeopot() != null)
                    .filter(x -> x.getGeopot().equals(maxHeight))
                    .findAny();

            Optional<Measurement> measurementMinHeight = measurementList
                    .parallelStream()
                    .filter(x -> x.getTime() != null && x.getTime() > -8888 && x.getGeopot() != null)
                    .filter(x -> x.getGeopot().equals(minHeight))
                    .findAny();

            if (measurementMaxHeight.isPresent() && measurementMinHeight.isPresent()) {
                Float time_maxHeight = measurementMaxHeight.get().getTime();
                Float time_minHeight = measurementMinHeight.get().getTime();

                ascentSpeed = (maxHeight - minHeight) / (time_maxHeight - time_minHeight);
            }
        }

        return ascentSpeed;
    }
}
