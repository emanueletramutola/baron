package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.UscrnConverterHourly;
import cnr.imaa.baron.model.UscrnHourly;
import cnr.imaa.baron.repository.BaseDataDAO;
import cnr.imaa.baron.repository.BaseHeaderDAO;
import cnr.imaa.baron.repository.BaseObjDAO;
import cnr.imaa.baron.repository.UscrnHourlyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class UscrnRetrieverHourly extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(UscrnRetrieverHourly.class);

    private final UscrnConverterHourly uscrnConverterHourly = new UscrnConverterHourly();
    private final UscrnHourlyDAO uscrnHourlyDAO = new UscrnHourlyDAO(this.getDataSource());

    @Override
    protected Object convertFileToBean(byte[] arr, String file) {
        String[] fileRows = UscrnRetriever.getFileRows(arr);

        List<UscrnHourly> uscrnHourlyList = (List<UscrnHourly>) uscrnConverterHourly.convert(fileRows);

        final String sitename = UscrnRetriever.getSitename(file);
        final Instant lastMod = Instant.now();

        uscrnHourlyList
                .parallelStream()
                .forEach(x -> {
                    x.setSitename(sitename);
                    x.setLastmod(lastMod);
                });

        return uscrnHourlyList;
    }

    @Override
    protected void saveBean(Object obj) {
        uscrnHourlyDAO.save(obj, BaseObjDAO.SQL_COMMAND.INSERT);
    }

    @Override
    protected BaseHeaderDAO getBaseHeaderDAO() {
        return null;
    }

    @Override
    protected BaseDataDAO getBaseDataDAO() {
        return null;
    }
}
