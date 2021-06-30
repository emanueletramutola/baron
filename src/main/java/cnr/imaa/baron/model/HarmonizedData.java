package cnr.imaa.baron.model;

import java.time.Instant;

public class HarmonizedData extends BaseData {
    public static final int TIME_CHECK_FIELD_IDX = 0;
    public static final int GEOPOT_CHECK_FIELD_IDX = 1;
    public static final int DPDP_CHECK_FIELD_IDX = 2;
    public static final int FP_CHECK_FIELD_IDX = 3;
    public static final int ASC_CHECK_FIELD_IDX = 4;
    public static final int SZA_CHECK_FIELD_IDX = 5;
    public static final int TEMP_CHECK_FIELD_IDX = 6;
    public static final int TEMP_PRODUCT_CHECK_FIELD_IDX = 7;
    public static final int TEMP_PRODUCT_COR_TEMP_CHECK_FIELD_IDX = 8;
    public static final int TEMP_PRODUCT_U_COR_TEMP_CHECK_FIELD_IDX = 9;
    public static final int TEMP_PRODUCT_COR_TEMP_TL_CHECK_FIELD_IDX = 10;
    public static final int TEMP_PRODUCT_U_COR_TEMP_TL_CHECK_FIELD_IDX = 11;
    public static final int TEMP_PRODUCT_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX = 12;
    public static final int TEMP_PRODUCT_U_COR_INTERCOMPARISON_TEMP_CHECK_FIELD_IDX = 13;
    public static final int TEMP_H_CHECK_FIELD_IDX = 14;
    public static final int ERR_TEMP_H_CHECK_FIELD_IDX = 15;
    public static final int RH_CHECK_FIELD_IDX = 16;
    public static final int RH_PRODUCT_CHECK_FIELD_IDX = 17;
    public static final int RH_PRODUCT_COR_RH_CHECK_FIELD_IDX = 18;
    public static final int RH_PRODUCT_U_COR_RH_CHECK_FIELD_IDX = 19;
    public static final int RH_PRODUCT_COR_RH_TL_CHECK_FIELD_IDX = 20;
    public static final int RH_PRODUCT_U_COR_RH_TL_CHECK_FIELD_IDX = 21;
    public static final int RH_PRODUCT_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX = 22;
    public static final int RH_PRODUCT_U_COR_INTERCOMPARISON_RH_CHECK_FIELD_IDX = 23;
    public static final int RH_H_CHECK_FIELD_IDX = 24;
    public static final int ERR_RH_H_CHECK_FIELD_IDX = 25;
    public static final int U_CHECK_FIELD_IDX = 26;
    public static final int U_PRODUCT_CHECK_FIELD_IDX = 27;
    public static final int U_PRODUCT_COR_U_CHECK_FIELD_IDX = 28;
    public static final int U_PRODUCT_U_COR_U_CHECK_FIELD_IDX = 29;
    public static final int U_PRODUCT_COR_U_RS92_CHECK_FIELD_IDX = 30;
    public static final int U_PRODUCT_U_COR_U_RS92_CHECK_FIELD_IDX = 31;
    public static final int U_PRODUCT_COR_U_NOT_RS92_CHECK_FIELD_IDX = 32;
    public static final int U_PRODUCT_U_COR_U_NOT_RS92_CHECK_FIELD_IDX = 33;
    public static final int U_H_CHECK_FIELD_IDX = 34;
    public static final int ERR_U_H_CHECK_FIELD_IDX = 35;
    public static final int V_CHECK_FIELD_IDX = 36;
    public static final int V_PRODUCT_CHECK_FIELD_IDX = 37;
    public static final int V_PRODUCT_COR_V_CHECK_FIELD_IDX = 38;
    public static final int V_PRODUCT_U_COR_V_CHECK_FIELD_IDX = 39;
    public static final int V_PRODUCT_COR_V_RS92_CHECK_FIELD_IDX = 40;
    public static final int V_PRODUCT_U_COR_V_RS92_CHECK_FIELD_IDX = 41;
    public static final int V_PRODUCT_COR_V_NOT_RS92_CHECK_FIELD_IDX = 42;
    public static final int V_PRODUCT_U_COR_V_NOT_RS92_CHECK_FIELD_IDX = 43;
    public static final int V_H_CHECK_FIELD_IDX = 44;
    public static final int ERR_V_H_CHECK_FIELD_IDX = 45;
    public static final int WVMR_CHECK_FIELD_IDX = 46;
    public static final int WVMR_PRODUCT_CHECK_FIELD_IDX = 47;
    public static final int WVMR_H_CHECK_FIELD_IDX = 48;
    public static final int WDIR_CHECK_FIELD_IDX = 49;
    public static final int WDIR_H_CHECK_FIELD_IDX = 50;
    public static final int ERR_WDIR_H_CHECK_FIELD_IDX = 51;
    public static final int WSPEED_CHECK_FIELD_IDX = 52;
    public static final int WSPEED_H_CHECK_FIELD_IDX = 53;
    public static final int ERR_WSPEED_H_CHECK_FIELD_IDX = 54;
    public static final int RELTIME_CHECK_FIELD_IDX = 55;
    public static final int NUM_CHECK_FIELDS = 56;

