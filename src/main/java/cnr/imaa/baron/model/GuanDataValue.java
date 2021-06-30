package cnr.imaa.baron.model;

import cnr.imaa.baron.commons.BaronCommons;
import org.decimal4j.util.DoubleRounder;

public class GuanDataValue extends BaseData implements Comparable<GuanDataValue> {
    private Long id;
    private Integer lvltyp1;
    private Integer lvltyp2;
    private Integer etime;
    private String pflag;
    private Integer gph;
    private String zflag;
    private Float temp;
    private String tflag;
    private Float rh;
    private Float dpdp;
    private Integer wdir;
    private Float wspd;
    private Float sza;
    private Integer press;
    private Integer version;

    public static final Integer LVLTYP1_BEGIN = 1;
    public static final Integer LVLTYP1_END = 1;
    public static final Integer LVLTYP2_BEGIN = 2;
    public static final Integer LVLTYP2_END = 2;
    public static final Integer ETIME_BEGIN = 4;
    public static final Integer ETIME_END = 8;
    public static final Integer PRESS_BEGIN = 10;
    public static final Integer PRESS_END = 15;
    public static final Integer PRESS_V1_BEGIN = 3;
    public static final Integer PRESS_V1_END = 8;
    public static final Integer PFLAG_BEGIN = 16;
    public static final Integer PFLAG_END = 16;
    public static final Integer PFLAG_V1_BEGIN = 9;
    public static final Integer PFLAG_V1_END = 9;
    public static final Integer GPH_BEGIN = 17;
    public static final Integer GPH_END = 21;
    public static final Integer GPH_V1_BEGIN = 10;
    public static final Integer GPH_V1_END = 14;
    public static final Integer ZFLAG_BEGIN = 22;
    public static final Integer ZFLAG_END = 22;
    public static final Integer ZFLAG_V1_BEGIN = 15;
    public static final Integer ZFLAG_V1_END = 15;
    public static final Integer TEMP_BEGIN = 23;
    public static final Integer TEMP_END = 27;
    public static final Integer TEMP_V1_BEGIN = 16;
    public static final Integer TEMP_V1_END = 20;
    public static final Integer TFLAG_BEGIN = 28;
    public static final Integer TFLAG_END = 28;
    public static final Integer TFLAG_V1_BEGIN = 21;
    public static final Integer TFLAG_V1_END = 21;
    public static final Integer RH_BEGIN = 29;
    public static final Integer RH_END = 33;
    public static final Integer DPDP_BEGIN = 35;
    public static final Integer DPDP_END = 39;
    public static final Integer DPDP_V1_BEGIN = 22;
    public static final Integer DPDP_V1_END = 26;
    public static final Integer WDIR_BEGIN = 41;
    public static final Integer WDIR_END = 45;
    public static final Integer WDIR_V1_BEGIN = 27;
    public static final Integer WDIR_V1_END = 31;
    public static final Integer WSPD_BEGIN = 47;
    public static final Integer WSPD_END = 51;
    public static final Integer WSPD_V1_BEGIN = 32;
    public static final Integer WSPD_V1_END = 36;

    public Long getId() {
        return id;
    }

    public Integer getPress() {
        return press;
    }

    public void setPress(Integer press) {
        this.press = press;
    }

    public Integer getLvltyp1() {
        return lvltyp1;
    }

    public void setLvltyp1(Integer lvltyp1) {
        this.lvltyp1 = lvltyp1;
    }

    public Integer getLvltyp2() {
        return lvltyp2;
    }

    public void setLvltyp2(Integer lvltyp2) {
        this.lvltyp2 = lvltyp2;
    }

    public Integer getEtime() {
        return etime;
    }

    public void setEtime(Integer etime) {
        this.etime = etime;
    }

    public String getPflag() {
        return pflag;
    }

    public void setPflag(String pflag) {
        this.pflag = pflag;
    }

    public Integer getGph() {
        return gph;
    }

