package cnr.imaa.baron.model;

public class WindRs92Corr extends BaseObj {
    private Integer id;
    private Float pressure;
    private Float cor_u;
    private Float u_cor_u;
    private Float cor_v;
    private Float u_cor_v;
    private Boolean isDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getCor_u() {
        return cor_u;
    }

    public void setCor_u(Float cor_u) {
        this.cor_u = cor_u;
    }

    public Float getU_cor_u() {
        return u_cor_u;
    }

    public void setU_cor_u(Float u_cor_u) {
        this.u_cor_u = u_cor_u;
    }

    public Float getCor_v() {
        return cor_v;
    }

    public void setCor_v(Float cor_v) {
        this.cor_v = cor_v;
    }

    public Float getU_cor_v() {
        return u_cor_v;
    }

    public void setU_cor_v(Float u_cor_v) {
        this.u_cor_v = u_cor_v;
    }

    public Boolean getDay() {
        return isDay;
    }

    public void setDay(Boolean day) {
        isDay = day;
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
