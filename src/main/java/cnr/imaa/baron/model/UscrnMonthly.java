package cnr.imaa.baron.model;

import java.time.Instant;

public class UscrnMonthly extends UscrnObj {
    private Integer id;
    private String sitename;
    private Instant lastmod;
    private Integer wbanno;
    private String lst_yrmo;
    private String crx_vn_monthly;
    private Double precise_longitude;
    private Double precise_latitude;
    private Double t_monthly_max;
    private Double t_monthly_min;
    private Double t_monthly_mean;
    private Double t_monthly_avg;
    private Double p_monthly_calc;
    private Double solrad_monthly_avg;
    private String sur_temp_monthly_type;
    private Double sur_temp_monthly_max;
    private Double sur_temp_monthly_min;
    private Double sur_temp_monthly_avg;
    private Instant date_of_observation_lst;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public Instant getLastmod() {
        return lastmod;
    }

    public void setLastmod(Instant lastmod) {
        this.lastmod = lastmod;
    }

    public Integer getWbanno() {
        return wbanno;
    }

    public void setWbanno(Integer wbanno) {
        this.wbanno = wbanno;
    }

    public String getLst_yrmo() {
        return lst_yrmo;
    }

    public void setLst_yrmo(String lst_yrmo) {
        this.lst_yrmo = lst_yrmo;
    }

    public String getCrx_vn_monthly() {
        return crx_vn_monthly;
    }

    public void setCrx_vn_monthly(String crx_vn_monthly) {
        this.crx_vn_monthly = crx_vn_monthly;
    }

    public Double getPrecise_longitude() {
        return precise_longitude;
    }

    public void setPrecise_longitude(Double precise_longitude) {
        this.precise_longitude = precise_longitude;
    }

    public Double getPrecise_latitude() {
        return precise_latitude;
    }

    public void setPrecise_latitude(Double precise_latitude) {
        this.precise_latitude = precise_latitude;
    }

    public Double getT_monthly_max() {
        return t_monthly_max;
    }

    public void setT_monthly_max(Double t_monthly_max) {
        this.t_monthly_max = t_monthly_max;
    }

    public Double getT_monthly_min() {
        return t_monthly_min;
    }

    public void setT_monthly_min(Double t_monthly_min) {
        this.t_monthly_min = t_monthly_min;
    }

    public Double getT_monthly_mean() {
        return t_monthly_mean;
    }

    public void setT_monthly_mean(Double t_monthly_mean) {
        this.t_monthly_mean = t_monthly_mean;
    }

    public Double getT_monthly_avg() {
        return t_monthly_avg;
    }

    public void setT_monthly_avg(Double t_monthly_avg) {
        this.t_monthly_avg = t_monthly_avg;
    }

    public Double getP_monthly_calc() {
        return p_monthly_calc;
    }

    public void setP_monthly_calc(Double p_monthly_calc) {
        this.p_monthly_calc = p_monthly_calc;
    }

    public Double getSolrad_monthly_avg() {
        return solrad_monthly_avg;
    }

    public void setSolrad_monthly_avg(Double solrad_monthly_avg) {
        this.solrad_monthly_avg = solrad_monthly_avg;
    }

    public String getSur_temp_monthly_type() {
        return sur_temp_monthly_type;
    }

    public void setSur_temp_monthly_type(String sur_temp_monthly_type) {
        this.sur_temp_monthly_type = sur_temp_monthly_type;
    }

    public Double getSur_temp_monthly_max() {
        return sur_temp_monthly_max;
    }

    public void setSur_temp_monthly_max(Double sur_temp_monthly_max) {
        this.sur_temp_monthly_max = sur_temp_monthly_max;
    }

    public Double getSur_temp_monthly_min() {
        return sur_temp_monthly_min;
    }

    public void setSur_temp_monthly_min(Double sur_temp_monthly_min) {
        this.sur_temp_monthly_min = sur_temp_monthly_min;
    }

    public Double getSur_temp_monthly_avg() {
        return sur_temp_monthly_avg;
    }

    public void setSur_temp_monthly_avg(Double sur_temp_monthly_avg) {
        this.sur_temp_monthly_avg = sur_temp_monthly_avg;
    }

    public Instant getDate_of_observation_lst() {
        return date_of_observation_lst;
    }

    public void setDate_of_observation_lst(Instant date_of_observation_lst) {
        this.date_of_observation_lst = date_of_observation_lst;
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
