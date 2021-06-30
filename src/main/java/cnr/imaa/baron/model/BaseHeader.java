package cnr.imaa.baron.model;

import java.util.Set;

public abstract class BaseHeader extends BaseObjBaronPartitionTable {
    private Set<BaseData> data;

    public Set<BaseData> getData() {
        return data;
    }

    public void setData(Set<BaseData> data) {
        this.data = data;
    }
}
