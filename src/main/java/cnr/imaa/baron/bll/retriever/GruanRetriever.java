package cnr.imaa.baron.bll.retriever;

import cnr.imaa.baron.bll.converter.GruanConverter;
import cnr.imaa.baron.model.BaseData;
import cnr.imaa.baron.model.GruanDataHeader;
import cnr.imaa.baron.model.GruanDataValue;
import cnr.imaa.baron.repository.*;
import cnr.imaa.baron.utils.FileResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.nc2.NetcdfFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GruanRetriever extends RetrieverFTP {
    private static final Logger log = LoggerFactory.getLogger(GruanRetriever.class);

    private Boolean importFromBulk = Boolean.parseBoolean(this.getFileResourceUtils().getConfig().get("importFromBulk"));

    @Override
    protected Object convertFileToBean(byte[] arr,String file) {
        GruanDataHeader gruanDataHeader;
        try {
            NetcdfFile ncfile = NetcdfFile.openInMemory("", arr);

            gruanDataHeader = getGruanDataHeaderToSave((GruanDataHeader) new GruanConverter().convert(ncfile));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return gruanDataHeader;
    }

    @Override
    protected void saveBean(Object gruanDataHeader) {
        new GruanDataHeaderDAO(this.getDataSource()).save(gruanDataHeader, BaseObjDAO.SQL_COMMAND.INSERT);
    }

    @Override
    protected BaseHeaderDAO getBaseHeaderDAO() {
        return new GruanDataHeaderDAO(this.getDataSource());
    }

    @Override
    protected BaseDataDAO getBaseDataDAO() {
        return new GruanDataValueDAO(this.getDataSource());
    }

    private GruanDataHeader getGruanDataHeaderToSave(GruanDataHeader gruanDataHeaderFile) {
        if (importFromBulk)
            return gruanDataHeaderFile;
        else {
            GruanDataHeader gruanDataHeaderToSave;

            GruanDataHeaderDAO gruanDataHeaderDAO = new GruanDataHeaderDAO(this.getDataSource());

            List<GruanDataHeader> gruanDataHeaderDBlist = (List<GruanDataHeader>) gruanDataHeaderDAO.getById(gruanDataHeaderFile);

            if (gruanDataHeaderDBlist != null && gruanDataHeaderDBlist.size() == 1) {
                GruanDataHeader gruanDataHeaderDB = gruanDataHeaderDBlist.get(0);

                gruanDataHeaderToSave = new GruanDataHeader();

                List<GruanDataValue> gruanDataValuesDB = (List<GruanDataValue>) gruanDataHeaderDAO.getData(gruanDataHeaderDB);

                List<Float> pressDBList = gruanDataValuesDB
                        .stream()
                        .map(x -> ((GruanDataValue) x).getPress())
                        .collect(Collectors.toList());

                List<Float> pressFileList = gruanDataHeaderFile.getData()
                        .stream()
                        .map(x -> ((GruanDataValue) x).getPress())
                        .collect(Collectors.toList());

                List<Float> pressToSave = pressFileList.stream().filter(press -> !pressDBList.contains(press)).collect(Collectors.toList());

                List<BaseData> gruanDataValueListToSave = gruanDataHeaderFile.getData()
                        .stream()
                        .filter(gruanDataValue -> pressToSave.contains(((GruanDataValue) gruanDataValue).getPress()))
                        .collect(Collectors.toList());

                if (gruanDataValueListToSave.size() > 0) {
                    gruanDataHeaderToSave.setData(new HashSet<>(gruanDataValueListToSave));
                } else {
                    gruanDataHeaderToSave = null;
                }
            } else {
                gruanDataHeaderToSave = gruanDataHeaderFile;
            }

            return gruanDataHeaderToSave;
        }
    }
}
