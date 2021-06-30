package cnr.imaa.baron.model;

import java.time.Instant;

public class StationToHarmonize extends BaseObjBaron {
    public enum STATUS {
        PROCESSING('P'),
        FINISHED('F'),
        PROCESSING_NETCDF('N'),
        EXPORTED('E');

        public final Character value;

        STATUS(Character value) {
            this.value = value;
        }
    }

    public enum PHASE {
        INITLIST,
        READIGRA,
        PRODUCT,
        CALCH,
        ENDSETH,
        INTERPFINAL,
        DB,
        TOTAL
    }

    private Character status;
    private Integer launches;
    private Integer initlist;
    private Integer readigra;
    private Integer product;
    private Integer calch;
    private Integer endseth;
    private Integer interpfinal;
    private Integer db;
    private Integer total;
    private Instant lastupdate;

    @Override
    public Object getIdPK() {
        return getStation().getIdStation();
    }

    @Override
    public String getIdPKField() {
        return "idstation";
    }

    @Override
    public void setIdPK(Object idPK) {
        Station station = new Station();
        station.setIdStation((String) idPK);
        setStation(station);
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Integer getLaunches() {
        return launches;
    }

    public void setLaunches(Integer launches) {
        this.launches = launches;
    }

    public Integer getInitlist() {
        return initlist;
    }

    public void setInitlist(Integer initlist) {
        this.initlist = initlist;
    }

    public Integer getReadigra() {
        return readigra;
    }

    public void setReadigra(Integer readigra) {
        this.readigra = readigra;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getCalch() {
        return calch;
    }

    public void setCalch(Integer calch) {
        this.calch = calch;
    }

    public Integer getEndseth() {
        return endseth;
    }

    public void setEndseth(Integer endseth) {
        this.endseth = endseth;
    }

    public Integer getInterpfinal() {
        return interpfinal;
    }

    public void setInterpfinal(Integer interpfinal) {
        this.interpfinal = interpfinal;
    }

    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Instant getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Instant lastupdate) {
        this.lastupdate = lastupdate;
    }
}
