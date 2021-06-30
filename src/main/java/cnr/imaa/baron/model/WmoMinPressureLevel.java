package cnr.imaa.baron.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WmoMinPressureLevel extends BaseObj {
    private Integer id;
    private Float min_press;
    private String sondeId;
    private String sonde_code;
    private String sonde_name;
    private String ecv;
    private Boolean isDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMin_press() {
        return min_press;
    }

    public void setMin_press(Float min_press) {
        this.min_press = min_press;
    }

    public String getSondeId() {
        return sondeId;
    }

    public void setSondeId(String sondeId) {
        this.sondeId = sondeId;
    }

    public String getSonde_code() {
        return sonde_code;
    }

    public void setSonde_code(String sonde_code) {
        this.sonde_code = sonde_code;
    }

    public String getSonde_name() {
        return sonde_name;
    }

    public void setSonde_name(String sonde_name) {
        this.sonde_name = sonde_name;
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

    public List<Integer> getSondeIdList() {
        return Stream.of(this.sondeId.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
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
