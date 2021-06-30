package cnr.imaa.baron.test;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.bll.retriever.GruanRetriever;
import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.BaseObjBaron;
import cnr.imaa.baron.model.GruanDataHeader;
import cnr.imaa.baron.model.Station;
import cnr.imaa.baron.repository.GruanDataHeaderDAO;
import cnr.imaa.baron.utils.FileResourceUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GruanRetrieveTest extends RetrieveTest {
    private static final Logger log = LoggerFactory.getLogger(GruanRetrieveTest.class);

    @BeforeAll
    protected static void setUp() {
        new FileResourceUtils("application_GRUAN_test.properties");
    }

    @Test
    @Order(1)
    public void testGruanRetrieveBarrow2018_CleanDB_FTP_NoSaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = "/pub/data/gruan/processing/level2/RS92-GDP/version-002/BAR/2018/";

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    //    @Disabled
    @Test
    @Order(2)
    public void testGruanRetrieveBarrow2018_CleanDB_FTP_SaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = true;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = true;
//        String pathDestination = "/Data/GRUAN/";
        String pathDestination = "/Dati_2TB/GRUAN";
        String remotePathFtp = "/pub/data/gruan/processing/level2/RS92-GDP/version-002/BAR/2018";

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    //    @Disabled
    @Test
    @Order(3)
    public void testGruanRetrieveBarrow2018_NoCleanDB_FTP_NoSaveDisk() {
        boolean cleanDb = false;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = "/pub/data/gruan/processing/level2/RS92-GDP/version-002/BAR/2018";

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    //    @Disabled
    @Test
    @Order(4)
    public void testGruanRetrieveBarrow2018_NoCleanDB_FTP_SaveDisk() {
        boolean cleanDb = false;
        boolean cleanDisk = true;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = true;
//        String pathDestination = "/Data/GRUAN/";
        String pathDestination = "/Dati_2TB/GRUAN";
        String remotePathFtp = "/pub/data/gruan/processing/level2/RS92-GDP/version-002/BAR/2018";

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    @Test
    @Order(5)
    public void testGruanRetrieveBarrow2018_CleanDB_LocalDrive_NoSaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = true;
//        String pathData = "/Data/GRUAN/";
        String pathData = "/Dati_2TB/GRUAN/";
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = null;

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    @Test
    @Order(6)
    public void testGruanRetrieveBarrow2018_NoCleanDB_LocalDrive_NoSaveDisk() {
        boolean cleanDb = false;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.GRUAN_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = true;
//        String pathData = "/Data/GRUAN/";
        String pathData = "/Dati_2TB/GRUAN/";
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = null;

        testGruanRetrieveBarrow2018(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    public void testGruanRetrieveBarrow2018(boolean cleanDB, boolean cleanDisk, String[] args) {
        //delete and create the tables
        log.info("cleanDB");
        if (cleanDB) cleanDB(DBManager.TABLE.GRUAN);

        //clean data folder
        log.info("cleanDisk");
        if (cleanDisk) cleanDisk(args[Task.ARGS.PATH_DESTINATION.position]);

        //import all Barrow data of 2018
        log.info("importData");
        importData(args);

        //read data from DB
        log.info("read data from DB");
        GruanDataHeader gruanDataHeader = new GruanDataHeader();
        Station station = new Station();
        station.setIdStation("BAR");
        gruanDataHeader.setStation(station);

        List<GruanDataHeader> gruanDataHeaderList = (List<GruanDataHeader>) getData(gruanDataHeader);

        //obtain the list of data from source
        log.info("read data from source");
        List<String> dataList;
        if (Boolean.parseBoolean(args[Task.ARGS.IMPORT_DATA_FROM_DISK.position])) {
            dataList = (List<String>) getListOfDataFromDisk(args[Task.ARGS.PATH_DATA.position]);
        } else {
            dataList = (List<String>) getListOfDataFromRemote(args[Task.ARGS.REMOTE_PATH.position]);
        }

        //test
        assertEquals(dataList.size(), gruanDataHeaderList.size());
    }

//    @Override
//    protected Object getData(BaseObjBaron objInput) {
//        GruanDataHeaderDAO gruanDataHeaderDAO = new GruanDataHeaderDAO();
//        return gruanDataHeaderDAO.getByIdStation(objInput);
//    }

    @Override
    protected Object getData(BaseObjBaron objInput) {
        return null;
    }

    @Override
    protected void importData(String[] args) {
        GruanRetriever gruanRetriever = new GruanRetriever();
        gruanRetriever.run(args);
    }

    @Override
    protected Object getListOfDataFromRemote(String remotePath) {
        GruanRetriever gruanRetriever = new GruanRetriever();
        return gruanRetriever.retrieveFileList(remotePath);
    }
}