    private Integer id;
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

    //temperature section
    private Float temp;
    private Float temp_product;
    private Float temp_product_cor_temp;
    private Float temp_product_u_cor_temp;
    private Float temp_product_cor_temp_tl;
    private Float temp_product_u_cor_temp_tl;
    private Float temp_product_cor_intercomparison_temp;
    private Float temp_product_u_cor_intercomparison_temp;
    private Float temp_h;
    private Float err_temp_h;

    //rh section
    private Float rh;
    private Float rh_product;
    private Float rh_product_cor_rh;
    private Float rh_product_u_cor_rh;
    private Float rh_product_cor_rh_tl;
    private Float rh_product_u_cor_rh_tl;
    private Float rh_product_cor_intercomparison_rh;
    private Float rh_product_u_cor_intercomparison_rh;
    private Float rh_h;
    private Float err_rh_h;

    //u section
    private Float u;
    private Float u_product;
    private Float u_product_cor_u;
    private Float u_product_u_cor_u;
    private Float u_product_cor_u_rs92;
    private Float u_product_u_cor_u_rs92;
    private Float u_product_cor_u_notRs92;
    private Float u_product_u_cor_u_notRs92;
    private Float u_h;
    private Float err_u_h;

    //v section
    private Float v;
    private Float v_product;
    private Float v_product_cor_v;
    private Float v_product_u_cor_v;
    private Float v_product_cor_v_rs92;
    private Float v_product_u_cor_v_rs92;
    private Float v_product_cor_v_notRs92;
    private Float v_product_u_cor_v_notRs92;
    private Float v_h;
    private Float err_v_h;

    //wvmr section
    private Float wvmr;
    private Float wvmr_product;
    private Float wvmr_h;

    //wdir section
    private Float wdir;
    private Float wdir_h;
    private Float err_wdir_h;

    //wspeed section
    private Float wspeed;
    private Float wspeed_h;
    private Float err_wspeed_h;

