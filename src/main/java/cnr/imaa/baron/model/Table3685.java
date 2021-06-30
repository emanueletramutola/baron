package cnr.imaa.baron.model;

import java.time.Instant;

public class Table3685 extends BaseObj {
    private Integer id;
    private Integer tac_code;
    private Integer bufr_code;
    private String description;
    private Instant dateFrom;
    private Instant dateTo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTac_code() {
        return tac_code;
    }

    public void setTac_code(Integer tac_code) {
        this.tac_code = tac_code;
    }

    public Integer getBufr_code() {
        return bufr_code;
    }

    public void setBufr_code(Integer bufr_code) {
        this.bufr_code = bufr_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
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
        setId((Integer) idPK);
    }
}
