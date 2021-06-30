package cnr.imaa.baron.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WmoIntercomparison extends BaseObj {
    private Long id;
    private Float press;
    private String sondeId;
    private Integer sonde_code;
    private String sonde_name;
    private Float mean;
    private Float std_dev;
    private String ecv;
    private Boolean isDay;

    public WmoIntercomparison() {
    }

    public WmoIntercomparison(Long id, Float press, String sondeId, Integer sonde_code, String sonde_name, Float mean, Float std_dev, String ecv, Boolean isDay) {
        this.id = id;
        this.press = press;
        this.sondeId = sondeId;
        this.sonde_code = sonde_code;
        this.sonde_name = sonde_name;
        this.mean = mean;
        this.std_dev = std_dev;
        this.ecv = ecv;
        this.isDay = isDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPress() {
        return press;
    }

    public void setPress(Float press) {
        this.press = press;
    }

    public String getSondeId() {
        return sondeId;
    }

    public void setSondeId(String sondeId) {
        this.sondeId = sondeId;
    }

    public Integer getSonde_code() {
        return sonde_code;
    }

    public void setSonde_code(Integer sonde_code) {
        this.sonde_code = sonde_code;
    }

    public String getSonde_name() {
        return sonde_name;
    }

    public void setSonde_name(String sonde_name) {
        this.sonde_name = sonde_name;
    }

    public Float getMean() {
        return mean;
    }

    public void setMean(Float mean) {
        this.mean = mean;
    }

    public Float getStd_dev() {
        return std_dev;
    }

    public void setStd_dev(Float std_dev) {
        this.std_dev = std_dev;
    }

    public String getEcv() {
        return ecv;
    }

    public void setEcv(String ecv) {
        this.ecv = ecv;
    }

    public Boolean getDay() {
        return isDay;
    }

    public void setDay(Boolean day) {
        isDay = day;
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
        setId((Long) idPK);
    }

    public List<Integer> getSondeIdList() {
        return Stream.of(this.sondeId.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
