package cnr.imaa.baron.model;

public class GruanRadiation extends BaseObj {
    private Long id;
    private Double sza;
    private Double clear;
    private Double cloudy;
    private Double quota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSza() {
        return sza;
    }

    public void setSza(Double sza) {
        this.sza = sza;
    }

    public Double getClear() {
        return clear;
    }

    public void setClear(Double clear) {
        this.clear = clear;
    }

    public Double getCloudy() {
        return cloudy;
    }

    public void setCloudy(Double cloudy) {
        this.cloudy = cloudy;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
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