    public void setGph(Integer gph) {
        this.gph = gph;
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

    public Integer getWdir() {
        return wdir;
    }

    public void setWdir(Integer wdir) {
        this.wdir = wdir;
    }

    public Float getSza() {
        return sza;
    }

    public void setSza(Float sza) {
        this.sza = sza;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Float getDpdp() {
        return dpdp;
    }

    public void setDpdp(Float dpdp) {
        this.dpdp = dpdp;
    }

    public Float getWspd() {
        return wspd;
    }

    public void setWspd(Float wspd) {
        this.wspd = wspd;
    }

    public Float getTime_seconds() {
        //MMMSS
        //-8888 = Value removed by IGRA quality assurance, but valid
        //data remain at the same level.
        //-9999 = Value missing prior to quality assurance.
        if (this.getEtime() != null && this.getEtime() != -8888 && this.getEtime() != -9999) {
            Integer minutes = Integer.parseInt(String.format("%05d", this.getEtime()).substring(0, 3));
            Integer seconds = Integer.parseInt(String.format("%05d", this.getEtime()).substring(3));

            return new Float(((minutes * 60) + seconds));
        } else {
            return null;
        }
    }

    public Float getPress_hPa() {
        //Pa or mb * 100
        //-9999 = missing

        if (this.getPress() != -9999) {
            //conversion to hPa
            return this.getPress() / 100.0f;
        } else {
            return this.getPress().floatValue();
        }
    }

    public Float getTemp_Kelvin() {
        //(degrees C to tenths, e.g., 11 = 1.1°C). The following special values are used:

        //-8888 = Value removed by IGRA quality assurance, but valid
        //        data remain at the same level.
        //-9999 = Value missing prior to quality assurance.

        return getTemp_Kelvin(this.getTemp());
    }

    public static Float getTemp_Kelvin(Float tempInput) {
        Float temp;

        if (tempInput >= -8888) {
            //convert to K
            temp = BaronCommons.celsiusToKelvin(tempInput / 10.0f);

            temp = new Float(DoubleRounder.round(temp, 2));
        } else {
            temp = tempInput.floatValue();
        }

        return temp;
    }

    public Float getTemp_Celsius() {
        //(degrees C to tenths, e.g., 11 = 1.1°C). The following special values are used:
        //
        //  -8888 = Value removed by IGRA quality assurance, but valid
        //          data remain at the same level.
        //  -9999 = Value missing prior to quality assurance.
        Float temp;

        if (this.getTemp() != -8888 && this.getTemp() != -9999) {
            temp = new Float((this.getTemp() / 10.0f));

            if (!temp.isNaN()) {
                temp = new Float(DoubleRounder.round(temp, 2));
            }

            return temp;
        } else {
            temp = new Float(this.getTemp());
        }

        return temp;
    }

    public Float getRh_calculated_orig() {
        //-8888 = Value removed by IGRA quality assurance, but valid
        //        data remain at the same level.
        //-9999 = Value missing prior to quality assurance.

        Float rh;

        if ((this.getTemp() == -8888 || this.getTemp() == -9999)
                || (this.getDpdp() == -8888 || this.getDpdp() == -9999)) {
            if (this.getRh() == null) {
                rh = -9999.0f;
            } else if (this.getRh() != -8888 && this.getRh() != -9999) {
                rh = this.getRh() / 10.0f;
            } else {
                rh = this.getRh().floatValue();
            }
        } else {
            Float temperature = this.getTemp_Kelvin();
            Float dewpoint = this.getDPDP_value();

            if (dewpoint >= 30.0f) {
                rh = 0.0f;
            } else {
                rh = calculateRh(temperature, dewpoint);

                if (rh.isNaN()) {
                    if (this.getRh() == null) {
                        rh = -9999.0f;
                    } else if (this.getRh() > -8888) {
                        rh = this.getRh() / 10.0f;
                    } else {
                        rh = this.getRh().floatValue();
                    }
                }
            }
        }

        if (rh > -8888.0f) {
            rh /= 100.0f;
        }

        return rh;
    }

    public Float getRh_calculated() {
        return getRh_calculated(this.getTemp(), this.getRh(), this.getDpdp());
    }

    public static Float getRh_calculated(Float tempInput, Float rhInput, Float dpdpInput) {
        //-8888 = Value removed by IGRA quality assurance, but valid
        //        data remain at the same level.
        //-9999 = Value missing prior to quality assurance.

        Float rh;

        if ((tempInput == -8888 || tempInput == -9999)
                || (dpdpInput == -8888 || dpdpInput == -9999)) {
            if (rhInput == null) {
                rh = -9999.0f;
            } else if (rhInput != -8888 && rhInput != -9999) {
                rh = rhInput / 10.0f;
            } else {
                rh = rhInput.floatValue();
            }
        } else {
            Float temperature = getTemp_Kelvin(tempInput);
            Float dewpoint = getDPDP_value(dpdpInput);

            if (dewpoint >= 30.0f) {
                rh = 0.0f;
            } else {
                rh = calculateRh(temperature, dewpoint);

                if (rh.isNaN()) {
                    if (rhInput == null) {
                        rh = -9999.0f;
                    } else if (rhInput > -8888) {
                        rh = rhInput / 10.0f;
                    } else {
                        rh = rhInput.floatValue();
                    }
                }
            }
        }

        if (rh > -8888.0f) {
            rh /= 100.0f;
        }

        return new Float(DoubleRounder.round(rh, 4));
    }

    public static Float getRh_calculated_CDMFormat(Float temperature, Float rhInput, Float dewpoint) {
        Float rh;

        if (temperature == null || dewpoint == null) {
            if (rhInput == null) {
                rh = null;
            } else {
                rh = rhInput;
            }
        } else {
            if (dewpoint >= 30.0f) {
                rh = 0.0f;
            } else {
                rh = calculateRh(temperature, dewpoint);

                if (rh.isNaN()) {
                    if (rhInput == null) {
                        rh = null;
                    } else {
                        rh = rhInput;
                    }
                }
            }
        }

        if (rh != null && rh > 0.0f) {
            rh /= 100.0f;
            return new Float(DoubleRounder.round(rh, 4));
        } else {
            return null;
        }
    }

    private static Float calculateRh(Float temperature, Float dewpoint) {
        /*
        From the user, an air temperature (T ) , a dewpoint temperature (T d) are given. To convert the saturated vapor pressure and/or the actual vapor pressure, the
        temperature values must be converted to degrees Celsius (°C) .
        Then, saturated vapor pressure (es) and the actual vapor pressure (e) can be calculated using the formula listed here:

        https://www.weather.gov/media/epz/wxcalc/vaporPressure.pdf
         */

        Float dewpoint_temperature = temperature - dewpoint;

        double e_actual = BaronCommons.calculatePsat(dewpoint_temperature.doubleValue());
        double e_saturation = BaronCommons.calculatePsat(temperature.doubleValue());

        Float rh = (float) (100 * e_actual / e_saturation);

        return rh;
    }

    public Float getWSPD_value() {
        //-8888 = Value removed by IGRA quality assurance, but valid
        //      data remain at the same level.
        //-9999 = Value missing prior to quality assurance.

        if (this.getWspd() > -8888) {
            return this.getWspd() / 10.0f;
        } else {
            return this.getWspd().floatValue();
        }
    }

    public String getPFLAG_value() {
        //blank = Not checked by any climatology checks. If data value
        //        not equal to -9999, it passed all other applicable
        //        checks.
        //A     = Value falls within "tier-1" climatological limits
        //        based on all days of the year and all times of day
        //        at the station, but not checked by
        //        "tier-2" climatology checks due to
        //        insufficient data.
        //B     = Value passes checks based on both the tier-1
        //        climatology and a "tier-2" climatology specific to
        //        the time of year and time of day of the data value.

        if (!this.getPflag().trim().equals("")) {
            return this.getPflag();
        } else {
            return null;
        }
    }

    public String getZFLAG_value() {
        //blank = Not checked by any climatology checks or flag not
        //        applicable. If data value not equal to -8888 or -9999,
        //        it passed all other applicable checks.
        //A     = Value falls within "tier-1" climatological limits
        //        based on all days of the year and all times of day
        //        at the station, but not checked by
        //        "tier-2" climatology checks due to insufficient data.
        //B     = Value passes checks based on both the tier-1
        //        climatology and a "tier-2" climatology specific to
        //        the time of year and time of day of the data value.

        if (!this.getZflag().trim().equals("")) {
            return this.getZflag();
        } else {
            return null;
        }
    }

    public String getTFLAG_value() {
        //blank = Not checked by any climatology checks or flag not
        //        applicable. If data value not equal to -8888 or -9999,
        //        it passed all other applicable checks.
        //A     = Value falls within "tier-1" climatological limits
        //        based on all days of the year and all times of day
        //        at the station, but not checked by "tier-2"
        //        climatology checks due to insufficient data.
        //B     = Value passes checks based on both the tier-1
        //        climatology and a "tier-2" climatology specific to
        //        the time of year and time of day of the data value.

        if (!this.getTflag().trim().equals("")) {
            return this.getTflag();
        } else {
            return null;
        }
    }

    public Float getDPDP_value() {
        //-8888 = Value removed by IGRA quality assurance, but valid
        //        data remain at the same level.
        //-9999 = Value missing prior to quality assurance.

        return getDPDP_value(this.getDpdp());
    }

    public static Float getDPDP_value(Float dpdpInput) {
        if (dpdpInput > -8888) {
            return dpdpInput / 10.0f;
        } else {
            return dpdpInput.floatValue();
        }
    }

    public Float getZonalWind() {
        Float wspd = this.getWSPD_value();
        Float wdir = this.getWdir().floatValue();

        if ((wspd == -8888.0f || wspd == -9999.0f)
                || (wdir == -8888.0f || wdir == -9999.0f)) {
            return -9999.0f;
        } else {
            Float u = BaronCommons.calculateU(wspd, wdir);

            return u;
        }
    }

    public Float getZonalWind_CDMFormat() {
        Float wspd = this.getWspd();
        Float wdir = this.getWdir() == null ? null : this.getWdir().floatValue();

        if (wspd == null || wdir == null) {
            return null;
        } else {
            return BaronCommons.calculateU(wspd, wdir);
        }
    }

    public Float getMeridionalWind() {
        Float wspd = this.getWSPD_value();
        Float wdir = this.getWdir().floatValue();

        if ((wspd == -8888.0f || wspd == -9999.0f)
                || (wdir == -8888.0f || wdir == -9999.0f)) {
            return -9999.0f;
        } else {
            Float v = BaronCommons.calculateV(wspd, wdir);

            return v;
        }
    }

    public Float getMeridionalWind_CDMFormat() {
        Float wspd = this.getWspd();
        Float wdir = this.getWdir() == null ? null : this.getWdir().floatValue();

        if (wspd == null || wdir == null) {
            return null;
        } else {
            return BaronCommons.calculateV(wspd, wdir);
        }
    }

    public Float getFrostpoint() {
        //https://weather.station.software/blog/what-are-dew-and-frost-points/
        Float frostpoint = null;

        Float dpdp = this.getDPDP_value();
        Float temp = this.getTemp_Kelvin();
        if (dpdp != null && temp != null && dpdp > -8888.0f && temp > -8888.0f) {
            Float dewpoint_temperature = temp - dpdp;

            frostpoint = dewpoint_temperature + (2671.02f / ((2954.61f / temp) + (2.193665f * new Float(Math.log(temp)).floatValue()) - 13.3448f)) - temp;
        }

        if (frostpoint != null) {
            frostpoint = new Float(DoubleRounder.round(frostpoint, 2));
        }

        return frostpoint;
    }

    public Float getFrostpoint_CDMFormat() {
        //https://weather.station.software/blog/what-are-dew-and-frost-points/
        Float frostpoint = null;

        Float dpdp = this.getDpdp() == null ? null : this.getDpdp() - 273.15f;
        Float temp = this.getTemp();
        if (dpdp != null && temp != null) {
            Float dewpoint_temperature = temp - dpdp;

            frostpoint = dewpoint_temperature + (2671.02f / ((2954.61f / temp) + (2.193665f * new Float(Math.log(temp)).floatValue()) - 13.3448f)) - temp;
        }

        if (frostpoint != null) {
            frostpoint = new Float(DoubleRounder.round(frostpoint, 2));
        }

        return frostpoint;
    }

    public Float getWvmr() {
        /*
        ; Source Hyland, R. W. and A. Wexler, Formulations for the Thermodynamic Properties of the saturated Phases of H2O from 173.15K to 473.15K, ASHRAE Trans, 89(2A), 500-519, 1983.
        Psat = EXP(  -0.58002206E4 / T $
                + 0.13914993E1 $
                - 0.48640239E-1 * T $
                + 0.41764768E-4 * T^2. $
                - 0.14452093E-7 * T^3. $
                + 0.65459673E1 * ALOG(T)) / 100.
        */

        Float temperature = this.getTemp_Kelvin();
        Float dewpoint = this.getDPDP_value();
        Float press = this.getPress_hPa();

        Float dewpoint_temperature = temperature - dewpoint;

        return BaronCommons.calculateWvmrFromDewpoint(dewpoint_temperature, press);
    }

    public Float getWvmr_CDMFormat() {
        /*
        ; Source Hyland, R. W. and A. Wexler, Formulations for the Thermodynamic Properties of the saturated Phases of H2O from 173.15K to 473.15K, ASHRAE Trans, 89(2A), 500-519, 1983.
        Psat = EXP(  -0.58002206E4 / T $
                + 0.13914993E1 $
                - 0.48640239E-1 * T $
                + 0.41764768E-4 * T^2. $
                - 0.14452093E-7 * T^3. $
                + 0.65459673E1 * ALOG(T)) / 100.
        */
        Float wvmr = null;

        Float dewpoint = this.getDpdp() == null ? null : this.getDpdp() - 273.15f;
        if (dewpoint != null) {
            Float temperature = this.getTemp();
            Float press = this.getPress_hPa();
            Float dewpoint_temperature = temperature - dewpoint;

            wvmr = BaronCommons.calculateWvmrFromDewpoint(dewpoint_temperature, press);
        }

        return wvmr;
    }

    @Override
    public int compareTo(GuanDataValue o) {
        return this.getDateOfObservation().compareTo(o.getDateOfObservation());
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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