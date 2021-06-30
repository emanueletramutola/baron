package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObjBaron;

public abstract class BaseObjBaronDAO extends BaseObjDAO {
    public BaseObjBaronDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Object getByIdStation(BaseObjBaron obj) {
        BaseObjBaron baseObjFilter = (BaseObjBaron) getBaseObjFilter();
        baseObjFilter.setStation(obj.getStation());

        return get(getParams(baseObjFilter, SQL_COMMAND.SELECT));
    }
}
