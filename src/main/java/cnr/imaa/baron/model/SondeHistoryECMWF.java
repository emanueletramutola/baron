package cnr.imaa.baron.model;

import java.time.Instant;

public class SondeHistoryECMWF extends BaseObj {
    public static final int WMOID_FIELD_NUMBER = 0;
    public static final int NREP_FIELD_NUMBER = 1;
    public static final int NWT_FIELD_NUMBER = 2;
    public static final int NWW_FIELD_NUMBER = 3;
    public static final int N99_FIELD_NUMBER = 4;
    public static final int LATITUDE_FIELD_NUMBER = 5;
    public static final int LONGITUDE_FIELD_NUMBER = 6;
    public static final int RADIOSONDE_TYPE_FIELD_NUMBER = 7;
    public static final int MAXL_FIELD_NUMBER = 8;
    public static final int NDP_FIELD_NUMBER = 9;
    public static final int NDT_FIELD_NUMBER = 10;
    public static final int BUFR_NREP_FIELD_NUMBER = 11;
    public static final int BUFR_NWT_FIELD_NUMBER = 12;
    public static final int BUFR_NWW_FIELD_NUMBER = 13;
    public static final int BUFR_N99_FIELD_NUMBER = 14;
    public static final int BUFR_NHR_FIELD_NUMBER = 15;
    public static final int BUFR_LATITUDE_FIELD_NUMBER = 16;
    public static final int BUFR_LONGITUDE_FIELD_NUMBER = 17;
    public static final int BUFR_RADIOSONDE_TYPE_FIELD_NUMBER = 18;
    public static final int BUFR_MAXL_FIELD_NUMBER = 19;
    public static final int BUFR_NDP_FIELD_NUMBER = 20;
    public static final int BUFR_NDT_FIELD_NUMBER = 21;
    public static final int NOTES = 22;

    private Long id;
    private Integer wmoid;
    private Integer nrep;
    private Integer nwt;
    private Integer nww;
    private Integer n99;
    private Float latitude;
    private Float longitude;
    private Integer radiosonde_type;
    private Integer maxl;
    private Integer ndp;
    private Integer ndt;
    private Integer bufr_nrep;
    private Integer bufr_nwt;
    private Integer bufr_nww;
    private Integer bufr_n99;
    private Integer bufr_nhr;
    private Float bufr_latitude;
    private Float bufr_longitude;
    private Integer bufr_radiosonde_type;
    private Integer bufr_maxl;
    private Integer bufr_ndp;
    private Integer bufr_ndt;
    private String notes;
    private Instant date;
    private Boolean verified;
    private Instant dateEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWmoid() {
        return wmoid;
    }

    public void setWmoid(Integer wmoid) {
        this.wmoid = wmoid;
    }

    public Integer getNrep() {
        return nrep;
    }

    public void setNrep(Integer nrep) {
        this.nrep = nrep;
    }

    public Integer getNwt() {
        return nwt;
    }

    public void setNwt(Integer nwt) {
        this.nwt = nwt;
    }

    public Integer getNww() {
        return nww;
    }

    public void setNww(Integer nww) {
        this.nww = nww;
    }

    public Integer getN99() {
        return n99;
    }

    public void setN99(Integer n99) {
        this.n99 = n99;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getRadiosonde_type() {
        return radiosonde_type;
    }

    public void setRadiosonde_type(Integer radiosonde_type) {
        this.radiosonde_type = radiosonde_type;
    }

    public Integer getMaxl() {
        return maxl;
    }

    public void setMaxl(Integer maxl) {
        this.maxl = maxl;
    }

    public Integer getNdp() {
        return ndp;
    }

    public void setNdp(Integer ndp) {
        this.ndp = ndp;
    }

    public Integer getNdt() {
        return ndt;
    }

    public void setNdt(Integer ndt) {
        this.ndt = ndt;
    }

    public Integer getBufr_nrep() {
        return bufr_nrep;
    }

    public void setBufr_nrep(Integer bufr_nrep) {
        this.bufr_nrep = bufr_nrep;
    }

    public Integer getBufr_nwt() {
        return bufr_nwt;
    }

    public void setBufr_nwt(Integer bufr_nwt) {
        this.bufr_nwt = bufr_nwt;
    }

    public Integer getBufr_nww() {
        return bufr_nww;
    }

    public void setBufr_nww(Integer bufr_nww) {
        this.bufr_nww = bufr_nww;
    }

    public Integer getBufr_n99() {
        return bufr_n99;
    }

    public void setBufr_n99(Integer bufr_n99) {
        this.bufr_n99 = bufr_n99;
    }

    public Integer getBufr_nhr() {
        return bufr_nhr;
    }

    public void setBufr_nhr(Integer bufr_nhr) {
        this.bufr_nhr = bufr_nhr;
    }

    public Float getBufr_latitude() {
        return bufr_latitude;
    }

    public void setBufr_latitude(Float bufr_latitude) {
        this.bufr_latitude = bufr_latitude;
    }

    public Float getBufr_longitude() {
        return bufr_longitude;
    }

    public void setBufr_longitude(Float bufr_longitude) {
        this.bufr_longitude = bufr_longitude;
    }

    public Integer getBufr_radiosonde_type() {
        return bufr_radiosonde_type;
    }

    public void setBufr_radiosonde_type(Integer bufr_radiosonde_type) {
        this.bufr_radiosonde_type = bufr_radiosonde_type;
    }

    public Integer getBufr_maxl() {
        return bufr_maxl;
    }

    public void setBufr_maxl(Integer bufr_maxl) {
        this.bufr_maxl = bufr_maxl;
    }

    public Integer getBufr_ndp() {
        return bufr_ndp;
    }

    public void setBufr_ndp(Integer bufr_ndp) {
        this.bufr_ndp = bufr_ndp;
    }

    public Integer getBufr_ndt() {
        return bufr_ndt;
    }

    public void setBufr_ndt(Integer bufr_ndt) {
        this.bufr_ndt = bufr_ndt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Instant getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public Object getIdPK() {
        return getId();
    }

    @Override
    public String getIdPKField() {
        return "id";
    }

    @Override
    public void setIdPK(Object idPK) {
        setId((Long) idPK);
    }
}
