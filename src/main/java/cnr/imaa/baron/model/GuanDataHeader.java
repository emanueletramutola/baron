package cnr.imaa.baron.model;

import cnr.imaa.baron.commons.BaronCommons;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class GuanDataHeader extends BaseHeader {
    private Integer guandataheader_id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer reltime;
    private Integer numlev;
    private String p_src;
    private String np_src;
    private Float lat;
    private Float lon;
    private Integer version;

    public static final Integer HEADREC_BEGIN = 1;
    public static final Integer HEADREC_END = 1;
    public static final Integer ID_BEGIN = 2;
    public static final Integer ID_END = 12;
    public static final Integer ID_V1_END = 6;
    public static final Integer YEAR_BEGIN = 14;
    public static final Integer YEAR_END = 17;
    public static final Integer YEAR_V1_BEGIN = 7;
    public static final Integer YEAR_V1_END = 10;
    public static final Integer MONTH_BEGIN = 19;
    public static final Integer MONTH_END = 20;
    public static final Integer MONTH_V1_BEGIN = 11;
    public static final Integer MONTH_V1_END = 12;
    public static final Integer DAY_BEGIN = 22;
    public static final Integer DAY_END = 23;
    public static final Integer DAY_V1_BEGIN = 13;
    public static final Integer DAY_V1_END = 14;
    public static final Integer HOUR_BEGIN = 25;
    public static final Integer HOUR_END = 26;
    public static final Integer HOUR_V1_BEGIN = 15;
    public static final Integer HOUR_V1_END = 16;
    public static final Integer RELTIME_BEGIN = 28;
    public static final Integer RELTIME_END = 31;
    public static final Integer RELTIME_V1_BEGIN = 17;
    public static final Integer RELTIME_V1_END = 20;
    public static final Integer NUMLEV_BEGIN = 33;
    public static final Integer NUMLEV_END = 36;
    public static final Integer NUMLEV_V1_BEGIN = 21;
    public static final Integer NUMLEV_V1_END = 24;
    public static final Integer P_SRC_BEGIN = 38;
    public static final Integer P_SRC_END = 45;
    public static final Integer NP_SRC_BEGIN = 47;
    public static final Integer NP_SRC_END = 54;
    public static final Integer LAT_BEGIN = 56;
    public static final Integer LAT_END = 62;
    public static final Integer LON_BEGIN = 64;
    public static final Integer LON_END = 71;

    public enum Field {
        idStation, year, month, day, hour, reltime, numlev, p_src, np_src, lat, lon
    }

    public Integer getGuandataheader_id() {
        return guandataheader_id;
    }

    public void setGuandataheader_id(Integer guandataheader_id) {
        this.guandataheader_id = guandataheader_id;
    }

    @Override
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getReltime() {
        return reltime;
    }

    public void setReltime(Integer reltime) {
        this.reltime = reltime;
    }

    public Integer getNumlev() {
        return numlev;
    }

    public void setNumlev(Integer numlev) {
        this.numlev = numlev;
    }

    public String getP_src() {
        return p_src;
    }

    public void setP_src(String p_src) {
        this.p_src = p_src;
    }

    public String getNp_src() {
        return np_src;
    }

    public void setNp_src(String np_src) {
        this.np_src = np_src;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getZenithSun() {
        return new Double(BaronCommons.calculateSza_IDL(this.year, this.month, this.day, this.hour, 0, 0, this.lat, this.lon));
    }

    public Instant getDateOfRel() {
        Instant dateTime = null;

        if (this.year > 0 && this.month > 0 && this.day > 0) {
            Integer hour_rel_time = (String.format("%04d", this.reltime).substring(0, 2).compareToIgnoreCase("99") != 0) ? Integer.parseInt(String.format("%04d", this.reltime).substring(0, 2)) : null;
            Integer minutes_rel_time = (String.format("%04d", this.reltime).substring(2).compareToIgnoreCase("99") != 0) ? Integer.parseInt(String.format("%04d", this.reltime).substring(2)) : 0;

            if (hour_rel_time == null) {
                hour_rel_time = !this.hour.equals(99) ? this.hour : 0;
            }

            dateTime = Instant.parse(
                    String.format("%04d", this.year) + "-" +
                            String.format("%02d", this.month) + "-" +
                            String.format("%02d", this.day) + "T" +
                            String.format("%02d", hour_rel_time) + ":" +
                            String.format("%02d", minutes_rel_time) + ":00Z");

            if (hour.equals(0) && hour_rel_time > 20) {
                dateTime = dateTime.minus(1, ChronoUnit.DAYS);
            }
        }

        return dateTime;
    }

    public static Instant calculateDate(Integer year, Integer month, Integer day, Integer hours, Integer relTime) {
        Instant dateOfFile = null;

        if (year != null && month != null && day != null) {
            Integer minutes = 0;

            if (hours == null) {
                hours = (String.format("%04d", relTime).substring(0, 2).compareToIgnoreCase("99") != 0) ? Integer.parseInt(String.format("%04d", relTime).substring(0, 2)) : 0;
                minutes = (String.format("%04d", relTime).substring(2).compareToIgnoreCase("99") != 0) ? Integer.parseInt(String.format("%04d", relTime).substring(2)) : 0;
            }

            dateOfFile = Instant.parse(
                    String.format("%04d", year) + "-" +
                            String.format("%02d", month) + "-" +
                            String.format("%02d", day) + "T" +
                            String.format("%02d", hours) + ":" +
                            String.format("%02d", minutes) + ":00Z");
        }

        return dateOfFile;
    }

    public void calculateDateOfObservation() {
        super.setDateOfObservation(calculateDate(this.year, this.month, this.day, this.hour, this.reltime));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!GuanDataHeader.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final GuanDataHeader other = (GuanDataHeader) obj;

        if ((getStation() == null) ? (other.getStation() != null) : !getStation().equals(other.getStation())) {
            return false;
        }
        if ((this.p_src == null) ? (other.p_src != null) : !this.p_src.equals(other.p_src)) {
            return false;
        }
        if ((this.np_src == null) ? (other.np_src != null) : !this.np_src.equals(other.np_src)) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        if (this.hour != other.hour) {
            return false;
        }
        if (this.reltime != other.reltime) {
            return false;
        }
        if (this.numlev != other.numlev) {
            return false;
        }
        if (this.lat != other.lat) {
            return false;
        }
        if (this.lon != other.lon) {
            return false;
        }

        return true;
    }

    @Override
    public Object getIdPK() {
        return getGuandataheader_id();
    }

    @Override
    public String getIdPKField() {
        return "g_product_id";
    }

    @Override
    public void setIdPK(Object idPK) {
        this.setGuandataheader_id((Integer) idPK);
    }
}
