package cnr.imaa.baron.bll.harmonizer;

import cnr.imaa.baron.bll.Task;
import cnr.imaa.baron.model.StationToHarmonize;
import cnr.imaa.baron.repository.BaseObjDAO;
import cnr.imaa.baron.repository.StationToHarmonizeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Rharm extends Task {
    private static final Logger log = LoggerFactory.getLogger(Rharm.class);
    private final StationToHarmonizeDAO stationToHarmonizeDAO = new StationToHarmonizeDAO(this.getDataSource());

    private String rs92codes = this.getFileResourceUtils().getConfig().get("rs92codes");

    @Override
    public void run(String... args) {
        log.info("Rharm START");
        Instant start = Instant.now();

        List<StationToHarmonize> stationsToHarmonize = (List<StationToHarmonize>) stationToHarmonizeDAO.getAll();
        stationsToHarmonize
                .stream()
                .forEach(stationToHarmonize -> {
                    processStation(stationToHarmonize);
                });

        Instant finish = Instant.now();
        log.info("Elapsed time: " + Duration.between(start, finish).toMillis());

        log.info("Rharm END");
    }

    private void processStation(StationToHarmonize stationToHarmonize) {
        setStatus(stationToHarmonize, StationToHarmonize.STATUS.PROCESSING);

        CommonStaticDataStructure commonStaticList = new CommonStaticDataStructure(rs92codes, this.getDataSource());
        new IgraHarmonizer(stationToHarmonize, commonStaticList, this.getDataSource()).process();

        setStatus(stationToHarmonize, StationToHarmonize.STATUS.FINISHED);
    }

    private void setStatus(StationToHarmonize stationToHarmonize, StationToHarmonize.STATUS status) {
        stationToHarmonize.setStatus(status.value);
        stationToHarmonize.setLastupdate(Instant.now());
        stationToHarmonizeDAO.save(stationToHarmonize, BaseObjDAO.SQL_COMMAND.UPDATE);
    }
}
