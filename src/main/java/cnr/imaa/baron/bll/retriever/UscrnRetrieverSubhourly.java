package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.UscrnConverterSubhourly;
import cnr.imaa.baron.model.UscrnSubHourly;
import cnr.imaa.baron.repository.BaseDataDAO;
import cnr.imaa.baron.repository.BaseHeaderDAO;
import cnr.imaa.baron.repository.BaseObjDAO;
import cnr.imaa.baron.repository.UscrnSubHourlyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class UscrnRetrieverSubhourly extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(UscrnRetrieverSubhourly.class);

    private final UscrnConverterSubhourly uscrnConverterSubhourly = new UscrnConverterSubhourly();
    private final UscrnSubHourlyDAO uscrnSubHourlyDAO = new UscrnSubHourlyDAO(this.getDataSource());

    @Override
    protected Object convertFileToBean(byte[] arr, String file) {
        String[] fileRows = UscrnRetriever.getFileRows(arr);

        List<UscrnSubHourly> uscrnSubHourlyList = (List<UscrnSubHourly>) uscrnConverterSubhourly.convert(fileRows);

        final String sitename = UscrnRetriever.getSitename(file);
        final Instant lastMod = Instant.now();

        uscrnSubHourlyList
                .parallelStream()
                .forEach(x -> {
                    x.setSitename(sitename);
                    x.setLastmod(lastMod);
                });

        return uscrnSubHourlyList;
    }

    @Override
    protected void saveBean(Object obj) {
        uscrnSubHourlyDAO.save(obj, BaseObjDAO.SQL_COMMAND.INSERT);
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
