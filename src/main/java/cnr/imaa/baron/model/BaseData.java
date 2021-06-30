package cnr.imaa.baron.model;

public abstract class BaseData extends BaseObjBaronPartitionTable {
    private BaseHeader header;

    public BaseHeader getHeader() {
        return header;
    }

    public void setHeader(BaseHeader header) {
        this.header = header;
    }

    public void setIdFK(Object idFK){
        this.getHeader().setIdPK(idFK);
    }

    public abstract Object getFieldUnique();
}
