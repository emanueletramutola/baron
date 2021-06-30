package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.UscrnConverterMonthly;
import cnr.imaa.baron.model.UscrnMonthly;
import cnr.imaa.baron.repository.BaseDataDAO;
import cnr.imaa.baron.repository.BaseHeaderDAO;
import cnr.imaa.baron.repository.BaseObjDAO;
import cnr.imaa.baron.repository.UscrnMonthlyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class UscrnRetrieverMonthly extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(UscrnRetrieverMonthly.class);

    private final UscrnConverterMonthly uscrnConverterMonthly = new UscrnConverterMonthly();
    private final UscrnMonthlyDAO uscrnMonthlyDAO = new UscrnMonthlyDAO(this.getDataSource());

    @Override
    protected Object convertFileToBean(byte[] arr, String file) {
        String[] fileRows = UscrnRetriever.getFileRows(arr);

        List<UscrnMonthly> uscrnMonthlyList = (List<UscrnMonthly>) uscrnConverterMonthly.convert(fileRows);

        final String sitename = UscrnRetriever.getSitename(file);
        final Instant lastMod = Instant.now();

        uscrnMonthlyList
                .parallelStream()
                .forEach(x -> {
                    x.setSitename(sitename);
                    x.setLastmod(lastMod);
                });

        return uscrnMonthlyList;
    }

    @Override
    protected void saveBean(Object obj) {
        uscrnMonthlyDAO.save(obj, BaseObjDAO.SQL_COMMAND.INSERT);
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
