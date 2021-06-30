package cnr.imaa.baron.model;

public class Station extends BaseObj implements Comparable<Station> {
    private Long id;
    private String continent;
    private String countryCode;
    private Float elevation;
    private String idStation;
    private Float latitude;
    private Float longitude;
    private String name;
    private String network;
    private Integer wmoid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
        this.elevation = elevation;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public Integer getWmoid() {
        return wmoid;
    }

    public void setWmoid(Integer wmoid) {
        this.wmoid = wmoid;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public int compareTo(Station o) {
        return idStation.compareToIgnoreCase(o.getIdStation());
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
}