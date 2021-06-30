package cnr.imaa.baron.model;

public abstract class BaseObjBaron extends BaseObj {
    private Station station;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
