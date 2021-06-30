package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.IgraConverter;
import cnr.imaa.baron.commons.ZipUtility;
import cnr.imaa.baron.model.BaseData;
import cnr.imaa.baron.model.BaseHeader;
import cnr.imaa.baron.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class IgraRetriever extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(IgraRetriever.class);

    private final IgraConverter igraConverter = new IgraConverter();
    private final GuanDataHeaderDAO guanDataHeaderDAO = new GuanDataHeaderDAO(this.getDataSource());

    @Override
    protected Object convertFileToBean(byte[] arr, String file) {
        OutputStream outputStream;
        String fileContent;

        try {
            outputStream = ZipUtility.unzipOnFly(arr);
            fileContent = outputStream.toString();
            outputStream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        List<String> fileRows = Arrays.asList(fileContent.split(System.lineSeparator()));

        List<BaseData> baseDataListToSave = getBaseHeadersToSave((List<BaseData>) igraConverter.convert(fileRows));

        Map<BaseHeader, List<BaseData>> baseDataListToSaveGroupedByDate = baseDataListToSave
                .parallelStream()
                .collect(groupingBy(BaseData::getHeader));

        baseDataListToSaveGroupedByDate.entrySet()
                .parallelStream()
                .forEach(h -> {
                    h.getKey().setData(new HashSet<>(h.getValue()));
                });

        return new ArrayList<>(baseDataListToSaveGroupedByDate.keySet());
    }

    @Override
    protected void saveBean(Object guanDataHeadersToSave) {
        guanDataHeaderDAO.save(guanDataHeadersToSave, BaseObjDAO.SQL_COMMAND.INSERT);
    }

    @Override
    protected BaseHeaderDAO getBaseHeaderDAO() {
        return new GuanDataHeaderDAO(this.getDataSource());
    }

    @Override
    protected BaseDataDAO getBaseDataDAO() {
        return new GuanDataValueDAO(this.getDataSource());
    }
}
