package cnr.imaa.baron.model;

import java.time.Instant;

public class Product {
    private Header header;
    private Float time;
    private Float press;
    private Float geopot;
    private Float lvltyp1;
    private Float lvltyp2;
    private String pflag;
    private String zflag;
    private String tflag;
    private Float dpdp;
    private Float fp;
    private Float asc;
    private Float sza;
    private Float szaIdl;
    private Float deltaTvaisala;
    private Float deltaTgruan;
    private Integer radiosondeId;

    //temperature section
    private Float temp;
    private Float cor_temp;
    private Float u_cor_temp;
    private Float cor_temp_tl;
    private Float u_cor_temp_tl;
    private Float cor_intercomparison_temp;
    private Float u_cor_intercomparison_temp;

    //rh section
    private Float rh;
    private Float cor_rh;
    private Float u_cor_rh;
    private Float cor_rh_tl;
    private Float u_cor_rh_tl;
    private Float cor_intercomparison_rh;
    private Float u_cor_intercomparison_rh;

    //u section
    private Float u;
    private Float cor_u;
    private Float u_cor_u;
    private Float cor_u_rs92;
    private Float u_cor_u_rs92;
    private Float cor_u_notRs92;
    private Float u_cor_u_notRs92;

    //v section
    private Float v;
    private Float cor_v;
    private Float u_cor_v;
    private Float cor_v_rs92;
    private Float u_cor_v_rs92;
    private Float cor_v_notRs92;
    private Float u_cor_v_notRs92;

    private Float wdir;
    private Float wspeed;

    private Float wvmr;

    private Instant dateOfObservation;
    private String idStation;

    private Float cor_temp_calc;
    private Float f;
    private Double pSatNumeratore;
    private Double pSatDenominatore;
    private Float cor_rh_calc;

    private Boolean isCloudy;

    public Float getCor_temp_calc() {
        return cor_temp_calc;
    }

    public void setCor_temp_calc(Float cor_temp_calc) {
        this.cor_temp_calc = cor_temp_calc;
    }

    public Float getF() {
        return f;
    }

    public void setF(Float f) {
        this.f = f;
    }

    public Double getpSatNumeratore() {
        return pSatNumeratore;
    }

    public void setpSatNumeratore(Double pSatNumeratore) {
        this.pSatNumeratore = pSatNumeratore;
    }

    public Double getpSatDenominatore() {
        return pSatDenominatore;
    }

    public void setpSatDenominatore(Double pSatDenominatore) {
        this.pSatDenominatore = pSatDenominatore;
    }

    public Float getCor_rh_calc() {
        return cor_rh_calc;
    }

