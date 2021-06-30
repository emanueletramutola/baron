package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.UscrnConverterDaily;
import cnr.imaa.baron.model.UscrnDaily;
import cnr.imaa.baron.repository.BaseDataDAO;
import cnr.imaa.baron.repository.BaseHeaderDAO;
import cnr.imaa.baron.repository.BaseObjDAO;
import cnr.imaa.baron.repository.UscrnDailyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class UscrnRetrieverDaily extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(UscrnRetrieverDaily.class);

    private final UscrnConverterDaily uscrnConverterDaily = new UscrnConverterDaily();
    private final UscrnDailyDAO uscrnDailyDAO = new UscrnDailyDAO(this.getDataSource());

    @Override
    protected Object convertFileToBean(byte[] arr, String file) {
        String[] fileRows = UscrnRetriever.getFileRows(arr);

        List<UscrnDaily> uscrnDailyList = (List<UscrnDaily>) uscrnConverterDaily.convert(fileRows);

        final String sitename = UscrnRetriever.getSitename(file);
        final Instant lastMod = Instant.now();

        uscrnDailyList
                .parallelStream()
                .forEach(x -> {
                    x.setSitename(sitename);
                    x.setLastmod(lastMod);
                });

        return uscrnDailyList;
    }

    @Override
    protected void saveBean(Object obj) {
        uscrnDailyDAO.save(obj, BaseObjDAO.SQL_COMMAND.INSERT);
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
