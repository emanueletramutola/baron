package cnr.imaa.baron.bll.harmonizer;

import cnr.imaa.baron.commons.BaronCommons;

public class HarmonizationSigmaU_V {
    private Integer year;
    private Integer month;
    private Double press;
    private Double[] data_rms_scalar_1;
    private BaronCommons.ECV ecv;
    private BaronCommons.ZEN zen;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getPress() {
        return press;
    }

    public void setPress(Double press) {
        this.press = press;
    }

    public Double[] getData_rms_scalar_1() {
        return data_rms_scalar_1;
    }

    public void setData_rms_scalar_1(Double[] data_rms_scalar_1) {
        this.data_rms_scalar_1 = data_rms_scalar_1;
    }

    public BaronCommons.ECV getEcv() {
        return ecv;
    }

    public void setEcv(BaronCommons.ECV ecv) {
        this.ecv = ecv;
    }

    public BaronCommons.ZEN getZen() {
        return zen;
    }

    public void setZen(BaronCommons.ZEN zen) {
        this.zen = zen;
    }
}
