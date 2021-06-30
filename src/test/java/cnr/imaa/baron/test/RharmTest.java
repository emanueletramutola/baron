package cnr.imaa.baron.test;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.bll.harmonizer.Rharm;
import cnr.imaa.baron.model.GuanDataHeader;
import cnr.imaa.baron.model.Station;
import cnr.imaa.baron.utils.FileResourceUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RharmTest {
    private static final Logger log = LoggerFactory.getLogger(RharmTest.class);

    @BeforeAll
    protected static void setUp() {
        new FileResourceUtils("application_RHARM_test.properties");
    }

//    public void test() {
//        boolean cleanDb = true;
//        boolean cleanDisk = false;
//        String taskName = Task.TASK_NAME.IGRA_RETRIEVER.name().toUpperCase();
//        Boolean importDataFromDisk = false;
//        String pathData = null;
//        Boolean saveSourceFile = false;
//        String pathDestination = null;
//        String remotePathFtp = "/pub/data/igra/data/data-por/GMM00010393-data.txt.zip";
//
//        testIgraRetrieveLindenberg(cleanDb, cleanDisk, initParameters(taskName, importDataFromDisk, pathData, saveSourceFile, pathDestination, remotePathFtp));
//    }

    @Test
    public void test() {
        String[] args = new String[1];
        args[0] = Task.TASK_NAME.RHARM.name();

        log.info("cleanDB");
//        DBManager.initDB(DBManager.TABLE.RHARM);

        log.info("run RHARM");
        Rharm rharm = new Rharm();
        rharm.run(args);
//
//        //read data from DB
//        log.info("read data from DB");
//        GuanDataHeader guanDataHeader = new GuanDataHeader();
//        Station station = new Station();
//        station.setIdStation("GMM00010393");
//        guanDataHeader.setStation(station);
//
//        List<GuanDataHeader> guanDataHeaderList = (List<GuanDataHeader>) getData(guanDataHeader);
//
//        //obtain the list of data from source
//        log.info("read data from source");
//        List<String> dataList;
//        if (Boolean.parseBoolean(args[Task.ARGS.IMPORT_DATA_FROM_DISK.position])) {
//            dataList = (List<String>) getListOfDataFromDisk(args[Task.ARGS.PATH_DATA.position]);
//        } else {
//            dataList = (List<String>) getListOfDataFromRemote(args[Task.ARGS.REMOTE_PATH.position]);
//        }
    }
}
