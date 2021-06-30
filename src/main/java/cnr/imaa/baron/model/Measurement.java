package cnr.imaa.baron.model;

public class Measurement extends BaseObjBaronPartitionTable {
    private Float time;
    private Float press;
    private Float temp;
    private Float rh;
    private Float wdir;
    private Float wspeed;
    private Float geopot;
    private Float lvltyp1;
    private Float lvltyp2;
    private String pflag;
    private String zflag;
    private String tflag;
    private Float dpdp;
    private Float u;
    private Float v;
    private Float fp;
    private Float wvmr;
    private Float asc;

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public Float getPress() {
        return press;
    }

    public void setPress(Float press) {
        this.press = press;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getRh() {
        return rh;
    }

    public void setRh(Float rh) {
        this.rh = rh;
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

    public Float getGeopot() {
        return geopot;
    }

    public void setGeopot(Float geopot) {
        this.geopot = geopot;
    }

    public Float getLvltyp1() {
        return lvltyp1;
    }

    public void setLvltyp1(Float lvltyp1) {
        this.lvltyp1 = lvltyp1;
    }

    public Float getLvltyp2() {
        return lvltyp2;
    }

    public void setLvltyp2(Float lvltyp2) {
        this.lvltyp2 = lvltyp2;
    }

    public String getPflag() {
        return pflag;
    }

    public void setPflag(String pflag) {
        this.pflag = pflag;
    }

    public String getZflag() {
        return zflag;
    }

    public void setZflag(String zflag) {
        this.zflag = zflag;
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public Float getDpdp() {
        return dpdp;
    }

    public void setDpdp(Float dpdp) {
        this.dpdp = dpdp;
    }

    public Float getU() {
        return u;
    }

    public void setU(Float u) {
        this.u = u;
    }

    public Float getV() {
        return v;
    }

    public void setV(Float v) {
        this.v = v;
    }

    public Float getFp() {
        return fp;
    }

    public void setFp(Float fp) {
        this.fp = fp;
    }

    public Float getWvmr() {
        return wvmr;
    }

    public void setWvmr(Float wvmr) {
        this.wvmr = wvmr;
    }

    public Float getAsc() {
        return asc;
    }

    public void setAsc(Float asc) {
        this.asc = asc;
    }

    @Override
    public Object getIdPK() {
        return null;
    }

    @Override
    public String getIdPKField() {
        return null;
    }

    @Override
    public void setIdPK(Object idPK) {

    }
}
