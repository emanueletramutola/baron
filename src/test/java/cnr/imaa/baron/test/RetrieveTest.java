package cnr.imaa.baron.test;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.BaseObjBaron;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class RetrieveTest {
    private static final Logger log = LoggerFactory.getLogger(RetrieveTest.class);
    private DBManager dbManager;
//    @Test
//    public void testEma(){
//        GruanConverter gruanConverter = new GruanConverter();
//
//        try {
//            NetcdfFile ncfile = NetcdfFile.openInMemory("", Files.readAllBytes(Paths.get( "/home/emanuele/ownCloud/src/baron/src/test/data/GRUAN/BAR/2018/BAR-RS-01_2_RS92-GDP_002_20180104T230000_1-000-001.nc")));
//
//            GruanDataHeader gruanDataHeader = (GruanDataHeader) gruanConverter.convert(ncfile);
//
//            GruanDataHeaderDAO gruanDataHeaderDAO = new GruanDataHeaderDAO();
//            gruanDataHeaderDAO.save(gruanDataHeader);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }


    protected abstract Object getData(BaseObjBaron objInput);

    protected abstract void importData(String[] args);

    protected abstract Object getListOfDataFromRemote(String remotePath);

    protected Object getListOfDataFromDisk(String pathData) {
        List<String> dataList = new ArrayList<>();

        try {
            if (Files.exists(Paths.get(pathData))) {
                Files.walk(Paths.get(pathData))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile);

                dataList = Files.walk(Paths.get(pathData))
                        .map(Path::toFile)
                        .filter(x -> !x.isDirectory())
                        .map(File::getPath)
                        .collect(Collectors.toList());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return dataList;
    }

    protected void cleanDB(DBManager.TABLE table) {
//        DBManager.initDB(table);
    }

    protected void cleanDisk(String pathDestination) {
        try {
            if (Files.exists(Paths.get(pathDestination))) {
                Files.walk(Paths.get(pathDestination))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String[] initParameters(String taskName, Boolean importDataFromDisk, String pathData, Boolean saveSourceFile, String pathDestination, String remotePathFtp) {
        String[] args = new String[Task.ARGS.values().length];

        args[Task.ARGS.TASK_NAME.position] = taskName;
        if (importDataFromDisk != null) args[Task.ARGS.IMPORT_DATA_FROM_DISK.position] = importDataFromDisk.toString();
        if (pathData != null) args[Task.ARGS.PATH_DATA.position] = pathData;
        if (saveSourceFile != null) args[Task.ARGS.SAVE_SOURCE_FILE.position] = saveSourceFile.toString();
        if (pathDestination != null) args[Task.ARGS.PATH_DESTINATION.position] = pathDestination;
        if (remotePathFtp != null) args[Task.ARGS.REMOTE_PATH.position] = remotePathFtp;

        return args;
    }
}
