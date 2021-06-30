package cnr.imaa.baron.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Header extends BaseHeader {
    private Integer headerId;
    private String conventions;
    private String title;
    private String source;
    private String history;
    private String references;
    private String disclaimer;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Instant reltime;
    private Integer numlev;
    private Float lat;
    private Float lon;
    private String name;
    private String state;
    private Float elevation;
    private Integer wmo_index;
    private Integer radiosonde_id;
    private Integer radiosonde_code;
    private String radiosonde_name;
    private String radiosonde_code_source;
    private Integer equipment_code;
    private String equipment_code_source;

    private Set<Measurement> measurements;
    private Set<Product> products;
    private Float sza;

    public Integer getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Integer headerId) {
        this.headerId = headerId;
    }

    public String getConventions() {
        return conventions;
    }

    public void setConventions(String conventions) {
        this.conventions = conventions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Instant getReltime() {
        return reltime;
    }

    public void setReltime(Instant reltime) {
        this.reltime = reltime;
    }

    public Integer getNumlev() {
        return numlev;
    }

    public void setNumlev(Integer numlev) {
        this.numlev = numlev;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
        this.elevation = elevation;
    }

    public Integer getWmo_index() {
        return wmo_index;
    }

    public void setWmo_index(Integer wmo_index) {
        this.wmo_index = wmo_index;
    }

    public Integer getRadiosonde_code() {
        return radiosonde_code;
    }

    public void setRadiosonde_code(Integer radiosonde_code) {
        this.radiosonde_code = radiosonde_code;
    }

    public String getRadiosonde_code_source() {
        return radiosonde_code_source;
    }

    public void setRadiosonde_code_source(String radiosonde_code_source) {
        this.radiosonde_code_source = radiosonde_code_source;
    }

    public Integer getEquipment_code() {
        return equipment_code;
    }

    public void setEquipment_code(Integer equipment_code) {
        this.equipment_code = equipment_code;
    }

    public String getEquipment_code_source() {
        return equipment_code_source;
    }

    public void setEquipment_code_source(String equipment_code_source) {
        this.equipment_code_source = equipment_code_source;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public List<Measurement> getMeasurementsList() {
        return new ArrayList<>(measurements);
    }

    public void setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Float getSza() {
        return sza;
    }

    public void setSza(Float sza) {
        this.sza = sza;
    }

    public Integer getRadiosonde_id() {
        return radiosonde_id;
    }

    public void setRadiosonde_id(Integer radiosonde_id) {
        this.radiosonde_id = radiosonde_id;
    }

    public String getRadiosonde_name() {
        return radiosonde_name;
    }

    public void setRadiosonde_name(String radiosonde_name) {
        this.radiosonde_name = radiosonde_name;
    }

    @Override
    public Object getIdPK() {
        return getHeaderId();
    }

    @Override
    public String getIdPKField() {
        return "header_id";
    }

    @Override
    public void setIdPK(Object idPK) {
        setHeaderId((Integer) idPK);
    }
}