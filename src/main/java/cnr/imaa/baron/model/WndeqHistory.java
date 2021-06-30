package cnr.imaa.baron.model;

import java.time.Instant;

public class WndeqHistory extends BaseObjBaron {
    private Long id;
    private String fullrow;
    private Integer begyear;
    private Integer begmonth;
    private Integer begday;
    private Integer beghour;
    private Integer endyear;
    private Integer endmonth;
    private Integer endday;
    private Integer endhour;
    private Integer code;
    private Instant begin;
    private Instant enddate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullrow() {
        return fullrow;
    }

    public void setFullrow(String fullrow) {
        this.fullrow = fullrow;
    }

    public Integer getBegyear() {
        return begyear;
    }

    public void setBegyear(Integer begyear) {
        this.begyear = begyear;
    }

    public Integer getBegmonth() {
        return begmonth;
    }

    public void setBegmonth(Integer begmonth) {
        this.begmonth = begmonth;
    }

    public Integer getBegday() {
        return begday;
    }

    public void setBegday(Integer begday) {
        this.begday = begday;
    }

    public Integer getBeghour() {
        return beghour;
    }

    public void setBeghour(Integer beghour) {
        this.beghour = beghour;
    }

    public Integer getEndyear() {
        return endyear;
    }

    public void setEndyear(Integer endyear) {
        this.endyear = endyear;
    }

    public Integer getEndmonth() {
        return endmonth;
    }

    public void setEndmonth(Integer endmonth) {
        this.endmonth = endmonth;
    }

    public Integer getEndday() {
        return endday;
    }

    public void setEndday(Integer endday) {
        this.endday = endday;
    }

    public Integer getEndhour() {
        return endhour;
    }

    public void setEndhour(Integer endhour) {
        this.endhour = endhour;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Instant getBegin() {
        return begin;
    }

    public void setBegin(Instant begin) {
        this.begin = begin;
    }

    public Instant getEnddate() {
        return enddate;
    }

    public void setEnddate(Instant enddate) {
        this.enddate = enddate;
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
