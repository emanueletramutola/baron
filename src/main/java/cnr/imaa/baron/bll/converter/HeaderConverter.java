package cnr.imaa.baron.bll.converter;

import cnr.imaa.baron.bll.harmonizer.CommonStaticDataStructure;
import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.model.*;
import cnr.imaa.baron.repository.GuanDataHeaderSourceDAO;
import cnr.imaa.baron.repository.StationDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HeaderConverter implements Converter {
    private CommonStaticDataStructure commonStaticList;

    private List<SondeHistory> sondeHistory_filtered;
    private List<SondeHistoryECMWF> sondeHistoryECMWF_filtered;
    private List<WndeqHistory> wndeq_filtered;

    private final GuanDataHeaderSourceDAO guanDataHeaderSourceDAO;
    private final StationDAO stationDAO;

    public HeaderConverter(CommonStaticDataStructure commonStaticListInput) {
        this.commonStaticList = commonStaticListInput;

        guanDataHeaderSourceDAO = new GuanDataHeaderSourceDAO(commonStaticList.getDataSource());
        stationDAO = new StationDAO(commonStaticList.getDataSource());
    }

    @Override
    public Object convert(Object input) {
        List<GuanDataHeader> guanDataHeaderList = (List<GuanDataHeader>) input;

        Station station = stationDAO.getByIdStationAndNetwork(guanDataHeaderList.get(0).getStation().getIdStation(), "GUAN").get(0);

        List<Header> headers = new ArrayList<>();

        sondeHistory_filtered = commonStaticList.sondeHistory_all
                .stream()
                .filter(x -> x.getStation().getIdStation().equals(station.getIdStation()))
                .collect(Collectors.toList());

        if (station.getWmoid() != null) {
            sondeHistoryECMWF_filtered = commonStaticList.sondeHistoryECMWF_all
                    .stream()
                    .filter(x -> x.getWmoid().equals(station.getWmoid()) && x.getVerified().equals(true))
                    .collect(Collectors.toList());

            sondeHistoryECMWF_filtered
                    .parallelStream()
                    .forEach(x -> {
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(x.getDate(), ZoneId.of("UTC"));

                        Integer year = localDateTime.getYear();
                        Integer month = localDateTime.getMonthValue();

                        if (month == 12) {
                            year++;
                            month = 1;
                        } else {
                            month++;
                        }

                        x.setDateEnd(Instant.parse(year + "-" + String.format("%02d", month) + "-01T00:00:00Z").plus(-1, ChronoUnit.SECONDS));
                    });
        }

        wndeq_filtered = commonStaticList.wndeq_all
                .stream()
                .filter(x -> x.getStation().getIdStation().equals(station.getIdStation()))
                .collect(Collectors.toList());

        guanDataHeaderList.stream()
                .forEach(guanDataHeader -> {
                    headers.add(buildHeader(guanDataHeader, station));
                });

        return headers;
    }

    public Header buildHeader(GuanDataHeader guanDataHeader, Station station) {
        Header header = new Header();
        header.setConventions("CF-1.4");
        header.setTitle("IGRA radiosondes (Version 2)");
        header.setSource(commonStaticList.guanDataHeaderSourceMap.get(guanDataHeader.getP_src()));
        header.setReferences("Durre, I., and X. Yin, 2011: Enhancements of the dataset of sounding parameters derived from the Integrated Global Radiosonde Archive. 23rd Conference on Climate Variability and Change, Seattle, WA, 25 January 2011.");
        //header.setCommentMeasurement("IGRA Data Product");
        header.setDisclaimer("http://www.noaa.gov/disclaimer");
        header.setStation(guanDataHeader.getStation());
        header.setYear(guanDataHeader.getYear());
        header.setMonth(guanDataHeader.getMonth());
        header.setDay(guanDataHeader.getDay());
        header.setHour(guanDataHeader.getHour());
        header.setReltime(guanDataHeader.getDateOfRel());
        header.setNumlev(guanDataHeader.getNumlev());
        header.setLat(station.getLatitude());
        header.setLon(station.getLongitude());
        header.setName(station.getName());
        header.setState(station.getCountryCode());
        header.setElevation(station.getElevation());
        //TODO - delete the following fields
        //header.setFirstYear(Integer.toString(guanStation.getFstyear()));
        //header.setLastYear(Integer.toString(guanStation.getLstyear()));

        if (!guanDataHeader.getStation().getIdStation().substring(0, 2).toUpperCase().equals("ZZ")) {
            header.setWmo_index(Integer.parseInt(guanDataHeader.getStation().getIdStation().substring(guanDataHeader.getStation().getIdStation().length() - 5)));
        }

        header.setDateOfObservation(guanDataHeader.getDateOfObservation());

        Table3685 sonde = this.detectSonde(guanDataHeader.getDateOfObservation());
        if (sonde != null) {
            header.setRadiosonde_id(sonde.getId());
            header.setRadiosonde_code(sonde.getTac_code());
            header.setRadiosonde_name(sonde.getDescription());
            header.setRadiosonde_code_source("WMO Common Code Table 3685");
        }

        WndeqHistory wndeq = this.detectWndeq(guanDataHeader.getDateOfObservation());
        if (wndeq != null) {
            header.setEquipment_code((wndeq != null) ? wndeq.getCode() : null);
            header.setEquipment_code_source("WMO Common Code Table 0265");
        }

        header.setSza(BaronCommons.calculateSza_IDL(header.getYear(), header.getMonth(), header.getDay(), header.getHour(), 0, 0, header.getLat(), header.getLon()));

        return header;
    }

    private Table3685 detectSonde(Instant date) {
        Table3685 sonde = null;

        Optional<SondeHistory> sondeHistoryFounded = sondeHistory_filtered.stream()
                .filter(x -> (x.getBegin().isBefore(date) || x.getBegin().equals(date))
                        && (x.getEnddate().isAfter(date) || x.getEnddate().equals(date)))
                .findAny();

        Optional<Table3685> table3685RowFounded = null;

        if (sondeHistoryFounded.isPresent()) {
            SondeHistory sondeHistory = sondeHistoryFounded.get();

            table3685RowFounded = commonStaticList.table3685List
                    .stream()
                    .filter(x -> x.getTac_code() != null
                            && x.getTac_code().equals(sondeHistory.getCode())
                            && x.getDateFrom().compareTo(sondeHistory.getBegin()) <= 0
                            && (x.getDateTo() == null || x.getDateTo().compareTo(sondeHistory.getEnddate()) >= 0))
                    .findAny();
        }

        if ((table3685RowFounded == null || !table3685RowFounded.isPresent()) && sondeHistoryECMWF_filtered != null) {
            Optional<SondeHistoryECMWF> sondeHistoryECMWF_Founded = sondeHistoryECMWF_filtered.stream()
                    .filter(x -> x.getDate().compareTo(date) <= 0
                            && x.getDateEnd().compareTo(date) >= 0)
                    .findAny();

            if (sondeHistoryECMWF_Founded.isPresent()) {
                SondeHistoryECMWF sondeHistoryECMWF = sondeHistoryECMWF_Founded.get();

                table3685RowFounded = commonStaticList.table3685List
                        .stream()
                        .filter(x -> x.getTac_code() != null
                                && x.getTac_code().equals(sondeHistoryECMWF.getRadiosonde_type())
                                && x.getBufr_code().equals(sondeHistoryECMWF.getBufr_radiosonde_type())
                                && x.getDateFrom().compareTo(date) <= 0
                                && (x.getDateTo() == null || x.getDateTo().compareTo(date) >= 0))
                        .findAny();
            }
        }

        if (table3685RowFounded != null && table3685RowFounded.isPresent()) {
            sonde = table3685RowFounded.get();
        }

        return sonde;
    }

    private WndeqHistory detectWndeq(Instant date) {
        WndeqHistory wndeq = null;

        Optional<WndeqHistory> wndeqHistoryFounded = wndeq_filtered.stream()
                .filter(x -> (x.getBegin().isBefore(date) || x.getBegin().equals(date))
                        && (x.getEnddate().isAfter(date) || x.getEnddate().equals(date)))
                .findAny();

        if (wndeqHistoryFounded.isPresent()) {
            wndeq = wndeqHistoryFounded.get();
        }

        return wndeq;
    }
}
