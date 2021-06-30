package cnr.imaa.baron.model;

public class GruanDataValue extends BaseData {
    private Long id;
    private Float alt;
    private Float asc_;
    private Float cor_rh;
    private Float cor_temp;
    private Float fp;
    private Float geopot;
    private Float lat;
    private Float lon;
    private Float press;
    private Float res_rh;
    private Float rh;
    private Float swrad;
    private Float temp;
    private Float time;
    private Float u;
    private Float u_alt;
    private Float u_cor_rh;
    private Float u_cor_temp;
    private Float u_press;
    private Float u_rh;
    private Float u_std_rh;
    private Float u_std_temp;
    private Float u_swrad;
    private Float u_temp;
    private Float u_wdir;
    private Float u_wspeed;
    private Float v;
    private Float wdir;
    private Float wspeed;
    private Float wvmr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public Float getAsc_() {
        return asc_;
    }

    public void setAsc_(Float asc_) {
        this.asc_ = asc_;
    }

    public Float getCor_rh() {
        return cor_rh;
    }

    public void setCor_rh(Float cor_rh) {
        this.cor_rh = cor_rh;
    }

    public Float getCor_temp() {
        return cor_temp;
    }

    public void setCor_temp(Float cor_temp) {
        this.cor_temp = cor_temp;
    }

    public Float getFp() {
        return fp;
    }

    public void setFp(Float fp) {
        this.fp = fp;
    }

    public Float getGeopot() {
        return geopot;
    }

    public void setGeopot(Float geopot) {
        this.geopot = geopot;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getPress() {
        return press;
    }

    public void setPress(Float press) {
        this.press = press;
    }

    public Float getRes_rh() {
        return res_rh;
    }

    public void setRes_rh(Float res_rh) {
        this.res_rh = res_rh;
    }

    public Float getRh() {
        return rh;
    }

    public void setRh(Float rh) {
        this.rh = rh;
    }

    public Float getSwrad() {
        return swrad;
    }

    public void setSwrad(Float swrad) {
        this.swrad = swrad;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public Float getU() {
        return u;
    }

    public void setU(Float u) {
        this.u = u;
    }

    public Float getU_alt() {
        return u_alt;
    }

    public void setU_alt(Float u_alt) {
        this.u_alt = u_alt;
    }

    public Float getU_cor_rh() {
        return u_cor_rh;
    }

    public void setU_cor_rh(Float u_cor_rh) {
        this.u_cor_rh = u_cor_rh;
    }

    public Float getU_cor_temp() {
        return u_cor_temp;
    }

    public void setU_cor_temp(Float u_cor_temp) {
        this.u_cor_temp = u_cor_temp;
    }

    public Float getU_press() {
        return u_press;
    }

    public void setU_press(Float u_press) {
        this.u_press = u_press;
    }

    public Float getU_rh() {
        return u_rh;
    }

    public void setU_rh(Float u_rh) {
        this.u_rh = u_rh;
    }

    public Float getU_std_rh() {
        return u_std_rh;
    }

    public void setU_std_rh(Float u_std_rh) {
        this.u_std_rh = u_std_rh;
    }

    public Float getU_std_temp() {
        return u_std_temp;
    }

    public void setU_std_temp(Float u_std_temp) {
        this.u_std_temp = u_std_temp;
    }

    public Float getU_swrad() {
        return u_swrad;
    }

    public void setU_swrad(Float u_swrad) {
        this.u_swrad = u_swrad;
    }

    public Float getU_temp() {
        return u_temp;
    }

    public void setU_temp(Float u_temp) {
        this.u_temp = u_temp;
    }

    public Float getU_wdir() {
        return u_wdir;
    }

    public void setU_wdir(Float u_wdir) {
        this.u_wdir = u_wdir;
    }

    public Float getU_wspeed() {
        return u_wspeed;
    }

    public void setU_wspeed(Float u_wspeed) {
        this.u_wspeed = u_wspeed;
    }

    public Float getV() {
        return v;
    }

    public void setV(Float v) {
        this.v = v;
    }

    public Float getWdir() {
        return wdir;
    }

    public void setWdir(Float wdir) {
        this.wdir = wdir;
    }

    public Float getWspeed() {
        return wspeed;
    }

    public void setWspeed(Float wspeed) {
        this.wspeed = wspeed;
    }

    public Float getWvmr() {
        return wvmr;
    }

    public void setWvmr(Float wvmr) {
        this.wvmr = wvmr;
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
        this.setId((Long) idPK);
    }

    @Override
    public Object getFieldUnique() {
        return this.getPress();
    }
}
