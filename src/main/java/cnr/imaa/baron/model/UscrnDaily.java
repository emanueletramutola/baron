package cnr.imaa.baron.model;

import java.time.Instant;

public class UscrnDaily extends UscrnObj {
    private Integer id;
    private String sitename;
    private Instant lastmod;
    private Integer wbanno;
    private String lst_date;
    private String crx_vn;
    private Double longitude;
    private Double latitude;
    private Double t_daily_max;
    private Double t_daily_min;
    private Double t_daily_mean;
    private Double t_daily_avg;
    private Double p_daily_calc;
    private Double solarad_daily;
    private String sur_temp_daily_type;
    private Double sur_temp_daily_max;
    private Double sur_temp_daily_min;
    private Double sur_temp_daily_avg;
    private Double rh_daily_max;
    private Double rh_daily_min;
    private Double rh_daily_avg;
    private Double soil_moisture_5_daily;
    private Double soil_moisture_10_daily;
    private Double soil_moisture_20_daily;
    private Double soil_moisture_50_daily;
    private Double soil_moisture_100_daily;
    private Double soil_temp_5_daily;
    private Double soil_temp_10_daily;
    private Double soil_temp_20_daily;
    private Double soil_temp_50_daily;
    private Double soil_temp_100_daily;
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

    public String getLst_date() {
        return lst_date;
    }

    public void setLst_date(String lst_date) {
        this.lst_date = lst_date;
    }

    public String getCrx_vn() {
        return crx_vn;
    }

    public void setCrx_vn(String crx_vn) {
        this.crx_vn = crx_vn;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getT_daily_max() {
        return t_daily_max;
    }

    public void setT_daily_max(Double t_daily_max) {
        this.t_daily_max = t_daily_max;
    }

    public Double getT_daily_min() {
        return t_daily_min;
    }

    public void setT_daily_min(Double t_daily_min) {
        this.t_daily_min = t_daily_min;
    }

    public Double getT_daily_mean() {
        return t_daily_mean;
    }

    public void setT_daily_mean(Double t_daily_mean) {
        this.t_daily_mean = t_daily_mean;
    }

    public Double getT_daily_avg() {
        return t_daily_avg;
    }

    public void setT_daily_avg(Double t_daily_avg) {
        this.t_daily_avg = t_daily_avg;
    }

    public Double getP_daily_calc() {
        return p_daily_calc;
    }

    public void setP_daily_calc(Double p_daily_calc) {
        this.p_daily_calc = p_daily_calc;
    }

    public Double getSolarad_daily() {
        return solarad_daily;
    }

    public void setSolarad_daily(Double solarad_daily) {
        this.solarad_daily = solarad_daily;
    }

    public String getSur_temp_daily_type() {
        return sur_temp_daily_type;
    }

    public void setSur_temp_daily_type(String sur_temp_daily_type) {
        this.sur_temp_daily_type = sur_temp_daily_type;
    }

    public Double getSur_temp_daily_max() {
        return sur_temp_daily_max;
    }

    public void setSur_temp_daily_max(Double sur_temp_daily_max) {
        this.sur_temp_daily_max = sur_temp_daily_max;
    }

    public Double getSur_temp_daily_min() {
        return sur_temp_daily_min;
    }

    public void setSur_temp_daily_min(Double sur_temp_daily_min) {
        this.sur_temp_daily_min = sur_temp_daily_min;
    }

    public Double getSur_temp_daily_avg() {
        return sur_temp_daily_avg;
    }

    public void setSur_temp_daily_avg(Double sur_temp_daily_avg) {
        this.sur_temp_daily_avg = sur_temp_daily_avg;
    }

    public Double getRh_daily_max() {
        return rh_daily_max;
    }

    public void setRh_daily_max(Double rh_daily_max) {
        this.rh_daily_max = rh_daily_max;
    }

    public Double getRh_daily_min() {
        return rh_daily_min;
    }

    public void setRh_daily_min(Double rh_daily_min) {
        this.rh_daily_min = rh_daily_min;
    }

    public Double getRh_daily_avg() {
        return rh_daily_avg;
    }

    public void setRh_daily_avg(Double rh_daily_avg) {
        this.rh_daily_avg = rh_daily_avg;
    }

    public Double getSoil_moisture_5_daily() {
        return soil_moisture_5_daily;
    }

    public void setSoil_moisture_5_daily(Double soil_moisture_5_daily) {
        this.soil_moisture_5_daily = soil_moisture_5_daily;
    }

    public Double getSoil_moisture_10_daily() {
        return soil_moisture_10_daily;
    }

    public void setSoil_moisture_10_daily(Double soil_moisture_10_daily) {
        this.soil_moisture_10_daily = soil_moisture_10_daily;
    }

    public Double getSoil_moisture_20_daily() {
        return soil_moisture_20_daily;
    }

    public void setSoil_moisture_20_daily(Double soil_moisture_20_daily) {
        this.soil_moisture_20_daily = soil_moisture_20_daily;
    }

    public Double getSoil_moisture_50_daily() {
        return soil_moisture_50_daily;
    }

    public void setSoil_moisture_50_daily(Double soil_moisture_50_daily) {
        this.soil_moisture_50_daily = soil_moisture_50_daily;
    }

    public Double getSoil_moisture_100_daily() {
        return soil_moisture_100_daily;
    }

    public void setSoil_moisture_100_daily(Double soil_moisture_100_daily) {
        this.soil_moisture_100_daily = soil_moisture_100_daily;
    }

    public Double getSoil_temp_5_daily() {
        return soil_temp_5_daily;
    }

    public void setSoil_temp_5_daily(Double soil_temp_5_daily) {
        this.soil_temp_5_daily = soil_temp_5_daily;
    }

    public Double getSoil_temp_10_daily() {
        return soil_temp_10_daily;
    }

    public void setSoil_temp_10_daily(Double soil_temp_10_daily) {
        this.soil_temp_10_daily = soil_temp_10_daily;
    }

    public Double getSoil_temp_20_daily() {
        return soil_temp_20_daily;
    }

    public void setSoil_temp_20_daily(Double soil_temp_20_daily) {
        this.soil_temp_20_daily = soil_temp_20_daily;
    }

    public Double getSoil_temp_50_daily() {
        return soil_temp_50_daily;
    }

    public void setSoil_temp_50_daily(Double soil_temp_50_daily) {
        this.soil_temp_50_daily = soil_temp_50_daily;
    }

    public Double getSoil_temp_100_daily() {
        return soil_temp_100_daily;
    }

    public void setSoil_temp_100_daily(Double soil_temp_100_daily) {
        this.soil_temp_100_daily = soil_temp_100_daily;
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
