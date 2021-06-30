package cnr.imaa.baron.bll.harmonizer;

import cnr.imaa.baron.model.TlSmoothRs92;
import cnr.imaa.baron.model.WindRs92Corr;
import cnr.imaa.baron.model.WmoIntercomparison;

import java.time.Instant;
import java.util.Optional;

public class ProductVariables {
    private Instant dateOfObservation;
    private Float tempRaw;
    private Float tempCorrected;
    private Float cor_temp;
    private Float cor_temp_toPrint;
    private Float u_cor_temp;
    private Float press;
    private Float geopot;
    private Float quota;
    private Float sza;
    private Float deltaTvaisala;
    private Float ia;
    private Float deltaTgruan;
    private Float wspeed;
    private Float rhRaw;
    private Float rhCorrected;
    private Float cor_rh;
    private Float cor_rh_toPrint;
    private Float u_cor_rh;
    private Float f;
    private Double pSatNumeratore;
    private Double pSatDenominatore;
    private Float wvmrRaw;
    private Float wvmrCorrected;
    private Float cor_temp_tl;
    private Float u_cor_temp_tl;
    private Float cor_rh_tl;
    private Float u_cor_rh_tl;
    private Float iaClear;
    private Float iaCloudy;
    private Optional<WindRs92Corr> windRs92CorrOptional;
    private Optional<TlSmoothRs92> tlSmoothRs92Optional;
    private WmoIntercomparison wmoIntercomparison_TEMP;
    private WmoIntercomparison wmoIntercomparison_RH;
    private WmoIntercomparison wmoIntercomparison_U;
    private WmoIntercomparison wmoIntercomparison_V;
    private Boolean isRS92;
    private Float cor_intercomparison_temp;
    private Float cor_intercomparison_rh;
    private Float u_cor_intercomparison_temp;
    private Float u_cor_intercomparison_rh;
    private Float cor_u;
    private Float u_corrected;
    private Float cor_u_toPrint;
    private Float cor_v;
    private Float v_corrected;
    private Float cor_v_toPrint;
    private Float u_cor_u;
    private Float u_cor_v;
    private Boolean isCloudy;

    public Instant getDateOfObservation() {
        return dateOfObservation;
    }

    public void setDateOfObservation(Instant dateOfObservation) {
        this.dateOfObservation = dateOfObservation;
    }

    public Float getTempRaw() {
        return tempRaw;
    }

    public void setTempRaw(Float tempRaw) {
        this.tempRaw = tempRaw;
    }

    public Float getTempCorrected() {
        return tempCorrected;
    }

    public void setTempCorrected(Float tempCorrected) {
        this.tempCorrected = tempCorrected;
    }

    public Float getCor_temp() {
        return cor_temp;
    }

    public void setCor_temp(Float cor_temp) {
        this.cor_temp = cor_temp;
    }

    public Float getCor_temp_toPrint() {
        return cor_temp_toPrint;
    }

    public void setCor_temp_toPrint(Float cor_temp_toPrint) {
        this.cor_temp_toPrint = cor_temp_toPrint;
    }

    public Float getU_cor_temp() {
        return u_cor_temp;
    }