    private Instant reltime;
    private Integer[] check;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Object getFieldUnique() {
        return this.getPress();
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

    public Float getFp() {
        return fp;
    }

    public void setFp(Float fp) {
        this.fp = fp;
    }

    public Float getAsc() {
        return asc;
    }

    public void setAsc(Float asc) {
        this.asc = asc;
    }

    public Float getSza() {
        return sza;
    }

    public void setSza(Float sza) {
        this.sza = sza;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getTemp_product() {
        return temp_product;
    }

    public void setTemp_product(Float temp_product) {
        this.temp_product = temp_product;
    }

    public Float getTemp_product_cor_temp() {
        return temp_product_cor_temp;
    }

    public void setTemp_product_cor_temp(Float temp_product_cor_temp) {
        this.temp_product_cor_temp = temp_product_cor_temp;
    }

    public Float getTemp_product_u_cor_temp() {
        return temp_product_u_cor_temp;
    }

    public void setTemp_product_u_cor_temp(Float temp_product_u_cor_temp) {
        this.temp_product_u_cor_temp = temp_product_u_cor_temp;
    }

    public Float getTemp_product_cor_temp_tl() {
        return temp_product_cor_temp_tl;
    }

    public void setTemp_product_cor_temp_tl(Float temp_product_cor_temp_tl) {
        this.temp_product_cor_temp_tl = temp_product_cor_temp_tl;
    }

    public Float getTemp_product_u_cor_temp_tl() {
        return temp_product_u_cor_temp_tl;
    }

    public void setTemp_product_u_cor_temp_tl(Float temp_product_u_cor_temp_tl) {
        this.temp_product_u_cor_temp_tl = temp_product_u_cor_temp_tl;
    }

    public Float getTemp_product_cor_intercomparison_temp() {
        return temp_product_cor_intercomparison_temp;
    }

    public void setTemp_product_cor_intercomparison_temp(Float temp_product_cor_intercomparison_temp) {
        this.temp_product_cor_intercomparison_temp = temp_product_cor_intercomparison_temp;
    }

    public Float getTemp_product_u_cor_intercomparison_temp() {
        return temp_product_u_cor_intercomparison_temp;
    }

    public void setTemp_product_u_cor_intercomparison_temp(Float temp_product_u_cor_intercomparison_temp) {
        this.temp_product_u_cor_intercomparison_temp = temp_product_u_cor_intercomparison_temp;
    }

    public Float getTemp_h() {
        return temp_h;
    }

    public void setTemp_h(Float temp_h) {
        this.temp_h = temp_h;
    }

    public Float getErr_temp_h() {
        return err_temp_h;
    }

    public void setErr_temp_h(Float err_temp_h) {
        this.err_temp_h = err_temp_h;
    }

    public Float getRh() {
        return rh;
    }

    public void setRh(Float rh) {
        this.rh = rh;
    }

    public Float getRh_product() {
        return rh_product;
    }

    public void setRh_product(Float rh_product) {
        this.rh_product = rh_product;
    }

    public Float getRh_product_cor_rh() {
        return rh_product_cor_rh;
    }

    public void setRh_product_cor_rh(Float rh_product_cor_rh) {
        this.rh_product_cor_rh = rh_product_cor_rh;
    }

    public Float getRh_product_u_cor_rh() {
        return rh_product_u_cor_rh;
    }

    public void setRh_product_u_cor_rh(Float rh_product_u_cor_rh) {
        this.rh_product_u_cor_rh = rh_product_u_cor_rh;
    }

    public Float getRh_product_cor_rh_tl() {
        return rh_product_cor_rh_tl;
    }

    public void setRh_product_cor_rh_tl(Float rh_product_cor_rh_tl) {
        this.rh_product_cor_rh_tl = rh_product_cor_rh_tl;
    }

    public Float getRh_product_u_cor_rh_tl() {
        return rh_product_u_cor_rh_tl;
    }

    public void setRh_product_u_cor_rh_tl(Float rh_product_u_cor_rh_tl) {
        this.rh_product_u_cor_rh_tl = rh_product_u_cor_rh_tl;
    }

    public Float getRh_product_cor_intercomparison_rh() {
        return rh_product_cor_intercomparison_rh;
    }

    public void setRh_product_cor_intercomparison_rh(Float rh_product_cor_intercomparison_rh) {
        this.rh_product_cor_intercomparison_rh = rh_product_cor_intercomparison_rh;
    }

    public Float getRh_product_u_cor_intercomparison_rh() {
        return rh_product_u_cor_intercomparison_rh;
    }

    public void setRh_product_u_cor_intercomparison_rh(Float rh_product_u_cor_intercomparison_rh) {
        this.rh_product_u_cor_intercomparison_rh = rh_product_u_cor_intercomparison_rh;
    }

    public Float getRh_h() {
        return rh_h;
    }

    public void setRh_h(Float rh_h) {
        this.rh_h = rh_h;
    }

    public Float getErr_rh_h() {
        return err_rh_h;
    }

    public void setErr_rh_h(Float err_rh_h) {
        this.err_rh_h = err_rh_h;
    }

    public Float getU() {
        return u;
    }

    public void setU(Float u) {
        this.u = u;
    }

    public Float getU_product() {
        return u_product;
    }

    public void setU_product(Float u_product) {
        this.u_product = u_product;
    }

    public Float getU_product_cor_u() {
        return u_product_cor_u;
    }

    public void setU_product_cor_u(Float u_product_cor_u) {
        this.u_product_cor_u = u_product_cor_u;
    }

    public Float getU_product_u_cor_u() {
        return u_product_u_cor_u;
    }

    public void setU_product_u_cor_u(Float u_product_u_cor_u) {
        this.u_product_u_cor_u = u_product_u_cor_u;
    }

    public Float getU_product_cor_u_rs92() {
        return u_product_cor_u_rs92;
    }

    public void setU_product_cor_u_rs92(Float u_product_cor_u_rs92) {
        this.u_product_cor_u_rs92 = u_product_cor_u_rs92;
    }

    public Float getU_product_u_cor_u_rs92() {
        return u_product_u_cor_u_rs92;
    }

    public void setU_product_u_cor_u_rs92(Float u_product_u_cor_u_rs92) {
        this.u_product_u_cor_u_rs92 = u_product_u_cor_u_rs92;
    }

    public Float getU_product_cor_u_notRs92() {
        return u_product_cor_u_notRs92;
    }

    public void setU_product_cor_u_notRs92(Float u_product_cor_u_notRs92) {
        this.u_product_cor_u_notRs92 = u_product_cor_u_notRs92;
    }

    public Float getU_product_u_cor_u_notRs92() {
        return u_product_u_cor_u_notRs92;
    }

    public void setU_product_u_cor_u_notRs92(Float u_product_u_cor_u_notRs92) {
        this.u_product_u_cor_u_notRs92 = u_product_u_cor_u_notRs92;
    }

    public Float getU_h() {
        return u_h;
    }

    public void setU_h(Float u_h) {
        this.u_h = u_h;
    }

    public Float getErr_u_h() {
        return err_u_h;
    }

    public void setErr_u_h(Float err_u_h) {
        this.err_u_h = err_u_h;
    }

    public Float getV() {
        return v;
    }

    public void setV(Float v) {
        this.v = v;
    }

    public Float getV_product() {
        return v_product;
    }

    public void setV_product(Float v_product) {
        this.v_product = v_product;
    }

    public Float getV_product_cor_v() {
        return v_product_cor_v;
    }

    public void setV_product_cor_v(Float v_product_cor_v) {
        this.v_product_cor_v = v_product_cor_v;
    }

    public Float getV_product_u_cor_v() {
        return v_product_u_cor_v;
    }

    public void setV_product_u_cor_v(Float v_product_u_cor_v) {
        this.v_product_u_cor_v = v_product_u_cor_v;
    }

    public Float getV_product_cor_v_rs92() {
        return v_product_cor_v_rs92;
    }

    public void setV_product_cor_v_rs92(Float v_product_cor_v_rs92) {
        this.v_product_cor_v_rs92 = v_product_cor_v_rs92;
    }

    public Float getV_product_u_cor_v_rs92() {
        return v_product_u_cor_v_rs92;
    }

    public void setV_product_u_cor_v_rs92(Float v_product_u_cor_v_rs92) {
        this.v_product_u_cor_v_rs92 = v_product_u_cor_v_rs92;
    }

    public Float getV_product_cor_v_notRs92() {
        return v_product_cor_v_notRs92;
    }

    public void setV_product_cor_v_notRs92(Float v_product_cor_v_notRs92) {
        this.v_product_cor_v_notRs92 = v_product_cor_v_notRs92;
    }

    public Float getV_product_u_cor_v_notRs92() {
        return v_product_u_cor_v_notRs92;
    }

    public void setV_product_u_cor_v_notRs92(Float v_product_u_cor_v_notRs92) {
        this.v_product_u_cor_v_notRs92 = v_product_u_cor_v_notRs92;
    }

    public Float getV_h() {
        return v_h;
    }

    public void setV_h(Float v_h) {
        this.v_h = v_h;
    }

    public Float getErr_v_h() {
        return err_v_h;
    }

    public void setErr_v_h(Float err_v_h) {
        this.err_v_h = err_v_h;
    }

    public Float getWvmr() {
        return wvmr;
    }

    public void setWvmr(Float wvmr) {
        this.wvmr = wvmr;
    }

    public Float getWvmr_product() {
        return wvmr_product;
    }

    public void setWvmr_product(Float wvmr_product) {
        this.wvmr_product = wvmr_product;
    }

    public Float getWvmr_h() {
        return wvmr_h;
    }

    public void setWvmr_h(Float wvmr_h) {
        this.wvmr_h = wvmr_h;
    }

    public Float getWdir() {
        return wdir;
    }

    public void setWdir(Float wdir) {
        this.wdir = wdir;
    }

    public Float getWdir_h() {
        return wdir_h;
    }

    public void setWdir_h(Float wdir_h) {
        this.wdir_h = wdir_h;
    }

    public Float getErr_wdir_h() {
        return err_wdir_h;
    }

    public void setErr_wdir_h(Float err_wdir_h) {
        this.err_wdir_h = err_wdir_h;
    }

    public Float getWspeed() {
        return wspeed;
    }

    public void setWspeed(Float wspeed) {
        this.wspeed = wspeed;
    }

    public Float getWspeed_h() {
        return wspeed_h;
    }

    public void setWspeed_h(Float wspeed_h) {
        this.wspeed_h = wspeed_h;
    }

    public Float getErr_wspeed_h() {
        return err_wspeed_h;
    }

    public void setErr_wspeed_h(Float err_wspeed_h) {
        this.err_wspeed_h = err_wspeed_h;
    }

    public Instant getReltime() {
        return reltime;
    }

    public void setReltime(Instant reltime) {
        this.reltime = reltime;
    }

    public Integer[] getCheck() {
        return check;
    }

    public void setCheck(Integer[] check) {
        this.check = check;
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