package cnr.imaa.baron.test;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.bll.retriever.IgraRetriever;
import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.BaseObjBaron;
import cnr.imaa.baron.model.GuanDataHeader;
import cnr.imaa.baron.model.Station;
import cnr.imaa.baron.repository.GuanDataHeaderDAO;
import cnr.imaa.baron.utils.FileResourceUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IgraRetrieveTest extends RetrieveTest {
    private static final Logger log = LoggerFactory.getLogger(IgraRetrieveTest.class);

    @BeforeAll
    protected static void setUp() {
        new FileResourceUtils("application_IGRA_test.properties");
    }

    @Test
    @Order(1)
    public void testIgraRetrieveLindenberg_CleanDB_FTP_NoSaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.IGRA_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = "/pub/data/igra/data/data-por/GMM00010393-data.txt.zip";

        testIgraRetrieveLindenberg(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    @Test
    @Order(2)
    public void testIgraRetrieveLindenberg_CleanDB_FTP_SaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = true;
        String taskName = Task.TASK_NAME.IGRA_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = false;
        String pathData = null;
        Boolean saveSourceFile = true;
        String pathDestination = "/Data/IGRA/";
//        String pathDestination = "/Dati_2TB/IGRA";
        String remotePathFtp = "/pub/data/igra/data/data-por/GMM00010393-data.txt.zip";

        testIgraRetrieveLindenberg(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    @Test
    @Order(5)
    public void testIgraRetrieveLindenberg_CleanDB_LocalDrive_NoSaveDisk() {
        boolean cleanDb = true;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.IGRA_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = true;
//        String pathData = "/Data/IGRA_light/";
//        String pathData = "/Data/IGRA/";
        String pathData = "/Dati_2TB/IGRA/";
//        String pathData = "/Dati_2TB/IGRA_light/";
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = null;

        testIgraRetrieveLindenberg(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    @Test
    @Order(6)
    public void testIgraRetrieveLindenberg_NoCleanDB_LocalDrive_NoSaveDisk() {
        boolean cleanDb = false;
        boolean cleanDisk = false;
        String taskName = Task.TASK_NAME.IGRA_RETRIEVER.name().toUpperCase();
        Boolean importDataFromDisk = true;
//        String pathData = "/Data/IGRA_light/";
        String pathData = "/Data/IGRA/";
//        String pathData = "/Dati_2TB/IGRA/";
//        String pathData = "/Dati_2TB/IGRA_light/";
        Boolean saveSourceFile = false;
        String pathDestination = null;
        String remotePathFtp = null;

        testIgraRetrieveLindenberg(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
    }

    public void testIgraRetrieveLindenberg(boolean cleanDB, boolean cleanDisk, String[] args){
        //delete and create the tables
        log.info("cleanDB");
        if (cleanDB) cleanDB(DBManager.TABLE.IGRA);

        //clean data folder
        log.info("cleanDisk");
        if (cleanDisk) cleanDisk(args[Task.ARGS.PATH_DESTINATION.position]);

        //import Lindenberg data
        log.info("importData");
        importData(args);

        //read data from DB
        log.info("read data from DB");
        GuanDataHeader guanDataHeader = new GuanDataHeader();
        Station station = new Station();
        station.setIdStation("GMM00010393");
        guanDataHeader.setStation(station);

        List<GuanDataHeader> guanDataHeaderList = (List<GuanDataHeader>) getData(guanDataHeader);

        //obtain the list of data from source
        log.info("read data from source");
        List<String> dataList;
        if (Boolean.parseBoolean(args[Task.ARGS.IMPORT_DATA_FROM_DISK.position])) {
            dataList = (List<String>) getListOfDataFromDisk(args[Task.ARGS.PATH_DATA.position]);
        } else {
            dataList = (List<String>) getListOfDataFromRemote(args[Task.ARGS.REMOTE_PATH.position]);
        }
    }

    @Override
    protected Object getData(BaseObjBaron guanDataHeader) {
//        GuanDataHeaderDAO guanDataHeaderDAO = new GuanDataHeaderDAO();
//        return guanDataHeaderDAO.getById(guanDataHeader);
        return null;
    }

    @Override
    protected void importData(String[] args) {
        IgraRetriever igraRetriever = new IgraRetriever();
        igraRetriever.run(args);
    }

    @Override
    protected List<String> getListOfDataFromRemote(String remotePath) {
        IgraRetriever igraRetriever = new IgraRetriever();
        return igraRetriever.retrieveFileList(remotePath);
    }
}