    public void setCor_rh_calc(Float cor_rh_calc) {
        this.cor_rh_calc = cor_rh_calc;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

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

    public Float getCor_temp() {
        return cor_temp;
    }

    public void setCor_temp(Float cor_temp) {
        this.cor_temp = cor_temp;
    }

    public Float getU_cor_temp() {
        return u_cor_temp;
    }

    public void setU_cor_temp(Float u_cor_temp) {
        this.u_cor_temp = u_cor_temp;
    }

    public Float getCor_rh() {
        return cor_rh;
    }

    public void setCor_rh(Float cor_rh) {
        this.cor_rh = cor_rh;
    }

    public Float getU_cor_rh() {
        return u_cor_rh;
    }

    public void setU_cor_rh(Float u_cor_rh) {
        this.u_cor_rh = u_cor_rh;
    }

    public Float getCor_temp_tl() {
        return cor_temp_tl;
    }

    public void setCor_temp_tl(Float cor_temp_tl) {
        this.cor_temp_tl = cor_temp_tl;
    }

    public Float getU_cor_temp_tl() {
        return u_cor_temp_tl;
    }

    public void setU_cor_temp_tl(Float u_cor_temp_tl) {
        this.u_cor_temp_tl = u_cor_temp_tl;
    }

    public Float getCor_rh_tl() {
        return cor_rh_tl;
    }

    public void setCor_rh_tl(Float cor_rh_tl) {
        this.cor_rh_tl = cor_rh_tl;
    }

    public Float getU_cor_rh_tl() {
        return u_cor_rh_tl;
    }

    public void setU_cor_rh_tl(Float u_cor_rh_tl) {
        this.u_cor_rh_tl = u_cor_rh_tl;
    }

    public Float getCor_intercomparison_temp() {
        return cor_intercomparison_temp;
    }

    public void setCor_intercomparison_temp(Float cor_intercomparison_temp) {
        this.cor_intercomparison_temp = cor_intercomparison_temp;
    }

    public Float getCor_intercomparison_rh() {
        return cor_intercomparison_rh;
    }

    public void setCor_intercomparison_rh(Float cor_intercomparison_rh) {
        this.cor_intercomparison_rh = cor_intercomparison_rh;
    }

    public Float getU_cor_intercomparison_temp() {
        return u_cor_intercomparison_temp;
    }

    public void setU_cor_intercomparison_temp(Float u_cor_intercomparison_temp) {
        this.u_cor_intercomparison_temp = u_cor_intercomparison_temp;
    }

    public Float getU_cor_intercomparison_rh() {
        return u_cor_intercomparison_rh;
    }

    public void setU_cor_intercomparison_rh(Float u_cor_intercomparison_rh) {
        this.u_cor_intercomparison_rh = u_cor_intercomparison_rh;
    }

    public Instant getDateOfObservation() {
        return dateOfObservation;
    }

    public void setDateOfObservation(Instant dateOfObservation) {
        this.dateOfObservation = dateOfObservation;
    }

    public Float getSza() {
        return sza;
    }

    public void setSza(Float sza) {
        this.sza = sza;
    }

    public Float getSzaIdl() {
        return szaIdl;
    }

    public void setSzaIdl(Float szaIdl) {
        this.szaIdl = szaIdl;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
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

    public Float getCor_u_rs92() {
        return cor_u_rs92;
    }

    public void setCor_u_rs92(Float cor_u_rs92) {
        this.cor_u_rs92 = cor_u_rs92;
    }

    public Float getU_cor_u_rs92() {
        return u_cor_u_rs92;
    }

    public void setU_cor_u_rs92(Float u_cor_u_rs92) {
        this.u_cor_u_rs92 = u_cor_u_rs92;
    }

    public Float getCor_v_rs92() {
        return cor_v_rs92;
    }

    public void setCor_v_rs92(Float cor_v_rs92) {
        this.cor_v_rs92 = cor_v_rs92;
    }

    public Float getU_cor_v_rs92() {
        return u_cor_v_rs92;
    }

    public void setU_cor_v_rs92(Float u_cor_v_rs92) {
        this.u_cor_v_rs92 = u_cor_v_rs92;
    }

    public Float getCor_u_notRs92() {
        return cor_u_notRs92;
    }

    public void setCor_u_notRs92(Float cor_u_notRs92) {
        this.cor_u_notRs92 = cor_u_notRs92;
    }

    public Float getU_cor_u_notRs92() {
        return u_cor_u_notRs92;
    }

    public void setU_cor_u_notRs92(Float u_cor_u_notRs92) {
        this.u_cor_u_notRs92 = u_cor_u_notRs92;
    }

    public Float getCor_v_notRs92() {
        return cor_v_notRs92;
    }

    public void setCor_v_notRs92(Float cor_v_notRs92) {
        this.cor_v_notRs92 = cor_v_notRs92;
    }

    public Float getU_cor_v_notRs92() {
        return u_cor_v_notRs92;
    }

    public void setU_cor_v_notRs92(Float u_cor_v_notRs92) {
        this.u_cor_v_notRs92 = u_cor_v_notRs92;
    }

    public Float getDeltaTvaisala() {
        return deltaTvaisala;
    }

    public void setDeltaTvaisala(Float deltaTvaisala) {
        this.deltaTvaisala = deltaTvaisala;
    }

    public Float getDeltaTgruan() {
        return deltaTgruan;
    }

    public void setDeltaTgruan(Float deltaTgruan) {
        this.deltaTgruan = deltaTgruan;
    }

    public Integer getRadiosondeId() {
        return radiosondeId;
    }

    public void setRadiosondeId(Integer radiosondeId) {
        this.radiosondeId = radiosondeId;
    }

    public Boolean getIsCloudy() {
        return isCloudy;
    }

    public void setIsCloudy(Boolean cloudy) {
        isCloudy = cloudy;
    }
}