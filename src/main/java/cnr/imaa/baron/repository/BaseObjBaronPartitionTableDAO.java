package cnr.imaa.baron.repository;

import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.model.BaseObjBaronPartitionTable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cnr.imaa.baron.commons.BaronCommons.partitionByCores;

public abstract class BaseObjBaronPartitionTableDAO extends BaseObjBaronDAO {
    private final Integer YEAR_START = 1978;

    private Integer year;

    public BaseObjBaronPartitionTableDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    protected String getSQLInsert() {
        String sqlInsert = super.getSQLInsert();

        if (this.year != null) {
            sqlInsert = sqlInsert.replace(getTableName(), getTableName() + "_" + year);
        }

        return sqlInsert;
    }

    @Override
    protected String getSQLSelect(String sqlFilter) {
        String sqlSelect = super.getSQLSelect(sqlFilter);

        if (this.year != null) {
            sqlSelect = sqlSelect.replace(getTableName(), getTableName() + "_" + this.year);
        }

        return sqlSelect;
    }

    @Override
    protected String getSQLUpdate(Object obj, String sqlFilter) {
        String sqlUpdate = super.getSQLUpdate(obj, sqlFilter);

        if (this.year != null) {
            sqlUpdate = sqlUpdate.replace(getTableName(), getTableName() + "_" + this.year);
        }

        return sqlUpdate;
    }

    protected Object get(BaseObjBaronPartitionTable objInput) {
        if (objInput.getYear() == null) {
            List<Integer> yearsToRead = IntStream.rangeClosed(YEAR_START, BaronCommons.getYear(Instant.now()))
                    .boxed()
                    .collect(Collectors.toList());

            Collection<List<Integer>> yearsToReadPartitioned = partitionByCores(yearsToRead);

            List<Object> objList = Collections.synchronizedList(new ArrayList<>());

            yearsToReadPartitioned
                    .stream()
                    .forEach(yTrP -> {
                        yTrP
                                .parallelStream()
                                .forEach(y -> {
                                    objList.addAll(
                                            (List) super.get(getParams(objInput, SQL_COMMAND.SELECT))
                                    );
                                });
                    });

            return objList;
        } else {
            return get(getParams(objInput, SQL_COMMAND.SELECT));
        }
    }

}