    public void setU_cor_temp(Float u_cor_temp) {
        this.u_cor_temp = u_cor_temp;
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

    public Float getQuota() {
        return quota;
    }

    public void setQuota(Float quota) {
        this.quota = quota;
    }

    public Float getSza() {
        return sza;
    }

    public void setSza(Float sza) {
        this.sza = sza;
    }

    public Float getDeltaTvaisala() {
        return deltaTvaisala;
    }

    public void setDeltaTvaisala(Float deltaTvaisala) {
        this.deltaTvaisala = deltaTvaisala;
    }

    public Float getIa() {
        return ia;
    }

    public void setIa(Float ia) {
        this.ia = ia;
    }

    public Float getDeltaTgruan() {
        return deltaTgruan;
    }

    public void setDeltaTgruan(Float deltaTgruan) {
        this.deltaTgruan = deltaTgruan;
    }

    public Float getWspeed() {
        return wspeed;
    }

    public void setWspeed(Float wspeed) {
        this.wspeed = wspeed;
    }

    public Float getRhRaw() {
        return rhRaw;
    }

    public void setRhRaw(Float rhRaw) {
        this.rhRaw = rhRaw;
    }

    public Float getRhCorrected() {
        return rhCorrected;
    }

    public void setRhCorrected(Float rhCorrected) {
        this.rhCorrected = rhCorrected;
    }

    public Float getCor_rh() {
        return cor_rh;
    }

    public void setCor_rh(Float cor_rh) {
        this.cor_rh = cor_rh;
    }

    public Float getCor_rh_toPrint() {
        return cor_rh_toPrint;
    }

    public void setCor_rh_toPrint(Float cor_rh_toPrint) {
        this.cor_rh_toPrint = cor_rh_toPrint;
    }

    public Float getU_cor_rh() {
        return u_cor_rh;
    }

    public void setU_cor_rh(Float u_cor_rh) {
        this.u_cor_rh = u_cor_rh;
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

    public Float getWvmrRaw() {
        return wvmrRaw;
    }

    public void setWvmrRaw(Float wvmrRaw) {
        this.wvmrRaw = wvmrRaw;
    }

    public Float getWvmrCorrected() {
        return wvmrCorrected;
    }

    public void setWvmrCorrected(Float wvmrCorrected) {
        this.wvmrCorrected = wvmrCorrected;
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

    public Float getIaClear() {
        return iaClear;
    }

    public void setIaClear(Float iaClear) {
        this.iaClear = iaClear;
    }

    public Float getIaCloudy() {
        return iaCloudy;
    }

    public void setIaCloudy(Float iaCloudy) {
        this.iaCloudy = iaCloudy;
    }

    public Optional<WindRs92Corr> getWindRs92CorrOptional() {
        return windRs92CorrOptional;
    }

    public void setWindRs92CorrOptional(Optional<WindRs92Corr> windRs92CorrOptional) {
        this.windRs92CorrOptional = windRs92CorrOptional;
    }

    public Optional<TlSmoothRs92> getTlSmoothRs92Optional() {
        return tlSmoothRs92Optional;
    }

    public void setTlSmoothRs92Optional(Optional<TlSmoothRs92> tlSmoothRs92Optional) {
        this.tlSmoothRs92Optional = tlSmoothRs92Optional;
    }

    public WmoIntercomparison getWmoIntercomparison_TEMP() {
        return wmoIntercomparison_TEMP;
    }

    public void setWmoIntercomparison_TEMP(WmoIntercomparison wmoIntercomparison_TEMP) {
        this.wmoIntercomparison_TEMP = wmoIntercomparison_TEMP;
    }

    public WmoIntercomparison getWmoIntercomparison_RH() {
        return wmoIntercomparison_RH;
    }

    public void setWmoIntercomparison_RH(WmoIntercomparison wmoIntercomparison_RH) {
        this.wmoIntercomparison_RH = wmoIntercomparison_RH;
    }

    public WmoIntercomparison getWmoIntercomparison_U() {
        return wmoIntercomparison_U;
    }

    public void setWmoIntercomparison_U(WmoIntercomparison wmoIntercomparison_U) {
        this.wmoIntercomparison_U = wmoIntercomparison_U;
    }

    public WmoIntercomparison getWmoIntercomparison_V() {
        return wmoIntercomparison_V;
    }

    public void setWmoIntercomparison_V(WmoIntercomparison wmoIntercomparison_V) {
        this.wmoIntercomparison_V = wmoIntercomparison_V;
    }

    public Boolean getRS92() {
        return isRS92;
    }

    public void setRS92(Boolean RS92) {
        isRS92 = RS92;
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

    public Float getCor_u() {
        return cor_u;
    }

    public void setCor_u(Float cor_u) {
        this.cor_u = cor_u;
    }

    public Float getU_corrected() {
        return u_corrected;
    }

    public void setU_corrected(Float u_corrected) {
        this.u_corrected = u_corrected;
    }

    public Float getCor_u_toPrint() {
        return cor_u_toPrint;
    }

    public void setCor_u_toPrint(Float cor_u_toPrint) {
        this.cor_u_toPrint = cor_u_toPrint;
    }

    public Float getCor_v() {
        return cor_v;
    }

    public void setCor_v(Float cor_v) {
        this.cor_v = cor_v;
    }

    public Float getV_corrected() {
        return v_corrected;
    }

    public void setV_corrected(Float v_corrected) {
        this.v_corrected = v_corrected;
    }

    public Float getCor_v_toPrint() {
        return cor_v_toPrint;
    }

    public void setCor_v_toPrint(Float cor_v_toPrint) {
        this.cor_v_toPrint = cor_v_toPrint;
    }

    public Float getU_cor_u() {
        return u_cor_u;
    }

    public void setU_cor_u(Float u_cor_u) {
        this.u_cor_u = u_cor_u;
    }

    public Float getU_cor_v() {
        return u_cor_v;
    }

    public void setU_cor_v(Float u_cor_v) {
        this.u_cor_v = u_cor_v;
    }

    public Boolean getCloudy() {
        return isCloudy;
    }

    public void setCloudy(Boolean cloudy) {
        isCloudy = cloudy;
    }
}
