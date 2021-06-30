package cnr.imaa.baron.model;

public class HarmonizationBreak extends BaseObj {
    private Long id;
    private String idStation;
    private Double fabio;
    private Integer ecvref;
    private Integer zenref;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Double press;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public Double getFabio() {
        return fabio;
    }

    public void setFabio(Double fabio) {
        this.fabio = fabio;
    }

    public Integer getEcvref() {
        return ecvref;
    }

    public void setEcvref(Integer ecvref) {
        this.ecvref = ecvref;
    }

    public Integer getZenref() {
        return zenref;
    }

    public void setZenref(Integer zenref) {
        this.zenref = zenref;
    }

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

    public Double getPress() {
        return press;
    }

    public void setPress(Double press) {
        this.press = press;
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