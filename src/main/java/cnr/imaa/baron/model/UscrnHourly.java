package cnr.imaa.baron.model;

import java.time.Instant;

public class UscrnHourly extends UscrnObj {
    private Integer id;
    private String sitename;
    private Instant lastmod;
    private Integer wbanno;
    private String utc_date;
    private String utc_time;
    private String lst_date;
    private String lst_time;
    private String crx_vn;
    private Double longitude;
    private Double latitude;
    private Double t_calc;
    private Double t_hr_avg;
    private Double t_max;
    private Double t_min;
    private Double p_calc;
    private Double solarad;
    private String solarad_flag;
    private Double solarad_max;
    private String solarad_max_flag;
    private Double solarad_min;
    private String solarad_min_flag;
    private String sur_temp_type;
    private Double sur_temp;
    private String sur_temp_flag;
    private Double sur_temp_max;
    private String sur_temp_max_flag;
    private Double sur_temp_min;
    private String sur_temp_min_flag;
    private Double rh_hr_avg;
    private String rh_hr_avg_flag;
    private Double soil_moisture_5;
    private Double soil_moisture_10;
    private Double soil_moisture_20;
    private Double soil_moisture_50;
    private Double soil_moisture_100;
    private Double soil_temp_5;
    private Double soil_temp_10;
    private Double soil_temp_20;
    private Double soil_temp_50;
    private Double soil_temp_100;
    private Instant date_of_observation_utc;
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

    public String getUtc_date() {
        return utc_date;
    }

    public void setUtc_date(String utc_date) {
        this.utc_date = utc_date;
    }

    public String getUtc_time() {
        return utc_time;
    }

    public void setUtc_time(String utc_time) {
        this.utc_time = utc_time;
    }

    public String getLst_date() {
        return lst_date;
    }

    public void setLst_date(String lst_date) {
        this.lst_date = lst_date;
    }

    public String getLst_time() {
        return lst_time;
    }

