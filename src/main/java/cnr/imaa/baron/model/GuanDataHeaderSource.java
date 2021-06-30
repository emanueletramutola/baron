package cnr.imaa.baron.model;

public class GuanDataHeaderSource extends BaseObj {
    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object getIdPK() {
        return getCode();
    }

    @Override
    public String getIdPKField() {
        return "code";
    }

    @Override
    public void setIdPK(Object idPK) {
        setCode((String) idPK);
    }
}
