package cnr.imaa.baron.model;

public class TlSmoothRs92 extends BaseObj {
    private Float pressure;
    private Float cor_temp_tl_bias;
    private Float u_cor_temp_tl;
    private Float cor_rh_tl_night;
    private Float cor_rh_tl_day;
    private Float u_cor_rh_tl_night;
    private Float u_cor_rh_tl_day;

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getCor_temp_tl_bias() {
        return cor_temp_tl_bias;
    }

    public void setCor_temp_tl_bias(Float cor_temp_tl_bias) {
        this.cor_temp_tl_bias = cor_temp_tl_bias;
    }

    public Float getU_cor_temp_tl() {
        return u_cor_temp_tl;
    }

    public void setU_cor_temp_tl(Float u_cor_temp_tl) {
        this.u_cor_temp_tl = u_cor_temp_tl;
    }

    public Float getCor_rh_tl_night() {
        return cor_rh_tl_night;
    }

    public void setCor_rh_tl_night(Float cor_rh_tl_night) {
        this.cor_rh_tl_night = cor_rh_tl_night;
    }

    public Float getCor_rh_tl_day() {
        return cor_rh_tl_day;
    }

    public void setCor_rh_tl_day(Float cor_rh_tl_day) {
        this.cor_rh_tl_day = cor_rh_tl_day;
    }

    public Float getU_cor_rh_tl_night() {
        return u_cor_rh_tl_night;
    }

    public void setU_cor_rh_tl_night(Float u_cor_rh_tl_night) {
        this.u_cor_rh_tl_night = u_cor_rh_tl_night;
    }

    public Float getU_cor_rh_tl_day() {
        return u_cor_rh_tl_day;
    }

    public void setU_cor_rh_tl_day(Float u_cor_rh_tl_day) {
        this.u_cor_rh_tl_day = u_cor_rh_tl_day;
    }

    @Override
    public Object getIdPK() {
        return getPressure();
    }

    @Override
    public String getIdPKField() {
        return "pressure";
    }

    @Override
    public void setIdPK(Object idPK) {
        setPressure((Float) idPK);
    }
}
