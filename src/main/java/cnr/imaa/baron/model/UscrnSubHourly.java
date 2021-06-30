package cnr.imaa.baron.model;

import java.time.Instant;

public class UscrnSubHourly extends UscrnObj {
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
    private Double air_temperature;
    private Double precipitation;
    private Double solar_radiation;
    private String sr_flag;
    private Double surface_temperature;
    private String st_type;
    private String st_flag;
    private Double relative_humidity;
    private String rh_flag;
    private Double soil_moisture_5;
    private Double soil_temperature_5;
    private Double wetness;
    private String wet_flag;
    private Double wind_1_5;
    private String wind_flag;
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

    public Double getAir_temperature() {
        return air_temperature;
    }

    public void setAir_temperature(Double air_temperature) {
        this.air_temperature = air_temperature;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getSolar_radiation() {
        return solar_radiation;
    }

    public void setSolar_radiation(Double solar_radiation) {
        this.solar_radiation = solar_radiation;
    }

    public String getSr_flag() {
        return sr_flag;
    }

    public void setSr_flag(String sr_flag) {
        this.sr_flag = sr_flag;
    }

    public Double getSurface_temperature() {
        return surface_temperature;
    }

    public void setSurface_temperature(Double surface_temperature) {
        this.surface_temperature = surface_temperature;
    }

    public String getSt_type() {
        return st_type;
    }

    public void setSt_type(String st_type) {
        this.st_type = st_type;
    }

    public String getSt_flag() {
        return st_flag;
    }

    public void setSt_flag(String st_flag) {
        this.st_flag = st_flag;
    }

    public Double getRelative_humidity() {
        return relative_humidity;
    }

    public void setRelative_humidity(Double relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public String getRh_flag() {
        return rh_flag;
    }

    public void setRh_flag(String rh_flag) {
        this.rh_flag = rh_flag;
    }

    public Double getSoil_moisture_5() {
        return soil_moisture_5;
    }

    public void setSoil_moisture_5(Double soil_moisture_5) {
        this.soil_moisture_5 = soil_moisture_5;
    }

    public Double getSoil_temperature_5() {
        return soil_temperature_5;
    }

    public void setSoil_temperature_5(Double soil_temperature_5) {
        this.soil_temperature_5 = soil_temperature_5;
    }

    public Double getWetness() {
        return wetness;
    }

    public void setWetness(Double wetness) {
        this.wetness = wetness;
    }

    public String getWet_flag() {
        return wet_flag;
    }

    public void setWet_flag(String wet_flag) {
        this.wet_flag = wet_flag;
    }

    public Double getWind_1_5() {
        return wind_1_5;
    }

    public void setWind_1_5(Double wind_1_5) {
        this.wind_1_5 = wind_1_5;
    }

    public String getWind_flag() {
        return wind_flag;
    }

    public void setWind_flag(String wind_flag) {
        this.wind_flag = wind_flag;
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