    public void setLst_time(String lst_time) {
        this.lst_time = lst_time;
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

    public Double getT_calc() {
        return t_calc;
    }

    public void setT_calc(Double t_calc) {
        this.t_calc = t_calc;
    }

    public Double getT_hr_avg() {
        return t_hr_avg;
    }

    public void setT_hr_avg(Double t_hr_avg) {
        this.t_hr_avg = t_hr_avg;
    }

    public Double getT_max() {
        return t_max;
    }

    public void setT_max(Double t_max) {
        this.t_max = t_max;
    }

    public Double getT_min() {
        return t_min;
    }

    public void setT_min(Double t_min) {
        this.t_min = t_min;
    }

    public Double getP_calc() {
        return p_calc;
    }

    public void setP_calc(Double p_calc) {
        this.p_calc = p_calc;
    }

    public Double getSolarad() {
        return solarad;
    }

    public void setSolarad(Double solarad) {
        this.solarad = solarad;
    }

    public String getSolarad_flag() {
        return solarad_flag;
    }

    public void setSolarad_flag(String solarad_flag) {
        this.solarad_flag = solarad_flag;
    }

    public Double getSolarad_max() {
        return solarad_max;
    }

    public void setSolarad_max(Double solarad_max) {
        this.solarad_max = solarad_max;
    }

    public String getSolarad_max_flag() {
        return solarad_max_flag;
    }

    public void setSolarad_max_flag(String solarad_max_flag) {
        this.solarad_max_flag = solarad_max_flag;
    }

    public Double getSolarad_min() {
        return solarad_min;
    }

    public void setSolarad_min(Double solarad_min) {
        this.solarad_min = solarad_min;
    }

    public String getSolarad_min_flag() {
        return solarad_min_flag;
    }

    public void setSolarad_min_flag(String solarad_min_flag) {
        this.solarad_min_flag = solarad_min_flag;
    }

    public String getSur_temp_type() {
        return sur_temp_type;
    }

    public void setSur_temp_type(String sur_temp_type) {
        this.sur_temp_type = sur_temp_type;
    }

    public Double getSur_temp() {
        return sur_temp;
    }

    public void setSur_temp(Double sur_temp) {
        this.sur_temp = sur_temp;
    }

    public String getSur_temp_flag() {
        return sur_temp_flag;
    }

    public void setSur_temp_flag(String sur_temp_flag) {
        this.sur_temp_flag = sur_temp_flag;
    }

    public Double getSur_temp_max() {
        return sur_temp_max;
    }

    public void setSur_temp_max(Double sur_temp_max) {
        this.sur_temp_max = sur_temp_max;
    }

    public String getSur_temp_max_flag() {
        return sur_temp_max_flag;
    }

    public void setSur_temp_max_flag(String sur_temp_max_flag) {
        this.sur_temp_max_flag = sur_temp_max_flag;
    }

    public Double getSur_temp_min() {
        return sur_temp_min;
    }

    public void setSur_temp_min(Double sur_temp_min) {
        this.sur_temp_min = sur_temp_min;
    }

    public String getSur_temp_min_flag() {
        return sur_temp_min_flag;
    }

    public void setSur_temp_min_flag(String sur_temp_min_flag) {
        this.sur_temp_min_flag = sur_temp_min_flag;
    }

    public Double getRh_hr_avg() {
        return rh_hr_avg;
    }

    public void setRh_hr_avg(Double rh_hr_avg) {
        this.rh_hr_avg = rh_hr_avg;
    }

    public String getRh_hr_avg_flag() {
        return rh_hr_avg_flag;
    }

    public void setRh_hr_avg_flag(String rh_hr_avg_flag) {
        this.rh_hr_avg_flag = rh_hr_avg_flag;
    }

    public Double getSoil_moisture_5() {
        return soil_moisture_5;
    }

    public void setSoil_moisture_5(Double soil_moisture_5) {
        this.soil_moisture_5 = soil_moisture_5;
    }

    public Double getSoil_moisture_10() {
        return soil_moisture_10;
    }

    public void setSoil_moisture_10(Double soil_moisture_10) {
        this.soil_moisture_10 = soil_moisture_10;
    }

    public Double getSoil_moisture_20() {
        return soil_moisture_20;
    }

    public void setSoil_moisture_20(Double soil_moisture_20) {
        this.soil_moisture_20 = soil_moisture_20;
    }

    public Double getSoil_moisture_50() {
        return soil_moisture_50;
    }

    public void setSoil_moisture_50(Double soil_moisture_50) {
        this.soil_moisture_50 = soil_moisture_50;
    }

    public Double getSoil_moisture_100() {
        return soil_moisture_100;
    }

    public void setSoil_moisture_100(Double soil_moisture_100) {
        this.soil_moisture_100 = soil_moisture_100;
    }

    public Double getSoil_temp_5() {
        return soil_temp_5;
    }

    public void setSoil_temp_5(Double soil_temp_5) {
        this.soil_temp_5 = soil_temp_5;
    }

    public Double getSoil_temp_10() {
        return soil_temp_10;
    }

    public void setSoil_temp_10(Double soil_temp_10) {
        this.soil_temp_10 = soil_temp_10;
    }

    public Double getSoil_temp_20() {
        return soil_temp_20;
    }

    public void setSoil_temp_20(Double soil_temp_20) {
        this.soil_temp_20 = soil_temp_20;
    }

    public Double getSoil_temp_50() {
        return soil_temp_50;
    }

    public void setSoil_temp_50(Double soil_temp_50) {
        this.soil_temp_50 = soil_temp_50;
    }

    public Double getSoil_temp_100() {
        return soil_temp_100;
    }

    public void setSoil_temp_100(Double soil_temp_100) {
        this.soil_temp_100 = soil_temp_100;
    }

    public Instant getDate_of_observation_utc() {
        return date_of_observation_utc;
    }

    public void setDate_of_observation_utc(Instant date_of_observation_utc) {
        this.date_of_observation_utc = date_of_observation_utc;
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
